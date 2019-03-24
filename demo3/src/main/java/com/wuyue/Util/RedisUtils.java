package com.wuyue.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:redicache 工具类 
 */
@Component
@ComponentScan
public class RedisUtils {

    private static String commonSuffix="_user";
    //默认过期时间40分钟,自己另外设置单位,否则为毫秒值
    private static Integer expireTime=40;
	@Autowired
	private RedisTemplate redisTemplate;


	/**
	 * 功能描述:批量删除带通用后缀的key
	 */
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(getCommonSuffixKey(pattern));
		if (keys.size() > 0){
			redisTemplate.delete(keys);
		}

	}

	/**
	 * 功能描述:删除通用后缀对应的value
	 */
	public void removeCommonSuffix(final String key){
		if (exists(key)) {
			redisTemplate.delete(getCommonSuffixKey(key));
		}
	}

	/**
	 * 功能描述:判断缓存中是否有对应的value
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(getCommonSuffixKey(key));
	}


	/**
	 * 功能描述:获取key所对应的值
	 */
	public Object get(final String key) {
		Object result = null;
		result = redisTemplate.opsForValue().get(getCommonSuffixKey(key));
		return result;
	}

	/**
	 * 
	 * 功能描述:写入缓存
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
            redisTemplate.opsForValue().set(getCommonSuffixKey(key), value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 指定key增量
	 */
	public boolean incr(final String key,double num){
		try {
            redisTemplate.opsForValue().increment(getCommonSuffixKey(key),num);
			return   true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * 功能描述:写入缓存
	 */
	public boolean set(final String key, Object value, Integer expireTime) {
		try {
            redisTemplate.opsForValue().set(getCommonSuffixKey(key), value,expireTime, TimeUnit.MINUTES);
			return   true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 说明： 通过表达式获取key
	 */
	public Set<String> getKeys(String pattern){
		Set<String> keys = new HashSet<>();
		try{
			keys = redisTemplate.keys(getCommonSuffixKey(pattern));
		}catch (Exception e){
			e.printStackTrace();
		}
		return keys;
	}
	
	/**
	 * 功能描述:以前缀删除
	 */
	public boolean removeByPrex(String prex){
		return removeByStr(prex+"*");
	}
	
	/**
	 * 功能描述:以后缀删除
	 */
	public boolean removeBySuffix(String suffix){
		 return removeByStr("*"+suffix);
	}
	private boolean removeByStr(String str){
	    if(str!=null){
            try{
                Set<String> keys = redisTemplate.keys(str);
                redisTemplate.delete(keys);
                return true;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

	private String getCommonSuffixKey(String key){

		if(key.endsWith(commonSuffix)){
			return key;
		}
		return key + commonSuffix;
	}

}
