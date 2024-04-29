package com.tsfn.sec.repository;

import com.tsfn.sec.model.UserRole;
import com.tsfn.sec.model.UserRoleId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    // You can add custom query methods here if needed
}
