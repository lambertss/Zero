package com.wuyue.service.publicService;


import com.wuyue.common.Result;
import com.wuyue.pojo.Request;
import com.wuyue.pojo.User;

/**
 * 说明： 公共api
 */
public interface IPublicApi {

    /**
     * 说明： 登录
     */
    Result login(Request<User> request) ;

    /**
     * 说明：获取用户权限
     */
    Result getAuths(Request<User> request);

}
