package org.geosatis.surveillancemanager.controller;

import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.ScheduleRepository;
import org.geosatis.surveillancemanager.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/search")
public class SearchController {

    @Autowired
    ScheduleRepository scheduleRepo;

    @Autowired
    private WebUtils webUtils;

    @RequestMapping(value = { "/", "/searchByDate"})
    public String scheduleDashboard(Model model) {

        return "scheduleTemplates/scheduleDashboard";
    }
    @GetMapping(value = "/searchByDate")
    public String scheduleRegistration(Model model, @RequestParam LocalDate searchDate, @RequestParam String areaName, BindingResult result) {
        Schedule schedule = new Schedule();
        User user = webUtils.getUser();
        List<Schedule> allSchedules = user.getSchedules();

        if(areaName.isEmpty()){
            schedule = scheduleRepo.findByScheduleName(areaName);

            if (schedule != null) {
                //We add 1 extra day here as datesUntl excludes last day
                LocalDate endDate = schedule.getEndDate().plusDays(1);
                //Create a list with all days between our 2 dates
                List<LocalDate> dateList = schedule.getStartDate().datesUntil(endDate)
                        .collect(Collectors.toList());

                if(dateList.contains(searchDate)){
                    model.addAttribute("scheduleSearch", schedule);
                    return "searchTemplates/scheduleSearch";
                }else{
                    result.rejectValue("searchDate", null, "That date was not found");
                }


            }else{
                result.rejectValue("areaName", null, "There is no schedule with that name");
            }

        }

        model.addAttribute("scheduleRegistration", schedule);
        return "scheduleTemplates/scheduleRegistration";
    }

}
