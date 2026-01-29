package com.example.demo.post.service.port;

import com.example.demo.post.infrastructure.PostEntity;

import java.util.Optional;

public interface PostRepository {

    PostEntity save(PostEntity postEntity);

    Optional<PostEntity> findById(long id);
}
