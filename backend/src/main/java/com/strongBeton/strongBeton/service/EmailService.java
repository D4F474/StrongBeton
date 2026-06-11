package com.strongBeton.strongBeton.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
