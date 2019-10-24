package org.geosatis.surveillancemanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduleExemption ")
public class ScheduleExcemption {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long excemptionId;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @NotNull(message= "Exception is mandatory")
    private LocalDate excemptionDate;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name="scheduleId")
    @JsonBackReference(value="excemption")
    private Schedule schedule;

    public ScheduleExcemption(){}

    public ScheduleExcemption(Schedule schedule, LocalDate excemptionDate){
        this.schedule = schedule;
        this.excemptionDate = excemptionDate;
    }

    public Long getExcemptionId() {
        return excemptionId;
    }
    public LocalDate getExcemptionDate() {
        return excemptionDate;
    }

    public void setExcemptionDate(LocalDate excemptionDate) {
        this.excemptionDate = excemptionDate;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
