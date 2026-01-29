package com.example.demo.user.service;

import com.example.demo.user.service.port.MailSender;

public class FakeMailSender implements MailSender {

    public String emeil;
    public String title;
    public String content;

    @Override
    public void send(String email, String title, String content) {
        this.emeil=email;
        this.title=title;
        this.content=content;
    }
}
