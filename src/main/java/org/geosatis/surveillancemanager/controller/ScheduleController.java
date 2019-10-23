package org.geosatis.surveillancemanager.controller;

import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.model.ScheduleExcemption;
import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.ScheduleExcemptionRepository;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepo;

    @Autowired
    private ScheduleExcemptionRepository scheduleExcemptionRepo;

    @Autowired
    private WebUtils webUtils;

    //Create this schedule here so we can dynamically add to it
    Schedule schedule = new Schedule();

    @RequestMapping(value = { "/", "/scheduleDashboard"})
    public String scheduleDashboard(Model model) {
        //Get the user and load their owned schedules to the page
        User user = webUtils.getUser();
        model.addAttribute("schedules", user.getSchedules());
        return "scheduleTemplates/scheduleDashboard";
    }
    @GetMapping(value = "/scheduleRegistration")
    public String scheduleRegistration(Model model) {
        schedule = new Schedule();
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

            //Excemption rows schedule is not automatically set on frontend - So we set here and then save to DB
            schedule.getScheduleExcemptions().stream().forEach(x-> x.setSchedule(schedule));
            schedule.getScheduleExcemptions().stream().forEach(x -> scheduleExcemptionRepo.save(x));

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
        schedule = scheduleRepo.findScheduleByScheduleId(idForEditing);
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
        schedule = scheduleRepo.findScheduleByScheduleId(idForViewing);

        LocalDate startDate = schedule.getStartDate();
        //We add 1 extra day here as datesUntl excludes last day
        LocalDate endDate = schedule.getEndDate().plusDays(1);

        //We want to return a list of all excemption dates
        List<LocalDate> excemptionDateList = schedule.getScheduleExcemptions().stream()
                .map(x -> x.getExcemptionDate())
                .collect(Collectors.toList());

        //Create a list with all days between our 2 dates
        List<LocalDate> dateList = startDate.datesUntil(endDate)
                .collect(Collectors.toList());

        //Remove all of the points where the arrays intersect - In this case we are removing all of the excemptions
        List<LocalDate> returnedDateList = dateList.stream()
                .distinct()
                .filter( x -> !excemptionDateList.contains(x))
                .collect(Collectors.toList());

        //This is only usable in Java 11 - I left it in because...well, trying to show off Java knowledge in this project...
        /*List<LocalDate> returnedDateList = dateList.stream()
                .distinct()
                .filter(Predicate.not(excemptionDateList::contains))
                .collect(Collectors.toList());*/

        model.addAttribute("monitoredDates", returnedDateList);
        return "scheduleTemplates/scheduleView";
    }
}
