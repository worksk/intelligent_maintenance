package com.message.user_message.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.common.parent_model.common.Constant;
import com.message.user_message.entity.JWTMessage;
import com.common.parent_model.common.CodeEnum;
import com.common.parent_model.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 21:41
 * @Description: Json web token 身份验证
 */
@Slf4j
public class JWTUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 密钥, 不可暴露
    private static final String secret = "CooCaa";

    /**
     * 生成 token
     */
    public static String createTokenWithClaim(JWTMessage jwtMessage) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Map<String, Object> map = new HashMap<>();
            jwtMessage.setNowDate(jwtMessage.getLastLoginTime());
            jwtMessage.setExpireDate(DateUtil.getAfterDate(jwtMessage.getNowDate(),0,0,0,2,0,0));//2小过期
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            String token = JWT.create()
                    /*设置头部信息 Header*/
                    .withHeader(map)
                    /*设置 载荷 Payload*/
                    .withClaim("id", jwtMessage.getId())
                    .withIssuer(jwtMessage.getIssuer())//签名是有谁生成 例如 服务器
                    .withSubject(simpleDateFormat.format(jwtMessage.getLastLoginTime()))//签名的主题
                    .withAudience(jwtMessage.getAudience())//签名的观众 也可以理解谁接受签名的
                    .withIssuedAt(jwtMessage.getNowDate()) //生成签名的时间
                    .withExpiresAt(jwtMessage.getExpireDate())//签名过期的时间
                    /*签名 Signature */
                    .sign(algorithm);

            log.info("create token: {} at time: {}", jwtMessage, simpleDateFormat.format(jwtMessage.getLastLoginTime()));
            return token;
        } catch (JWTCreationException e){
            throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 验证 token
     */
    public static String verifyToken(JWTMessage jwtMessage) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtMessage.getIssuer())
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(jwtMessage.getToken());
            String subject = jwt.getSubject();
            Map<String, Claim> claims = jwt.getClaims();
            Integer userId = claims.get("id").asInt();
//            String audience = jwt.getAudience().get(0);

            checkClaim(subject, userId, jwtMessage);
        } catch (TokenExpiredException e){
            log.error("token 过期");
            return Constant.JWT_TIMEOUT;
        } catch (Exception e){
            log.error("JWT Verify error: {}", e.getMessage());
            throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), "token 验证失败, 请重新登录");
        }

        return Constant.JWT_VERIFY_SUCCESS;
    }

    /**
     * 参数对比验证
     */
    private static void checkClaim(String lastLoginTime, Integer userId, JWTMessage jwtMessage){
        if(!lastLoginTime.equals(simpleDateFormat.format(jwtMessage.getLastLoginTime())) || !userId.equals(jwtMessage.getId())){
            throw new BaseException(CodeEnum.PARAM_ILLEGAL.getCode(), "token 验证失败, 请重新登录");
        }
        log.info("JWT verify successfully");
    }

}
