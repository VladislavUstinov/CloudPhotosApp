package com.cloudphotosapp.cloudphotosapp.repository;

import com.cloudphotosapp.cloudphotosapp.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
