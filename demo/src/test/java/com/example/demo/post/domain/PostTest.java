package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    public void PostCreate로_게시물을_만들_수_있다 () {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("hello world")
                .build();

        User writer = User.builder()
                .email("kok2@test.com")
                .nickname("tester")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode(UUID.randomUUID().toString())
                .build();

        Post post = Post.from(writer, postCreate,  new TestClockHolder(1679530673958L));

        //when
        Assertions.assertThat(post.getContent()).isEqualTo("hello world");
        Assertions.assertThat(post.getWriter().getEmail()).isEqualTo("kok2@test.com");
        Assertions.assertThat(post.getWriter().getNickname()).isEqualTo("tester");
        Assertions.assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        //then
    }

}