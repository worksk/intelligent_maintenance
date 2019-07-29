package com.message.user_message.entity.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 21:44
 * @Description: 封装返回给前端的 JWT 必要信息
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JWTDTO {

    @NotNull(message = "userId 不能为空")
    private Integer id;

    @NotNull(message = "token 不能为空")
    private String token;

}
