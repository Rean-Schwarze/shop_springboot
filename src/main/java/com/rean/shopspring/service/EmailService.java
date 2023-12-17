package com.rean.shopspring.service;

public interface EmailService {
    void sendTextMailMessage(String to,String subject,String text);
}
