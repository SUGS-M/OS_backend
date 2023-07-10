package com.example.backend.mapper;

import com.example.backend.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 18599
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-07-09 16:41:58
* @Entity com.example.backend.model.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




