package org.geosatis.surveillancemanager.controller;

import org.geosatis.surveillancemanager.model.RepeatingSchedule;
import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.model.ScheduleExcemption;
import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.RepeatingScheduleRepository;
import org.geosatis.surveillancemanager.repository.ScheduleExcemptionRepository;
import org.geosatis.surveillancemanager.repository.ScheduleRepository;
import org.geosatis.surveillancemanager.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(path="/scheduleAPI")
public class ScheduleAPIController {


    @Autowired
    private ScheduleRepository scheduleRepo;
    @Autowired
    private ScheduleExcemptionRepository scheduleExcemptionRepo;
    @Autowired
    private RepeatingScheduleRepository repeatingScheduleRepo;

    @Autowired
    private WebUtils webUtils;


    //TODO: Having issues with passing in full objects to Controller - Not sure why as all examples I have seen work, not enough time to fix so going for messier but simpler option - Fix if possible
    @PostMapping(value = "/addingSchedule",produces = "application/json")
    @ResponseBody
    public HashMap<String,Object> addScheduleViaAPI(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                      @RequestParam String areaName,@RequestParam String userName,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> excemptionDates){

        HashMap<String,Object> returnedValue = new HashMap<String,Object>();

        Schedule schedule = new Schedule();
        schedule.setScheduleName(areaName);

        Schedule existing = scheduleRepo.findByScheduleName(areaName);
        if (existing != null) {
            returnedValue.put("Error","A schedule with this name already exists, please change the name");
        }
        else if(startDate.isAfter(endDate) || startDate.isEqual(endDate)){
            returnedValue.put("Error","Please ensure the End date is after the start date");
        }else {
            User user = webUtils.getUser(userName);
            if(user != null){
                schedule.setUser(user);
                schedule.setStartDate(startDate);
                schedule.setEndDate(endDate);

                schedule.setUser(user);
                scheduleRepo.save(schedule);

                saveExcemptions(schedule,excemptionDates);

                returnedValue.put("Success", schedule);
            }else{
                returnedValue.put("Error","That user does not exist");
            }
        }
        return returnedValue;

    }

    @PostMapping(value = "/updateSchedule",produces = "application/json")
    @ResponseBody
    public HashMap<String,Object> updateSchedule(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                 @RequestParam String areaName,
                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> excemptionDates){
        HashMap<String,Object> returnedValue = new HashMap<String,Object>();

        //Only check the end date as I would think you can update an ongoing schedule, for example, if the confinement has been extended.
        if(endDate.isBefore(LocalDateTime.now())){
            returnedValue.put("Error","Cannot update schedules in the past");
        }else{
            Schedule existing = scheduleRepo.findByScheduleName(areaName);
            if (existing != null) {
                existing.setStartDate(startDate);
                existing.setEndDate(endDate);
                scheduleRepo.save(existing);
                saveExcemptions(existing,excemptionDates);

                returnedValue.put("Success",existing);

            }else{
                returnedValue.put("Error","This schedule does not exist, please create it before updating it");
            }

        }

        return returnedValue;
    }

    @PostMapping(value = "/restApiAddRepeatingSchedule",produces = "application/json")
    @ResponseBody
    public HashMap<String,Object> addRepeatingSchedule(@RequestParam String areaName, @RequestParam LocalTime scheduleStartTime, @RequestParam LocalTime scheduleEndTime,
                                        @RequestParam String activeScheduleDays, @RequestParam String userName,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) List<LocalDate> excemptionDates){

        HashMap<String,Object> returnedValue = new HashMap<String,Object>();

        Schedule existing = scheduleRepo.findByScheduleName(areaName);
        if (existing != null) {
            returnedValue.put("Error","A schedule with this name already exists, please change the name");
            return returnedValue;
        }
        //Set details of Schedule and save
        Schedule schedule = new Schedule();
        schedule.setScheduleName(areaName);
        schedule.setStartDate(LocalDateTime.now());
        schedule.setEndDate(LocalDateTime.now());
        User user = webUtils.getUser(userName);

        if(user != null) {
            schedule.setUser(user);
            scheduleRepo.save(schedule);

            //Set details of Repeat Schedule and save
            RepeatingSchedule repeatingSchedule = new RepeatingSchedule();
            repeatingSchedule.setSchedule(schedule);
            repeatingSchedule.setScheduleStartTime(scheduleStartTime);
            repeatingSchedule.setScheduleEndTime(scheduleEndTime);
            repeatingSchedule.setRepeatingDays(activeScheduleDays);
            repeatingScheduleRepo.save(repeatingSchedule);

            saveExcemptions(schedule,excemptionDates);

            returnedValue.put("Success", schedule);
        }else{
            returnedValue.put("Error","That user does not exist");
        }

        return returnedValue;
    }

    //Duplicate code used 3 times, create a small method for it
    public void saveExcemptions(Schedule schedule, List<LocalDate> excemptionDates){
        if (excemptionDates != null && excemptionDates.size() > 0) {
            //Just loop through the excemptions and save if present
            excemptionDates.stream()
                    .forEach(x -> scheduleExcemptionRepo.save(new ScheduleExcemption(schedule, x)));
        }
    }
}
