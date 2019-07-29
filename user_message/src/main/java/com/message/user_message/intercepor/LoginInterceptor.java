package com.message.user_message.intercepor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.parent_model.common.CodeEnum;
import com.common.parent_model.common.Constant;
import com.common.parent_model.exception.BaseException;
import com.message.user_message.common.RequestHolder;
import com.message.user_message.entity.JWTMessage;
import com.message.user_message.service.impl.RedisHelper;
import com.message.user_message.util.ClassUtil;
import com.message.user_message.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Auther: wyx
 * @Date: 2019-07-26 11:41
 * @Description:
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean skipResult = skipInterceptor(request.getRequestURI().trim());
        if(skipResult){
            return true;
        }

        String token = getToken(request);
        Integer userId = getId(request);

        RedisTemplate redisTemplate = ClassUtil.getBean("redisTemplate", RedisTemplate.class);
        RedisHelper redisHelper = new RedisHelper(redisTemplate);

        JWTMessage redisJWTMessage = getJWTMessageFromRedis(redisHelper, userId);

        String result = verifyToken(userId, token, redisJWTMessage);

        if(result.equals(Constant.JWT_VERIFY_SUCCESS)){
            RequestHolder.addUserId(userId);
            return true;
        } else if (result.equals(Constant.JWT_TIMEOUT)){
            refresh(redisJWTMessage, redisHelper, response);
            RequestHolder.addUserId(userId);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private Boolean skipInterceptor(String url){
        return url.contains("/login");
    }

    private String getToken(HttpServletRequest request){
        String token = request.getHeader("token");
        if(token == null){
            throw new BaseException(CodeEnum.PARAM_ILLEGAL.getCode(), "token 不能为空");
        }

        return token;
    }

    private Integer getId(HttpServletRequest request){
        String userIdStr = request.getHeader("userId");
        if(userIdStr == null || userIdStr.equals("")){
            throw new BaseException(CodeEnum.PARAM_ILLEGAL.getCode(), "用户 id 不能为空");
        }
        Integer userId = Integer.parseInt(userIdStr);

        return userId;
    }

    private JWTMessage getJWTMessageFromRedis(RedisHelper redisHelper, Integer userId){
        Object o = redisHelper.get(Constant.REDIS_USER_PREFFIX + userId);
        if (o == null){
            throw new BaseException(CodeEnum.TOKEN_INVALID.getCode(), CodeEnum.TOKEN_INVALID.getMsg());
        }
        return JSONObject.parseObject(String.valueOf(o), JWTMessage.class);
    }

    private String verifyToken(Integer userId, String token, JWTMessage redisJWTMessage){
        JWTMessage jwtMessage = new JWTMessage();
        jwtMessage.setId(userId);
        jwtMessage.setToken(token);
        jwtMessage.setLastLoginTime(redisJWTMessage.getLastLoginTime());

        return JWTUtil.verifyToken(jwtMessage);
    }

    private void refresh(JWTMessage redisJWTMessage, RedisHelper redisHelper, HttpServletResponse response){
        redisJWTMessage.setLastLoginTime(new Date());
        String newToken = JWTUtil.createTokenWithClaim(redisJWTMessage);
        redisJWTMessage.setToken(newToken);
        redisHelper.set(Constant.REDIS_USER_PREFFIX + redisJWTMessage.getId(), JSON.toJSONString(redisJWTMessage));

        response.setHeader("newToken", newToken);
    }

}
