package com.wuyue.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Table {
    private String tbName;
    private List<Column> columns;
    private String tbComment;
    private String newTbName;

    private Date createTime;
    private Integer creatorId;

}
