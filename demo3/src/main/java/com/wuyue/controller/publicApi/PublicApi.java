package com.wuyue.controller.publicApi;

import com.wuyue.Util.RedisUtils;
import com.wuyue.common.Cons;
import com.wuyue.common.Result;
import com.wuyue.pojo.Request;
import com.wuyue.pojo.User;
import com.wuyue.service.publicService.IPublicApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 说明： 公开的api接口
 */
@RestController
@RequestMapping("/web/public")
@Api(value = "/web/public", description = "公开的api接口")
public class PublicApi {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IPublicApi publicApiService;

    @ApiOperation(httpMethod = "GET", value = "判断用户是否登录", notes = "判断用户是否登录")
    @GetMapping("islogin")
    public Result isLogin(@RequestParam("token") String token) {

        Boolean exists = redisUtils.exists(token+ Cons.encryptKey);

        if(exists){
            return Result.success(exists);
        }
        return Result.fail();
    }

    @PostMapping("login")
    public Result login(@RequestBody Request<User> request) {
        return publicApiService.login(request);
    }


    @PostMapping("getauths")
    public Result getAuths(@RequestBody Request<User> request) {
        return publicApiService.getAuths(request);
    }



}
