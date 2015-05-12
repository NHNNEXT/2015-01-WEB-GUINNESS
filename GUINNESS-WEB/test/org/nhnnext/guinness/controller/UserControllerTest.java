package org.nhnnext.guinness.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nhnnext.guinness.dao.ConfirmDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.model.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class UserControllerTest {
	@Mock
	private UserDao userDao;
	@Mock
	private ConfirmDao confirmDao;
	@Mock
	private JavaMailSender mailSender;
	
	@InjectMocks
	private UserController userController;
	
	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = standaloneSetup(userController).build();
	}

	@Test
	public void init() throws Exception {
		standaloneSetup(userController).build().perform(get("/"))
				.andExpect(status().isOk()).andExpect(forwardedUrl("index"));
	}

	@Test
	public void create() throws Exception {
		//TODO mailSender객체를 불러오지 못해 테스트를 실패하는 것 같음.
		this.mockMvc.perform(
				post("/user")
					.param("userId", "parpermint@yopmail.com")
					.param("userPassword", "1q2w3e4r")
					.param("userName", "다스"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("sendEmail"));
	}
	
	@Test
	public void confirm() throws Exception {
		when(confirmDao.findUserIdByKeyAddress("qawsedrftg")).thenReturn("parpermint@yopmail.com");
		when(userDao.findUserByUserId("parpermint@yopmail.com")).thenReturn(new User("parpermint@yopmail.com", "다스", "1q2w3e4r", "R"));
		this.mockMvc.perform(
				post("/user/confirm/qawsedrftg"))
				.andDo(print())
				.andExpect(redirectedUrl("/"));
	}

}
