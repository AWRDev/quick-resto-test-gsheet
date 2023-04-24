package com.awrdev.gsheet.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Table {
    public String name;
    public String[][] cellValues;
    public String[][] cellFormula;
    public ArrayList<String>[][] cellDependants;

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", cellValues=" + Arrays.toString(cellValues) +
                ", cellFormula=" + Arrays.toString(cellFormula) +
                '}';
    }

    public Table(String name, String[][] cellValues) {
        this.name = name;
        this.cellValues = cellValues;
        this.cellFormula = new String[4][4];
        this.cellDependants = new ArrayList[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.cellValues[i][j] = "";
                this.cellFormula[i][j] = "";
                this.cellDependants[i][j] = new ArrayList<String>();
            }
        }
    }

    public Table(String name, int rows, int cols) {
        this.name = name;
        this.cellValues = new String[rows][cols];
        this.cellFormula = new String[rows][cols];
        this.cellDependants = new ArrayList[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.cellValues[i][j] = "";
                this.cellFormula[i][j] = "";
                this.cellDependants[i][j] = new ArrayList<String>();
            }
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[][] getCellValues() {
        return cellValues;
    }

    public void setCellValues(String[][] cellValues) {
        this.cellValues = cellValues;
    }

    public String[][] getCellFormula() {
        return cellFormula;
    }

    public void setCellFormula(String[][] cellFormula) {
        this.cellFormula = cellFormula;
    }

    public String getCellValue(String cellId){
        Integer colNum = cellId.charAt(0) - 'A';
        Integer rowNum = (int) cellId.charAt(1) - '0'-1;
        String cellValue = "0";
        try {
            cellValue = cellValues[rowNum][colNum];
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Cell doesn't exist");
        }
        return cellValue;
    }
    public static ArrayList<String> getMatches(String expression){
        ArrayList<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z]\\d+");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            String match = matcher.group();
            if (!matches.contains(match)){
                matches.add(match);
            }
        }
        return matches;
    }
    public static int[] getNumericCellId(String cellId){
        Integer colNum = cellId.charAt(0) - 'A';
        Integer rowNum = (int) cellId.charAt(1) - '0'-1;
        int[] numericCellId = {rowNum, colNum};
        return numericCellId;
    }
    public static String getStringCellId(int p_rowNum, int colNum) {
        int rowNum = p_rowNum + 1;
        char colChar = (char) (colNum + 'A');
        return String.format("%c%d", colChar, rowNum);
    }

    public String evaluateLinks(String expression){
        String evaluatedExpression = expression;
        Pattern pattern = Pattern.compile("[A-Z]\\d+");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            String match = matcher.group();
            evaluatedExpression= evaluatedExpression.replace(match, getCellValue(match));
        }
        return evaluatedExpression;
    }

    public void removeDependencies(String cellId) {
        int[] numericCellId = getNumericCellId(cellId);
        String oldExpression = cellFormula[numericCellId[0]][numericCellId[1]];
        cellFormula[numericCellId[0]][numericCellId[1]] = "";

        ArrayList<String> matches = Table.getMatches(oldExpression);
        for (String match: matches){
            int[] dependerCellId = Table.getNumericCellId(match);
            cellDependants[dependerCellId[0]][dependerCellId[1]].remove(cellId);
        }
    }
}
