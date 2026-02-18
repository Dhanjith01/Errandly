package com.errandly.Errandly.User.Service;

import org.springframework.stereotype.Service;

import com.errandly.Errandly.User.DTO.BecomeRunnerRequest;
import com.errandly.Errandly.User.DTO.CreateUserRequest;
import com.errandly.Errandly.User.Entity.Roles;
import com.errandly.Errandly.User.Entity.Runner;
import com.errandly.Errandly.User.Entity.User;
import com.errandly.Errandly.User.Repository.RunnerRepository;
import com.errandly.Errandly.User.Repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RunnerRepository runnerRepository;

    @Override
    @Transactional
    public User createUser(CreateUserRequest request){
        User user=User.builder()
                      .email(request.getEmail())
                      .studentId(request.getStudentId())
                      .name(request.getName())
                      .phone(request.getPhone())
                      .build();
        user.getRoles().add(Roles.CUSTOMER);

        return userRepository.save(user);
        
    }

    @Override
    @Transactional
    public void becomeRunner(Long id, BecomeRunnerRequest request){
        User user=userRepository.findById(id)
                                .orElseThrow(()->new RuntimeException("User Not Found"));
        
        if(user.getRunner()==null){
            Runner runner=Runner.builder().user(user).zone(request.getZone()).build();
            runnerRepository.save(runner);
            user.setRunner(runner);
        }
        userRepository.save(user);
    }

    @Override
    public User getUser(Long id){
        User user=userRepository.findById(id)
                                .orElseThrow(()->new RuntimeException("User not found"));
        return user;
    }

}
