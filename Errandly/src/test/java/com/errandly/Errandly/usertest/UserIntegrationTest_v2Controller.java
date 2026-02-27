package com.errandly.Errandly.usertest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.errandly.Errandly.exception.custom.UserNotFoundException;
import com.errandly.Errandly.security.dto.AuthRequest;
import com.errandly.Errandly.user.dto.RunnerRequestDTO;
import com.errandly.Errandly.user.dto.UserRequestDTO;
import com.errandly.Errandly.user.entity.Roles;
import com.errandly.Errandly.user.entity.User;
import com.errandly.Errandly.user.repository.UserRepository;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserIntegrationTest_v2Controller {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private UserRequestDTO userRequest;

    private String token;
    private Long userId;

    @BeforeEach
    void setup() throws Exception{
        userRepository.deleteAll();

        userRequest = UserRequestDTO.builder()
                .email("integration@gmail.com")
                .name("Integration Test")
                .phone("9999999999")
                .password("password123")
                .studentId("21920121")
                .build();
        
        String json=objectMapper.writeValueAsString(userRequest);
        System.out.println(json);
        String userresponse = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andReturn().getResponse().getContentAsString();

        userId = objectMapper.readTree(userresponse).get("id").asLong();

        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
        user.getRoles().add(Roles.ADMIN);
        userRepository.save(user);

        AuthRequest request = new AuthRequest();
        request.setEmail("integration@gmail.com");
        request.setPassword("password123");
        String response=mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andReturn().getResponse().getContentAsString();
        token=objectMapper.readTree(response).get("token").asString();
        System.out.println(token);
    }

    
    @Test
    void createUser_shouldThrowDuplicatResourceError() throws Exception {
        String json=objectMapper.writeValueAsString(userRequest);
        System.out.println(json);
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                        .andExpect(status().isConflict());

        assertTrue(userRepository.findByEmail(userRequest.getEmail()).isPresent());
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        System.out.println("Bearer "+token);

        mockMvc.perform(get("/user/" + userId).header("Authorization", "Bearer "+token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userRequest.getEmail()));
    }


    @Test
    void becomeRunner_shouldAssignRunnerSuccessfully() throws Exception {
        RunnerRequestDTO runnerRequest = RunnerRequestDTO.builder()
                .zone("Zone A")
                .build();

        mockMvc.perform(post("/user/" + userId + "/becomeRunner").header("Authorization", "Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(runnerRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void disableUser_shouldDisableUserSuccessfully() throws Exception{

        mockMvc.perform(patch("/user/"+userId+"/disable").header("Authorization", "Bearer "+token))
                .andExpect(status().isNoContent());
        
        RunnerRequestDTO runnerRequest = RunnerRequestDTO.builder()
                .zone("Zone A")
                .build();

        mockMvc.perform(post("/user/" + userId + "/becomeRunner")
                        .header("Authorization", "Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(runnerRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void enableUser_shouldEnableUserSuccessfully() throws Exception{
        mockMvc.perform(patch("/user/"+userId+"/disable").header("Authorization", "Bearer "+token))
                .andExpect(status().isNoContent());
        
        RunnerRequestDTO runnerRequest = RunnerRequestDTO.builder()
                .zone("Zone A")
                .build();

        mockMvc.perform(post("/user/" + userId + "/becomeRunner").header("Authorization", "Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(runnerRequest)))
                .andExpect(status().isInternalServerError());

        mockMvc.perform(patch("/user/"+userId+"/enable").header("Authorization", "Bearer "+token))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/user/" + userId + "/becomeRunner").header("Authorization", "Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(runnerRequest)))
                .andExpect(status().isCreated());
        
    }

}
