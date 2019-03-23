package com.wuyue.service;

import com.wuyue.Util.LogUtil;
import com.wuyue.common.Result;
import com.wuyue.mapper.TableMapper;
import com.wuyue.pojo.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableService {
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
                    LogUtil.DebugLog(e,e.getMessage());
                    return Result.fail("插入失败,请检查是否有同名列");
                }
                boolean b1 = queryTbNameExits(name);
                if(b1){
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
}
