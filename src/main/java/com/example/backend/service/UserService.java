package com.example.backend.service;

import com.example.backend.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.model.dto.user.UserLoginRequest;
import com.example.backend.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 18599
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-07-09 16:41:58
*/
public interface UserService extends IService<User> {

    Long userRegister(String userAccount, String userPassword, String checkPassword);


    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);
}
