package org.geosatis.surveillancemanager.repository;

import org.geosatis.surveillancemanager.model.ScheduleExcemption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleExcemptionRepository extends CrudRepository<ScheduleExcemption, Integer> {
    void deleteScheduleExcemptionByExcemptionId(long id);
    ScheduleExcemption findScheduleByExcemptionId(long id);
}
