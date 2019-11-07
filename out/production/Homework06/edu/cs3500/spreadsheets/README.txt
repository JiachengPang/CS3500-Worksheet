Changes made to our model:
1. Reworked our class structure for our cells and separated a cell and a cell content. We made
a cell to have an IContent, which can be a CellFunction, CellReference, or an IValue. An IValue
is one of a StringValue, DoubleValue, BooleanValue, or BlankValue. The cell now also has a position
field that keeps track of its coord on the worksheet.

2. We fixed our previous problem with not being able to detect cyclic reference and prevented
the worksheet from evaluating them (but allows them to be created). Now a cell has a list of
listeners that are also cells, which allowed us to check if a cell is referring to itself with
its coord.

3. We added a ToSexpVisitor that converts an IContent to a Sexp so we can use the toString
method to create a textual representation of our view.