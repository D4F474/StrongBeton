<<<<<<< HEAD
package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
}
=======
package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
}
>>>>>>> fda96bb (Add Dockerized backend and MySQL setup)
