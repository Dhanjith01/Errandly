package com.errandly.Errandly.User.Service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.errandly.Errandly.Exception.Custom.DuplicateResourceException;
import com.errandly.Errandly.Exception.Custom.UserNotFoundException;
import com.errandly.Errandly.User.DTO.RunnerRequestDTO;
import com.errandly.Errandly.User.DTO.RunnerResponseDTO;
import com.errandly.Errandly.User.DTO.UserRequestDTO;
import com.errandly.Errandly.User.DTO.UserResponseDTO;
import com.errandly.Errandly.User.Entity.AvailabilityStatus;
import com.errandly.Errandly.User.Entity.Roles;
import com.errandly.Errandly.User.Entity.Runner;
import com.errandly.Errandly.User.Entity.User;
import com.errandly.Errandly.User.Repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Primary
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        if (userRepository.existsByStudentId(request.getStudentId())) {
            throw new DuplicateResourceException("Student ID already exists");
        }

        User user=User.builder()
                      .email(request.getEmail())
                      .studentId(request.getStudentId())
                      .name(request.getName())
                      .phone(request.getPhone())
                      .password(passwordEncoder.encode(request.getPassword()))
                      .enabled(true)
                      .build();
        user.getRoles().add(Roles.CUSTOMER);

        User temp= userRepository.save(user);
        
        return mapResponseDTO(temp);
    }

    @Override
    @Transactional
    public void becomeRunner(Long id, RunnerRequestDTO request){
        User user=userRepository.findById(id)
                                .orElseThrow(()->new UserNotFoundException(id));
        
        if(!user.isEnabled()) {
            throw new IllegalStateException("Disabled user cannot become runner");
        }

        if(user.getRunner()==null){
            Runner runner=Runner.builder()
                                .user(user)
                                .zone(request.getZone())
                                .availabilityStatus(AvailabilityStatus.UNAVAILABLE)
                                .build();

            //runnerRepository.save(runner);   //not needed as cascade saves runner
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
                                .orElseThrow(()->new UserNotFoundException(id));
        

        return mapResponseDTO(user);
    }

    @Override
    @Transactional
    public void disableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void enableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setEnabled(true);
        userRepository.save(user);
    }

    private UserResponseDTO mapResponseDTO(User user){

        RunnerResponseDTO runnerinfo=null;

        if(user.getRunner()!=null){
            runnerinfo=RunnerResponseDTO.builder()
                                        .zone(user.getRunner().getZone())
                                        .availabilityStatus(user.getRunner().getAvailabilityStatus())
                                        .build();
        }

        UserResponseDTO response=UserResponseDTO.builder()
        .id(user.getId())
        .name(user.getName())
        .studentId(user.getStudentId())
        .reputationscore(user.getReputationscore())
        .email(user.getEmail())
        .phone(user.getPhone())
        .roles(user.getRoles())
        .runner(runnerinfo)
        .build();
        return response;
    }
}
