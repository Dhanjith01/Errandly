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

import com.errandly.Errandly.User.DTO.RunnerRequestDTO;
import com.errandly.Errandly.User.DTO.UserRequestDTO;
import com.errandly.Errandly.User.Repository.UserRepository;

import tools.jackson.databind.ObjectMapper;



@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private UserRequestDTO userRequest;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        userRequest = UserRequestDTO.builder()
                .email("integration@gmail.com")
                .name("Integration Test")
                .phone("9999999999")
                .password("password123")
                .studentId("21920121")
                .build();
    }

    
    @Test
    void createUser_shouldPersistUserInDatabase() throws Exception {
        String json=objectMapper.writeValueAsString(userRequest);
        System.out.println(json);
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(userRequest.getEmail()))
                .andExpect(jsonPath("$.name").value(userRequest.getName()));

        assertTrue(userRepository.findByEmail(userRequest.getEmail()).isPresent());

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {

        String response = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andReturn().getResponse().getContentAsString();

        Long userId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userRequest.getEmail()));
    }


    @Test
    void becomeRunner_shouldAssignRunnerSuccessfully() throws Exception {

        String response = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andReturn().getResponse().getContentAsString();

        Long userId = objectMapper.readTree(response).get("id").asLong();

        RunnerRequestDTO runnerRequest = RunnerRequestDTO.builder()
                .zone("Zone A")
                .build();

        mockMvc.perform(post("/user/" + userId + "/becomeRunner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(runnerRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void disableUser_shouldDisableUserSuccessfully() throws Exception{
        String response=mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                        .andReturn().getResponse().getContentAsString();
        Long userId=objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(patch("/user/"+userId+"/disable"))
                .andExpect(status().isNoContent());
        
        RunnerRequestDTO runnerRequest = RunnerRequestDTO.builder()
                .zone("Zone A")
                .build();

        mockMvc.perform(post("/user/" + userId + "/becomeRunner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(runnerRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void enableUser_shouldEnableUserSuccessfully() throws Exception{
        String response=mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                        .andReturn().getResponse().getContentAsString();
        Long userId=objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(patch("/user/"+userId+"/disable"))
                .andExpect(status().isNoContent());
        
        RunnerRequestDTO runnerRequest = RunnerRequestDTO.builder()
                .zone("Zone A")
                .build();

        mockMvc.perform(post("/user/" + userId + "/becomeRunner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(runnerRequest)))
                .andExpect(status().isInternalServerError());

        mockMvc.perform(patch("/user/"+userId+"/enable"))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/user/" + userId + "/becomeRunner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(runnerRequest)))
                .andExpect(status().isCreated());
        
    }
}
