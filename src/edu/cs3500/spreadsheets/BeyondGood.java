package edu.cs3500.spreadsheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.WorksheetGraphicsView;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;

/**
 * The main class for our program.
 */
public class BeyondGood {
  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    /*
      TODO: For now, look in the args array to obtain a filename and a cell name,
      - read the file and build a model from it,
      - evaluate all the cells, and
      - report any errors, or print the evaluated value of the requested cell.
    */

    Worksheet worksheet = new BasicWorksheet();

    if (args.length == 4) {
      if (!args[0].equals("-in") || !(args[2].equals("-eval") || args[2].equals("-save"))) {
        System.out.print("Command line is malformed.");

      } else {
        try {
          File toRead = new File(args[1]);
          worksheet = WorksheetReader.read(new BasicWorksheet.BasicWorksheetBuilder(),
                  new FileReader(toRead));
        } catch (FileNotFoundException e) {
          System.out.print("File not found");
        }

        if (args[2].equals("-eval")) {
          String cellName = args[3];
          char[] nameChar = cellName.toCharArray();
          int numIndex = 0;
          for (int i = 0; i < nameChar.length; i++) {
            if (Character.isDigit(nameChar[i])) {
              numIndex = i;
              break;
            }
          }

          try {
            System.out.print(worksheet.getValueAt(
                    Coord.colNameToIndex(cellName.substring(0, numIndex)),
                    Integer.parseInt(cellName.substring(numIndex))));
          } catch (NumberFormatException e) {
            System.out.print("Command line is malformed.");
          } catch (Exception e) {
            System.out.print("Error in cell " + cellName + ": " + e.getMessage());
          }
        } else {
          String newFileName = args[3];
         // WorksheetView textualView = new WorksheetTextualView(worksheet, newFileName);
          //textualView.render();
          WorksheetView gv = new WorksheetGraphicsView(worksheet);
          gv.makeVisible();
        }
      }
    }
  }
}