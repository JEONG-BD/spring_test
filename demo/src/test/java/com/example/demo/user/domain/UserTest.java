package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    public void User는_UserCreate_객체로_생성이_가능하다(){
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("kok2@test.com")
                .nickname("tester")
                .address("seoul")
                .build();

        //when
        User user = User.from(userCreate, new TestUuidHolder("aaaa-aaaa-aaaa-aaaa"));


        assertThat(user.getId()).isNull();
        assertThat(user.getNickname()).isEqualTo("tester");
        assertThat(user.getAddress()).isEqualTo("seoul");
        assertThat(user.getEmail()).isEqualTo("kok2@test.com");
        assertThat(user.getCertificationCode()).isEqualTo("aaaa-aaaa-aaaa-aaaa");
        //then
    }
    @Test
    public void User는_UserUpdate_객체로_수정이_가능하다(){
        //given

        User user = User.builder()
                .id(1L)
                .email("kok2@test.com")
                .nickname("tester")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();


        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("tester1")
                .address("busan")
                .build();

        //when
        User updatedUser = user.update(userUpdate);


        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getNickname()).isEqualTo("tester1");
        assertThat(updatedUser.getAddress()).isEqualTo("busan");
        assertThat(updatedUser.getEmail()).isEqualTo("kok2@test.com");
        assertThat(updatedUser.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        //then
    }

    @Test
    public void User는_로그인_할_수_있고_마지막_로그인_시간이_변경된다(){
        //given
        User user = User.builder()
                .id(1L)
                .email("kok2@test.com")
                .nickname("tester")
                .address("seoul")
                .lastLoginAt(100L)
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();

        //when
        user = user.login(new TestClockHolder(123434352354L));
        //then
        Assertions.assertThat(user.getLastLoginAt()).isEqualTo(123434352354L);
    }

    @Test
    public void User는_인증코드로_계정을_활성화_할_수_있다(){
        //given
        User user = User.builder()
                .id(1L)
                .email("kok2@test.com")
                .nickname("tester")
                .address("seoul")
                .lastLoginAt(100L)
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();


        //when
        user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        //then

        Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void User는_잘못된_인증코드로_계정을_활성화하면_에러를_던진다(){
        User user = User.builder()
                .id(1L)
                .email("kok2@test.com")
                .nickname("tester")
                .address("seoul")
                .lastLoginAt(100L)
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();

        //when
        //then
        assertThatThrownBy(()->user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}