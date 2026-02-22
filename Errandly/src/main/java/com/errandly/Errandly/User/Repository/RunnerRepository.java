package com.errandly.Errandly.User.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.errandly.Errandly.User.Entity.AvailabilityStatus;
import com.errandly.Errandly.User.Entity.Runner;

@Repository
public interface RunnerRepository extends JpaRepository<Runner,Long> {
    Optional<Runner> findById(Long id);
    
    List<Runner> findALLByAvailabilityStatus(AvailabilityStatus status);
}
