package com.wuyue.service.publicService.impl;

import com.wuyue.Util.EncrUtil;
import com.wuyue.Util.NumberUtil;
import com.wuyue.Util.RedisUtils;
import com.wuyue.common.Cons;
import com.wuyue.service.publicService.ISecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SecurityService implements ISecurity {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String pwdEncrypt(String pwd) {
        //避免撞库
        pwd = pwd + Cons.encryptKey;
        pwd = EncrUtil.GetMD5Code(pwd);

        return pwd;
    }

    @Override
    public String tokenEncrypt(String token) {
        String encryptFirst = EncrUtil.AESEncrypt(token);
        return EncrUtil.AESEncrypt(encryptFirst);
    }

    @Override
    public String tokenDecrypt(String encryptToken) {
        String encryptFirst = EncrUtil.AESDecrypt(encryptToken);
        return EncrUtil.AESDecrypt(encryptFirst);
    }

    public String generalCode(String key, Integer length, Integer expireTime) {
        String checkCode = NumberUtil.getRandNum(length);
        boolean isSet = redisUtils.set(key,checkCode,expireTime);
        if(isSet){
            return checkCode;
        }
        return null;

    }

    @Override
    public Boolean checkCode(String key, String code) {
        if(redisUtils.exists(key)){
            String exitCode = (String) redisUtils.get(key);
            if(exitCode!=null){
                return exitCode.equals(code);
            }
        }
        return false;
    }


}
