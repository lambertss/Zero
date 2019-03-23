package com.wuyue.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/base")
@Api(value = "admin/base", description = "通用控制器")
public class AdminBaseController extends CommController {

    public AdminBaseController() {

        packageD = "com.wuyue.pojo.";
    }

}
