package com.wuyue.Util;

import java.util.Random;

public class GenIdUtil {

    public static Integer genId(){
        Random random = new Random(5);
        int i = random.nextInt();
        System.out.println(i);
        return i;
    }
    public static void main(String[] arg0){
        Integer integer = genId();
        Integer integer1 = genId();

    };
}
