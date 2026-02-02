package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {
    @Test
    public void Post로_응답을_생성할_수_있다(){
        //given


        User writer = User.builder()
                .email("kok2@test.com")
                .nickname("tester")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode(UUID.randomUUID().toString())
                .build();

        Post post = Post.builder()
                .content("hello world")
                .writer(writer)
                .build();
        //when
        PostResponse postResponse = PostResponse.toResponse(post);
        Assertions.assertThat(postResponse.getContent()).isEqualTo("hello world");
        Assertions.assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(postResponse.getWriter().getNickname()).isEqualTo("tester");
        //then
    }

}