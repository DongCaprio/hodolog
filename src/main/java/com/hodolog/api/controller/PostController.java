package com.hodolog.api.controller;

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

    // SSR -> jsp, thymeleaf, mustache, freemarker
    // SPA ->
    //  vue -> vue+SSR = nuxt.js
    //  react -> react+SSR = next.js

    // getMapping, postMapping 이런거 http 요청 type 공부하기 get, post, 등등
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        // 데이터를 검증하는 이유
        // 1. client 개발자가 깜박할 수 있음, 휴먼에러 등
        // 2. client 버그 값이 누락될 수 있다.
        // 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
        // 4. DB에 값을 저장할 때 의도치 않은  오류가 발생할 수 있다.
        // 5. 서버 개발자의 편안함을 위해서

        /* 원래 확인하는 방법임 but 더 좋은 방법이 있다.(파라미터에 BindingResult 넣어야지 breakPoint 걸린다.)
        1. 왜냐면 항상 모든 메소드에 이런 방식을 넣을수는 없으므로(반복작업 X)
        2. 응답값에 HashMap -> 응답 클래스를 직접 만들어주는게 좋다
        3. 여러개의 에러처리 힘듬
        4.  3번이상의 반복작업은 피해야한다.(개발에 관한 모든것에 포함되어 있다 3번이상은 피하라)
        if(result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError fristFieldError = fieldErrors.get(0);
            String fieldName = fristFieldError.getField();
            String errorMsg = fristFieldError.getDefaultMessage();

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMsg);
            return error;
        }*/
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


}
