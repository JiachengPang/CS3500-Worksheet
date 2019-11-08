import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.IValue;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;

import static org.junit.Assert.*;

public class WorksheetViewTest {

  private Worksheet w1;
  private Worksheet smallWorksheet;
  private WorksheetView textualView_small;
  private WorksheetView textualView_w1;

  /**
   * Set all fields to default.
   */
  private void reset() {
    File file1 = new File("test/file1.txt");
    File smallFile = new File("test/smallFile.txt");
    w1 = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    smallWorksheet = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();


    try {
      w1 = WorksheetReader.read(new BasicWorksheet.BasicWorksheetBuilder(),
              new FileReader(file1));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    try {
      smallWorksheet = WorksheetReader.read(new BasicWorksheet.BasicWorksheetBuilder(),
              new FileReader(smallFile));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    textualView_small = new WorksheetTextualView(smallWorksheet, "test/newSmallFile.txt");
    textualView_w1 = new WorksheetTextualView(w1, "test/newFile1.txt");
  }

  @Test
  public void tester() {
    reset();
    System.out.println(w1.getAllCells().size());
    System.out.println(smallWorksheet.getAllCells().size());
  }


  @Test
  public void testRender() {
    reset();
    textualView_small.render();
    File newSmallFile = new File("test/newSmallFile.txt");
    /*
    Worksheet newSmallWorksheet = null;
    try {
      newSmallWorksheet = WorksheetReader.read(new BasicWorksheet.BasicWorksheetBuilder(),
              new FileReader(newSmallFile));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    assertTrue(smallWorksheet.getNumCols() == newSmallWorksheet.getNumCols());
    assertTrue(smallWorksheet.getNumRows() == newSmallWorksheet.getNumRows());
    for (int i = 1; i <= smallWorksheet.getNumCols(); i++) {
      for (int j = 1; j <= smallWorksheet.getNumRows(); j++) {
        IValue value1 = null;
        IValue value2 = null;
        try {
          value1 = smallWorksheet.getValueAt(i, j);
        } catch (IllegalStateException e1) {
          try {
            value2 = newSmallWorksheet.getValueAt(i, j);
          } catch (IllegalStateException e2) {
            assertEquals(e1.getMessage(), e2.getMessage());
          }
        }
        assertEquals(value1, value2);
      }


    }
     */
    /*
    for (int i = 0; i < newSmallWorksheet.getAllCells().size(); i++) {
      System.out.println(newSmallWorksheet.getAllCells().get(i) + "   " + smallWorksheet.getAllCells().get(i));
    }

     */

    //assertTrue(smallWorksheet.equals(newSmallWorksheet));
  }

  @Test
  public void testRender2() {
    reset();
    textualView_w1.render();
  }


}