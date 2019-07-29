package com.message.user_message.mapper.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.message.user_message.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.message.user_message.entity.UserMessage;
import com.message.user_message.entity.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
@Mapper
@Component
public interface UserMapper extends BaseMapper<User> {

    Boolean insert(UserMessage userMessage);

    Boolean insertReturnId(User user);

    Boolean deleteMessage(Integer id, Integer logic);

    Boolean updatePassword(User user);

    Boolean updatePhone(User user);

    Boolean updateMessage(UserMessage message);

    UserDTO selectUser(Integer id);

    IPage<UserDTO> selectAllUsers(Page page);

    User selectPassword(String phone);

}
