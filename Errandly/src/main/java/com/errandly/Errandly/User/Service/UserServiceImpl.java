package com.errandly.Errandly.User.Service;

import org.springframework.stereotype.Service;

import com.errandly.Errandly.User.DTO.BecomeRunnerRequest;
import com.errandly.Errandly.User.DTO.CreateUserRequest;
import com.errandly.Errandly.User.DTO.UserResponseDTO;
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
    public UserResponseDTO createUser(CreateUserRequest request){
        User user=User.builder()
                      .email(request.getEmail())
                      .studentId(request.getStudentId())
                      .name(request.getName())
                      .phone(request.getPhone())
                      .build();
        user.getRoles().add(Roles.CUSTOMER);

        User temp= userRepository.save(user);
        UserResponseDTO response=UserResponseDTO.builder()
        .name(temp.getName())
        .studentId(temp.getStudentId())
        .reputationscore(temp.getReputationscore())
        .email(temp.getEmail())
        .phone(temp.getPhone())
        .build();

        return response;
        
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
            userRepository.save(user);
        }
        else{
            throw new RuntimeException("User is already a Runner");
        }
        
    }

    @Override
    public UserResponseDTO getUser(Long id){
        User user=userRepository.findById(id)
                                .orElseThrow(()->new RuntimeException("User not found"));
        UserResponseDTO response=UserResponseDTO.builder()
        .name(user.getName())
        .studentId(user.getStudentId())
        .reputationscore(user.getReputationscore())
        .email(user.getEmail())
        .phone(user.getPhone())
        .build();

        return response;
    }

}
