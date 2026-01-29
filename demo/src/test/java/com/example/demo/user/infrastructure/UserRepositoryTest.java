package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql=true)
@TestPropertySource("classpath:test-application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @Order(1)
    @Disabled("연결 테스트는 더이상 필요없다")
    void UserRepository가_제대로_연결되었다(){
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("Tester");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaaa-aaaa-aaaaaaaaaa");
        //when
        UserEntity savedUser = userJpaRepository.save(userEntity);
        //then
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    @Order(2)
    void findByIdAndStatus는_데이터가_없으면_Optional_empty를_내려준다(){
        //given
        //when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.PENDING);
        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @Order(3)
    void findByEmailAndStatus로_사용자를_찾을_수_있다(){
        //given
        //when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("test@test.com", UserStatus.ACTIVE);
        //then
        assertThat(result.isPresent());
    }

    @Test
    void findByIdAndStatus로_사용자를_찾을_수_있다(){
        //given
        //when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.ACTIVE);
        //then
        assertThat(result.isPresent()).isTrue();
    }
}