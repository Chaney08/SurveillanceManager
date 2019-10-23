package org.geosatis.surveillancemanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.repository.ScheduleRepository;
import org.geosatis.surveillancemanager.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/search")
public class SearchController {

    @Autowired
    ScheduleRepository scheduleRepo;

    @Autowired
    private WebUtils webUtils;

    @RequestMapping(value = "/searchByDate",produces = "application/json")
    @ResponseBody
    public HashMap<String, String> scheduleRegistration(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate, @RequestParam String areaName) {
        Schedule schedule = new Schedule();
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> returnedMap = new HashMap<>();

        if(!areaName.isEmpty()){
            schedule = scheduleRepo.findByScheduleName(areaName);

            if (schedule != null) {
                //We add 1 extra day here as datesUntl excludes last day
                LocalDate endDate = schedule.getEndDate().plusDays(1);
                //Create a list with all days between our 2 dates
                List<LocalDate> dateList = schedule.getStartDate().datesUntil(endDate)
                        .collect(Collectors.toList());

                if(dateList.contains(searchDate)){
                    returnedMap.put("mustBeConfined", "true");
                    returnedMap.put("scheduleInformation", schedule.toString());
                }else{
                    returnedMap.put("mustBeConfined", "false");
                    returnedMap.put("scheduleInformation",  schedule.toString());
                }


            }else{
                returnedMap.put("error","There is no schedule with that name on the system");
            }

        }
        return returnedMap;
    }

}
