package org.geosatis.surveillancemanager.controller;

import org.geosatis.surveillancemanager.model.RepeatingSchedule;
import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.model.ScheduleExcemption;
import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.RepeatingScheduleRepository;
import org.geosatis.surveillancemanager.repository.ScheduleExcemptionRepository;
import org.geosatis.surveillancemanager.repository.UserRepository;
import org.geosatis.surveillancemanager.utils.WebUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.geosatis.surveillancemanager.repository.ScheduleRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.io.IOException;
import java.nio.channels.NonWritableChannelException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(
        locations = "classpath:applicationTest.properties")
public class ScheduleAPIControllerTests {

    String localURL = "http://localhost:8080/scheduleAPI";

    @MockBean
    private ScheduleRepository scheduleRepo;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RepeatingScheduleRepository repeatingScheduleRepo;
    @MockBean
    private WebUtils webUtils;

    @InjectMocks
    private ScheduleAPIController scheduleApiController;
    private MockMvc mockMvc;

    User user = mock(User.class);
    Schedule schedule = mock(Schedule.class);

    String urlForAddingSchedule = localURL + "/addingSchedule";
    String urlForUpdatingSchedule = localURL + "/updateSchedule";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(scheduleApiController).build();
    }

    @Test
    public void testAddingScheduleHappyPath() throws Exception {

        doReturn(null).when(scheduleRepo).findByScheduleName(anyString());
        doReturn(user).when(webUtils).getUser(anyString());

        this.mockMvc.perform(post(urlForAddingSchedule)
                .param("areaName", "TestRegistration").param("userName","user1").param("startDate","2019-10-24T14:50:31")
                .param("endDate","2019-11-27T14:50:31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.Success").value("TestRegistration"));
    }

    @Test
    public void testAddingScheduleExistingRepo() throws Exception {

        doReturn(schedule).when(scheduleRepo).findByScheduleName(anyString());

        this.mockMvc.perform(post(urlForAddingSchedule)
                .param("areaName", "TestRegistration").param("userName","user1").param("startDate","2019-10-24T14:50:31")
                .param("endDate","2019-11-27T14:50:31"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.Error").value("A schedule with this name already exists, please change the name"));
    }

    @Test
    public void testAddingScheduleStartDateAfterEndDate() throws Exception {

        doReturn(null).when(scheduleRepo).findByScheduleName(anyString());
        doReturn(user).when(webUtils).getUser(anyString());


        this.mockMvc.perform(post(urlForAddingSchedule)
                .param("areaName", "TestRegistration").param("userName","user1").param("startDate","2019-10-24T14:50:31")
                .param("endDate","2019-09-27T14:50:31"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.Error").value("Please ensure the End date is after the start date"));
    }

    @Test
    public void testAddingScheduleBadStartDate() throws Exception {

        doReturn(null).when(scheduleRepo).findByScheduleName(anyString());
        doReturn(user).when(webUtils).getUser(anyString());


        this.mockMvc.perform(post(urlForAddingSchedule)
                .param("areaName", "TestRegistration").param("userName","user1").param("startDate","2")
                .param("endDate","2019-09-27T14:50:31"))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void testAddingScheduleBadEndDate() throws Exception {

        doReturn(null).when(scheduleRepo).findByScheduleName(anyString());
        doReturn(user).when(webUtils).getUser(anyString());


        this.mockMvc.perform(post(urlForAddingSchedule)
                .param("areaName", "TestRegistration").param("userName","user1").param("startDate","2019-10-24T14:50:31")
                .param("endDate","2"))
                .andExpect(status().is4xxClientError());
    }

}
