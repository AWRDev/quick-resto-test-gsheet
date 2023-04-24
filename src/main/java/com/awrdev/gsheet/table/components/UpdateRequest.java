package com.awrdev.gsheet.table.components;

public class UpdateRequest {
    public String cellId;
    public String newValue;

    public UpdateRequest(String cellId, String newValue) {
        this.cellId = cellId;
        this.newValue = newValue;
    }

    public UpdateRequest() {
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "cellId='" + cellId + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
