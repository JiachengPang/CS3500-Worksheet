package edu.cs3500.spreadsheets.view;


import javax.swing.*;

public class WorksheetGraphicsView extends JFrame implements WorksheetView {


  @Override
  public void render() {
    this.repaint();
  }

  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);

  }
}
