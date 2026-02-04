package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class UserServiceImplTest {

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init(){
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userServiceImpl = UserServiceImpl.builder()
                .certificationService(new CertificationService(fakeMailSender))
                .clockHolder(new TestClockHolder(123456789))
                .uuidHolder(new TestUuidHolder("aaaa-1234-aaaa-5678"))
                .userRepository(fakeUserRepository)
                .build();

        fakeUserRepository.save(User.builder()
        .id(1L)
        .email("kok202@naver.com")
        .nickname("kok202")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(0L)
        .build());

        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("kok303@naver.com")
                .nickname("kok303")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }
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
        //
        //BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        User user = userServiceImpl.create(userCreate);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
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