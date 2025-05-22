package project.ottshare.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void send(String toEmail, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[ottShare] 임시 비밀번호 변경");
        message.setText("임시 비밀번호는 다음과 같습니다: " + tempPassword + "\n로그인 후 비밀번호를 꼭 변경해주세요.");
        message.setFrom("koyeonu@gmail.com");
        mailSender.send(message);
    }
}
