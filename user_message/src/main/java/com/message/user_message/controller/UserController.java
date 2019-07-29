package com.message.user_message.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.message.user_message.entity.User;
import com.message.user_message.entity.UserMessage;
import com.message.user_message.entity.dto.UserDTO;
import com.message.user_message.service.IUserService;
import com.message.user_message.util.Pbkdf2Util;
import com.common.parent_model.common.CodeEnum;
import com.common.parent_model.common.ReturnData;
import com.common.parent_model.exception.BaseException;
import com.common.parent_model.utils.BindingResultUtil;
import com.common.parent_model.utils.HelpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/addUser")
    @Transactional
    public ReturnData addUser(@RequestBody @Valid User user, BindingResult br){
        if(br.hasErrors()){
            BindingResultUtil.logError(br);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(userService.getOne(queryWrapper.eq("phone", user.getPhone())) != null){
            throw new BaseException(CodeEnum.PARAM_ILLEGAL.getCode(), "手机号重复, 请重试");
        }
        user.setSalt(Pbkdf2Util.getSalt());
        user.setPassword(Pbkdf2Util.encode(user.getPassword(), user.getSalt()));

        boolean resultUser = userService.insert(user);

        if(resultUser){
            UserMessage userMessage = UserMessage.builder().email("").nickName(user.getPhone().substring(7, 11)).id(user.getId()).build();
            boolean resultUserMessage = userService.insertMessage(userMessage);

            if(resultUserMessage){
                return ReturnData.success();
            }
        }

        throw new BaseException(CodeEnum.DATABASE_OPERATE_FAILURE.getCode(), CodeEnum.DATABASE_OPERATE_FAILURE.getMsg());
    }

    @GetMapping("/deleteUser/{id}")
    @Transactional
    public ReturnData deleteUser(@PathVariable("id") Integer id){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        boolean removeMessage = userService.deleteMessage(id);
        boolean removeUser = userService.remove(queryWrapper);


        return HelpUtil.checkDBResult(removeMessage && removeUser);
    }

    @PostMapping("/updatePassword")
    public ReturnData updatePassword(@RequestBody @Valid User user, BindingResult br){
        if(br.hasErrors()){
            BindingResultUtil.logError(br);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        User pastUser = userService.getOne(queryWrapper.eq("phone", user.getPhone()));

        user.setPassword(Pbkdf2Util.encode(user.getPassword(), pastUser.getSalt()));

        boolean result = userService.updatePassword(user);

        return HelpUtil.checkDBResult(result);
    }

    @PostMapping("/updateMessage")
    public ReturnData updateMessage(@RequestBody @Valid UserMessage message, BindingResult br){
        if(br.hasErrors()){
            BindingResultUtil.logError(br);
        }

        if(HelpUtil.isStringEmpty(message.getEmail()) && HelpUtil.isStringEmpty(message.getNickName()) || message.getId() == null){
            throw new BaseException(CodeEnum.PARAM_ILLEGAL.getCode(), CodeEnum.PARAM_ILLEGAL.getMsg());
        }

        boolean result = userService.updateMessage(message);

        return HelpUtil.checkDBResult(result);
    }

    @PostMapping("/updatePhone")
    public ReturnData updatePhone(@RequestBody User user){

        if(HelpUtil.isStringEmpty(user.getPhone()) || user.getId() == null){
            throw new BaseException(CodeEnum.PARAM_ILLEGAL.getCode(), CodeEnum.PARAM_ILLEGAL.getMsg());
        }

        //TODO 手机号连通性测试
        boolean result = userService.updatePhone(user);

        return HelpUtil.checkDBResult(result);
    }

    @GetMapping("/searchOwnMessage/{id}")
    public ReturnData searchOwnMessage(@PathVariable("id") Integer id){
        UserDTO userDTO = userService.searchOwn(id);
        return HelpUtil.checkEmpty(userDTO);
    }

    @GetMapping("/searchAllUsers/{current}/{size}")
    public ReturnData searchAllUsers(@PathVariable("current") Integer current, @PathVariable("size") Integer size){
        Page<UserDTO> page = new Page();
        page.setSize(size);
        page.setCurrent(current);

        IPage<UserDTO> users = userService.searchAllUsers(page);

        return HelpUtil.checkEmpty(users);
    }

}
