package com.newthread.android.bean;

/**
 * Created by 翌日黄昏 on 2014/10/1.
 */
public class MyTable {
    private int row = 0;
    private int col = 0;
    private String[][] table;

    public MyTable() {
        this.row = 1;
        this.col = 1;
        table = new String[this.row][this.col];
    }

    public MyTable(int col) {
        this.row = 1;
        this.col = col;
        table = new String[this.row][this.col];
    }

    public MyTable(int row, int col) {
        this.row = row;
        this.col = col;
        table = new String[this.row][this.col];
    }

    public MyTable getRow(int row) {
        MyTable table = new MyTable(1, this.col);
        for (int col = 0; col < this.col; col++) {
            table.setCell(this.table[0][col], 0, col);
        }
        return table;
    }

    public MyTable getCol(int col) {
        MyTable table = new MyTable(this.row, 1);
        for (int row = 0; row < this.row; row++) {
            table.setCell(this.table[row][0], row, 0);
        }
        return table;
    }

    public String getCell(int row, int col) {
        if (row >= this.row || col >= this.col) return null;
        return table[row][col];
    }

    public boolean setCell(String cell, int row, int col) {
        if (cell == null || row >= this.row || col >= this.col) return false;
        this.table[row][col] = cell;
        return true;
    }

    public int getCellSize() {
        return this.row * this.col;
    }

    public int getRowLength() {
        return row;
    }

    public int getColLength() {
        return col;
    }

    @Override
    public String toString() {
        String table = "";
        for(int row = 0; row < this.row; row ++) {
            for(int col = 0; col < this.col; col ++) {
                table += this.table[row][col] + " ";
            }
            table += "\n";
        }
        return "MyTable{" +
                "row=" + row +
                ", col=" + col +
                ", table=\n" + table +
                '}';
    }
}
