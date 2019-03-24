package com.wuyue.service.publicService.impl;

import com.wuyue.Util.EmptyUtil;
import com.wuyue.Util.EncrUtil;
import com.wuyue.Util.LogUtil;
import com.wuyue.Util.RedisUtils;
import com.wuyue.common.Cons;
import com.wuyue.common.Result;
import com.wuyue.pojo.Request;
import com.wuyue.pojo.User;
import com.wuyue.service.UserService;
import com.wuyue.service.publicService.IPublicApi;
import com.wuyue.service.publicService.ISecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 说明： 公共api接口实现
 */
@Service
public class PublicApiService implements IPublicApi {

    @Autowired
    private ISecurity securityService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserService userService;


    private final static Logger logger = LoggerFactory.getLogger(PublicApiService.class);

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Result login(Request<User> request) {

        String loginExpiretimeStr = "60";
        Integer loginExpiretime = Integer.valueOf(loginExpiretimeStr);
        User user = request.getData();
        if (user == null || EmptyUtil.isEmpty(user.getUsername()) || EmptyUtil.isEmpty(user.getPassword())) {
            return Result.fail("参数错误，请检查接口参数");
        }
        //密码加密
        String encryptPwd = securityService.pwdEncrypt(user.getPassword());
        user.setPassword(encryptPwd);
        //String netpointId = user.getNetpointId();

        List<User> userEntitys = userService.list(user);
        if (userEntitys == null || userEntitys.size() != 1) {
            return Result.fail("请检查用户名密码");
        }

        User userEntity = userEntitys.get(0);
        System.out.println(userEntity.toString());
//        if (User.STATUS_2.equals(user.getStatus())){
//            return Result.fail("该账号已经禁用");
//        }
//        UserbranchEntity userbranchEntity = new UserbranchEntity();
//        userbranchEntity.setUserId(userEntity.getId());
//
//        List<UserbranchEntity> userbranchEntitys = userbranchMapper.select(userbranchEntity);

//        List<String> netpointIds=new ArrayList<>();
//        List<String> roleIds=new ArrayList<>();
//        if(userbranchEntitys!=null&&userbranchEntitys.size()!=0){
//            for (UserbranchEntity userbranchEntity1 : userbranchEntitys) {
//                if(userbranchEntity1.getBranchId().equals(netpointId)){
//                    for (UserbranchEntity entity : userbranchEntitys) {
//                        netpointIds.add(entity.getBranchId());
//                    }
//
//                    UserroleEntity userroleEntity = new UserroleEntity();
//
//                    userroleEntity.setUserId(loginUser.getId());
//                    List<UserroleEntity> userroleEntities = userroleMapper.select(userroleEntity);
//                    for (UserroleEntity entity : userroleEntities) {
//                        roleIds.add(entity.getRoleId());
//                    }
//                    loginUser.setRoleIds(roleIds);
//                    loginUser.setNetpointIds(netpointIds);
//                    //先删除旧token
//                    if(!EmptyUtil.isEmpty(loginUser.getToken())){
//                        String oldToken = loginUser.getToken();
//                        String oldSecret = EncrUtil.GetMD5Code(oldToken);
//                        redisUtils.remove(oldToken + Cons.encryptKey);
//                        redisUtils.remove(oldSecret + Cons.encryptKey);
//                    }
//
//
//                    loginUser.setLastLoginDate(new Date());
//                    loginUser.setLoginCount(loginUser.getLoginCount() + 1);
//                    loginUser.setTimeStamp(timeStamp);
//                    ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//                    HttpServletRequest httprequest = sra.getRequest();
//                    loginUser.setIpAddr(LogUtil.getIpAddr(httprequest));
//
//                    loginUser.setOperatorId(loginUser.getId());
//                    userService.update(loginUser);
//                    loginUser.setTimeStamp(null);
//
//                    if (!AppCache.loginTree.contains(loginUser)) {
//                        AppCache.loginTree.add(loginUser);
//                    }
//                    LogUtil.DebugLog(this, "当前登录人数为：" + AppCache.loginTree.size());
//                    return Result.success()(loginUser, "登录成功");
//
//                }
//        }

 //       }
        //从
        User loginUser = userService.loadOne(userEntity);
        Long timeStamp = System.currentTimeMillis();
                    String token = EncrUtil.generateComplexToken(loginUser.getId()+"", timeStamp);
                    LogUtil.DebugLog(this, "token is" + token);
                    loginUser.setToken(token);
        String secret = EncrUtil.GetMD5Code(token);
//                    loginUser.setSecret(secret);

        redisUtils.set(token + Cons.encryptKey, loginUser.getId(), loginExpiretime);
//                    redisUtils.set(secret + Cons.encryptKey, loginUser, loginExpiretime);
        logger.info(user.getUsername()+"登录失败");
        return Result.fail("没有网点权限");

    }

    @Override
    public Result getAuths(Request<User> request) {
        User loginUser = request.getData();

        if(EmptyUtil.isEmpty(request.getToken()) || !redisUtils.exists(request.getToken() + Cons.encryptKey)){
            return Result.fail("获取用户权限失败");
        }

        String secret = EncrUtil.GetMD5Code(request.getToken());

        if (EmptyUtil.isEmpty(secret)) {
            return Result.fail("secret 不能为空");
        }
        User authorsUser = (User) redisUtils.get(secret + Cons.encryptKey);
        if (authorsUser == null) {
            return Result.fail("获取用户权限失败");
        }

        return Result.success(authorsUser);
    }




}
