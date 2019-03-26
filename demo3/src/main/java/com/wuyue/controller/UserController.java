package com.wuyue.controller;

import com.wuyue.common.Result;
import com.wuyue.mapper.TableMapper;
import com.wuyue.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "user", description = "用户控制器")
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TableMapper tableMapper;
    @GetMapping("loadTbs")
    public Result loadTbs(){


        return null;
    }


}
