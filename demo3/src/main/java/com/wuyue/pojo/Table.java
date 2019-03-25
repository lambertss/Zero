package com.wuyue.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@javax.persistence.Table(name = "tb_config")
public class Table {

    private String tbName;
    private List<Column> columns;
    private String tbComment;
    @Transient
    private String newTbName;

    private Date createTime;
    private Integer creatorId;

}
