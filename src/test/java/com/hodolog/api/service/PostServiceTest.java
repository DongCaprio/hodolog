package com.hodolog.api.service;

import com.hodolog.api.domain.Post;
import com.hodolog.api.repository.PostRepository;
import com.hodolog.api.repository.PostSearch;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1(){
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        //when
        postService.write(postCreate);
        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test2(){
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response =  postService.get(requestPost.getId());
        // then
        assertNotNull(response);
        assertEquals(1L,postRepository.count());
        assertEquals("foo",response.getTitle());
        assertEquals("bar",response.getContent());

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3(){
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar "+i)
                        .build())
                        .toList();
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> posts =  postService.getlist(postSearch);
        // then
        assertEquals(10,posts.size());
        assertEquals("foo19",posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4(){
        // given
        Post post =  Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        // when
        postService.edit(post.getId(), postEdit);
        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(()-> new RuntimeException("글이 존재하지 않습니다. id="+post.getId()));

        assertEquals("호돌걸", changePost.getTitle());
        assertEquals("반포자이", changePost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5(){
        // given
        Post post =  Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("초가집")
                .build();

        // when
        postService.edit(post.getId(), postEdit);
        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(()-> new RuntimeException("글이 존재하지 않습니다. id="+post.getId()));

        assertEquals("호돌걸", changePost.getTitle());
        assertEquals("초가집", changePost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6(){
        Post post =  Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        assertEquals(1, postRepository.count());
        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }

}