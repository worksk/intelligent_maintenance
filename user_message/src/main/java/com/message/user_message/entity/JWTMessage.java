package com.message.user_message.entity;

import com.message.user_message.entity.dto.JWTDTO;
import lombok.*;

import java.util.Date;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 21:44
 * @Description: 生成 JWT 所需信息封装
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JWTMessage extends JWTDTO {

    private final String issuer = "CoocaaServer";

    private final String audience = "CoocaaClient";

    private String subject;

    private Date nowDate;

    private Date expireDate;

    private Date lastLoginTime;

}