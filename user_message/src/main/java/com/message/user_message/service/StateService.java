package com.message.user_message.service;

import com.message.user_message.entity.User;
import com.message.user_message.entity.dto.JWTDTO;

/**
 * @Auther: wyx
 * @Date: 2019-07-26 10:59
 * @Description: 登录注销
 */
public interface StateService {

    JWTDTO login(User user);

    void logout();

}
