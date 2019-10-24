package edu.cs3500.spreadsheets.model;

public interface Worksheet<K> {

  void clearAll();
  void clear(Coord coord);
  void edit(Coord coord, Formula cell);
  int getNumRows();
  int getNumCols();
  <S> S getValueAt(Coord coord);
  void sum(Coord coord1, Coord coord2);
  void product(Coord coord1, Coord coord2);
  void smallerThan(Coord coord1, Coord coord2);
  void append(Coord coord1, Coord coord2);
  <S> S applyVisitor(Coord coord, CellVisitor visitor);

}
