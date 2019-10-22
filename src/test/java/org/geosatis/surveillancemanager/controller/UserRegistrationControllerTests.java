package org.geosatis.surveillancemanager.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.UserRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(
        locations = "classpath:applicationTest.properties")
public class UserRegistrationControllerTests {

    @Resource
    private UserRepository userRepo;

    User user = new User();


    @Before
    public void setup() {
        //Although only one test is in this class - I still done the setup method as it makes it easier to expand
        user.setUserName("Paul");
        user.setFullName("Paul Chaney");
        user.setPassword("Whatever");
        userRepo.save(user);
    }
    @Test
    public void scheduleRegistration() {

        //The save is done already so we just need to verify it worked
        User user2 = userRepo.findByUserName("Paul");
        assertEquals("Paul", user2.getUserName());
    }

}
