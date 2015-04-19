package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class UserDaoTest {
	
    @Autowired
    private UserDao userDao;
	
	@Test
	public void NotExistCreateUserTest() throws ClassNotFoundException {
		System.out.println(userDao.readUser("test@guinness.org").toString());
		assertNotNull(userDao.readUser("test@guinness.org"));
	}
	
	@Test
	public void CRUDTest() throws ClassNotFoundException, AlreadyExistedUserIdException {
		userDao.createUser(new User("daoTest@guinness.com","Name","password"));
		User user = userDao.readUser("daoTest@guinness.com");
		assertNotNull(user);
	}
}
