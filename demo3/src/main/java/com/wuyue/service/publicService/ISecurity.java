package com.wuyue.service.publicService;

/**
 * 说明： 系统安全接口
 */
public interface ISecurity {

    /**
     * 说明： 密码加密
     */
    String pwdEncrypt(String pwd);

    String tokenEncrypt(String token);

    String tokenDecrypt(String encryptToken);

    /**
     * 说明： 生成随机码
     */
    String generalCode(String key, Integer length, Integer expireTime);

    /**
     * 说明： 检查随机码是否有效
     */
    Boolean checkCode(String key, String code);
}
