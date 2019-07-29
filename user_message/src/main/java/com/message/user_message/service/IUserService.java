package com.message.user_message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.message.user_message.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.message.user_message.entity.UserMessage;
import com.message.user_message.entity.dto.UserDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
public interface IUserService extends IService<User> {

    /**
     * 插入用户信息
     * @param message
     * @return
     */
    Boolean insertMessage(UserMessage message);

    /**
     * 插入数据并返回其自增 ID
     * @param user
     * @return
     */
    Boolean insert(User user);

    /**
     * 删除用户信息
     * @param id
     * @return
     */
    Boolean deleteMessage(Integer id);

    /**
     * 根据手机号修改密码
     * @param user
     * @return
     */
    Boolean updatePassword(User user);

    /**
     * 根据用户 ID 修改手机号
     * @param user
     * @return
     */
    Boolean updatePhone(User user);

    /**
     * 修改用户邮箱或昵称
     * @param message
     * @return
     */
    Boolean updateMessage(UserMessage message);

    /**
     * 根据 ID 获取用户信息
     * @param id
     * @return
     */
    UserDTO searchOwn(Integer id);

    /**
     * 获取全部用户
     * @return
     */
    IPage<UserDTO> searchAllUsers(Page page);

}
