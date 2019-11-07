package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a ContentVisitor that appends the string representation of the cell it visits, and
 * returns a StringValue containing the final result. A BlankValue is treated as an empty string.
 */
public class AppendVisitor implements ContentVisitor<StringValue> {

  @Override
  public StringValue visitFunction(CellFunction func) {
    String result = "";
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
    return new StringValue(result);
  }

  @Override
  public StringValue visitReference(CellReference ref) {
    String result = "";
    for (Cell cell : ref.getReferences()) {
      result += cell.getCurrentValue().accept(this).getRawValue();
    }
    return new StringValue(result);
  }


  @Override
  public StringValue visitDouble(DoubleValue num) {
    return new StringValue(num.toString());
  }

  @Override
  public StringValue visitString(StringValue str) {
    return str;
  }

  @Override
  public StringValue visitBoolean(BooleanValue bool) {
    return new StringValue(bool.toString());
  }

  @Override
  public StringValue visitBlank(BlankValue blank) {
    return new StringValue("");
  }
}
