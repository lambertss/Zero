package com.wuyue.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@Table(name = "tb_column")
public class Column {

    private String tbName;
    private String colName;
    @javax.persistence.Column(name = "col_length")
    private String length="30";
    @javax.persistence.Column(name = "col_comment")
    private String comment ;
    private String dataType;
    //type=1代表默认字符串类型,2代表数值类型,3代表date类型,4代表double类型
    @Transient
    private Integer type =1;
    @Transient
    private String newName;


}
