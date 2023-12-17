package com.rean.shopspring.controller;

import com.rean.shopspring.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试邮件发送
 * @author qzz
 */
@RestController
@RequestMapping("/mail")
public class SendEmailController {

    @Autowired
    private EmailService mailService;


    /**
     * 发送文本邮件
     * @param to
     * @param subject
     * @param text
     */
    @RequestMapping("/sendTextMail")
    public void sendTextMail(String to,String subject,String text){
        mailService.sendTextMailMessage(to,subject,text);
    }
}

