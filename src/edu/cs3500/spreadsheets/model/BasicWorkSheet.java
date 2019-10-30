package edu.cs3500.spreadsheets.model;


import java.util.HashMap;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.ToCellVisitor;

public class BasicWorksheet implements Worksheet<CellFormula> {

  private HashMap<Coord, CellFormula> grid;

  public static class BasicWorksheetBuilder implements WorksheetReader.WorksheetBuilder<BasicWorksheet> {
    private HashMap<Coord, CellFormula> grid = new HashMap<Coord, CellFormula>();
    private static Parser parser = new Parser();
    private HashMap<String, CellVisitor> functions = this.initialFunctions();

    private HashMap<String, CellVisitor> initialFunctions() {
      HashMap<String, CellVisitor> result = new HashMap<String, CellVisitor>();
      result.put("SUM", new AddVisitor());
      result.put("PRODUCT", new ProductVisitor());
      result.put("APPEND", new AppendVisitor());
      result.put("<", new SmallerThanVisitor());
      return result;
    }


    public WorksheetReader.WorksheetBuilder<BasicWorksheet> addFunction(
            String name, CellVisitor function) {
      if (name == null || function == null) {
        throw new IllegalArgumentException("Inputs cannot be null.");
      }
      functions.put(name, function);
      return this;
    }

    @Override
    public WorksheetReader.WorksheetBuilder<BasicWorksheet> createCell(
            int col, int row, String contents) {
      if (contents == null) {
        return this;
      }
      this.initialCell(new Coord(col, row), contents);
      return this;
    }

    private void initialCell(Coord coord, String contents) {
      ToCellVisitor visitor = new ToCellVisitor(grid, functions);
      if (contents.length() == 0) {
        return;
      }
        if (contents.charAt(0) == '=') {
          grid.put(coord,parser.parse(contents.substring(1)).accept(visitor));
        } else {
          grid.put(coord, parser.parse(contents).accept(visitor));
        }
    }

    @Override
    public BasicWorksheet createWorksheet() {
      return new BasicWorksheet(this.grid);
    }
  }

  private BasicWorksheet(HashMap<Coord, CellFormula> grid) {
    if (grid == null) {
      throw new IllegalArgumentException("The input gird cannot be null.");
    }
    this.grid = grid;
  }

  private int getMaxCol(HashMap<Coord, CellFormula> grid) {
    HashMap<Integer, Integer> colLengths = new HashMap<>();
    for (Coord coord : grid.keySet()) {
      if (colLengths.containsKey(coord.row)) {
        colLengths.put(coord.row, colLengths.get(coord.row) + 1);
      } else {
        colLengths.put(coord.row, 1);
      }
    }
    int max = -1;
    for (Integer length : colLengths.values()) {
      if (length > max) {
        max = length;
      }
    }
    return max;
  }

  public BasicWorksheet() {
    grid = new HashMap<Coord, CellFormula>();
  }

  @Override
  public void clearAll() {
    grid.clear();
  }

  @Override
  public void clear(Coord coord) {
    if (coord == null) {
      throw new IllegalArgumentException("Coord cannot be null.");
    }
    grid.remove(coord);
  }

  @Override
  public void set(Coord coord, CellFormula cell) {
    if (coord == null || cell == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    grid.put(coord, cell);
  }

  @Override
  public int getNumRows() {
    return grid.size();
  }

  @Override
  public int getNumCols() {
    return this.getMaxCol(grid);
  }

  @Override
  public CellValue getCellAt(Coord coord) {
      return grid.get(coord).evaluate();
  }

}
