package org.geosatis.surveillancemanager.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "repeatingSchedule ")
public class RepeatingSchedule {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long repeatingScheduleId;

    @Column
    private LocalTime scheduleStartTime;
    @Column
    private LocalTime scheduleEndTime;
    @Column
    private String repeatingDays;

    @OneToOne
    @JoinColumn(name="scheduleId")
    @JsonManagedReference(value="repeating")
    private Schedule schedule;


    public LocalTime getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(LocalTime scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    public LocalTime getScheduleEndTime() {
        return scheduleEndTime;
    }

    public void setScheduleEndTime(LocalTime scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }

    public String getRepeatingDays() {
        return repeatingDays;
    }

    public void setRepeatingDays(String repeatingDays) {
        this.repeatingDays = repeatingDays;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
