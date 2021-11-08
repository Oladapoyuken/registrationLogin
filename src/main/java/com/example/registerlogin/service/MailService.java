package com.example.registerlogin.service;

import com.example.registerlogin.dao.EmailDao;
import com.example.registerlogin.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService implements EmailDao {

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }


    @Override
    public void sendMail(Mail mail) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mail.getRecipient(), mail.getRecipient());
        msg.setSubject(mail.getSubject());
        msg.setText(mail.getMessage());

        mailSender.send(msg);

    }

    @Override
    public void sendMailWithAttachment(Mail mail) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(msg, true);
        messageHelper.setTo(mail.getRecipient());
        messageHelper.setSubject(mail.getSubject());
        messageHelper.setText(mail.getMessage());
        FileSystemResource fileSystemResource = new FileSystemResource(new File(System.getProperty("user.dir") + "\\upload\\boy.jpg"));
        messageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);

        mailSender.send(msg);
    }
}
