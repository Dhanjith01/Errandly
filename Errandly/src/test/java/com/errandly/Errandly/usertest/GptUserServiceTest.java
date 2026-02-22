package com.errandly.Errandly.usertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.errandly.Errandly.Exception.Custom.DuplicateResourceException;
import com.errandly.Errandly.Exception.Custom.UserNotFoundException;
import com.errandly.Errandly.User.DTO.RunnerRequestDTO;
import com.errandly.Errandly.User.DTO.UserRequestDTO;
import com.errandly.Errandly.User.DTO.UserResponseDTO;
import com.errandly.Errandly.User.Entity.AvailabilityStatus;
import com.errandly.Errandly.User.Entity.Roles;
import com.errandly.Errandly.User.Entity.Runner;
import com.errandly.Errandly.User.Entity.User;
import com.errandly.Errandly.User.Repository.UserRepository;
import com.errandly.Errandly.User.Service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class GptUserServiceTest {
     @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO userRequest;
    private User user;

    @BeforeEach
    void setUp() {
        userRequest = UserRequestDTO.builder()
                .email("hello@gmail.com")
                .name("Hello")
                .phone("9999999999")
                .password("password123")
                .studentId("21910201")
                .build();

        user = User.builder()
                .id(1L)
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .phone(userRequest.getPhone())
                .studentId(userRequest.getStudentId())
                .password("encodedPassword")
                .enabled(true)
                .build();

        user.setRoles(Set.of(Roles.CUSTOMER));
    }

    //User Creation
    @Test
    void createUser_shouldCreateUserSuccessfully_whenValidRequest() {

        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByStudentId(userRequest.getStudentId())).thenReturn(false);
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.createUser(userRequest);

        assertNotNull(response);
        assertEquals(userRequest.getEmail(), response.getEmail());
        assertEquals(userRequest.getName(), response.getName());
        assertTrue(response.getRoles().contains(Roles.CUSTOMER));

        verify(passwordEncoder).encode(userRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_shouldThrowException_whenEmailAlreadyExists() {

        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> userService.createUser(userRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldThrowException_whenStudentIdAlreadyExists() {

        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByStudentId(userRequest.getStudentId())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> userService.createUser(userRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldSaveEncodedPassword() {

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByStudentId(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(userRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    //getUser
    @Test
    void getUser_shouldReturnUser_whenUserExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUser(1L);

        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getName(), response.getName());
        assertTrue(response.getRoles().contains(Roles.CUSTOMER));

        verify(userRepository).findById(1L);
    }

    @Test
    void getUser_shouldThrowException_whenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(1L));
    }

    //enable & disable user
    @Test
    void enableUser_shouldSetEnabledTrue_whenUserExists() {

        user.setEnabled(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.enableUser(1L);

        assertTrue(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void disableUser_shouldSetEnabledFalse_whenUserExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.disableUser(1L);

        assertFalse(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void enableUser_shouldThrowException_whenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.enableUser(1L));
    }

    @Test
    void disableUser_shouldThrowException_whenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.disableUser(1L));
    }


    //becomeRunner
    @Test
    void becomeRunner_shouldCreateRunner_whenUserEnabledAndNotRunner() {

        RunnerRequestDTO request = RunnerRequestDTO.builder()
                .zone("Zone A")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.becomeRunner(1L, request);

        assertNotNull(user.getRunner());
        assertEquals("Zone A", user.getRunner().getZone());
        assertEquals(AvailabilityStatus.UNAVAILABLE, user.getRunner().getAvailabilityStatus());

        verify(userRepository).save(user);
    }

     @Test
    void becomeRunner_shouldThrowException_whenUserDisabled() {

        user.setEnabled(false);

        RunnerRequestDTO request = RunnerRequestDTO.builder()
                .zone("Zone A")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class,
                () -> userService.becomeRunner(1L, request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void becomeRunner_shouldThrowException_whenAlreadyRunner() {

        user.setRunner(
                Runner.builder()
                        .user(user)
                        .zone("Zone A")
                        .availabilityStatus(AvailabilityStatus.UNAVAILABLE)
                        .build()
        );

        RunnerRequestDTO request = RunnerRequestDTO.builder()
                .zone("Zone B")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> userService.becomeRunner(1L, request));

        verify(userRepository, never()).save(any());
    }
}
