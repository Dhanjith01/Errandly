package com.errandly.Errandly.User.Service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.errandly.Errandly.Exception.Custom.RunnerNotFoundException;
import com.errandly.Errandly.User.Entity.AvailabilityStatus;
import com.errandly.Errandly.User.Entity.Runner;
import com.errandly.Errandly.User.Repository.RunnerRepository;

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
