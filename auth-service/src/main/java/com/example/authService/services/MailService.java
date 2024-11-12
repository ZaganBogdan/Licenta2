package com.example.authService.services;

import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final String defaultFrontendUrl;

    public MailService(final JavaMailSender javaMailSender, @Value("${sportPlanner.frontend.default-url}") String defaultFrontendUrl) {
        this.javaMailSender = javaMailSender;
        this.defaultFrontendUrl = defaultFrontendUrl;
    }

    @SneakyThrows
    public void sendForgotMessage(final String email, final String token, final String originUrl) {
        String url = originUrl != null ? originUrl : defaultFrontendUrl;
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Reset your password");
        helper.setText(String.format("""
                <html>
                <body>
                    <p>Salut,</p>
                    <p>Am primit o cerere de resetare a parolei pentru contul tău SportPlanner.</p>
                    <p>Pentru a-ți reseta parola, te rugăm să accesezi acest <a href="%s/reset/%s">link</a></p>
                    <p>Acest link este valabil pentru o singură accesare.</p>
                    <p>Dacă nu ai solicitat resetarea parolei, te rugăm să ignori acest e-mail. Parola ta va rămâne neschimbată.</p>
                </body>
                </html>
                            """, url, token), true);

        javaMailSender.send(message);
    }
}
