package com.wuyue.service.publicService;


import com.wuyue.common.Result;
import com.wuyue.pojo.Request;
import com.wuyue.pojo.User;

/**
 * 说明： 公共api
 * @author: ouyaokun
 * @date: Created in 16:56 2018/12/11
 * @modified: by autor in 16:56 2018/12/11
 */
public interface IPublicApi {

    /**
     * 说明： 登录
     * @author: ouyaokun
     * @date: Created in 16:54 2018/12/11
     * @modified: by autor in 16:54 2018/12/11
     * @param request 用户名 密码
     * @return 登录信息
     */
    Result login(Request<User> request) ;

    /**
     * 说明：获取用户权限
     * @author: ouyaokun
     * @date: Created in 16:55 2018/12/11
     * @modified: by autor in 16:55 2018/12/11
     * @param request 不需要参数
     * @return 用户权限信息
     */
    Result getAuths(Request<User> request);

}
