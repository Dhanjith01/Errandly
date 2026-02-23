package com.errandly.Errandly.usertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.errandly.Errandly.exception.custom.DuplicateResourceException;
import com.errandly.Errandly.exception.custom.UserNotFoundException;
import com.errandly.Errandly.user.dto.UserRequestDTO;
import com.errandly.Errandly.user.dto.UserResponseDTO;
import com.errandly.Errandly.user.entity.Roles;
import com.errandly.Errandly.user.entity.User;
import com.errandly.Errandly.user.repository.UserRepository;
import com.errandly.Errandly.user.service.UserServiceImpl;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    private User user;
    private UserRequestDTO request;
    @BeforeEach
    public void setup(){
        request=UserRequestDTO.builder()
                              .email("null")
                              .name("dji")
                              .phone("9999999999")
                              .password("dji")
                              .studentId("23bce")
                              .build();
        when(passwordEncoder.encode(anyString())).thenReturn("abcdefg");
        user=User.builder()
                 .email(request.getEmail())
                 .studentId(request.getStudentId())
                 .name(request.getName())
                 .phone(request.getPhone())
                 .password(passwordEncoder.encode(request.getPassword()))
                 .enabled(true)
                 .build();
        user.getRoles().add(Roles.CUSTOMER);
        
    }

    @Test
    public void shouldCreateUserSuccessfully(){
        
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByStudentId(anyString())).thenReturn(false);
        

        

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO respons=userService.createUser(request);

        assertEquals(request.getName(), respons.getName());
        assertEquals(request.getEmail(), respons.getEmail());
        assertEquals(request.getPhone(), respons.getPhone());
        assertEquals(request.getStudentId(), respons.getStudentId());

        verify(passwordEncoder,times(2)).encode(request.getPassword());

    }
    @Test
    public void shouldThrowDuplicateResourceException_CreateUser(){
        
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        //when(userRepository.existsByStudentId(anyString())).thenReturn(false);

        assertThrows(DuplicateResourceException.class,()->userService.createUser(request));

    }

    @Test
    public void shouldgetUserSuccessfully(){
        
        when(passwordEncoder.encode(anyString())).thenReturn("abcdefg");

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UserResponseDTO respons=userService.getUser(1L);

        assertEquals(request.getName(), respons.getName());
        assertEquals(request.getEmail(), respons.getEmail());
        assertEquals(passwordEncoder.encode(request.getPassword()), "abcdefg");
        assertEquals(request.getPhone(), respons.getPhone());
        assertEquals(request.getStudentId(), respons.getStudentId());

    }

    @Test
    public void shouldThrowUserNotFound_getUser(){
        
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userService.getUser(1L));

    }

    @Test
    void enableUser_shouldSetEnabledTrue_whenUserExists() {
        user.setEnabled(false);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.enableUser(1L);

        assertTrue(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void disableUser_shouldSetEnabledTrue_whenUserExists() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.disableUser(1L);

        assertFalse(user.isEnabled());
        verify(userRepository).save(user);
    }
}
