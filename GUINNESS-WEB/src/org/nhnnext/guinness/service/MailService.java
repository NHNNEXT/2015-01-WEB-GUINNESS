package org.nhnnext.guinness.service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.nhnnext.guinness.exception.SendMailException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
/**
 * Mail 전송 기능은 여려 곳에서 사용할 수 있고, 역할 측면에서 분리하는 것이 일반적임
 */
public class MailService {
	@Resource
	private JavaMailSender javaMailSender;

	public void sendMail(String keyAddress, String userId) throws SendMailException  {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setTo(userId);
			messageHelper.setFrom("hakimaru@naver.com");
			messageHelper.setSubject("환영합니다. 페이퍼민트 가입 인증 메일입니다.");
			messageHelper.setText("<a href='http://localhost:8080/user/confirm/" + keyAddress + "'> 페이퍼민트 시작하기 </a>", true);
			javaMailSender.send(message);
		} catch (MessagingException | NullPointerException | MailAuthenticationException e) {
			throw new SendMailException(e.getClass().getSimpleName());
		}
	}
}
