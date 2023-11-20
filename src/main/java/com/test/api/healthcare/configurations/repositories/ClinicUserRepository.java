package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.ClinicUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClinicUserRepository extends JpaRepository<ClinicUser, Long>, JpaSpecificationExecutor<ClinicUser> {
    @Query(value = """
        SELECT u FROM ClinicUser u
        LEFT JOIN FETCH u.clinicUserImages cui
        WHERE u.id = :clinicUserId AND cui.isActive = true
        """)
    Optional<ClinicUser> findClinicUserById(@Param("clinicUserId") Long clinicUserId);
}

