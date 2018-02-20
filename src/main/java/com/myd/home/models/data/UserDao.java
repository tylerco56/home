package com.myd.home.models.data;

import com.myd.home.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by Tyler Langenfeld
 */

@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer> {

    User findByEmail(String email);
}
