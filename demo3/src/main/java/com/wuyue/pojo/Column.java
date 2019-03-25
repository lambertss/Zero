package com.wuyue.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

@Getter
@Setter
@Table(name = "tb_column")
public class Column {

    private String colName;
    //type=1代表默认字符串类型,2代表数值类型,3代表date类型,4代表double类型
    private Integer type =1;
    private String length="30";
    private String comment ;
    private String newName;
    private String dataType;


}
