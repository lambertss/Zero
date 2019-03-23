package com.wuyue.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuyue.config.ExecutorConfig;
import com.wuyue.pojo.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutorService;

public class LogUtil {

    public static void DebugLog(Object o, String msg) {
        Logger logger = LoggerFactory.getLogger(o.getClass());
        logger.debug("=============>"+msg);
    }

    public static void InfoLog(Object o, String msg) {
        Logger logger = LoggerFactory.getLogger(o.getClass());
        logger.info("=============>"+msg);
    }

    public static void ErrorLog(Object o, String msg) {
        Logger logger = LoggerFactory.getLogger(o.getClass());
        logger.error("=============>"+msg);
    }

    public static void ErrorLog(Object o, String msg, Request request){
    	ErrorLog(o, "=============>"+msg);
	}

	/**
     * 说明： 写入访问日志文件
	 * @param savePath 访问日志路径
     */
    public static void writeSystemAccessFileLog(HttpServletRequest request, Object dtoRequest, Object result, String savePath, String inUserId, Date now){

        if(savePath!=null&&savePath.length()==0){
        	LogUtil.ErrorLog(LogUtil.class, "访问日志的保存路径为空");
        	return ;
		}

		//获取IP地址
		String ip = getIpAddr(request);
        //获取请求链接
        String urlTemp = "";
        if (request.getRequestURL() != null) {
            urlTemp = request.getRequestURL().toString();//request.getRequestURI();
        }

        final String url = urlTemp;
        final String method = request.getMethod();

		Runnable run = new Runnable() {
			@Override
			public void run() {
				FileWriter fileWriter = null;
				try {
					String userId = "";
					if(inUserId!=null&&inUserId.length()>0){

						userId = inUserId;
					}
					LogUtil.DebugLog(LogUtil.class, "收集信息");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("time", DateUtil.toString(now, DateUtil.DEFAULT_TIME_FORMAT));
					jsonObject.put("ip", ip);
					jsonObject.put("url", url);
					jsonObject.put("method", method);
					jsonObject.put("userId", userId);
					jsonObject.put("requestContent", dtoRequest == null ? null : JSON.toJSONString(dtoRequest));
//					jsonObject.put("resultContent", result == null ? null : JSON.toJSONString(result));
					String logContent = jsonObject.toJSONString() + ",\n\n";

					String prefix = "accesslog-";
					String date = DateUtil.toString(new Date(), DateUtil.DEFAULT_DATE_FORMAT);
					String suffix = ".log";

					LogUtil.DebugLog(LogUtil.class, "打开文件: " + savePath + prefix + date + suffix);
					File file = new File(savePath + prefix + date + suffix);
					//文件是否存在
					if(!file.exists()) {
						file.createNewFile();
						LogUtil.DebugLog(LogUtil.class, "创建文件");
					}

					fileWriter = new FileWriter(file, true);
					fileWriter.write(logContent);



				}catch (Throwable e){
					e.printStackTrace();
					InfoLog(LogUtil.class,"写入系统日志文件失败, 出错原因：" + e.getMessage());
				}finally {
					try {
						if (fileWriter != null) {
							fileWriter.close();
						}
					}catch (IOException e) {
						LogUtil.ErrorLog(LogUtil.class, e.getMessage());
					}
				}


			}
		};

        ExecutorService executorService = (ExecutorService) SpringBeanTools.getBeanByName(ExecutorConfig.EXECUTOR_SERVICE_NAME);
        executorService.submit(run);
	}

    /**
     * 功能描述:获取IP地址
     */
    private static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }

        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        //地址过长截取前100位
        if (ipAddress != null && ipAddress.length() > 100) {
            ipAddress = ipAddress.substring(0, 100);
        }
        return ipAddress;
    }

}
