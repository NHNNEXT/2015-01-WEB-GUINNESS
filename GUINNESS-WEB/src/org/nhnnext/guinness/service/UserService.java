package org.nhnnext.guinness.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.ConfirmDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.FailedLoginException;
import org.nhnnext.guinness.exception.NotExistedUserIdException;
import org.nhnnext.guinness.exception.SendMailException;
import org.nhnnext.guinness.exception.UserUpdateException;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
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
		if(userDao.findUserByUserId(user.getUserId()) != null) {
			throw new AlreadyExistedUserIdException("이미 존재하는 계정입니다.");
		}
		userDao.createUser(user);
		return userDao.findUserByUserId(user.getUserId());
	}

	private void createConfirm(User user, User existedUser) throws SendMailException {
		if("R".equals(existedUser.getUserStatus())) {
			confirmDao.deleteConfirmByUserId(user.getUserId());
		}
		String keyAddress = createKeyAddress();
		confirmDao.createConfirm(keyAddress, user.getUserId());
		mailService.sendMailforSignUp(keyAddress, user.getUserId());
	}
	
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
		if (user == null || !user.isCorrectPassword(userPassword) || !user.checkUserStatus("E")) {
			throw new FailedLoginException();
		}
		return user;
	}
	
	public void update(User user, String rootPath, MultipartFile profileImage) throws UserUpdateException {
		User dbUser = userDao.findUserByUserId(user.getUserId());
		try {
			user.setUserImage(dbUser.getUserImage());
			if (!profileImage.isEmpty()) {
				String fileName = user.getUserId();
				profileImage.transferTo(new File(rootPath + "img/profile/" + fileName));
				user.setUserImage(fileName);
			}
			dbUser.update(user);
			userDao.updateUser(dbUser);
		} catch (IllegalStateException | IOException | DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new UserUpdateException("잘못된 형식입니다.");
		}
	}

	public boolean checkUpdatePassword(String userId, String userPassword) {
		User user = userDao.findUserByUserId(userId);
		return user.isCorrectPassword(userPassword);
	}

	public void initPassword(String userId) throws NotExistedUserIdException, SendMailException {
		if(userDao.findUserByUserId(userId) == null) {
			throw new NotExistedUserIdException("사용자를 찾을 수 없습니다.");
		}
		String tempPassword = "temp_" + RandomFactory.getRandomId(4);
		User user = new User();
		user.setUserId(userId);
		user.setUserPassword(tempPassword);
		userDao.initPassword(user);
		mailService.sendMailforInitPassword(tempPassword, userId);
	}
}
