package edu.cs3500.spreadsheets.model;

import java.util.HashMap;

public class BasicWorkSheet implements Worksheet<Formula> {
  HashMap<Integer, HashMap<Integer, Formula>> grid;
  

  @Override
  public void clearAll() {

  }

  @Override
  public void clear(Coord coord) {

  }

  @Override
  public void edit(Coord coord, Formula cell) {

  }

  @Override
  public int getNumRows() {
    return 0;
  }

  @Override
  public int getNumCols() {
    return 0;
  }

  @Override
  public <S> S getValueAt(Coord coord) {
    return null;
  }

  @Override
  public void sum(Coord coord1, Coord coord2) {

  }

  @Override
  public void product(Coord coord1, Coord coord2) {

  }

  @Override
  public void smallerThan(Coord coord1, Coord coord2) {

  }

  @Override
  public void append(Coord coord1, Coord coord2) {

  }

  @Override
  public <S> S applyVisitor(Coord coord, CellVisitor visitor) {
    return null;
  }
}
