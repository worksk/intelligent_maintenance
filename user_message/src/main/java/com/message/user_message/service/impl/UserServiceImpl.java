package com.message.user_message.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.message.user_message.entity.User;
import com.message.user_message.entity.UserMessage;
import com.message.user_message.entity.dto.UserDTO;
import com.message.user_message.mapper.user.UserMapper;
import com.message.user_message.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${mybatis-plus.global-config.db-config.logic-delete-value}")
    private Integer logic;

    @Override
    public Boolean insertMessage(UserMessage message) {
        return userMapper.insert(message);
    }

    @Override
    public Boolean insert(User user) {
        return userMapper.insertReturnId(user);
    }

    @Override
    public Boolean deleteMessage(Integer id) {
        return userMapper.deleteMessage(id, logic);
    }

    @Override
    public Boolean updatePassword(User user) {
        return userMapper.updatePassword(user);
    }

    @Override
    public Boolean updatePhone(User user) {
        return userMapper.updatePhone(user);
    }

    @Override
    public Boolean updateMessage(UserMessage message) {
        return userMapper.updateMessage(message);
    }

    @Override
    public UserDTO searchOwn(Integer id) {
        return userMapper.selectUser(id);
    }

    @Override
    public IPage<UserDTO> searchAllUsers(Page page) {
        return userMapper.selectAllUsers(page);
    }
}
