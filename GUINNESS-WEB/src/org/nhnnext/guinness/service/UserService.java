package org.nhnnext.guinness.service;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.nhnnext.guinness.dao.ConfirmDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.FailedLoginException;
import org.nhnnext.guinness.exception.SendMailException;
import org.nhnnext.guinness.exception.UserUpdateException;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private ConfirmDao confirmDao;
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public void setConfirmDao(ConfirmDao confirmDao) {
		this.confirmDao = confirmDao;
	}
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void create(User user) throws AlreadyExistedUserIdException, SendMailException {
		logger.debug("User: {}", user);
		User existedUser = userDao.findUserByUserId(user.getUserId());
		
		if(existedUser == null) {
			userDao.createUser(user);
			existedUser = userDao.findUserByUserId(user.getUserId());
		}
		if("E".equals(existedUser.getStatus())) {
			throw new AlreadyExistedUserIdException("이미 존재하는 계정입니다.");
		}
		if("R".equals(existedUser.getStatus())) {
			confirmDao.deleteConfirmByUserId(user.getUserId());
		}
		
		String keyAddress = RandomFactory.getRandomId(10);
		sendMail(keyAddress, user.getUserId());
		confirmDao.createConfirm(keyAddress, user.getUserId());
	}
	
	public User confirm(String keyAddress) {
		String userId = confirmDao.findUserIdByKeyAddress(keyAddress);
		userDao.updateUserState(userId, "E");
		confirmDao.deleteConfirmByKeyAddress(keyAddress);
		return userDao.findUserByUserId(userId);
	}
	
	public User login(String userId, String userPassword) throws FailedLoginException {
		User user = userDao.findUserByUserId(userId);
		
		if (user == null || !user.getUserPassword().equals(userPassword) || !"E".equals(user.getStatus())) {
			throw new FailedLoginException();
		}
		return user;
	}

	private void sendMail(String keyAddress, String userId) throws SendMailException  {
		logger.debug("sendMail//keyAddress: {} userId: {}", keyAddress, userId);
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setTo(userId);
			messageHelper.setFrom("hakimaru@naver.com");
			messageHelper.setSubject("환영합니다. 페이퍼민트 가입 인증 메일입니다."); // 메일제목은 생략이 가능하다
			messageHelper.setText("<a href='http://localhost:8080/user/confirm/" + keyAddress + "'> 가입하기 </a>", true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			throw new SendMailException();
		} catch (NullPointerException e) {
			throw new SendMailException("MimeMessage가 생성이 되지 않습니다");
		} catch (MailAuthenticationException e) {
			throw new SendMailException("메일 계정을 확인하세요");
		}
		
		logger.debug("Mail is sended");
	}
	public void update(User user, String userOldPassword, Model model, String rootPath, MultipartFile profileImage) throws UserUpdateException {
		
		User prevUser = userDao.findUserByUserId(user.getUserId());
		if (!prevUser.getUserPassword().equals(userOldPassword)) {
			throw new UserUpdateException("비밀번호가 일치하지 않습니다.");
		}
		
		try {
			user.setUserImage(prevUser.getUserImage());
			if (!profileImage.isEmpty()) {
				String fileName = user.getUserId();
				profileImage.transferTo(new File(rootPath + "img/profile/" + fileName));
				user.setUserImage(fileName);
				logger.debug("Image is changed");
			}
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new UserUpdateException("잘못된 이미지 형식입니다.");
		}

		try {
			userDao.updateUser(user);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new UserUpdateException("잘못된 형식입니다.");
		}

	}
}
