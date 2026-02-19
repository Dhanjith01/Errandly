package com.errandly.Errandly.User.Service;

import com.errandly.Errandly.User.DTO.BecomeRunnerRequest;
import com.errandly.Errandly.User.DTO.CreateUserRequest;
import com.errandly.Errandly.User.DTO.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(CreateUserRequest request);
    void becomeRunner(Long id,BecomeRunnerRequest request);
    UserResponseDTO getUser(Long id);
}
