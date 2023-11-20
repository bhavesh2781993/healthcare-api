package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.ClinicUserTask;
import com.test.api.healthcare.configurations.models.entities.ClinicUserTask_;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import jakarta.persistence.criteria.JoinType;

public interface ClinicUserTaskRepository
    extends JpaRepository<ClinicUserTask, Long>, JpaSpecificationExecutor<ClinicUserTask> {

    @Query("""
        SELECT cut
        FROM ClinicUserTask cut
        JOIN FETCH cut.patient p
        WHERE cut.id = :clinicUserTaskId
        """)
    Optional<ClinicUserTask> findClinicUserTaskAndPatient(Long clinicUserTaskId);

    interface Specs {
        /**
         * Eager fetch to avoid N + 1 query problem.
         * @return returns Specification.
         */
        static Specification<ClinicUserTask> eagerFetchPatient() {
            return (root, query, builder) -> {
                /*
                 * Join fetch should be applied only for query to fetch the "data", not for "count" query to do pagination.
                 * Handled this by checking the criteriaQuery.getResultType(),
                 * if it's long that means query is for count so not appending join fetch else append it.
                 */
                if (Long.class != query.getResultType()) {
                    root.fetch(ClinicUserTask_.PATIENT, JoinType.LEFT);
                }
                return builder.conjunction();
            };
        }
    }

}
