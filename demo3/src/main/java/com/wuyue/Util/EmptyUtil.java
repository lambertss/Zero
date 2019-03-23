package com.wuyue.Util;

import java.util.Collection;
import java.util.Map;

/**
 * 功能描述:判空工具类，数组判空，字符串判空，集合判空,map判空
 */
public class EmptyUtil {


    public static boolean isEmpty(String str) {
      return  (str == null || str.trim().equals("")) ;
    }

    public static boolean isEmpty(Object[] array) {
        return  (array == null || array.length < 1) ;
    }

    public static  boolean isEmpty(Collection collection) {
        return collection == null || collection.size() < 1 ;
    }
    public static  boolean isEmpty(Map map){
        return map==null||map.size()<1;
    }

}
