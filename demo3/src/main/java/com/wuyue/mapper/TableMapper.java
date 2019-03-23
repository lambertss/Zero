package com.wuyue.mapper;

import com.wuyue.pojo.Table;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface TableMapper extends Mapper<Table> {
    void createTable(Table table);
    String[] queryAllTbName();
}
