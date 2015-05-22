package org.nhnnext.guinness.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.nhnnext.guinness.exception.SendMailException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Resource
	private JavaMailSender javaMailSender;

	public void sendMailforSignUp(String keyAddress, String userId) throws SendMailException, UnknownHostException  {
		String IPAddress = "localhost:8080";
		if(InetAddress.getLocalHost().getHostAddress().equals("125.209.193.185")) {
			IPAddress = InetAddress.getLocalHost().getHostAddress();
		}
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "utf-8");
			String htmlMsg = "<h3>페이퍼민트에 가입해주셔서 감사합니다.</h3>" +
		    "<a href='http://" + IPAddress + "/user/confirm/" + keyAddress + "' style='font-size: 15px;"
		    		+ "color: white; text-decoration:none'>"
		    		+ "<div style='padding: 10px; border: 0px; width: 150px;"
		    		+ "margin: 15px 5px; background-color: #74afad; "
		    		+ "text-align:center'>페이퍼민트 시작하기</div></a>" +
			"<p>Copyright &copy; by link413. All rights reserved.</p>";
			
			messageHelper.setTo(userId);
			messageHelper.setFrom("hakimaru@naver.com");
			messageHelper.setSubject("환영합니다. 페이퍼민트 가입 인증 메일입니다.");
			messageHelper.setText(htmlMsg, true);
			javaMailSender.send(message);
		} catch (MessagingException | NullPointerException | MailAuthenticationException e) {
			throw new SendMailException(e.getClass().getSimpleName());
		}
	}
	
	public void sendMailforInitPassword(String tempPassword, String userId) throws SendMailException  {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setTo(userId);
			messageHelper.setFrom("hakimaru@naver.com");
			messageHelper.setSubject("페이퍼민트 임시 비밀번호를 보내드립니다.");
			messageHelper.setText("임시 비밀번호는 " + tempPassword + " 입니다."
					+"<a href='http://localhost:8080/' style='font-size: 15px;"
		    		+ "color: white; text-decoration:none'>"
		    		+ "<div style='padding: 10px; border: 0px; width: 120px;"
		    		+ "margin: 15px 5px; background-color: #74afad; "
		    		+ "text-align:center'>페이퍼민트로 가기</div></a>" +
			"<p>Copyright &copy; by link413. All rights reserved.</p>", true);
			javaMailSender.send(message);
		} catch (MessagingException | NullPointerException | MailAuthenticationException e) {
			throw new SendMailException(e.getClass().getSimpleName());
		}
	}
}
