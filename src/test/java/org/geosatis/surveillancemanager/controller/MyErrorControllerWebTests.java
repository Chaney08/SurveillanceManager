package org.geosatis.surveillancemanager.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//This test class tests that all pages are loaded when required
public class MyErrorControllerWebTests {

    //Although no mock objects to inject here I want to keep all test pages to a similar format - Could just have put errorController = new MyErrorController() instead of injectmocks.
    @InjectMocks
    private MyErrorController errorController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(errorController).build();
    }

    @Test
    public void testErrorPage() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("errorPage"));
    }
}
