package com.errandly.Errandly.user.service;

import com.errandly.Errandly.user.dto.RunnerRequestDTO;
import com.errandly.Errandly.user.dto.UserRequestDTO;
import com.errandly.Errandly.user.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO request);
    void becomeRunner(Long id,RunnerRequestDTO request);
    UserResponseDTO getUser(Long id);
    void disableUser(Long id);
    public void enableUser(Long id);
}
