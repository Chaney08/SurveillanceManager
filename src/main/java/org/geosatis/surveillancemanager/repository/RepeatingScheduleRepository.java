package org.geosatis.surveillancemanager.repository;

import org.geosatis.surveillancemanager.model.RepeatingSchedule;
import org.geosatis.surveillancemanager.model.ScheduleExcemption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RepeatingScheduleRepository extends CrudRepository<RepeatingSchedule, Integer> {
   RepeatingSchedule findByRepeatingScheduleId(Long id);
}
