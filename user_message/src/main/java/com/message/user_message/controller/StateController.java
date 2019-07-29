package com.message.user_message.controller;

import com.common.parent_model.common.ReturnData;
import com.common.parent_model.utils.BindingResultUtil;
import com.message.user_message.entity.User;
import com.message.user_message.entity.dto.JWTDTO;
import com.message.user_message.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Auther: wyx
 * @Date: 2019-07-26 13:41
 * @Description: 登录控制器
 */
@RestController
@RequestMapping("/state")
public class StateController {

    @Autowired
    private StateService stateService;

    @PostMapping("/login")
    public ReturnData login(@RequestBody @Valid User user, BindingResult br){
        if(br.hasErrors()){
            BindingResultUtil.logError(br);
        }

        JWTDTO jwtdto = stateService.login(user);
        return ReturnData.success(jwtdto);
    }

    @GetMapping("/logout")
    public ReturnData logout(){
        stateService.logout();
        return ReturnData.success();
    }

}
