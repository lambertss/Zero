package com.wuyue.controller;

import com.wuyue.common.Result;
import com.wuyue.pojo.Request;
import com.wuyue.pojo.Table;
import com.wuyue.service.TableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("table")
@Api(value = "table", description = "表格控制器")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping("createTable")
    @ApiOperation(httpMethod = "POST", value = "创建表", notes = "tbName,colName")
    public Result createTable(@RequestBody Request<Table> request ){
        return tableService.createTable(request);
    }
    @PostMapping("forgeDropTable")
    @ApiOperation(httpMethod = "POST", value = "删除表", notes = "tbName")
    public Result forgeDropTable(@RequestBody Request<Table> request){
        return tableService.forgeDropTable(request.getData());
    }
    @PostMapping("dropTable")
    @ApiOperation(httpMethod = "POST",value = "删除表",notes = "tbName")
    public Result dropTable (@RequestBody Request<Table> request){
        return tableService.dropTable(request.getData());
    }
    @PostMapping("dropTbColumn")
    @ApiOperation(httpMethod = "POST", value = "删除列", notes = "tbName,colName")
    public Result dropTBColumn(@RequestBody Request<Table> request){
        return tableService.dropTBColumn(request.getData());
    }
    @PostMapping("addColumns")
    @ApiOperation(httpMethod = "POST", value = "新增列", notes = "tbName,colName")
    public Result addColumns(@RequestBody Request<Table> request){
        return tableService.addColumns(request.getData());
    }
    @PostMapping("updateTable")
    @ApiOperation(httpMethod = "POST", value = "更新表", notes = "tbName,colName")
    public Result updateTable(@RequestBody Request<Table> request){
        return tableService.updateTable(request.getData());
    }
    @PostMapping("renameTbName")
    @ApiOperation(httpMethod = "POST", value = "表重命名", notes = "tbName,newTbName")
    public Result renameTbName(@RequestBody Request<Table> request){
        Table table = request.getData();
        return tableService.renameTb(table.getNewTbName(),table.getTbName());
    }
    @PostMapping("renameCol")
    @ApiOperation(httpMethod = "POST", value = "列重命名", notes = "tbName,colName,newName")
    public Result renameColName(@RequestBody Request<Table> request){
        return tableService.renameColName(request.getData());
    }
    @PostMapping("queryAllColByTbName")
    @ApiOperation(httpMethod = "POST", value = "查一个表的所有列信息", notes = "tbName")
    public Result queryAllColNameByTbName(@RequestBody Request<Table> request){
        return tableService.queryAllColNameByTbName(request.getData());
    }




}
