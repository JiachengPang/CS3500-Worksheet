package edu.cs3500.spreadsheets.view;

import java.awt.*;

import javax.swing.*;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;

public class GridPanel extends JPanel {
  private Worksheet model;
  private int numCol;
  private int numRow;
  private int cellWidth;
  private int cellHeight;


  public GridPanel() {
    super();
    this.setBackground(new Color(240, 240, 240));
    this.numCol = 10;
    this.numRow = 30;
    this.cellWidth = 85;
    this.cellHeight = 25;
    this.model = new BasicWorksheet();
  }

  public void setModel(Worksheet model) {
    this.model = model;
    numCol = Math.max(numCol, model.getNumCols());
    numRow = Math.max(numRow, model.getNumRows());
  }


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.black);
    for (int i = 0; i <= numCol * cellWidth; i += cellWidth) {
      g2d.drawLine(i, 0, i, numRow * cellHeight);
    }
    for (int i = 0; i <= numRow * cellHeight; i += cellHeight) {
      g2d.drawLine(0, i, cellWidth * numCol, i);
    }

    g2d.setColor(new Color(142, 142, 109));
    for (int i = 0; i < numCol * cellWidth; i += cellWidth) {
      g2d.fillRect(i + 1, 1, cellWidth - 1, cellHeight - 1);
    }
    for (int i = 0; i < numRow * cellHeight; i += cellHeight) {
      g2d.fillRect(1, i + 1, cellWidth - 1, cellHeight - 1);
    }

    g2d.setColor(Color.black);
    for (int i = 0; i < numCol; i++) {
      for (int j = 0; j < numRow; j++) {
        int indentW1 = (cellWidth - g2d.getFontMetrics().stringWidth(Integer.toString(j))) / 2;
        int indentH1 = (cellHeight - (int)
                (g2d.getFontMetrics().getStringBounds(Coord.colIndexToName(i), g2d).getHeight() / 2));
        if (i == 0 && j != 0) {
          g2d.drawString(Integer.toString(j), indentW1, indentH1 + cellHeight * j);
        } else if (i != 0 && j == 0) {
          g2d.drawString(Coord.colIndexToName(i),
                  indentW1 + i * cellWidth, indentH1);
        }
        String str = "";
        try {
          str = model.getValueAt(i + 1, j + 1).toString();
        } catch (IllegalStateException e) {
          if (e.getMessage().equals("The cell is blank.")) {
            // do nothing
          }
        } catch (Exception e) {
          str = "ERROR!!!";
        }
        String cutStr = this.cutString(g2d, str);
        int indentW2 = (cellWidth - g2d.getFontMetrics().stringWidth(cutStr)) / 2;
        int indentH2 = (cellHeight - (int) (g2d.getFontMetrics().getStringBounds(cutStr, g2d).getHeight() / 2));
        g2d.drawString(cutStr, indentW2 + (i + 1) * cellWidth, indentH2 + cellHeight * (j + 1));
      }
    }
  }

  private String cutString(Graphics2D g2d, String str) {
    if (g2d.getFontMetrics().stringWidth(str) <= cellWidth - 2) {
      return str;
    }

    String result = "";
    for (int i = 0; i < str.length(); i++) {
      if (g2d.getFontMetrics().stringWidth(result + str.charAt(i)) < cellWidth - 2) {
        result += str.charAt(i);
      } else {
        break;
      }
    }
    return result;
  }


}
