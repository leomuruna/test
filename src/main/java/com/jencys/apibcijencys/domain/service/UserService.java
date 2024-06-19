package com.jencys.apibcijencys.domain.service;

import com.jencys.apibcijencys.dto.UserRequestDTO;
import com.jencys.apibcijencys.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequestDTO userRequestDTO);
}
