package edu.cs3500.spreadsheets.model;

public interface Worksheet<K> {

  void clearAll();
  void clear(Coord coord);
  void set(Coord coord, CellFormula cell);
  int getNumRows();
  int getNumCols();
  // getValueAt is the same as apply method in visitor class
  CellValue getValueAt(Coord coord);

}
