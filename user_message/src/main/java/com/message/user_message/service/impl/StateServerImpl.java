package com.message.user_message.service.impl;

import com.alibaba.fastjson.JSON;
import com.common.parent_model.common.CodeEnum;
import com.common.parent_model.common.Constant;
import com.common.parent_model.exception.BaseException;
import com.message.user_message.common.RequestHolder;
import com.message.user_message.entity.JWTMessage;
import com.message.user_message.entity.User;
import com.message.user_message.entity.dto.JWTDTO;
import com.message.user_message.mapper.user.UserMapper;
import com.message.user_message.service.StateService;
import com.message.user_message.util.JWTUtil;
import com.message.user_message.util.Pbkdf2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Auther: wyx
 * @Date: 2019-07-26 10:59
 * @Description:
 */
@Service
public class StateServerImpl implements StateService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private RedisHelper redisHelper;

    @Override
    public JWTDTO login(User user) {
        redisHelper = new RedisHelper(redisTemplate);
        User savedUser = userMapper.selectPassword(user.getPhone());
        user.setId(savedUser.getId());
        Boolean result = Pbkdf2Util.verification(user.getPassword(), savedUser.getSalt(), savedUser.getPassword());
        if(!result){
            throw new BaseException(CodeEnum.PARAM_ILLEGAL.getCode(), "用户名或密码错误");
        }

        JWTMessage jwtMessage = new JWTMessage();
        jwtMessage.setLastLoginTime(new Date());
        jwtMessage.setId(user.getId());
        String token = JWTUtil.createTokenWithClaim(jwtMessage);
        jwtMessage.setToken(token);
        redisHelper.set(Constant.REDIS_USER_PREFFIX + user.getId(), JSON.toJSONString(jwtMessage));
        redisHelper.expire(Constant.REDIS_USER_PREFFIX + user.getId(), 60 * 60 * 12 * 7);

        JWTDTO jwtdto = new JWTDTO();
        jwtdto.setId(user.getId());
        jwtdto.setToken(token);

        return jwtdto;
    }

    @Override
    public void logout() {
        redisHelper = new RedisHelper(redisTemplate);
        redisHelper.del(Constant.REDIS_USER_PREFFIX + RequestHolder.getCurrentUserId());
    }
}
