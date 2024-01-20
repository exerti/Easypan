package com.CloudPan.service.impl;

import com.CloudPan.entity.User;
import com.CloudPan.mapper.UserMapper;
import com.CloudPan.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wxz
 * @since 2024-01-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    public User login(String username, String password) {
      User user =
          baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username).eq(User::getPassword, password));
      if(user!=null){
          return user;
      }
        return  null;
    }

}
