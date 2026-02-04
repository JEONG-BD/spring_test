package com.example.demo.user.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_확인한다() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationServiceImpl = new CertificationService(fakeMailSender);

        certificationServiceImpl.send("test@test.co.kr", 1, "tttt-tttt");
        String title = "Please certify your email address";
        String content = "Please click the following link to certify your email address" + "tttt-tttt";
        Assertions.assertThat(fakeMailSender.emeil).isEqualTo("test@test.co.kr");
        Assertions.assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        Assertions.assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address" + "http://localhost:8080/api/users/" + 1 + "/verify?certificationCode=" +  "tttt-tttt");
    }
 }
