package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.BusinessException;
import com.example.backend.common.ErrorCode;
import com.example.backend.model.User;
import com.example.backend.model.dto.user.UserLoginRequest;
import com.example.backend.model.vo.LoginUserVO;
import com.example.backend.service.UserService;
import com.example.backend.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;


/**
* @author 18599
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-07-09 16:41:58
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private static final String SALT = "user";

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.检验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号小于4字符");
        }
        if(userPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码小于8字符");
        }
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不一致");
        }
        //2.入库
        synchronized (userAccount.intern()){
            //2.1 账号不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount",userAccount);
            Long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
            }
            //2.2 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
            //2.3 创建新用户
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            //2.4 入库
            Boolean saveResult = this.save(user);
            if (!saveResult){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request ) {
        //1.检验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号错误");
        }
        if(userPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        }
        //2. 数据库匹配
        //2.1 密码加密==解密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //2.2 数据库查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        //Long count = this.baseMapper.selectCount(queryWrapper);
        //if (count == 0){
            //throw new BusinessException(ErrorCode.SYSTEM_ERROR,"用户不存在或输入错误");
        //}
        User user = this.baseMapper.selectOne(queryWrapper);
        if (user == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"用户不存在或输入错误");
        }
        //2.3 记录用户的登录态
        request.getSession().setAttribute("user_login", user);//必须更新用户态，注销需要的 toDo:拉一个用户常量表
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if(request.getSession().getAttribute("user_login") == null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"未登录");
        }
        request.getSession().removeAttribute("user_login");
        return true;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        //if(request.getSession().getAttribute("user_login") == null){
         //   throw new BusinessException(ErrorCode.OPERATION_ERROR,"未登录");
        //}
        Object userObj = request.getSession().getAttribute("user_login");
        User loginUser = (User) userObj;
        if (loginUser == null || loginUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //查询
        Long userId = loginUser.getId();
        loginUser = this.getById(userId);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return loginUser;
    }


}




