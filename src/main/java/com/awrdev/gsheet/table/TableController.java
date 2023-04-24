package com.awrdev.gsheet.table;


import com.awrdev.gsheet.table.components.UpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/table")
@CrossOrigin
public class TableController {
    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }
    @GetMapping()
    public Table getTable(){
        return tableService.getTable();
    }

    @GetMapping("/{cellId}")
    public String getCellValue(@PathVariable String cellId){
        return tableService.getCellValue(cellId);
    }
    @GetMapping("clearTable")
    public void clearTable(){
        tableService.clearTable();
    }
    @PutMapping(path = "/a")
    public String updateTable(@RequestBody UpdateRequest updateRequest){
        System.out.println(updateRequest);
        if (updateRequest.newValue.charAt(0) == '='){
            tableService.updateCellFormula(updateRequest.cellId, updateRequest.newValue);
        }
        else {
            tableService.updateCellValueByRequest(updateRequest.cellId, updateRequest.newValue);
        }
        return "All good!";
    }
}
