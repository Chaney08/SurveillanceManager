package org.geosatis.surveillancemanager.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.geosatis.surveillancemanager.model.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//This test class tests that all pages are loaded when required
public class UserRegistrationControllerWebTests {

    //Although no mock objects to inject here I want to keep all test pages to a similar format - Could just have put userController = new UserRegistrationController() instead of injectmocks.
    @InjectMocks
    private UserRegistrationController userController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testUserRegistrationPage() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("userTemplates/registration"))
                .andExpect(model().attribute("userRegistration",instanceOf(User.class)));
    }
}
