package com.wuyue.pojo.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 功能描述:重置、修改账号密码数据封装;
 */
@NoArgsConstructor
@Setter
@Getter
public class ChangePassword {
    private String userId;       // 账号id;
    private String oldPassword;  // 原密码;
    private String newPassword;  // 新密码;


}
