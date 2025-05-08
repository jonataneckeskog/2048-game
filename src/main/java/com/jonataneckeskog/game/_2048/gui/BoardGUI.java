package com.jonataneckeskog.game._2048.gui;

import javax.swing.*;
import java.awt.*;

import com.jonataneckeskog.game._2048.game.BoardPosition;
import com.jonataneckeskog.game._2048.game.Cell;
import com.jonataneckeskog.game._2048.game.Board;

public class BoardGUI extends JPanel {

   private Board board;
   private int sidelength;
   private CellGUI[][] cells;

   public BoardGUI(Board board) {
      this.board = board;
      sidelength = board.getSidelength();
      cells = new CellGUI[sidelength][sidelength];

      setLayout(new GridLayout(sidelength, sidelength));
      initializeCells();

      setBackground(new Color(183, 177, 162));
   }

   private void initializeCells() {
      for (int row = 0; row < sidelength; row++) {
         for (int column = 0; column < sidelength; column++) {
            Cell cell = board.getCell(new BoardPosition(row, column));
            cells[row][column] = new CellGUI(cell.getValue());
            add(cells[row][column]);
         }
      }
   }

   public void update() {
      for (int row = 0; row < board.getSidelength(); row++) {
         for (int column = 0; column < board.getSidelength(); column++) {
            Cell cell = board.getCell(new BoardPosition(row, column));
            cells[row][column].update(cell.getValue());
         }
      }
      repaint();
   }

}
