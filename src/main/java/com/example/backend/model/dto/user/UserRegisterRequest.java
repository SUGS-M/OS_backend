package com.example.backend.model.dto.user;

import com.example.backend.service.UserService;
import lombok.Data;

@Data
public class UserRegisterRequest {

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
