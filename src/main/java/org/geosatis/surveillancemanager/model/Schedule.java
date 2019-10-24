package org.geosatis.surveillancemanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule")
public class Schedule {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long scheduleId;

    @NotBlank(message = "Schedule Name is mandatory")
    private String scheduleName;
    private String description;
    //Dates require NotNull as NotBlank is for Strings only
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message= "Start date is mandatory")
    private LocalDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message= "To date is mandatory")
    private LocalDateTime endDate;


    @CreationTimestamp
    private LocalDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name="userId")
    @JsonManagedReference(value="user")
    private User user;

    @OneToMany(mappedBy="schedule")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonBackReference(value="excemption")
    private List<ScheduleExcemption> scheduleExcemptions;

    @OneToOne(mappedBy="schedule")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonBackReference(value="repeating")
    private RepeatingSchedule repeatingSchedule;



    public Schedule(){}

    public void addInvoiceRow(ScheduleExcemption newExcemption) {
        if(scheduleExcemptions == null){
            scheduleExcemptions = new ArrayList<>();
        }
        this.scheduleExcemptions.add(newExcemption);
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public List<ScheduleExcemption> getScheduleExcemptions() {
        return scheduleExcemptions;
    }

    public void setScheduleExcemptions(List<ScheduleExcemption> scheduleExcemptions) {
        this.scheduleExcemptions = scheduleExcemptions;
    }

    public RepeatingSchedule getRepeatingSchedule() {
        return repeatingSchedule;
    }

    public void setRepeatingSchedule(RepeatingSchedule repeatingSchedule) {
        this.repeatingSchedule = repeatingSchedule;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", scheduleName='" + scheduleName + '\'' +
                ", description='" + description + '\'' +
                ", endDate=" + endDate +
                ", startDate=" + startDate +
                ", createDateTime=" + createDateTime +
                ", user=" + user +
                '}';
    }
}
