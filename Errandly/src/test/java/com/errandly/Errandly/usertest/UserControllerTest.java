package com.errandly.Errandly.usertest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.errandly.Errandly.User.Controller.UserController;
import com.errandly.Errandly.User.DTO.RunnerRequestDTO;
import com.errandly.Errandly.User.DTO.UserRequestDTO;
import com.errandly.Errandly.User.DTO.UserResponseDTO;
import com.errandly.Errandly.User.Entity.Roles;
import com.errandly.Errandly.User.Service.RunnerService;
import com.errandly.Errandly.User.Service.UserService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RunnerService runnerService;

    //create user
    @Test
    void createUser_shouldReturn201_andUserResponse() throws Exception {

        UserRequestDTO request = UserRequestDTO.builder()
                .email("test@vit.ac.in")
                .name("Dhanjith")
                .phone("9999999999")
                .password("password123")
                .studentId("23BCE001")
                .build();

        UserResponseDTO response = UserResponseDTO.builder()
                .id(1L)
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .studentId(request.getStudentId())
                .roles(Set.of(Roles.CUSTOMER))
                .build();
        when(userService.createUser(any(UserRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/user")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@vit.ac.in"))
                .andExpect(jsonPath("$.name").value("Dhanjith"));

        verify(userService).createUser(any(UserRequestDTO.class));
    }

    //getUser
    @Test
    void getUser_shouldReturn200_andUserResponse() throws Exception {

        UserResponseDTO response = UserResponseDTO.builder()
                .id(1L)
                .email("test@vit.ac.in")
                .name("Dhanjith")
                .studentId("23BCE001")
                .roles(Set.of(Roles.CUSTOMER))
                .build();

        when(userService.getUser(1L)).thenReturn(response);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@vit.ac.in"));

        verify(userService).getUser(1L);
    }

    //becomeRunner
    @Test
    void becomeRunner_shouldReturn201() throws Exception {

        RunnerRequestDTO request = RunnerRequestDTO.builder()
                .zone("Zone A")
                .build();

        doNothing().when(userService)
                .becomeRunner(eq(1L), any(RunnerRequestDTO.class));

        mockMvc.perform(post("/user/1/becomeRunner")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(userService).becomeRunner(eq(1L), any(RunnerRequestDTO.class));
    }

    //enable and disable runner
     @Test
    void enableRunner_shouldReturn204() throws Exception {

        doNothing().when(runnerService).enableRunner(1L);

        mockMvc.perform(post("/user/1/runner/enable"))
                .andExpect(status().isNoContent());

        verify(runnerService).enableRunner(1L);
    }

    @Test
    void disableRunner_shouldReturn204() throws Exception {

        doNothing().when(runnerService).disableRunner(1L);

        mockMvc.perform(post("/user/1/runner/disable"))
                .andExpect(status().isNoContent());

        verify(runnerService).disableRunner(1L);
    }

}
