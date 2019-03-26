package com.wuyue.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter
@javax.persistence.Table(name = "tb_user_create")
public class Table {

    private Integer id;
    private String tbName;
    private Integer userId;
    private String tbComment;
    @Transient
    private String newTbName;
    @Transient
    private List<Column> columns;


}
