package com.jonataneckeskog.game._2048.gui;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;

import com.jonataneckeskog.game._2048.game.Board;

public class GameController {

   private Board board;
   private BoardGUI boardGUI;

   public GameController(Board board, BoardGUI boardGUI) {
      this.board = board;
      this.boardGUI = boardGUI;
   }

   // Starts listening for key input
   public void start() {
      boardGUI.setFocusable(true);
      setupKeyBindings();
      boardGUI.update();
   }

   private void setupKeyBindings() {
      InputMap im = boardGUI.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
      ActionMap am = boardGUI.getActionMap();

      im.put(KeyStroke.getKeyStroke("UP"), "moveUp");
      im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
      im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
      im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");

      am.put("moveUp", new MoveAction('U'));
      am.put("moveDown", new MoveAction('D'));
      am.put("moveLeft", new MoveAction('L'));
      am.put("moveRight", new MoveAction('R'));
   }

   private class MoveAction extends AbstractAction {
      private char directionChar;

      public MoveAction(char directionChar) {
         this.directionChar = directionChar;
      }

      // Handles movement actions
      @Override
      public void actionPerformed(ActionEvent e) {
         boolean isGameOver = !board.update(directionChar);
         boardGUI.update();
         if (isGameOver) {
            boardGUI.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).clear();
            boardGUI.getActionMap().clear();
            JOptionPane.showMessageDialog(boardGUI, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
         }
      }
   }

}
