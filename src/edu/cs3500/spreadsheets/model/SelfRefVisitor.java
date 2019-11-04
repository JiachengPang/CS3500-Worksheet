package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
/*
public class SelfRefVisitor implements ContentVisitor<Boolean> {

  Cell cell;
  Boolean flag;
  List<IContent> visited;

  public SelfRefVisitor(Cell cell) {
    if (cell == null) {
      throw new IllegalArgumentException("Cell cannot be null.");
    }
    this.cell = cell;
    this.flag = false;
    this.visited = new ArrayList<>();
  }

  @Override
  public Boolean visitFunction(CellFunction func) {
    if (!visited.contains(func)) {
      visited.add(func);
      if (flag == true) {
        return true;
      }
      for (IContent arg : func.getArgs()) {
        flag = flag || arg.accept(this);
      }
    }
    return false;
  }

  @Override
  public Boolean visitReference(CellReference ref) {
    if (!visited.contains(ref)) {

    Boolean result = false;
    for (Cell reference : ref.getReferences()) {
      result = result || cell.getDirectListeners().contains(reference);
      if (result == true) {
        return true;
      }
      for (Cell indirect : cell.getDirectListeners()) {
        result = result || indirect.getContent().accept(this);
        if (result == true) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Boolean visitDouble(DoubleValue num) {
    return false;
  }

  @Override
  public Boolean visitString(StringValue str) {
    return false;
  }

  @Override
  public Boolean visitBoolean(BooleanValue bool) {
    return false;
  }

  @Override
  public Boolean visitBlank(BlankValue blank) {
    return false;
  }
}

 */
