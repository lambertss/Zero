package com.wuyue.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Column {

    private String colName;
    //type=0代表默认字符串类型,1代表数值类型,2代表date类型
    private Integer type =0;
    private Integer length=30;
    private String comment ;



}
