package com.errandly.Errandly.User.Service;

import com.errandly.Errandly.User.DTO.BecomeRunnerRequest;
import com.errandly.Errandly.User.DTO.CreateUserRequest;
import com.errandly.Errandly.User.Entity.User;

public interface UserService {

    User createUser(CreateUserRequest request);
    void becomeRunner(Long id,BecomeRunnerRequest request);
    User getUser(Long id);
}
