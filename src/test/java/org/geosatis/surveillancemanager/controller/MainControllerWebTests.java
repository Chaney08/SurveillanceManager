package org.geosatis.surveillancemanager.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

//This test class tests that all pages are loaded when required
public class MainControllerWebTests {

    @InjectMocks
    private MainController mainController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }


    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("userTemplates/loginPage"));
    }
    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(get("/logoutSuccessful"))
                .andExpect(status().isOk())
                .andExpect(view().name("userTemplates/loginPage"));
    }
    @Test
    public void test403() throws Exception {
        mockMvc.perform(get("/403"))
                .andExpect(status().isOk())
                .andExpect(view().name("403Page"));
    }
}
