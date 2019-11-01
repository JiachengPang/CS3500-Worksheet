package edu.cs3500.spreadsheets.sexp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFunction;
import edu.cs3500.spreadsheets.model.CellReference;
import edu.cs3500.spreadsheets.model.CellVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.StringValue;


public class ToCellVisitor implements SexpVisitor<Cell> {

  HashMap<Coord, Cell> grid;
  HashMap<String, CellVisitor> functions;


  public ToCellVisitor(HashMap<Coord, Cell> grid, HashMap<String, CellVisitor> functions) {
    if (grid == null || functions == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    this.grid = grid;
    this.functions = functions;
  }


  @Override
  public Cell visitBoolean(boolean b) {
    return new BooleanValue(b);
  }

  @Override
  public Cell visitNumber(double d) {
    return new DoubleValue(d);
  }

  @Override
  public Cell visitSList(List<Sexp> l) {
    List<Cell> args = new ArrayList<>();
    for (int i = 1; i < l.size(); i++) {
      args.add(l.get(i).accept(this));
    }

    return this.createFunction(l.get(0), args);
  }

  @Override
  public Cell visitSymbol(String s) {
    if (functions.containsKey(s)) {
      return new StringValue(s);
    }

    if (!s.contains(":")) {
      int singleNumIndex = this.getNumIndex(s);
      if (singleNumIndex == 0) {
        throw new IllegalArgumentException("Invalid symbol.");
      }
      this.allNumberAfterNumIndex(s);
      Coord refCoord = new Coord(Coord.colNameToIndex(s.substring(0, singleNumIndex)),
              Integer.parseInt(s.substring(singleNumIndex)));
      return new CellReference(refCoord, refCoord, grid);
    }

    if (s.length() - 1 == s.indexOf(":")) {
      throw new IllegalArgumentException("Invalid reference. No ending point detected.");
    }

    String ref1 = s.substring(0, s.indexOf(":"));
    String ref2 = s.substring(s.indexOf(":") + 1);
    int numIndex1 = this.getNumIndex(ref1);
    int numIndex2 = this.getNumIndex(ref2);
    this.allNumberAfterNumIndex(ref1);
    this.allNumberAfterNumIndex(ref2);

    if (numIndex1 == 0 || numIndex2 == 0) {
      throw new IllegalArgumentException("Invalid reference. No column index detected.");
    }

    Coord coord1 = new Coord(Coord.colNameToIndex(ref1.substring(0, numIndex1)),
            Integer.parseInt(ref1.substring(numIndex1)));
    Coord coord2 = new Coord(Coord.colNameToIndex(ref2.substring(0, numIndex2)),
            Integer.parseInt(ref2.substring(numIndex2)));

    CellReference result = new CellReference(coord1, coord2, grid);
    return result;
  }


  private int getNumIndex(String ref) {
    char[] refChar = ref.toCharArray();
    int numIndex = 0;

    for (int i = 0; i < refChar.length; i++) {
      if (Character.isDigit(refChar[i])) {
        numIndex = i;
        break;
      }
    }
    return numIndex;
  }

  private void allNumberAfterNumIndex(String ref) throws IllegalArgumentException {
    int numIndex = this.getNumIndex(ref);
    char[] charAfterIndex = ref.substring(numIndex).toCharArray();
    for (Character c : charAfterIndex) {
      if (!Character.isDigit(c)) {
        throw new IllegalArgumentException("Reference is malformed.");
      }
    }
  }

  @Override
  public Cell visitString(String s) {
    return new StringValue(s);
  }

  private Cell createFunction(Sexp name, List<Cell> args) {
    String funName = (String) name.accept(this).evaluate().getValue();
    if (!functions.containsKey(funName)) {
      throw new IllegalArgumentException("The given function is not supported.");
    }

    return new CellFunction(functions.get(funName), args);
  }

}
