package com.wuyue.service;

import com.wuyue.Util.LogUtil;
import com.wuyue.common.Result;
import com.wuyue.mapper.TableMapper;
import com.wuyue.pojo.Column;
import com.wuyue.pojo.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TableService{
    @Autowired
    private TableMapper tableMapper;

    public Result createTable(Table table){

        String name = table.getTbName();
        if(name!=null){
            boolean b = queryTbNameExits(name);
            if (!b) {
                try {
                    tableMapper.createTable(table);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.DebugLog(TableService.class,e.getMessage());
                    return Result.fail("插入失败,请检查是否有同名列");
                }
                if(queryTbNameExits(name)){
                    return Result.success();
                }
            }
            return Result.fail("数据库中已经存在该表,请替换表名重新尝试");

        }

        return Result.fail("缺失表格名参数");
    }
    private boolean queryTbNameExits(String tbName){
        String[] s = tableMapper.queryAllTbName();
        if(tbName!=null){
            for (String s1 : s) {
                if(tbName.equals(s1)){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean queryColNameExits(String tbName,String colName){
        if(tbName!=null&&colName!=null){
            String[] colNames = tableMapper.queryAllColName(tbName);
            for (String name : colNames) {
                if(colName.equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    public Result dropTBColumn(Table table) {
        if(table!=null){
            String tbName = table.getTbName();

            if(tbName!=null){
                if(queryTbNameExits(tbName)){
                    List<Column> columns1 = table.getColumns();
                    Map<String,Object> map = new HashMap<>();

                    StringBuilder builder = new StringBuilder("成功删除:");
                    StringBuilder failBuder = new StringBuilder("失败删除:");
                    for (Column column : columns1) {
                        Table table1 = new Table();
                       table1.setTbName(tbName);
                        List<Column> list=new ArrayList<>();
                       list.add(column);
                       table1.setColumns(list);
                        try {
                            tableMapper.dropTbColumn(table1);
                            builder.append(column.getColName());
                        } catch (Exception e) {
                            LogUtil.InfoLog(TableService.class,"删除表"+tbName+
                                    ":"+column.getColName()+"出现异常,也许是表里本来就没这一列");
                            failBuder.append(column.getColName()+",");
                        }
                    }
                    map.put("成功删除列:",builder);
                    map.put("失败删除列:",failBuder);
                    return Result.success("成功删除的列数是",map);

                }
                return Result.fail("数据库中没有这个表");

            }

        }
        return Result.fail("缺失表名参数");

    }
    @Transactional
    public Result updateTable(Table table){
        if(table!=null){
            String tbName = table.getTbName();
            if(tbName !=null&&tbName.length()>0){
                String newTbName = table.getNewTbName();
                if(newTbName!=null&&newTbName.length()>0){
                     renameTb(newTbName,tbName);
                }
                    String newName = "update_" + tbName;
                    String[] names = tableMapper.queryAllTbName();
                    for (String name : names) {
                        if(newName.equals(name)){
                            Table table1 = new Table();
                            table1.setTbName(newName);
                            tableMapper.dropTb(table1);
                        }
                    }
                try {
                    tableMapper.renameTb(newName,tbName);
                } catch (Exception e) {
                    LogUtil.InfoLog(TableService.class,"更新重命名表失败"+tbName);
                }
                try {
                    tableMapper.createTable(table);
                    return Result.success();
                } catch (Exception e) {
                    LogUtil.InfoLog(TableService.class,"更新表"+tbName+"失败,请检查列名是否重复");
                    return Result.fail("更新表"+tbName+"失败,请检查列名是否重复");
                }
            }
        }
        return Result.fail();
    }

    public Result forgeDropTable(Table data) {
        if(data!=null){
            String tbName = data.getTbName();
            if(tbName!=null) {
                return renameTb("del_"+tbName,tbName);
            }
        }
        return Result.fail();

    }
    public Result renameTb(String newTbName,String oldTbName){
        if(newTbName!=null&&oldTbName!=null){
            if(!queryTbNameExits(newTbName)){
                try {
                    tableMapper.renameTb(newTbName, oldTbName);
                    return Result.success();
                } catch (Exception e) {
                    return Result.fail("数据库中没有这张旧表,请检查参数是否正确");
                }

            }
            return Result.fail("新表名已经被占用");

        }
        return Result.fail("缺参数");

    }

}
