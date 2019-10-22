package org.geosatis.surveillancemanager.model;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

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
    //Dates require NotNull as NotBlank ios for Strings only
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @NotNull(message= "To date is mandatory")
    private Date toDate;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @NotNull(message= "From date is mandatory")
    private Date fromDate;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;


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

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", scheduleName='" + scheduleName + '\'' +
                ", description='" + description + '\'' +
                ", toDate=" + toDate +
                ", fromDate=" + fromDate +
                ", createDateTime=" + createDateTime +
                ", user=" + user +
                '}';
    }
}
