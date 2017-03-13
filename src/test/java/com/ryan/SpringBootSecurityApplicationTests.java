package com.ryan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.ryan.domain.User;
import com.ryan.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootSecurityApplicationTests {
	@Autowired
	UserRepository userRepository;

	@Test
	public void contextLoads() {
		
	}

	@Test
	public void BCryptPassword(){
		User u=userRepository.findByUsername("u2");
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		u.setPassword(bpe.encode("123"));
		userRepository.save(u);
	}
}
