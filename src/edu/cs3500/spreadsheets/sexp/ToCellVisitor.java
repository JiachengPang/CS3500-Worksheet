package edu.cs3500.spreadsheets.sexp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.CellFormula;
import edu.cs3500.spreadsheets.model.CellFunction;
import edu.cs3500.spreadsheets.model.CellReference;
import edu.cs3500.spreadsheets.model.CellVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.StringValue;


public class ToCellVisitor implements SexpVisitor<CellFormula> {


  HashMap<Coord, CellFormula> grid;
  HashMap<String, CellVisitor> functions;


  public ToCellVisitor(HashMap<Coord, CellFormula> grid, HashMap<String, CellVisitor> functions) {
    if (grid == null || functions == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    this.grid = grid;
    this.functions = functions;
  }


  @Override
  public CellFormula visitBoolean(boolean b) {
    return new BooleanValue(b);
  }

  @Override
  public CellFormula visitNumber(double d) {
    return new DoubleValue(d);
  }

  @Override
  public CellFormula visitSList(List<Sexp> l) {
    List<CellFormula> args = new ArrayList<>();
    for (int i = 1; i < l.size(); i++) {
      args.add(l.get(i).accept(this));
    }
    return this.createFunction(l.get(0), args);
  }

  @Override
  public CellFormula visitSymbol(String s) {
    if (functions.containsKey(s)) {
      return new StringValue(s);
    }
    int singleNumIndex = this.getNumIndex(s);
    if (singleNumIndex == 0) {
      throw new IllegalArgumentException("Invalid symbol.");
    }
    this.allNumberAfterNumIndex(s);
    if (!s.contains(":")) {
      return new CellReference(Arrays.asList(grid.get(
              new Coord(Coord.colNameToIndex(s.substring(0, singleNumIndex)),
                      Integer.parseInt(s.substring(singleNumIndex))))));
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

    List<CellFormula> cells = new ArrayList<>();
    if (grid.containsKey(coord1) && grid.containsKey(coord2)) {
      for (int i = coord1.row; i <= coord2.row; i++) {
        for (int j = coord1.col; j <= coord2.col; j++) {
          cells.add(grid.get(new Coord(j, i)));
        }
      }
    }

    return new CellReference(cells);
  }

  private int getNumIndex(String ref) {
    char[] refChar = ref.toCharArray();
    int numIndex = 0;

    for (int i = 0; i < refChar.length; i++) {
      if (Character.isDigit(refChar[i])) {
        numIndex = i;
      }
    }
    return numIndex;
  }

  private void allNumberAfterNumIndex(String ref) throws IllegalArgumentException {
    int numIndex = this.getNumIndex(ref);
    char[] charAfterIndex = ref.substring(numIndex).toCharArray();
    for (Character c: charAfterIndex) {
      if (!Character.isDigit(c)) {
        throw new IllegalArgumentException("Reference is malformed.");
      }
    }
  }

  @Override
  public CellFormula visitString(String s) {
    return new StringValue(s);
  }

  private CellFormula createFunction(Sexp name, List<CellFormula> args) {
    String funName = (String) name.accept(this).evaluate().getValue();
    if (!functions.containsKey(funName)) {
      throw new IllegalArgumentException("The given function is not supported.");
    }
    return new CellFunction(functions.get(funName), args);

  }

}
