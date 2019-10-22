package org.geosatis.surveillancemanager.repository;

import org.geosatis.surveillancemanager.model.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Integer> {
    Schedule findByScheduleName(String name);
    void deleteScheduleByScheduleId(long id);
    Schedule findScheduleByScheduleId(long id);
}
