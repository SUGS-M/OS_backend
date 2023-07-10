package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.model.Experience;
import com.example.backend.service.ExperienceService;
import com.example.backend.mapper.ExperienceMapper;
import org.springframework.stereotype.Service;

/**
* @author 18599
* @description 针对表【experience(八股表)】的数据库操作Service实现
* @createDate 2023-07-10 13:56:34
*/
@Service
public class ExperienceServiceImpl extends ServiceImpl<ExperienceMapper, Experience>
    implements ExperienceService{

}




