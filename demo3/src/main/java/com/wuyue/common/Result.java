package com.wuyue.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private boolean flag;
    private String message;
    private Integer total;
    private Object data;

    private static String operateS="操作成功";
    private static String operateF="操作失败";

    private Result(String message, Object data) {
        this.message = message;
        this.data = data;
        if(data instanceof Collection){
            total=((Collection) data).size();
        }else if(data instanceof Map){
            total=((Map) data).size();
        }else{
            total=1;
        }

    }

    private Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public static Result success(String message, Object object){
        return new Result(message,object);
    }
    public static Result success(String message){
        return new Result(true,message);
    }
    public static Result success(Object obj){
        return new Result(operateS,obj);
    }
    public static Result success(){return new Result(true,operateS);}
    public static Result fail(String message){
        return new Result(false,message);
    }
    public static Result fail(){return new Result(false,operateF);}

}
