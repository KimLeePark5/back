package com.kimleepark.thesilver.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    final JavaMailSender javaMailSender;

    public void sendResetPasswordEmail(String to, String newPassword) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;


        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("TheSilver 임시 비밀번호 안내");
            String emailMessage =
                    "<div style=\"font-family: 'Nanum Gothic', Arial, sans-serif; text-align: center; background-color: #f0f0f0; padding:20px; padding-bottom: 50px;\">" +
                            "<h1 style='color: #333; font-size: 24px; margin-bottom: 10px;'>TheSilver 임시 비밀번호 안내</h1>" +
                            "<p style='color: #555; font-size: 16px;'>안녕하세요. TheSilver에서 임시 비밀번호를 안내드립니다.<br> 로그인 후 반드시 비밀번호를 변경해주세요.<br></p>" +
                            "<br>" +
                            "<div style='color: white; background-color: #01092B; border-radius: 10px; padding: 20px; width: 400px; margin: 0 auto;'>" +
                            "<h2 style='color: white; margin-bottom: 20px; font-size: 20px; font-weight: 300;'>임시 비밀번호: " + newPassword + "</h2>" +
                            "<a href='http://localhost:3000/login' style='text-decoration: none;'>" +
                            "<button style='background-color: #4AA8D8; color: white; padding: 10px 20px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer;'>로그인하러 가기</button>" +
                            "</a>" +
                            "</div>" +
                            "</div>";
            helper.setText(emailMessage, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
