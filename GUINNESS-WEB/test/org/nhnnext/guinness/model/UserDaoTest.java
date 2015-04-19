package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
<<<<<<< HEAD
import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
=======
import org.nhnnext.guinness.model.dao.UserDao;
>>>>>>> 41ada4c9c66dc4186ab8ee44766aece4d297bfa6
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class UserDaoTest {
	
    @Autowired
    private UserDao userDao;
	
	@Test
	public void ExistCreateUserTest() throws ClassNotFoundException {
		assertEquals(null, userDao.readUser("h@s.com"));
	}
	
	@Test
	public void CRUDTest() throws ClassNotFoundException, AlreadyExistedUserIdException {
		userDao.createUser(new User("daoTest@guinness.com","Name","password"));
		User user = userDao.readUser("daoTest@guinness.com");
		assertNotNull(user);
	}
}
