package edu.cs3500.spreadsheets.model;

public interface Worksheet {

  void clearAll();
  void clear(int col, int row);
  void set(int col, int row, Cell cell);
  int getNumRows();
  int getNumCols();
  // getValueAt is the same as apply method in visitor class
  CellValue getCellAt(int col, int row);

}
