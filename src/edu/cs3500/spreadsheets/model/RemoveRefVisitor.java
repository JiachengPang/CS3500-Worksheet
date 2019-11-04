package edu.cs3500.spreadsheets.model;

/**
 * Represents a ContentVisitor that removes the oldCell from the list of listeners from its
 * references.
 */
public class RemoveRefVisitor implements ContentVisitor<IContent> {

  Cell oldCell;

  /**
   * Construcs a RemoveRefVisitor with a cell to be removed.
   * @param oldCell cell to be removed
   */
  public RemoveRefVisitor(Cell oldCell) {
    if (oldCell == null) {
      throw new IllegalArgumentException("Cell cannot be null.");
    }
    this.oldCell = oldCell;
  }

  @Override
  public IContent visitFunction(CellFunction func) {
    for (IContent arg : func.getArgs()) {
      arg.accept(this);
    }
    return func;
  }

  @Override
  public IContent visitReference(CellReference ref) {
    for (Cell cell : ref.getReferences()) {
      cell.removeInterest(oldCell);
    }
    return ref;
  }

  @Override
  public IContent visitDouble(DoubleValue num) {
    return num;
  }

  @Override
  public IContent visitString(StringValue str) {
    return str;
  }

  @Override
  public IContent visitBoolean(BooleanValue bool) {
    return bool;
  }

  @Override
  public IContent visitBlank(BlankValue blank) {
    return blank;
  }
}
