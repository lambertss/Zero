package com.wuyue.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public  class ClassUtil {

    public static Object getFieldValByName(Object obj, String fieldName){
        if(fieldName==null||fieldName.length()==0){
            return null;
        }
        String fieldFragments = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        try {
            Method getMethod = obj.getClass().getMethod("get" + fieldFragments);
            if(getMethod!=null){
                return getMethod.invoke(obj);
            }
        }catch (Exception e){
            System.out.println("没有这种成员变量或者调用成员变量名写错");
        }
        return null;
    }

    public  static Boolean setValue(Object t,String fieldName,Object value){
        if(fieldName!=null&&fieldName.length()!=0){
            try {
                Method method = t.getClass().getMethod("set" + fieldName, value.getClass());
                if(method != null) {
                    method.invoke(t, value);
                    return true;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                System.out.println("输入的成员变量名有误或者没有对应的成员变量");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
