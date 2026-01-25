package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({ @Sql(value = "/sql/user-service-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)})
 class UserServiceTest {

    @Autowired
    private UserService userService;


    @Test
    @Order(1)
    public void getByEmail은_ACTIVATE_상태인_사용자를_찾아올_수_있다(){
        String mail = "kok202@naver.com";
        UserEntity userEntity = userService.getByEmail(mail);

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo(mail);
        assertThat(userEntity.getNickname()).isEqualTo("kok202");
    }

    @Test
    @Order(2)
    public void getByEmail은_PENDING_상태인_사용자를_찾아올_수_없다(){
        String mail = "kok303@naver.com";

        assertThatThrownBy(() ->  {
            UserEntity userEntity = userService.getByEmail(mail);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @Order(3)
    public void getById는_PENDING_상태인_사용자를_찾아올_수_없다(){

        assertThatThrownBy(()-> {
            Optional<UserEntity> userEntity = userService.findById(2);
        }).isInstanceOf(ResourceNotFoundException.class);

    }
}