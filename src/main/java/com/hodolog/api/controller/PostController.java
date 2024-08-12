package com.hodolog.api.controller;

import com.hodolog.api.exception.InvalidRequest;
import com.hodolog.api.repository.PostSearch;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.response.PostResponse;
import com.hodolog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/test")
    public String test() {
        return "hello";
    }

    @GetMapping("/foo")
    public String foo() {
        return "foo";
    }

    // SSR -> jsp, thymeleaf, mustache, freemarker
    // SPA ->
    //  vue -> vue+SSR = nuxt.js
    //  react -> react+SSR = next.js

    // getMapping, postMapping 이런거 http 요청 type 공부하기 get, post, 등등
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        request.validate();
        postService.write(request);
    }


    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     *
     * @return
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId){
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSerach){
        return postService.getlist(postSerach);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request){
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }


}
