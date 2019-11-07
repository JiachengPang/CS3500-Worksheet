package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CellVisitor that reduces the given cell to a list of most basic IValues, including
 * DoubleValue, StringValue, BooleanValue, or BlankValue.
 */
public class FlattenArgsVisitor implements ContentVisitor<List<IValue>> {

  @Override
  public List<IValue> visitFunction(CellFunction func) {
    List<IValue> result = new ArrayList<>();
    IValue flatValue = func.getValue();
    result.add(flatValue);
    return result;
  }

  @Override
  public List<IValue> visitReference(CellReference ref) {
    List<IValue> result = new ArrayList<>();
    if (ref.getReferences().size() == 1) {
      try {
        IValue flatValue = ref.getReferences().get(0).getCurrentValue();
        result.add(flatValue);
      } catch (IllegalStateException e) {
        if (e.getMessage().equals("Blank cell does not have a value.")) {
          return result;
        } else {
          throw e;
        }
      }
    } else {
      for (Cell reference : ref.getReferences()) {
        try {
          result.add(reference.getCurrentValue());
        } catch (IllegalStateException e) {
          if (!e.getMessage().equals("Blank cell does not have a value.")) {
            throw e;
          }
        }
      }
    }
    return result;
  }

  @Override
  public List<IValue> visitDouble(DoubleValue num) {
    List<IValue> result = new ArrayList<>();
    result.add(num);
    return result;
  }

  @Override
  public List<IValue> visitString(StringValue str) {
    List<IValue> result = new ArrayList<>();
    result.add(str);
    return result;
  }

  @Override
  public List<IValue> visitBoolean(BooleanValue bool) {
    List<IValue> result = new ArrayList<>();
    result.add(bool);
    return result;
  }

  @Override
  public List<IValue> visitBlank(BlankValue blank) {
    List<IValue> result = new ArrayList<>();
    result.add(blank);
    return result;
  }
}
