package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a ContentVisitor adds the cell it visits and returns a DoubleValue containing the
 * final result. StringValue, BooleanValue, and BlankValue are treated as 0.
 */
public class AddVisitor implements ContentVisitor<DoubleValue> {

  @Override
  public DoubleValue visitFunction(CellFunction func) {
    Double result = 0.0;
    FlattenArgsVisitor flatVisitor = new FlattenArgsVisitor();
    List<IValue> flatArgs = new ArrayList<>();
    for (IContent arg : func.getArgs()) {
      flatArgs.addAll(arg.accept(flatVisitor));
    }

    for (IValue arg : flatArgs) {
      try {
        result += arg.accept(this).getRawValue();
      } catch (IllegalStateException e) {
        if (e.getMessage().equals("Blank cell cannot be evaluated.")) {
          break;
        } else {
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
      result += cell.getCurrentValue().accept(this).getRawValue();
    }
    return new DoubleValue(result);
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
  public DoubleValue visitBlank(BlankValue blank) {
    return new DoubleValue(0.0);
  }
}
