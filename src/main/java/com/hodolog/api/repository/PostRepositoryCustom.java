package com.hodolog.api.repository;

import com.hodolog.api.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);

}
