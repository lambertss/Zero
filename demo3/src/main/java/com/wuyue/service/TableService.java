package com.wuyue.service;

import com.wuyue.Util.EmptyUtil;
import com.wuyue.Util.LogUtil;
import com.wuyue.common.Result;
import com.wuyue.mapper.TableMapper;
import com.wuyue.pojo.Column;
import com.wuyue.pojo.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            if (!queryTbNameExits(name)) {
                try {
                    tableMapper.createTable(table);
                } catch (Exception e) {

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
            Map[] colNames = tableMapper.queryAllColName(tbName);
            for (Map map : colNames) {
                Object column_name = map.get("COLUMN_NAME");
                if(colName.equals(column_name)){
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
                    List<Column> columns = table.getColumns();
                    Map<String,Object> map = new HashMap<>();

                    StringBuilder successStr = new StringBuilder("成功删除:");
                    StringBuilder failStr = new StringBuilder("失败删除:");
                    for (int i=0;i<columns.size();i++) {
                        String colName = columns.get(i).getColName();
                        if(colName!=null&&colName.length()>0){
                            if(!queryColNameExits(tbName,colName)){
                                failStr.append("表中没有"+colName);
                                columns.remove(i);
                                i--;
                            }
                        }

                    }
                    if(columns.size()>0){
                        tableMapper.dropTbColumn(table);
                        for (Column column : columns) {
                            successStr.append(column.getColName()+",");
                        }
                    }

                    map.put("成功删除列:",successStr);
                    map.put("失败删除列:",failStr);
                    return Result.success(map);

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

    public Result addColumns(Table table) {
        List<Column> columns = table.getColumns();
        String tbName = table.getTbName();
        Map<String,Object> map = new HashMap<>();
        int a =0;
        if(columns!=null&&columns.size()>0&&tbName!=null) {
            for (int i = 0; i < columns.size(); i++) {
                String colName = columns.get(i).getColName();
                if (queryColNameExits(tbName, colName)) {
                    columns.remove(i);
                    i--;
                    map.put("failMsg"+ a++,"表中已经有"+colName+"这一列");
                }
            }
            if(columns.size()>0){
                tableMapper.addColumns(table);
                for (int i = 0; i < columns.size(); i++) {
                    String colName = columns.get(i).getColName();
                    map.put("successAdd"+i, colName);
                }
            }
            return Result.success(map);
        }
        return Result.fail();
    }

    public Result renameColName(Table table) {
        if(table!=null){
            String tbName = table.getTbName();
            if(!EmptyUtil.isEmpty(tbName)){
                if(queryTbNameExits(tbName)){
                    Column column = table.getColumns().get(0);
                    if(column!=null){
                        String colName = column.getColName();
                        if(!EmptyUtil.isEmpty(colName)){
                            if(queryColNameExits(tbName,colName)){
                                Integer type = column.getType();
                                if(type!=null){
                                    String length = column.getLength();

                                    if(type==1){
                                        if(!EmptyUtil.isEmpty(length)){
                                            column.setDataType(" varchar("+length+") ");
                                        }else{
                                            column.setDataType(" varchar(50) ");
                                        }
                                    }else if(type==2){
                                        if(!EmptyUtil.isEmpty(length)){
                                            column.setDataType(" bigint("+length+") ");
                                        }else{
                                            column.setDataType(" bigint(30) ");
                                        }
                                    }else if(type==3){
                                        column.setDataType(" date ");
                                    }else if(type==4){
                                        column.setDataType(" double ");
                                    }
                                }else {
                                    String dataType = tableMapper.selectColType(table);
                                    if ("varchar".equalsIgnoreCase(dataType)) {
                                        dataType = dataType + "(60)";
                                    } else if ("int".equalsIgnoreCase(dataType)
                                            || "bigint".equalsIgnoreCase(dataType)) {
                                        dataType = dataType + "(50)";
                                    }
                                    column.setDataType(dataType);

                                }
                                try {
                                    tableMapper.renameCol(table);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return Result.fail("转换失败,可能是已有的数据无法转变为新类型");
                                }
                                return Result.success();
                            }

                        }
                    }

                }

            }
        }
        return Result.fail();
    }
}
