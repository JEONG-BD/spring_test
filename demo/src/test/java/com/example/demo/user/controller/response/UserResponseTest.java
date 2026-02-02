package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    public void User로_응답을_생성할_수_있다(){
        //given

        User user = User.builder()
                .email("kok2@test.com")
                .nickname("tester")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();

        //when
        MyProfileResponse myProfileResponse = MyProfileResponse.toMyProfileResponse(user);

        //then
        assertThat(myProfileResponse.getAddress()).isEqualTo("seoul");
        assertThat(myProfileResponse.getNickname()).isEqualTo("tester");
        assertThat(myProfileResponse.getEmail()).isEqualTo("kok2@test.com");
    }

}