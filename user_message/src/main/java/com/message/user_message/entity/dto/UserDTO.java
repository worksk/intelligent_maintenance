package com.message.user_message.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 20:48
 * @Description: 用户信息传输封装
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String phone;

    private String email;

    private String nickName;

}
