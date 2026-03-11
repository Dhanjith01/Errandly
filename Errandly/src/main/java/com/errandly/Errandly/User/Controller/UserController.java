package com.errandly.Errandly.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

import com.errandly.Errandly.user.service.RunnerService;
import com.errandly.Errandly.user.dto.RunnerRequestDTO;
import com.errandly.Errandly.user.dto.UserRequestDTO;
import com.errandly.Errandly.user.dto.UserResponseDTO;
import com.errandly.Errandly.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//@RestController
//@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RunnerService runnerService;
    
    
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request){
        UserResponseDTO user=userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id){
        UserResponseDTO user=userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/becomeRunner")
    public ResponseEntity<Void> becomeRunner(@PathVariable Long id,@Valid @RequestBody RunnerRequestDTO request){
        userService.becomeRunner(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/runner/available")
    public ResponseEntity<Void> availableRunner(@PathVariable Long id){
        runnerService.availableRunner(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/runner/unavailable")
    public ResponseEntity<Void> unavailableRunner(@PathVariable Long id){
        runnerService.unavailableRunner(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> enableUser(@PathVariable Long id){
        userService.enableUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable Long id){
        userService.disableUser(id);
        return ResponseEntity.noContent().build();
    }
}
