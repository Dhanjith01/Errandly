package com.errandly.Errandly.User.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.errandly.Errandly.User.DTO.RunnerRequestDTO;
import com.errandly.Errandly.User.DTO.UserRequestDTO;
import com.errandly.Errandly.User.DTO.UserResponseDTO;
import com.errandly.Errandly.User.Service.RunnerService;
import com.errandly.Errandly.User.Service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RunnerService runnerService;
    
    
    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO request){
        UserResponseDTO user=userService.createUser(request);
        return user;
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id){
        UserResponseDTO user=userService.getUser(id);
        return user;
    }

    @PostMapping("/{id}/becomeRunner")
    public void becomeRunner(@PathVariable Long id,@Valid @RequestBody RunnerRequestDTO request){
        userService.becomeRunner(id, request);
    }

    @PostMapping("/{id}/runner/enable")
    public void enableRunner(@PathVariable Long id){
        runnerService.enableRunner(id);
    }

    @PostMapping("/{id}/runner/disable")
    public void disableRunner(@PathVariable Long id){
        runnerService.disableRunner(id);
    }
}
