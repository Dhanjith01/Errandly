package com.errandly.Errandly.user.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.errandly.Errandly.user.entity.AvailabilityStatus;
import com.errandly.Errandly.user.entity.Runner;
import com.errandly.Errandly.user.repository.RunnerRepository;
import com.errandly.Errandly.exception.custom.RunnerNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Primary
public class RunnerServiceImpl implements RunnerService{
    private final RunnerRepository runnerRepository;
    
    @Override
    @Transactional
    public void enableRunner(Long id){
        Runner runner=runnerRepository.findById(id)
                                      .orElseThrow(()->new RunnerNotFoundException(id));        
        
        runner.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        
        runnerRepository.save(runner);
    }

    @Override
    @Transactional
    public void disableRunner(Long id){
        Runner runner=runnerRepository.findById(id)
                                      .orElseThrow(()->new RunnerNotFoundException(id));
        
        runner.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
        
        runnerRepository.save(runner);
    }
}
