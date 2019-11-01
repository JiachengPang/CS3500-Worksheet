package edu.cs3500.spreadsheets.model;

/**
 * Represents a CellVisitor adds the cell it visits
 * and returns a DoubleValue containing the final result.
 * StringValues and BooleanValues are treated as 0.
 */
public class AddVisitor implements CellVisitor<DoubleValue> {

  @Override
  public DoubleValue visitFunction(CellFunction func) {
    Double result = 0.0;
    for (Cell cell : func.getArgs()) {
      try {
        result += cell.evaluate().accept(this).getValue();
      } catch (IllegalStateException e) {
        if (e.getMessage().equals("Blank cell cannot be evaluated.")) {
          break;
        }
        else {
          throw e;
        }
      }
    }
    return new DoubleValue(result);
  }

  @Override
  public DoubleValue visitReference(CellReference ref) {
    Double result = 0.0;
    for (Cell cell : ref.getReferences()) {
      result += cell.accept(this).getValue();
    }
    return new DoubleValue(result);
  }

  @Override
  public DoubleValue visitValue(CellValue val) {
    return val.accept(this);
  }

  @Override
  public DoubleValue visitDouble(DoubleValue num) {
    return num;
  }

  @Override
  public DoubleValue visitString(StringValue str) {
    return new DoubleValue(0.0);
  }

  @Override
  public DoubleValue visitBoolean(BooleanValue bool) {
    return new DoubleValue(0.0);
  }

  @Override
  public DoubleValue visitBlank(CellBlank blank) {
    return new DoubleValue(0.0);
  }

}
