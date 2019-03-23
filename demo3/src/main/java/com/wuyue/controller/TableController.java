package com.wuyue.controller;

import com.wuyue.common.Result;
import com.wuyue.pojo.Request;
import com.wuyue.pojo.Table;
import com.wuyue.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("table")
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
    @PostMapping("updateTable")
    public Result updateTable(@RequestBody Request<Table> request){
        return tableService.updateTable(request.getData());
    }
    @PostMapping("renameTbName")
    public Result renameTbName(@RequestBody Request<Table> request){
        return tableService.updateTable(request.getData());
    }
}
