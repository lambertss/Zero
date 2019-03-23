package com.wuyue.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

	private final static String PROFILESACTIVE = "spring.profiles.active";

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
    private Environment env;

	/**
	 * 功能描述:批量删除key
	 */
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(getCommonSuffixKey(pattern));
		if (keys.size() > 0){
			redisTemplate.delete(keys);
		}

	}

	/**
	 * 功能描述:删除对应的value
	 * @param key
	 * @Author:dengshuai
	 * @Date:2016年9月29日 下午6:08:59
	 */
	public void remove(final String key){
		if (exists(key)) {
			redisTemplate.delete(getCommonSuffixKey(key));
		}
	}

	/**
	 * 功能描述:判断缓存中是否有对应的value
	 * @param key
	 * @return
	 * @Author:dengshuai
	 * @Date:2016年9月29日 下午6:08:36
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(getCommonSuffixKey(key));
	}


	/**
	 * 功能描述:获取key所对应的值
	 * @param key
	 * @return
	 * @Author:dengshuai
	 * @Date:2016年9月29日 下午5:40:28
	 */
	public Object get(final String key) {
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		result = operations.get(getCommonSuffixKey(key));
		return result;
	}

	/**
	 * 
	 * 功能描述:写入缓存
	 * @param key 键
	 * @param value 值
	 * @return
	 * @Author:dengshuai
	 * @Date:2016年9月29日 下午5:39:04
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(getCommonSuffixKey(key), value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 指定key增量
	 * @param key
	 * @param num
	 * @return
	 */
	public boolean incr(final String key,double num){
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.increment(getCommonSuffixKey(key),num);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * 功能描述:写入缓存
	 * @param key 键
	 * @param value 值
	 * @param expireTime 过期时间单位/秒
	 * @return
	 * @Author:dengshuai
	 * @Date:2016年9月29日 下午5:39:04
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(getCommonSuffixKey(key), value);
			redisTemplate.expire(getCommonSuffixKey(key), expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 说明： 通过表达式获取key
	 * @author: ouyaokun
	 * @date: Created in 14:58 2018/11/29
	 * @modified: by autor in 14:58 2018/11/29
	 * @param pattern 表达式
	 * @return 搜索到的keys
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
	 * @param prex
	 * @return
	 * @Author:dengshuai
	 * @Date:2017年2月28日 下午7:24:33
	 */
	public boolean removeByPrex(String prex){
		boolean result = false;
		try{
			Set<String> keys = redisTemplate.keys(prex+"*"+getCommonSuffix());
			redisTemplate.delete(keys);
			result = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 功能描述:以后缀删除
	 * @return
	 * @Author:dengshuai
	 * @Date:2017年2月28日 下午7:24:33
	 */
	public boolean removeBySuffix(String suffix){
		boolean result = false;
		try{
			Set<String> keys = redisTemplate.keys("*"+getCommonSuffixKey(suffix));
			redisTemplate.delete(keys);
			result = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getCommonSuffixKey(String key){
		String profile = env.getProperty(PROFILESACTIVE);
		String suffix = "_" + profile;
		if(key.endsWith(suffix)){
			return key;
		}
		return key + suffix;
	}

	/**
	 * 说明： redis缓存key通用后缀
	 * @author: ouyaokun
	 * @date: Created in 15:39 2018/11/19
	 * @modified: by autor in 15:39 2018/11/19
	 */
	public String getCommonSuffix(){
		String profile = env.getProperty(PROFILESACTIVE);
		return "_" + profile;
	}
}
