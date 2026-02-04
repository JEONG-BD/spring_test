package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class UserControllerTest {

    @Test
    @Order(1)
    public void 사용자는_특정_사용자의_정보를_전달_받을_수_있다() throws Exception{

        //given
        //UserController userController = UserController.builder()
        //        .userReadService(new UserReadService() {
        //            @Override
        //            public User getByEmail(String email) {
        //                return null;
        //            }
        //
        //            @Override
        //            public User getById(long id) {
        //                return User.builder()
        //                        .id(1L)
        //                        .email("kok2@test.com")
        //                        .nickname("tester")
        //                        .address("seoul")
        //                        .status(UserStatus.ACTIVE)
        //                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
        //                        .build();
        //            }
        //        })
        //        .build();
        //
        ////when
        //ResponseEntity<UserResponse> result = userController.getUserById(1L);
        //
        ////then
        //assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        //assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(result.getBody().getNickname()).isEqualTo("tester");

        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok2@test.com")
                .nickname("tester")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build());

        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1);

        Assertions.assertThat(result.getBody().getNickname()).isEqualTo("tester");
        Assertions.assertThat(result.getBody().getEmail()).isEqualTo("kok2@test.com");
    }

    @Test
    @Order(2)
    public void 사용자는_존재하지_않는_사용자의_아이디로_API를_호출할_경우_404_응답을_받는다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder().build();


        //then
        assertThatThrownBy(() -> testContainer.userController.getUserById(2345234L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @Order(3)
    public void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception{
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok202@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .lastLoginAt(100L)
                .build());

        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1).getStatus()).isEqualTo(UserStatus.ACTIVE);
     }


    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok202@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .lastLoginAt(100L)
                .build());

        // when
        assertThatThrownBy(() -> {
            testContainer.userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 123456789L)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok202@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .lastLoginAt(100L)
                .build());

        ResponseEntity<MyProfileResponse> myInfo = testContainer.userController.getMyInfo("kok202@naver.com");
        assertThat(myInfo.getBody().getAddress()).isEqualTo("Seoul");
    }


    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok202@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .lastLoginAt(100L)
                .build());

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("kok202@naver.com", UserUpdate.builder()
                .address("Pangyo")
                .nickname("kok202-n")
                .build());

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("kok202@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("kok202-n");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100);
        assertThat(result.getBody().getAddress()).isEqualTo("Pangyo");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}
