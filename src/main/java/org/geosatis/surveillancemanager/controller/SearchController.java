package org.geosatis.surveillancemanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geosatis.surveillancemanager.model.RepeatingSchedule;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public HashMap<String, String> scheduleRegistration(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime searchDate, @RequestParam String areaName) {
        HashMap<String, String> returnedMap = new HashMap<>();
        boolean mustBeConfined = false;

        if(!areaName.isEmpty()){
            Schedule schedule = scheduleRepo.findByScheduleName(areaName);

            if (schedule != null) {
                LocalDate startDate = schedule.getStartDate().toLocalDate();
                //We add 1 extra day here as datesUntl excludes last day
                LocalDate endDate = schedule.getEndDate().toLocalDate().plusDays(1);
                //Create a list with all days between our 2 dates
                List<LocalDate> dateList = startDate.datesUntil(endDate)
                        .collect(Collectors.toList());

                List<LocalDate> excemptionDateList = schedule.getScheduleExcemptions().stream()
                        .map(x -> x.getExcemptionDate())
                        .collect(Collectors.toList());

                //If our search lands on an excemption, just put mustBeConfined = false as it will be false regardless if its a repeating schedule or not.
                if(excemptionDateList.contains(searchDate.toLocalDate())){
                    mustBeConfined = false;
                }else {
                    //We are now working with a repeating schedule so we need to look at the repeat schedule information
                    if (schedule.getRepeatingSchedule() != null) {
                        //I like seperating into individual variables, although it takes more lines of code, I think it makes it easier to read what is going on
                        RepeatingSchedule repeatingSchedule = schedule.getRepeatingSchedule();
                        LocalTime searchTime = searchDate.toLocalTime();
                        LocalTime startRepeatingTime = repeatingSchedule.getScheduleStartTime();
                        LocalTime endRepeatingTime = repeatingSchedule.getScheduleEndTime();

                        if (searchTime.isAfter(startRepeatingTime) && searchTime.isBefore(endRepeatingTime)) {
                            String searchedDay = searchDate.getDayOfWeek().toString();

                            if (repeatingSchedule.getRepeatingDays().contains(searchedDay)) {
                                mustBeConfined = true;
                            }
                        }
                    }
                    //We are not dealing with a repeat schedule and instead dealing with a normal date range
                    else {

                        //Remove all of the points where the arrays intersect - In this case we are removing all of the excemptions
                        //TODO: Dont think this is actually neccessary since I moved the check for excemption date above but not enough time to remove and safely test
                        List<LocalDate> finalDateList = dateList.stream()
                                .distinct()
                                .filter(x -> !excemptionDateList.contains(x))
                                .collect(Collectors.toList());

                        LocalDate findDate = searchDate.toLocalDate();
                        if (finalDateList.contains(findDate)) {
                            mustBeConfined = true;

                        }
                    }
                }
            }else{
                returnedMap.put("error","There is no schedule with that name on the system");
                return returnedMap;
            }

        }else{
            //TODO: If I have time implement another search that will search all schedules and look for the searchDate(Assuming areaName is blank)
        }

        if(mustBeConfined){
            returnedMap.put("mustBeConfined", "true");
        }else{
            returnedMap.put("mustBeConfined", "false");
        }
        return returnedMap;
    }

}
