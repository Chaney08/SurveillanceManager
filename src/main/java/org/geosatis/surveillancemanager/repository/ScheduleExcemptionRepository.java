package org.geosatis.surveillancemanager.repository;

import org.geosatis.surveillancemanager.model.Schedule;
import org.geosatis.surveillancemanager.model.ScheduleExcemption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ScheduleExcemptionRepository extends CrudRepository<ScheduleExcemption, Integer> {
    void deleteScheduleExcemptionByExcemptionDate(LocalDate date);
    void deleteScheduleExcemptionBySchedule(Schedule schedule);
    ScheduleExcemption findScheduleByExcemptionId(long id);
}
