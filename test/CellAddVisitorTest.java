import org.junit.Test;

import java.util.Arrays;

import edu.cs3500.spreadsheets.model.AddVisitor;
import edu.cs3500.spreadsheets.model.AppendVisitor;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.CellFunction;
import edu.cs3500.spreadsheets.model.CellReference;
import edu.cs3500.spreadsheets.model.CellValue;
import edu.cs3500.spreadsheets.model.CellVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.SmallerThanVisitor;
import edu.cs3500.spreadsheets.model.ProductVisitor;
import edu.cs3500.spreadsheets.model.StringValue;
import edu.cs3500.spreadsheets.sexp.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CellAddVisitorTest {


  CellValue double1, double2, double3;
  CellValue string1, string2, string3;
  CellValue bool1, bool2;
  CellVisitor addVisitor, productVisitor, smallerThanVisitor, appendVisitor;
  CellReference ref1;
  CellFunction add1, add2;
  CellFunction product1, product2, smallerThan1, smallerThan2, append1, badSmallerThan, badSmallerThan2;
  BasicWorksheet worksheet;


  private void reset() {
    double1 = new DoubleValue(3.0);
    double2 = new DoubleValue(5.0);
    double3 = new DoubleValue(10.0);

    string1 = new StringValue("apple");
    string2 = new StringValue("banana");
    string3 = new StringValue("citrus");

    bool1 = new BooleanValue(true);
    bool2 = new BooleanValue(false);

    addVisitor = new AddVisitor();
    productVisitor = new ProductVisitor();
    smallerThanVisitor = new SmallerThanVisitor();
    appendVisitor = new AppendVisitor();

    worksheet = new BasicWorksheet.BasicWorksheetBuilder()
            .createCell(1, 1, "=1")
            .createCell(1, 2, "\"foo\"")
            .createCell(2, 3, "=A1:A2")
            .createCell(3, 3, "=(SUM A1 A2)")
            .createCell(4, 4, "=(SUM A1:A2)")
            .createCell(5, 5, "=(SUM B3)")
            .createCell(5, 4, "=A1").createWorksheet();

    ref1 = new CellReference(new Coord(1,1), new Coord(1,2), worksheet.grid);

    add1 = new CellFunction(addVisitor, Arrays.asList(double1, string1));
    add2 = new CellFunction(addVisitor, Arrays.asList(double1, double2, double3, string1,
            string2, add1, bool1));

    product1 = new CellFunction(productVisitor, Arrays.asList(double1, string1, bool1, add1,
            add2, string2));
    product2 = new CellFunction(productVisitor, Arrays.asList(ref1, product1, string3, bool2, double3));
    smallerThan1 = new CellFunction(smallerThanVisitor, Arrays.asList(double1, double2));
    smallerThan2 = new CellFunction(smallerThanVisitor, Arrays.asList(product1, add1));
    append1 = new CellFunction(appendVisitor, Arrays.asList(string1, string2, string3,
            ref1, bool1, double1, product2));
    badSmallerThan = new CellFunction(smallerThanVisitor, Arrays.asList(double1));
    badSmallerThan2 = new CellFunction(smallerThanVisitor, Arrays.asList(double1, string1, double3));

  }

  // need to give default value instead of passing in null when referencing a
  // to a new cell that is previously blank

  // need to check if cell can reference itself -> not allowed
  @Test
  public void testEval() {
    reset();
    assertEquals(new DoubleValue(21.0), add2.evaluate());
    assertEquals(new DoubleValue(189.0), product1.evaluate());
    assertEquals(new DoubleValue(1890.0), product2.evaluate());
    assertEquals(bool1, smallerThan1.evaluate());
    assertEquals(new StringValue("applebananacitrus1.000000true3.0000001890.000000")
            , append1.evaluate());

    assertEquals(new DoubleValue(1.0), worksheet.getCellAt(new Coord(1, 1)).evaluate());
    assertEquals(new StringValue("foo"), worksheet.getCellAt(new Coord(1, 2)).evaluate());
    assertEquals(new DoubleValue(1.0),
            worksheet.getCellAt(new Coord(2, 3)));
    assertEquals(1.0, (Double) worksheet.getCellAt(new Coord(4, 4))
            .evaluate().getValue(), 0.0001);
    assertEquals(bool2, smallerThan2.evaluate());
    try {
      badSmallerThan.evaluate();
    } catch (IllegalArgumentException e) {
      assertEquals("Smaller-than has to take in exactly 2 numeric inputs.", e.getMessage());
    }
    try {
      badSmallerThan2.evaluate();
    } catch (IllegalArgumentException e) {
      assertEquals("Smaller-than has to take in exactly 2 numeric inputs.", e.getMessage());
    }

    worksheet.set(1, 1, new DoubleValue(2.0));
    assertEquals(2.0, (Double) worksheet.getCellAt(new Coord(1, 1)).getValue(), 0.0001);
    assertEquals(2.0, (Double) worksheet.getCellAt(new Coord(3, 3)).getValue(), 0.0001);

    Parser parse = new Parser();

    assertEquals("hi", parse.parse("(SUN 5 A1)"));
  }

}
