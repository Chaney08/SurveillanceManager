package org.geosatis.surveillancemanager.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.repository.ScheduleRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(
        locations = "classpath:applicationTest.properties")
public class ScheduleControllerTests {

    @Resource
    private ScheduleRepository scheduleRepo;

    @Before
    public void setup() {

    }

    @Test
    public void scheduleRegistration() {
        Date fromDate = new Date();
        Date toDate = new Date();
        Schedule schedule = new Schedule();
        schedule.setScheduleName("TestRegistration");
        schedule.setDescription("This is a description");
        schedule.setFromDate(fromDate);
        schedule.setFromDate(toDate);
        scheduleRepo.save(schedule);

        //The save is done already so we just need to verify it worked
        Schedule schedule2 = scheduleRepo.findByScheduleName("TestRegistration");
        assertEquals("TestRegistration", schedule2.getScheduleName());
    }
    @Test
    public void deleteSchedule() {
        Schedule schedule = new Schedule();
        schedule.setScheduleName("TestDeletion");
        schedule.setDescription("This is a description");
        scheduleRepo.save(schedule);
        scheduleRepo.deleteScheduleByScheduleId(schedule.getScheduleId());
        assertNull(scheduleRepo.findByScheduleName("TestDeletion"));
    }
    @Test
    public void scheduleUpdate(){
        Schedule schedule = new Schedule();
        schedule.setScheduleName("TestUpdate");
        schedule.setDescription("This is a description");
        scheduleRepo.save(schedule);

        Schedule schedule2 = scheduleRepo.findByScheduleName("TestUpdate");
        assertEquals("TestUpdate", schedule2.getScheduleName());

        schedule.setScheduleName("Changed name");
        scheduleRepo.save(schedule);
        Schedule schedule3 = scheduleRepo.findByScheduleName("Changed name");
        assertNull(scheduleRepo.findByScheduleName("TestUpdate"));
        assertEquals("Changed name", schedule3.getScheduleName());
    }

}
