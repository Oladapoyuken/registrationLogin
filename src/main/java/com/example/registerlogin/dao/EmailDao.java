package com.example.registerlogin.dao;

import com.example.registerlogin.model.Mail;

import javax.mail.MessagingException;

public interface EmailDao {

    void sendMail(Mail mail);

    void sendMailWithAttachment(Mail mail, String filePath) throws MessagingException;
}
