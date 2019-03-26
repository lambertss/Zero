package com.wuyue.service;

import com.wuyue.Util.EmptyUtil;
import com.wuyue.Util.LogUtil;
import com.wuyue.Util.NumberUtil;
import com.wuyue.common.Result;
import com.wuyue.mapper.ColumnMapper;
import com.wuyue.mapper.TableMapper;
import com.wuyue.pojo.Column;
import com.wuyue.pojo.Request;
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
    @Autowired
    private ColumnMapper columnMapper;


    @Transactional(rollbackFor = Exception.class)
    public Result createTable(Request<Table> request){
        Table table = request.getData();
        if(table!=null&&!EmptyUtil.isEmpty(table.getTbName())){
            String name = table.getTbName();
            if(name!=null){
                if (!queryTbNameExits(name)) {
                    String randNum = NumberUtil.getRandNum(6);
                    table.setId(Integer.valueOf(randNum));
                    table.setUserId(111);
                    tableMapper.createTable(table);

                    int i = tableMapper.recordCreateRecord(table);
                    if(i==1){
                        List<Column> columns = table.getColumns();
                        if(columns!=null&&columns.size()>0){
                            List<Column> list = typeToDataTypes(columns);
                            table.setColumns(list);
                            columnMapper.createColumns(table);
                        }
                        return Result.success();
                    }
                    return Result.fail("创建表时失败!");
                }
                return Result.fail("数据库中已经存在该表,请替换表名重新尝试");

            }
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
    private boolean queryColNameExits(String tbName,String[] strs){
        if(strs!=null&&strs.length>0){
            int sum = strs.length;
            int count=0;
            Map[] colNames = tableMapper.queryAllColName(tbName);
            if(colNames!=null&&colNames.length>0){
                for (Map colName : colNames) {
                    for (String str : strs) {
                        if(str.equalsIgnoreCase((String) colName.get("COLUMN_NAME"))){
                            count++;
                        }
                    }
                }
            }
            return sum==count;
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
                            Column column1 = new Column();
                            column1.setTbName(table.getTbName());
                            column1.setColName(column.getColName());
                            columnMapper.delete(column1);
                        }
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
                Result result = renameTb("del_" + tbName, tbName);
                Table table = new Table();
                table.setTbName(tbName);
                table.setUserId(234);
                int i = tableMapper.delete(table);
                if(i==1){
                    Column column = new Column();
                    column.setTbName(tbName);
                    columnMapper.delete(column);

                }

                return result;
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
                table.setId(Integer.valueOf(NumberUtil.getRandNum(6)));
                table.setUserId(111);
                int i2 = tableMapper.insert(table);
                if(i2==1){
                    List<Column> list = table.getColumns();
                    List<Column> columns1 = typeToDataTypes(list);
                    for (Column column : columns1) {
                        column.setTbName(table.getTbName());
                        columnMapper.insert(column);
                    }

                }
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
                    List<Column> columns = table.getColumns();
                    if(columns!=null){
                        Column column = columns.get(0);
                        if(column!=null){
                            String colName = column.getColName();
                            if(!EmptyUtil.isEmpty(colName)){
                                if(queryColNameExits(tbName,colName)){
                                    Column column1 = new Column();
                                    column1.setTbName(tbName);
                                    column1.setColName(colName);
                                    List<Column> col = columnMapper.select(column1);
                                    if(col!=null&&col.size()==1){
                                        String dataType = col.get(0).getDataType();
                                        column.setDataType(dataType);
                                        tableMapper.renameCol(table);
                                    }
                                    return Result.fail("新增列时没有datatype数据或者datatype插入有误," +
                                            "请叫管理员修改");
                                }
                                return Result.fail("没有这一列");

                            }
                        }

                    }



                }

            }
        }
        return Result.fail();
    }

    public Result queryAllColNameByTbName(Table table) {
        if(table!=null&&!EmptyUtil.isEmpty(table.getTbName())){

            List<Column> columns=columnMapper.queryAllColNameByTbName(table);
            return Result.success(columns);
        }
        return Result.fail();
    }
    private List<Column> typeToDataTypes(List<Column> list){
        List<Column> list2=new ArrayList<>();
        for (Column column : list) {
            Column column1 = typeToDataType(column);
            list2.add(column1);
        }
        return list2;
    }
    private Column typeToDataType(Column column){
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
        }
        return column;
    }

    public Result dropTable(Table table) {
        if(table!=null){
            String tbName = table.getTbName();
            if(!EmptyUtil.isEmpty(tbName)){
                System.out.println("222");
            }
        }
        return Result.fail();

    }
}
