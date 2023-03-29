package com.gbsfo.ecommerce.repository.security;

import java.util.Optional;

import com.gbsfo.ecommerce.domain.security.ERole;
import com.gbsfo.ecommerce.domain.security.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}