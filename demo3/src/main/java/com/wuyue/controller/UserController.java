package com.wuyue.controller;

import com.wuyue.Util.DateUtil;
import com.wuyue.common.Result;
import com.wuyue.mapper.UserMapper;
import com.wuyue.pojo.Request;
import com.wuyue.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @PostMapping("test")
    public Result test(@RequestBody Request<User> request){
        User data = request.getData();
        if(data!=null){
            System.out.println(data.getName());
            System.out.println(data.getPassword());
            System.out.println(data.getCreated());
            System.out.println(data.getBirthday());
        }
        Date date1 = DateUtil.parse("2016-08-8 8:00:00", "yyyy-MM-dd HH:mm:ss");
        return Result.success(date1);
    }


}
