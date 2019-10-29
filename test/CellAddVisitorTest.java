import org.junit.Test;

import java.util.Arrays;

import edu.cs3500.spreadsheets.model.CellAdd;
import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.CellAppend;
import edu.cs3500.spreadsheets.model.CellFunction;
import edu.cs3500.spreadsheets.model.CellMultiply;
import edu.cs3500.spreadsheets.model.CellReference;
import edu.cs3500.spreadsheets.model.CellSmallerThan;
import edu.cs3500.spreadsheets.model.CellValue;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.StringValue;

import static org.junit.Assert.assertEquals;

public class CellAddVisitorTest {

  CellValue double1 = new DoubleValue(3.0);
  CellValue double2 = new DoubleValue(5.0);
  CellValue double3 = new DoubleValue(10.0);

  CellValue string1 = new StringValue("apple");
  CellValue string2 = new StringValue("banana");
  CellValue string3 = new StringValue("citrus");

  CellValue bool1 = new BooleanValue(true);
  CellValue bool2 = new BooleanValue(false);

  CellReference ref1 = new CellReference(Arrays.asList(double1, double2, double3));

  CellFunction add1 = new CellAdd(Arrays.asList(double1, string1));
  CellFunction add2 = new CellAdd(Arrays.asList(double1, double2, double3, string1,
          string2, add1, bool1));

  CellFunction product1 = new CellMultiply(Arrays.asList(double1, string1, bool1, add1,
          add2, string2));
  CellFunction product2 = new CellMultiply(Arrays.asList(ref1, product1, string3, bool2, double3));
  CellFunction smallerThan1 = new CellSmallerThan(double1, double2);
  CellFunction append1 = new CellAppend(Arrays.asList(string1, string2, string3,
          ref1, bool1, double1, product2));



  @Test
  public void testEval() {
    assertEquals(new DoubleValue(21.0), add2.evaluate());
    assertEquals(new DoubleValue(189.0), product1.evaluate());
    assertEquals(new DoubleValue(283500.0), product2.evaluate());
    assertEquals(bool1, smallerThan1.evaluate());
    assertEquals(new StringValue("applebananacitrus3.05.010.0true3.0283500.0"), append1.evaluate());
  }

}
