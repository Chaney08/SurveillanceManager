package org.geosatis.surveillancemanager.controller;

import org.junit.Before;
import org.junit.Test;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.ScheduleRepository;
import org.geosatis.surveillancemanager.utils.WebUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//This test class tests that all pages are loaded when required
public class ScheduleControllerWebTests {

    @Mock
    private WebUtils webUtils;
    @Mock
    private ScheduleRepository scheduleRepo;

    @InjectMocks
    private ScheduleController scheduleController;

    private MockMvc mockMvc;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @Test
    public void testDashboard() throws Exception {
        User user = new User();
        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList.add(new Schedule());
        user.setSchedules(scheduleList);

        when(webUtils.getUser()).thenReturn((User) user);
           mockMvc.perform(get("/schedule/"))
                .andExpect(status().isOk())
                .andExpect(view().name("scheduleTemplates/scheduleDashboard"))
                .andExpect(model().attribute("schedules",hasSize(1)));
    }

    @Test
    public void testRegistration() throws Exception {
        mockMvc.perform(get("/schedule/scheduleRegistration"))
                .andExpect(status().isOk())
                .andExpect(view().name("scheduleTemplates/scheduleRegistration"))
                .andExpect(model().attribute("scheduleRegistration",instanceOf(Schedule.class)));
    }

    @Test
    public void testScheduleUpdate() throws Exception {
        Long scheduleId = 1l;
        Schedule schedule = new Schedule();

        when(scheduleRepo.findScheduleByScheduleId(scheduleId)).thenReturn((Schedule) schedule);
        mockMvc.perform(get("/schedule/scheduleUpdate?scheduleId=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("scheduleTemplates/scheduleUpdate"))
                .andExpect(model().attribute("scheduleUpdate",instanceOf(Schedule.class)));
    }


}
