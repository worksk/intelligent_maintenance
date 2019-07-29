package com.message.user_message.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage extends Model<UserMessage> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nickName;

    private String email;

    @TableLogic
    private Integer logic;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
