package org.nhnnext.guinness.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.ConfirmDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.FailedLoginException;
import org.nhnnext.guinness.exception.SendMailException;
import org.nhnnext.guinness.exception.UserUpdateException;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
	@Resource
	private UserDao userDao;
	@Resource
	private ConfirmDao confirmDao;
	@Resource
	private MailService mailService;
	
	public void join(User user) throws AlreadyExistedUserIdException, SendMailException {
		User existedUser = createUser(user);
		createConfirm(user, existedUser);
	}
	
	private User createUser(User user) throws AlreadyExistedUserIdException {
		// 다음 형태로 많이 사용함.
		if(userDao.findUserByUserId(user.getUserId()) != null) {
			throw new AlreadyExistedUserIdException("이미 존재하는 계정입니다.");
		}
		userDao.createUser(user);
		return userDao.findUserByUserId(user.getUserId());
	}

	private void createConfirm(User user, User existedUser) throws SendMailException {
		// TODO offline 코드 리뷰 - 리팩토링 point는?
		if("R".equals(existedUser.getStatus())) {
			confirmDao.deleteConfirmByUserId(user.getUserId());
		}
		String keyAddress = createKeyAddress();
		confirmDao.createConfirm(keyAddress, user.getUserId());
		mailService.	sendMail(keyAddress, user.getUserId());
	}
	
	// TODO offline 코드 리뷰 - userId가 동적으로 변경되어야 하는 이유는?
	private String createKeyAddress() {
		String keyAddress = RandomFactory.getRandomId(10);
		if(confirmDao.isExistKeyAddress(keyAddress)) {
			return createKeyAddress();
		}
		return keyAddress;
	}
	
	public User confirm(String keyAddress) {
		String userId = confirmDao.findUserIdByKeyAddress(keyAddress);
		userDao.updateUserState(userId, "E");
		confirmDao.deleteConfirmByKeyAddress(keyAddress);
		return userDao.findUserByUserId(userId);
	}
	
	public User login(String userId, String userPassword) throws FailedLoginException {
		User user = userDao.findUserByUserId(userId);
		// TODO offline 코드 리뷰 - 리팩토링 point는? 
		if (user == null || !user.isCorrectPassword(userPassword) || !user.checkStatus("E")) {
			throw new FailedLoginException();
		}
		return user;
	}

	public void update(User user, Model model, String rootPath, MultipartFile profileImage) throws UserUpdateException {
		User prevUser = userDao.findUserByUserId(user.getUserId());
		try {
			// TODO offline 코드 리뷰 - 리팩토링 point는?
			if ("".equals(user.getUserPassword()))
				user.setUserPassword(prevUser.getUserPassword());
			user.setUserImage(prevUser.getUserImage());
			if (!profileImage.isEmpty()) {
				String fileName = user.getUserId();
				profileImage.transferTo(new File(rootPath + "img/profile/" + fileName));
				user.setUserImage(fileName);
			}
			userDao.updateUser(user);
		} catch (IllegalStateException | IOException | DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new UserUpdateException("잘못된 형식입니다.");
		}
	}

	public boolean checkUpdatePassword(String userId, String userPassword) {
		User user = userDao.findUserByUserId(userId);
		return user.isCorrectPassword(userPassword);
	}
}
