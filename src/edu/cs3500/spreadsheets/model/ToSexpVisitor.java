package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;

public class ToSexpVisitor implements ContentVisitor<Sexp> {

  private List<String> functionNames;

  /**
   * Constructs a visitor that create IContent of a basic worksheet model from a Sexp.
   *
   * @param functionNames the map of function names to function visitors that are supported by a basic
   *                  worksheet
   */
  public ToSexpVisitor(List<String> functionNames) {
    if (functionNames == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    this.functionNames = functionNames;
  }


  @Override
  public Sexp visitFunction(CellFunction func) {
    List<Sexp> result = new ArrayList<>();
    result.add(new SSymbol(func.name));
    for (IContent arg : func.getArgs()) {
      result.add(arg.accept(this));
    }

    return new SList(result);
  }

  @Override
  public Sexp visitReference(CellReference ref) {
    if (ref.start.equals(ref.end)) {
      return new SSymbol(ref.start.toString());
    } else {
      return new SSymbol(ref.start.toString() + ":" + ref.end.toString());
    }
  }

  @Override
  public Sexp visitDouble(DoubleValue num) {
    return new SNumber(num.getRawValue());
  }

  @Override
  public Sexp visitString(StringValue str) {
    return new SString(str.getRawValue());
  }

  @Override
  public Sexp visitBoolean(BooleanValue bool) {
    return new SBoolean(bool.getRawValue());
  }

  @Override
  public Sexp visitBlank(BlankValue blank) {
    return null;
  }
}

