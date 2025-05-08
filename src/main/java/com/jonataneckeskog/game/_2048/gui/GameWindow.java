package com.jonataneckeskog.game._2048.gui;

import javax.swing.*;

import com.jonataneckeskog.game._2048.game.Board;

public class GameWindow extends JFrame {

   public GameWindow(int sidelength) {
      Board board = new Board(sidelength);
      BoardGUI boardGUI = new BoardGUI(board);
      GameController controller = new GameController(board, boardGUI);

      add(boardGUI);
      setSize(800, 800);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setVisible(true);

      controller.start();
   }

   // Default board size
   public static void start() {
      start(4);
   }

   public static void start(int sidelength) {
      SwingUtilities.invokeLater(() -> new GameWindow(sidelength));
   }

}
