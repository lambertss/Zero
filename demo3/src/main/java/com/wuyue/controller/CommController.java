package com.wuyue.controller;

import com.alibaba.fastjson.JSON;
import com.wuyue.Util.ClassUtil;
import com.wuyue.Util.RedisUtils;
import com.wuyue.common.Cons;
import com.wuyue.common.Result;
import com.wuyue.factory.CrudServiceFactory;
import com.wuyue.pojo.BasePojo;
import com.wuyue.pojo.Request;
import com.wuyue.pojo.User;
import com.wuyue.service.CrudService;
import com.wuyue.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class CommController {

    @Autowired
    private CrudServiceFactory crudServiceFactory;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserService userService;
    protected String packageD = "";


    @PostMapping("/{domain}/create")
    @ApiOperation(httpMethod = "POST", value = "通用插入表数据", notes = "通用插入表数据")
    public Result create(@RequestBody Request request, @PathVariable String domain) {

        BasePojo tt = createEntityByClassName(request.getData(),domain);

        CrudService crudService = crudServiceFactory.createService(domain);
        if(crudService!=null){
            recordCreator(tt,request.getToken());
            Object o = crudService.create(tt);
            if(o!=null){

                return Result.success(o);
            }
        }
        return Result.fail();

    }
    @ApiOperation(httpMethod = "POST", value = "通用更新表数据", notes = "通用更新表数据")
    @PostMapping("/{domain}/update")
    public Result update(@RequestBody Request request, @PathVariable String domain)  {
        BasePojo tt = createEntityByClassName(request.getData(), domain);
        CrudService crudService = crudServiceFactory.createService(domain);
        if(crudService!=null){
            recordOperator(tt,request.getToken());
            BasePojo pojo = crudService.update(tt);
            if(pojo!=null){

                return Result.success(pojo);
            }
        }

        return Result.fail();

    }
    @PostMapping("/{domain}/loadById")
    public Result load(@RequestBody Request request, @PathVariable String domain)  {

        BasePojo tt = createEntityByClassName(request.getData(), domain);

        CrudService crudService = crudServiceFactory.createService(domain);
        if(crudService!=null){
            Object o = crudService.loadById(tt);
            if(o==null){
                return Result.success("根据ID查询不到数据");
            }
            return Result.success(o);
        }
        return Result.fail();
    }

    @PostMapping("/{domain}/loadOne")
    public Result loadOneByCondition(@RequestBody Request request, @PathVariable String domain)  {

        BasePojo tt = createEntityByClassName(request.getData(), domain);

        CrudService crudService = crudServiceFactory.createService(domain);
        if(crudService!=null){
            Object o = crudService.loadOne(tt);
            if(o==null){
                return Result.success("根据条件找不到你要的对象");
            }
            return Result.success(o);
        }
        return Result.fail("参数有误");
    }


    @PostMapping("/{domain}/list")
    public Result list(@RequestBody Request request, @PathVariable String domain) {

        BasePojo tt = createEntityByClassName(request.getData(), domain);
        CrudService crudService = crudServiceFactory.createService(domain);
        if(crudService!=null){
            List list = crudService.list(tt, request.getPage(), request.getLimit());
            return Result.success(list);
        }
        return Result.fail("参数有误");


    }

    @PostMapping("/{domain}/deleteByIds")
    public Result remove(@RequestBody Request request, @PathVariable String domain)  {

        BasePojo tt = createEntityByClassName(request.getData(), domain);

        CrudService crudService = crudServiceFactory.createService(domain);
        if(crudService!=null){
            recordOperator(tt,request.getToken());
            int row = crudService.deleteByIds(tt);
            if (row > 0) {
                return Result.success(row);
            }
        }
        return Result.fail("删除单条数据失败");
    }

    //根据类名全程将request中的对象obj转为BasePojo,不能使用request.getData()获取
    private BasePojo createEntityByClassName(Object obj,String domain) {
        try {
            Class clazz = Class.forName(packageD+domain);
            if(clazz!=null){
                String json = JSON.toJSONString(obj);
                return JSON.parseObject(json, (Type) clazz);
            }
        } catch (ClassNotFoundException e) {

        }
        return null;
    }

    private void recordCreator(Object obj, String token)  {
        setValue(obj, Cons.CreateTime,new Date());
        Integer userId = (Integer) redisUtils.get(token+ Cons.encryptKey);

        if(userId!=null){
            setValue(obj,Cons.CreaterId,userId);
            User user = new User();
            user.setId(userId);
            user.setDelStatus(0);
            user = userService.loadOne(user);
            if(user != null){
                setValue(obj,Cons.CreaterName,user.getName());
            }
        }

    }

    private void recordOperator(Object obj, String token)  {
        setValue(obj,Cons.OperateDate,new Date());
        Integer userId = (Integer) redisUtils.get(token+ Cons.encryptKey);

        if(userId!=null){
            setValue(obj,Cons.OperatorId,userId);
            User user = new User();
            user.setId(userId);
            user = userService.loadById(user);
            setValue(obj,Cons.Operator,user.getName());
        }

    }
    private Boolean setValue(Object t,String fieldName,Object o){
        return ClassUtil.setValue(t,fieldName,o);
    }

}
