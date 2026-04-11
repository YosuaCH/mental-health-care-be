package com.mental.health.care.backend.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String to, String name, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("Mental Health Care <noreply@mentalhealthcare.com>");
        helper.setTo(to);
        helper.setSubject("Permintaan Atur Ulang Kata Sandi - Mental Health Care");

        String content = "<html>" +
                "<head>" +
                "<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap' rel='stylesheet'>" +
                "<style>" +
                "body { font-family: 'Poppins', Arial, sans-serif; background-color: #f8f8f8; color: #333; margin: 0; padding: 0; }" +
                ".container { max-width: 600px; margin: 40px auto; padding: 40px; background-color: #ffffff; border-radius: 24px; text-align: left; }" +
                ".logo { width: 80px; height: 80px; margin: 0 auto 30px auto; " +
                "background-image: url('https://i.ibb.co.com/2Ydm0Vs5/logo-brand-removebg-preview.png'); " +
                "background-size: contain; background-repeat: no-repeat; background-position: center; }" +
                "h2 { color: #1e293b; font-size: 20px; margin-bottom: 20px; font-weight: 600; }" +
                "p { color: #64748b; font-size: 14px; line-height: 1.6; margin-bottom: 25px; }" +
                ".btn-wrapper { text-align: center; margin: 40px 0; }" +
                ".btn { background-color: #0f172a; color: #ffffff !important; padding: 14px 30px; text-decoration: none; border-radius: 12px; font-weight: 600; font-size: 14px; display: inline-block; box-shadow: 0 10px 15px -3px rgba(15, 23, 42, 0.2); }" +
                ".footer { margin-top: 40px; padding-top: 20px; border-top: 1px solid #f1f5f9; font-size: 12px; color: #94a3b8; text-align: center; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='logo'></div>" +
                "<h2>Halo " + name + ",</h2>" +
                "<p>Kata sandi akun Mental Health Care Anda dapat diatur ulang dengan mengklik tombol di bawah ini. Jika Anda tidak merasa meminta sandi baru, silakan abaikan email ini.</p>" +
                "<div class='btn-wrapper'>" +
                "<a href='" + resetLink + "' class='btn'>Atur Ulang Kata Sandi</a>" +
                "</div>" +
                "<div class='footer'>" +
                "Email ini dikirim secara otomatis oleh sistem kami." +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setText(content, true);
        mailSender.send(message);
    }
}
