package com.example.demo.medium;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.UserServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({ @Sql(value = "/sql/user-service-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)})
 class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @MockBean
    private JavaMailSender javaMailSender;


    @Test
    @Order(1)
    public void getByEmail은_ACTIVATE_상태인_사용자를_찾아올_수_있다(){
        String mail = "kok202@naver.com";
        User user = userServiceImpl.getByEmail(mail);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(mail);
        assertThat(user.getNickname()).isEqualTo("kok202");
    }

    @Test
    @Order(2)
    public void getByEmail은_PENDING_상태인_사용자를_찾아올_수_없다(){
        String mail = "kok303@naver.com";

        assertThatThrownBy(() ->  {
            User user = userServiceImpl.getByEmail(mail);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @Order(3)
    public void getById는_PENDING_상태인_사용자를_찾아올_수_없다(){

        assertThatThrownBy(()-> {
            User user = userServiceImpl.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    @Order(4)
    public void getById는_ACTIVATE_상태인_사용자를_찾아올_수_있다(){
        User user = userServiceImpl.getById(1);
        assertThat(user).isNotNull();
    }

    @Test
    @Order(5)
    public void UserCreateDto를_사용하여_사용자를_생성할_수_있다(){
        UserCreate userCreate = UserCreate.builder()
                .email("kok12180@mailinator.com")
                .address("busan")
                .nickname("kok202-1")
                .build();

        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        User User = userServiceImpl.create(userCreate);
        assertThat(User.getId()).isNotNull();
        assertThat(User.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    @Order(6)
    public void UserUpdateDto를_사용해서_사용자_정보를_수정할_수_있다(){
        UserUpdate updateUserDto = UserUpdate.builder()
                .address("daegu")
                .nickname("kok202-2")
                .build();

        userServiceImpl.update(1, updateUserDto);
        User User = userServiceImpl.getById(1);
        assertThat(User.getAddress()).isEqualTo(updateUserDto.getAddress());
        assertThat(User.getNickname()).isEqualTo(updateUserDto.getNickname());
    }

    @Test
    @Order(7)
    public void 사용자를_로그인시키면_마지막_로그인시간이_변경된다(){
        userServiceImpl.login(1);

        User User = userServiceImpl.getById(1);
        assertThat(User.getLastLoginAt()).isGreaterThan(0);
    }

    @Test
    @Order(8)
    public void PENDING_상태인_사용자를_인증코드로_ACTIVATE_할_수_있다(){
        userServiceImpl.verifyEmail(2,  "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        User User = userServiceImpl.getById(2);
        assertThat(User.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @Order(8)
    public void PENDING_상태인_사용자가_잘못된_인증코드를_받으면_에러를_던진다(){

        assertThatThrownBy(() ->
        {
            userServiceImpl.verifyEmail(2,  "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");})
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}