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
import java.time.LocalDate;
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
    @NotBlank(message = "Description is mandatory")
    private String description;
    //Dates require NotNull as NotBlank is for Strings only
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @NotNull(message= "From date is mandatory")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @NotNull(message= "To date is mandatory")
    private LocalDate endDate;


    @CreationTimestamp
    private LocalDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name="userId")
    @JsonManagedReference
    private User user;

    @OneToMany(mappedBy="schedule")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonBackReference
    private List<ScheduleExcemption> scheduleExcemptions;

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

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<ScheduleExcemption> getScheduleExcemptions() {
        return scheduleExcemptions;
    }

    public void setScheduleExcemptions(List<ScheduleExcemption> scheduleExcemptions) {
        this.scheduleExcemptions = scheduleExcemptions;
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
