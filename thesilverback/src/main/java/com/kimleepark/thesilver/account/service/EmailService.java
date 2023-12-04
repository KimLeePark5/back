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
                    "<div style=\"font-family: 'Nanum Gothic', Arial, sans-serif;\">"
                            + "<h3>안녕하세요, TheSilver에서 임시 비밀번호 안내드립니다.</h3>"
                            + "<h3>로그인 후 반드시 비밀번호를 변경해주시기 바랍니다.</h3>"
                            + "<div style='border-radius: 10px; padding-left: 20px; width: 400px; height: 100px; display: flex; flex-direction: column; justify-content: center; background-color: #F6EAC2;'>"
                            + "<h3>임시 비밀번호: <span style='color: red;'>" + newPassword + "</span></h3>"
                            + "</div></div>";

            helper.setText(emailMessage, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
