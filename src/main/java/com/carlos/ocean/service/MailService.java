package com.carlos.ocean.service;

import com.carlos.ocean.pojo.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Carlos Li
 * @date 2021/6/2
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender sender;

    public boolean sendMail(Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mail.getFrom());
        mailMessage.setTo(mail.getTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getContent());
        try {
            sender.send(mailMessage);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

    }

}