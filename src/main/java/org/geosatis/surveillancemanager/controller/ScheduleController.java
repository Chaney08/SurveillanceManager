package org.geosatis.surveillancemanager.controller;

import org.geosatis.surveillancemanager.model.RepeatingSchedule;
import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.model.ScheduleExcemption;
import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.ScheduleExcemptionRepository;
import org.geosatis.surveillancemanager.repository.ScheduleRepository;
import org.geosatis.surveillancemanager.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @RequestMapping(value = { "/", "/scheduleDashboard"})
    public String scheduleDashboard(Model model) {
        //Get the user and load their owned schedules to the page
        User user = webUtils.getUser();
        model.addAttribute("schedules", user.getSchedules());
        return "scheduleTemplates/scheduleDashboard";
    }
    @GetMapping(value = "/scheduleRegistration")
    public String scheduleRegistration(Model model) {
        Schedule schedule = new Schedule();
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

            if(schedule.getScheduleExcemptions() != null &&  schedule.getScheduleExcemptions().size() > 0) {
                //Excemption rows schedule is not automatically set on frontend - So we set here and then save to DB
                schedule.getScheduleExcemptions().stream().forEach(x -> x.setSchedule(schedule));
                schedule.getScheduleExcemptions().stream().forEach(x -> scheduleExcemptionRepo.save(x));
            }

            return "redirect:/schedule/scheduleDashboard";
        }
    }
    @Transactional
    @RequestMapping(value = "/deleteSchedule", method = RequestMethod.GET)
    public String deleteSchedule(@RequestParam(name="scheduleId") long idForDeletion) {
        Schedule schedule = scheduleRepo.findScheduleByScheduleId(idForDeletion);
        //Need to delete all the excemtions before deleting Schedule, not sure why this is not done automatically but not enough time to find out
        scheduleExcemptionRepo.deleteScheduleExcemptionBySchedule(schedule);
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
    @Transactional
    public String updateSchedule(@ModelAttribute("scheduleUpdate") @Valid Schedule schedule,
                             BindingResult result) {

        if (result.hasErrors()) {
            return "scheduleTemplates/scheduleUpdate";
        }else {
            schedule.setUser(webUtils.getUser());
            scheduleRepo.save(schedule);

            if(schedule.getScheduleExcemptions() != null &&  schedule.getScheduleExcemptions().size() > 0) {
                //Excemption rows schedule is not automatically set on frontend - So we set here and then save to DB
                schedule.getScheduleExcemptions().stream().forEach(x -> x.setSchedule(schedule));
                //Although not implemented on frontend yet - This delete is so we can remove all excemptions on the DB and only save the ones remaining on the frontend - So if the user deletes
                //an excemption on the frontend this will ensure that the information is persisted as the deletion is only done on form submit and not via an ajax call
                schedule.getScheduleExcemptions().stream().forEach(x -> scheduleExcemptionRepo.deleteScheduleExcemptionByExcemptionDate(x.getExcemptionDate()));
                schedule.getScheduleExcemptions().stream().forEach(x -> scheduleExcemptionRepo.save(x));
            }

            return "redirect:/schedule/scheduleDashboard";
        }

    }

    //A method for viewing a calendor on the front end that only shows days the schedule is active - Excemptions are excluded
    @GetMapping(value = "/viewSchedule")
    public String viewSchedule(Model model,@RequestParam(name="scheduleId") int idForViewing) {
        Schedule schedule = scheduleRepo.findScheduleByScheduleId(idForViewing);

        LocalDate startDate = schedule.getStartDate().toLocalDate();
        //We add 1 extra day here as datesUntl excludes last day
        LocalDate endDate = schedule.getEndDate().toLocalDate().plusDays(1);

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
