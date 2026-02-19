package com.errandly.Errandly.User.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.errandly.Errandly.User.Entity.Runner;

@Repository
public interface RunnerRepository extends JpaRepository<Runner,Long> {
    
}
