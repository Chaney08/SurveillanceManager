package org.geosatis.surveillancemanager.controller;

import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.ScheduleRepository;
import org.geosatis.surveillancemanager.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepo;

    @Autowired
    private WebUtils webUtils;

    @RequestMapping(value = { "/", "/scheduleDashboard"})
    public String scheduleDashboard(Model model) {
        //Get the user and load their owned schedules to the page
        User user = webUtils.getUser();
        model.addAttribute("schedules", user.getSchedules());
        return "scheduleTemplates/scheduleDashboard";
    }
    @GetMapping(value = "/scheduleRegistration")
    public String scheduleRegistration(Model model) {
        Schedule schedule= new Schedule();
        model.addAttribute("scheduleRegistration", schedule);
        return "scheduleTemplates/scheduleRegistration";
    }

    @PostMapping(value = "/scheduleRegistration")
    public String scheduleRegistration(@ModelAttribute("scheduleRegistration") @Valid Schedule schedule,
                                      BindingResult result) {

        Schedule existing = scheduleRepo.findByScheduleName(schedule.getScheduleName());
        if (existing != null) {
            result.rejectValue("scheduleName", null, "There is already a schedule with that name");
        }

        if (result.hasErrors()) {
            return "scheduleTemplates/scheduleRegistration";
        }else {
            schedule.setUser(webUtils.getUser());
            scheduleRepo.save(schedule);
            return "redirect:/schedule/scheduleDashboard";
        }
    }

    @Transactional
    @RequestMapping(value = "/deleteSchedule", method = RequestMethod.GET)
    public String deleteSchedule(@RequestParam(name="scheduleId") long idForDeletion) {
        scheduleRepo.deleteScheduleByScheduleId(idForDeletion);
        return "redirect:/schedule/scheduleDashboard";
    }

    @GetMapping(value = "/scheduleUpdate")
    public String updateSchedule(Model model,@RequestParam(name="scheduleId") int idForEditing) {
        Schedule schedule = scheduleRepo.findScheduleByScheduleId(idForEditing);
        model.addAttribute("scheduleUpdate", schedule);
        return "scheduleTemplates/scheduleUpdate";
    }

    @PostMapping(value = "/scheduleUpdate")
    public String updateSchedule(@ModelAttribute("scheduleUpdate") @Valid Schedule schedule,
                             BindingResult result) {

        if (result.hasErrors()) {
            return "scheduleTemplates/scheduleUpdate";
        }else {
            schedule.setUser(webUtils.getUser());
            scheduleRepo.save(schedule);
            return "redirect:/schedule/scheduleDashboard";
        }

    }

    @GetMapping(value = "/viewSchedule")
    public String viewSchedule(Model model,@RequestParam(name="scheduleId") int idForViewing) {
        Schedule schedule = scheduleRepo.findScheduleByScheduleId(idForViewing);

        LocalDate startDate = schedule.getStartDate();
        //We add 1 extra day here as datesUntl excludes last day
        LocalDate endDate = schedule.getEndDate().plusDays(1);

        List<LocalDate> dateList = startDate.datesUntil(endDate)
                .collect(Collectors.toList());

        model.addAttribute("monitoredDates", dateList);
        return "scheduleTemplates/scheduleView";
    }


}
