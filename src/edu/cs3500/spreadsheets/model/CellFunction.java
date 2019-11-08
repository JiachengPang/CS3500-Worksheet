package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an IContent that applies a function to one or more IContents as its inputs.
 */
public class CellFunction implements IContent {

  public final String name;
  private final ContentVisitor visitor;
  private List<IContent> args;

  /**
   * Constructs a cell function.
   *
   * @param name    the name of the function
   * @param visitor the function this cell executes
   * @param args    a list of input IContents
   * @throws IllegalArgumentException if the given list is null or any entry in the list is null
   */
  public CellFunction(String name, ContentVisitor visitor, List<IContent> args) {
    super();
    if (args == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    for (IContent arg : args) {
      if (arg == null) {
        throw new IllegalArgumentException("Inputs cannot be null.");
      }
    }
    this.name = name;
    this.args = args;
    this.visitor = visitor;
  }

  @Override
  public <S> S accept(ContentVisitor<S> visitor) {
    return visitor.visitFunction(this);
  }

  @Override
  public IValue getValue() {
    return (IValue) this.visitor.visitFunction(this);
  }

  /**
   * Gets the list of arguments.
   *
   * @return the list of arguments.
   */
  public List<IContent> getArgs() {
    return new ArrayList<>(args);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CellFunction that = (CellFunction) o;
    if (this.args.size() != that.args.size()) {
      return false;
    }
    for (IContent arg : this.args) {
      if (!that.args.contains(arg)) {
        return false;
      }
    }
    for (IContent arg : that.args) {
      if (!this.args.contains(arg)) {
        return false;
      }
    }
    return Objects.equals(visitor, that.visitor)
            && this.name.equals(that.name);
  }


}
