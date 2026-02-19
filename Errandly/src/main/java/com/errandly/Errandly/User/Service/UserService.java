package com.errandly.Errandly.User.Service;

import com.errandly.Errandly.User.DTO.RunnerRequestDTO;
import com.errandly.Errandly.User.DTO.UserRequestDTO;
import com.errandly.Errandly.User.DTO.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO request);
    void becomeRunner(Long id,RunnerRequestDTO request);
    UserResponseDTO getUser(Long id);
    void disableUser(Long id);
    public void enableUser(Long id);
}
