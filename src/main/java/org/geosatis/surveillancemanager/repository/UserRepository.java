package org.geosatis.surveillancemanager.repository;

import org.geosatis.surveillancemanager.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserName(String username);
}
