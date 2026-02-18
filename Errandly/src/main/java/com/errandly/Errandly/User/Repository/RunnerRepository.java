package com.errandly.Errandly.User.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.errandly.Errandly.User.Entity.Runner;

public interface RunnerRepository extends JpaRepository<Runner,Long> {
    
}
