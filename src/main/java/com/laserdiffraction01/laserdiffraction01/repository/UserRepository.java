package com.laserdiffraction01.laserdiffraction01.repository;

import com.laserdiffraction01.laserdiffraction01.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
