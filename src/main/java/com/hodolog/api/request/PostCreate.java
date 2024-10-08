package com.hodolog.api.request;

import com.hodolog.api.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.NotFound;

@Getter
@Setter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;
    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;

    @Builder //builder는 이런식으로 직접 생성자에 달아주는 것이 좋다(클래스에 바로 달거나 하지말고)
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate(){
        if(title.contains("바보")){
            throw new InvalidRequest("title", "제목에 바보를 입력할 수 없습니다.");
        }
    }
}
