package com.awrdev.gsheet.table;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class TableService {

    public Table table = new Table("table_1", 4, 4);
    public Table getTable(){
        return table;
    }

    public void updateTable(String cellId, String newValue) {
        Integer colNum = cellId.charAt(0) - 'A';
        Integer rowNum = (int) cellId.charAt(1) - '0'-1;

        table.cellValues[rowNum][colNum] = newValue;
        if (!table.cellDependants[rowNum][colNum].isEmpty()){
            for (String dependantCellId: table.cellDependants[rowNum][colNum]){
                reevaluateCellFormula(dependantCellId);
            }
        }
    }

    public void reevaluateCellFormula(String cellId){
        Integer colNum = cellId.charAt(0) - 'A';
        Integer rowNum = (int) cellId.charAt(1) - '0'-1;
        String expression = table.cellFormula[rowNum][colNum];

        String eval1 = table.evaluateLinks(expression.substring(1));
        System.out.println(eval1);
//        int evaluatedString = evaluate(eval1);
        //int evaluatedString = Expression.eval(eval1).asInt();
        int evaluatedString = new DoubleEvaluator().evaluate(eval1).intValue();
        //table.cellValues[rowNum][colNum] = Integer.toString(evaluatedString);
        updateTable(cellId, Integer.toString(evaluatedString));
    }

    public void updateCellFormula(String cellId, String formula){
        Integer colNum = cellId.charAt(0) - 'A';
        Integer rowNum = (int) cellId.charAt(1) - '0'-1;
        if (!Objects.equals(table.cellFormula[rowNum][colNum], "")){
            table.removeDependencies(cellId);
        }
        table.cellFormula[rowNum][colNum] = formula;

        ArrayList<String> matches = Table.getMatches(formula);
        for (String match: matches){
            System.out.println(match);
            int[] dependerCellId = Table.getNumericCellId(match);
            table.cellDependants[dependerCellId[0]][dependerCellId[1]].add(Table.getStringCellId(rowNum, colNum));
        }
        String eval1 = table.evaluateLinks(formula.substring(1));
        System.out.println(eval1);

//        table.cellValues[rowNum][colNum] = Integer
//                .toString(evaluate(
//                        eval1));

        //int evaluatedString = evaluate(eval1);
        //int evaluatedString = Expression.eval(eval1).asInt();
        int evaluatedString = new DoubleEvaluator().evaluate(eval1).intValue();
        updateTable(cellId, Integer.toString(evaluatedString));
    }

    public String getCellValue(String cellId){
        return this.table.getCellValue(cellId);
    }

    public void updateCellValueByRequest(String cellId, String newValue) {
        Integer colNum = cellId.charAt(0) - 'A';
        Integer rowNum = (int) cellId.charAt(1) - '0'-1;
        table.cellValues[rowNum][colNum] = newValue;
        if (!Objects.equals(table.cellFormula[rowNum][colNum], "")){
            table.removeDependencies(cellId);
        }
        table.cellFormula[rowNum][colNum] = "";
        if (!table.cellDependants[rowNum][colNum].isEmpty()){
            for (String dependantCellId: table.cellDependants[rowNum][colNum]){
                reevaluateCellFormula(dependantCellId);
            }
        }
    }

    public void clearTable() {
        table = new Table("table_1", 4, 4);
    }
}
