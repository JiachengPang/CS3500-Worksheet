package edu.cs3500.spreadsheets.sexp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFunction;
import edu.cs3500.spreadsheets.model.CellReference;
import edu.cs3500.spreadsheets.model.ContentVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.IContent;
import edu.cs3500.spreadsheets.model.StringValue;

/**
 * Represents a SexpVisitor that transforms and returns an s-expression to a Cell.
 */
public class ToContentVisitor implements SexpVisitor<IContent> {

  private HashMap<Coord, Cell> grid;
  private HashMap<String, ContentVisitor> functions;
  private Coord position;

  /**
   * Constructs a visitor that create IContent of a basic worksheet model from a Sexp.
   *
   * @param grid      the map of coord to cells in the current worksheet
   * @param functions the map of function names to function visitors that are supported by a basic
   *                  worksheet
   * @param position  the position of the cell that holds this content
   */
  public ToContentVisitor(HashMap<Coord, Cell> grid,
                          HashMap<String, ContentVisitor> functions, Coord position) {
    if (grid == null || functions == null || position == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    this.grid = grid;
    this.functions = functions;
    this.position = position;
  }


  @Override
  public IContent visitBoolean(boolean b) {
    return new BooleanValue(b);
  }

  @Override
  public IContent visitNumber(double d) {
    return new DoubleValue(d);
  }

  @Override
  public IContent visitSList(List<Sexp> l) {
    List<IContent> args = new ArrayList<>();
    for (int i = 1; i < l.size(); i++) {
      args.add(l.get(i).accept(this));
    }

    return this.createFunction(l.get(0), args);
  }

  @Override
  public IContent visitSymbol(String s) {
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
      return new CellReference(position, refCoord, refCoord, grid);
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

    CellReference result = new CellReference(position, coord1, coord2, grid);
    return result;
  }


  /**
   * Get the index of the first number in the given string.
   *
   * @param ref the given string
   * @return the first number index
   */
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

  /**
   * Checks if all characters after the first number are also numbers in the given string.
   *
   * @param ref the given string
   * @throws IllegalArgumentException if the given string does not satisfy the above condition
   */
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
  public IContent visitString(String s) {
    return new StringValue(s);
  }

  /**
   * Creates a function of the given name and takes in the given arguments.
   *
   * @param name the s expression representing the name of the function
   * @param args the arguments that the function takes in
   * @return
   */
  private IContent createFunction(Sexp name, List<IContent> args) {
    String funName = name.accept(this).getValue().getRawValue();
    if (!functions.containsKey(funName)) {
      throw new IllegalArgumentException("The given function is not supported.");
    }
    return new CellFunction(funName, functions.get(funName), args);
  }

}