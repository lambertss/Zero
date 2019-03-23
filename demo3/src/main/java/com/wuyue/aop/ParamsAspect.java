package com.wuyue.aop;

import com.alibaba.fastjson.JSON;
import com.wuyue.Util.EmptyUtil;
import com.wuyue.Util.LogUtil;
import com.wuyue.Util.RedisUtils;
import com.wuyue.common.Result;
import com.wuyue.config.UrlConfig;
import com.wuyue.pojo.Request;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * 说明： 后台业务拦截器
 */
@Aspect
@Component
public class ParamsAspect {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UrlConfig urlConfig;

    @Around("execution(com.wuyue.common.Result com.wuyue.controller..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest hsRequest = sra.getRequest();
        String url = hsRequest.getRequestURL().toString();

        LogUtil.DebugLog(this, "当前请求的URL是:" + url);
        Request request = null;
        String simpleMethodName = pjp.getSignature().getName();
        LogUtil.InfoLog("方法名为:",simpleMethodName);
        String userId = "";
        Date now = new Date();
        Object[] os = pjp.getArgs();
        for (int i = 0; i < os.length; i++) {
            if (os[i] instanceof Request) {
                request = (Request) os[i];
                if (request == null) {
                    LogUtil.writeSystemAccessFileLog(hsRequest, null, Result.fail("请求参数request不能为空"), urlConfig.getAccesslog(), userId, now);
                    return Result.fail("请求参数request不能为空");
                }
                String jsonString = JSON.toJSONString(request);
                if (EmptyUtil.isEmpty(request.getToken())) {
                    LogUtil.writeSystemAccessFileLog(hsRequest, request,
                            Result.fail( "请求参数中没有token!收到的请求是>>>>>" + jsonString),
                            urlConfig.getAccesslog(), userId, now);
                    return Result.fail( "请求参数中没有token!收到的请求是>>>>>" + jsonString);
                }



                // 判断用户是否登录
//                if (!Constant.AUTHORID.contains(request.getToken())
//                        && !url.contains("/wechat/") && !url.contains("/web/public")
//                        && !redisUtils.exists(request.getToken() + Constant.encryptKey)) {
//                    LogUtil.DebugLog(this, "获得的请求数据是>>>>>>" + jsonString);
//                    LogUtil.writeSystemAccessFileLog(hsRequest, request, Result.fail(Constant.encryptKey, ResponseEnum.ERROR_CODE_4003.getCode()), urlConfig.getAccesslog(), userId, now);
//                    return Result.fail(Constant.encryptKey, ResponseEnum.ERROR_CODE_4003.getCode());
//                } else {
//                    //更新缓存
//                    if (redisUtils.exists(request.getToken() + Constant.encryptKey)) {
//                        //续期token
//                        userId = (String) redisUtils.get(request.getToken() + Constant.encryptKey);
//                        redisUtils.set(request.getToken() + Constant.encryptKey, userId, loginExpiretime);
//
//                        //续期权限信息
//                        String secret = EncrUtil.GetMD5Code(request.getToken());
//                        User authorsUser = (User) redisUtils.get(secret + Constant.encryptKey);
//                        redisUtils.set(secret + Constant.encryptKey, authorsUser, loginExpiretime);
//                        LogUtil.DebugLog(this, "更新登录状态...延长{}" + loginExpiretime);
//                    }
//                }

                if (simpleMethodName.startsWith("list")) {
                    //改变做法，page为空 自动添加page
                    if (request.getPage() == null) {
                        request.setPage(0);
                        request.setLimit(15);
                    }
                }

                if (request.getPage() != null && request.getPage() > 0 && request.getLimit() != null
                        && request.getLimit() <= 0) {
                    LogUtil.writeSystemAccessFileLog(hsRequest, request,
                            Result.fail("page参数大于0时,limit参数必须大于0"), urlConfig.getAccesslog(), userId, now);
                    return Result.fail("page参数大于0时,limit参数必须大于0");
                }

                break;
            }
        }

        Object object = null;
        String tips = "";
        try {
            object = pjp.proceed();

        } catch (Throwable e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            e.printStackTrace();
            if (e instanceof ClassNotFoundException) {
                tips = "请检查路径是否合法!";
            }

            if (e.getMessage() != null && !"".equals(e.getMessage())) {
                tips += e.getMessage();
                LogUtil.ErrorLog(this, tips, request);
            } else {
                tips += "后台异常信息为:" + sw.toString();
                LogUtil.ErrorLog(this, tips, request);
            }
            LogUtil.writeSystemAccessFileLog(hsRequest, request, Result.fail(tips),
                    urlConfig.getAccesslog(), userId, now);
            return Result.fail(tips);
        }

        LogUtil.writeSystemAccessFileLog(hsRequest, request, object, urlConfig.getAccesslog(), userId, now);

        return object;
    }

}
