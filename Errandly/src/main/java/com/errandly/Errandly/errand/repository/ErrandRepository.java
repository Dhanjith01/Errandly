package com.errandly.Errandly.errand.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.errandly.Errandly.errand.entity.Errand;
import com.errandly.Errandly.errand.entity.ErrandStatus;
import com.errandly.Errandly.user.entity.Runner;

@Repository
public interface ErrandRepository extends JpaRepository<Errand,Long>{

    @Query("""
            SELECT e FROM Errand e WHERE (e.createdAt>:createdAt OR (e.createdAt=:createdAt AND e.id>:id))
            AND e.status=:status
            """)
    public Page<Errand> findByStatusAndCreatedAtGreaterThan(@Param("createdAt")LocalDateTime createdAt,
                                                            @Param("id")Long id,
                                                            @Param("status")ErrandStatus status,
                                                            Pageable pageable);
    @Query("""
            SELECT e FROM Errand e WHERE (e.createdAt<:createdAt OR (e.createdAt=:createdAt AND e.id<:id))
            AND e.status=:status
            """)
    public Page<Errand> findByStatusAndCreatedAtLessThan(@Param("createdAt")LocalDateTime createdAt,
                                                        @Param("id")Long id,
                                                        @Param("status")ErrandStatus status,
                                                        Pageable pageable);
    
    List<Errand> findByRunnerIdOrderByCreatedAtDesc(Long runnerId);
    List<Errand> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Errand> findByStatus(ErrandStatus status);

    @Modifying
    @Query("""
        UPDATE Errand e
        SET e.status = :newStatus,
            e.runner = :runner,
            e.acceptedAt = CURRENT_TIMESTAMP
        WHERE e.id = :errandId
          AND e.status = :expectedStatus
    """)
    int atomicAccept(
            @Param("errandId") Long errandId,
            @Param("expectedStatus") ErrandStatus expectedStatus,
            @Param("newStatus") ErrandStatus newStatus,
            @Param("runner") Runner runner
    );

     @Query("""
        SELECT e FROM Errand e
        WHERE e.status = :status
          AND e.createdAt <= :expiryTime
    """)
    List<Errand> findExpiredErrands(
            @Param("status") ErrandStatus status,
            @Param("expiryTime") LocalDateTime expiryTime
    );

    @Query("""
        SELECT e FROM Errand e
        WHERE e.runner.id = :runnerId
          AND e.status IN :statuses
    """)
    Page<Errand> findActiveErrandsForRunner(
            @Param("runnerId") Long runnerId,
            @Param("statuses") List<ErrandStatus> statuses,
            Pageable pageable
    );

}
