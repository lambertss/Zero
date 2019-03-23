package com.wuyue.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * 说明： 请求体包装
 */
@Getter
@Setter
public class Request<T> {

    /**
     * 用户token;
     */
    private String token;
    private Integer page=1;
    private Integer limit=15;
    /**
     * 请求数据;
     */
    private T data;

}
