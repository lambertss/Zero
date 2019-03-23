package com.wuyue.Util;

import java.util.List;


/**
 * 功能描述:字符串处理工具类;
 */
public abstract class StringUtil {

	/**
	 * 功能描述:将数据元素为字符串的List集合转换成为一个普通的字符串,各个字符串之间以','(英文)分隔;
	 */
	public static String convertToString(List<String> items) {
		StringBuffer buffer = new StringBuffer();
		for(String item : items) {
			buffer.append(item + ",");
		}
		return buffer.deleteCharAt(buffer.length() - 1).toString();
	}
	

	
	//将首字母小写
    public static String lowerFirstChar(String a){
        if(a!=null&&a.length()!=0){
            return a.substring(0,1).toLowerCase()+a.substring(1);
        }
        return null;
    }
    public static String upcaseFirstChar(String a ){
        if(a!=null&&a.length()>0){
            String replace = a.replace(" ", "").trim();
            return replace.substring(0,1).toUpperCase()+a.substring(1);
        }
        return null;
    }
}
