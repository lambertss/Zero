package com.wuyue.controller;

import com.wuyue.common.Result;
import com.wuyue.pojo.Request;
import com.wuyue.pojo.Table;
import com.wuyue.service.TableService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("table")
@Api(value = "table", description = "表格控制器")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping("createTable")
    public Result createTable(@RequestBody Request<Table> request ){
        return tableService.createTable(request.getData());
    }
    @PostMapping("dropTable")
    public Result dropTable(@RequestBody Request<Table> request){
        return tableService.forgeDropTable(request.getData());
    }
    @PostMapping("dropTbColumn")
    public Result dropTBColumn(@RequestBody Request<Table> request){
        return tableService.dropTBColumn(request.getData());
    }
    @PostMapping("addColumns")
    public Result addColumns(@RequestBody Request<Table> request){
        return tableService.addColumns(request.getData());
    }
    @PostMapping("updateTable")
    public Result updateTable(@RequestBody Request<Table> request){
        return tableService.updateTable(request.getData());
    }
    @PostMapping("renameTbName")
    public Result renameTbName(@RequestBody Request<Table> request){
        Table table = request.getData();
        return tableService.renameTb(table.getNewTbName(),table.getTbName());
    }
    @PostMapping("renameCol")
    public Result renameColName(@RequestBody Request<Table> request){
        return tableService.renameColName(request.getData());
    }
    @PostMapping("queryAllColNameByTbName")
    public Result queryAllColNameByTbName(@RequestBody Request<Table> request){

        return tableService.queryAllColNameByTbName(request.getData());
    }

}
