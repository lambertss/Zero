package com.wuyue.mapper;

import com.wuyue.pojo.Table;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

@Repository
public interface TableMapper extends Mapper<Table> {
    void createTable(Table table);
    String[] queryAllTbName();
    Map[] queryAllColName(@Param("tbName") String tbName);
    void dropTbColumn(Table table);
    void dropTb(Table table);
    void forgeDropTb(@Param("tbName") String tbName,@Param("delName")String delName);
    void renameTb(@Param("newName")String newName,@Param("oldName")String oldName);

    void addColumns(Table table);

    String selectColType(Table table);

    void renameCol(Table table);

    int recordCreateRecord(Table table);

}
