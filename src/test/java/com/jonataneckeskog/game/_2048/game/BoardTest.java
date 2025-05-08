package com.jonataneckeskog.game._2048.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

   @Test
   void testInitialBoard() {
      Board board = new Board();

      int[] filledCount = { 0 };

      board.forEachCell((position) -> {
         Cell cell = board.getCell(position);
         if (!cell.isEmpty()) {
            filledCount[0]++;
         }
      });

      assertTrue(filledCount[0] == 1, "Board should only contain one filled cell after initialization");
   }

   @Test
   void testBuildFromString() {
      // Valid test cases
      String stringData1 = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
      String stringData2 = "2,2,0,0,0,0,0,0,3,0,0,0,0,1,0,0";
      String stringData3 = "20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
      String stringData4 = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";

      Board board1 = Board.buildFromString(stringData1);
      Board board2 = Board.buildFromString(stringData2);

      assertEquals(Cell.emptyCell(), board1.getCell(new BoardPosition(3, 2)));
      assertEquals(new Cell(4), board2.getCell(new BoardPosition(0, 0)));
      assertDoesNotThrow(() -> Board.buildFromString(stringData3));
      assertDoesNotThrow(() -> Board.buildFromString(stringData4));

      // Invalid test cases
      String stringData5 = "0,0,i,0,0,0,0,0,0,0,0,0,0,0,0,0";
      String stringData6 = "48,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
      String stringData7 = "0,0,0";

      assertThrows(IllegalArgumentException.class, () -> Board.buildFromString(stringData5),
            "Should throw for invalid characters.");
      assertThrows(IllegalArgumentException.class, () -> Board.buildFromString(stringData6),
            "Should throw for characters larger than 30");
      assertThrows(IllegalArgumentException.class, () -> Board.buildFromString(stringData7),
            "Should throw for invalid string length");
   }

   @Test
   void testMoveDirections() {
      String stringData = "2,2,0,0,0,0,0,0,3,0,0,0,0,1,0,0";
      Board board1 = Board.buildFromString(stringData);
      Board board2 = Board.buildFromString(stringData);
      Board board3 = Board.buildFromString(stringData);
      Board board4 = Board.buildFromString(stringData);

      String upString = "0,0,0,0,0,0,0,0,2,2,0,0,3,1,0,0";
      String downString = "2,2,0,0,3,1,0,0,0,0,0,0,0,0,0,0";
      String rightString = "0,0,0,3,0,0,0,0,0,0,0,3,0,0,0,1";
      String leftString = "3,0,0,0,0,0,0,0,3,0,0,0,1,0,0,0,";

      Board expectedBoard1 = Board.buildFromString(upString);
      Board expectedBoard2 = Board.buildFromString(downString);
      Board expectedBoard3 = Board.buildFromString(rightString);
      Board expectedBoard4 = Board.buildFromString(leftString);

      // Test illegal direction
      assertThrows(IllegalArgumentException.class, () -> board1.move('X'));

      // Move up
      board1.move('U');
      assertEquals(board1, expectedBoard1);

      // Move down
      board2.move('S');
      assertEquals(board2, expectedBoard2);

      // Move right
      board3.move('R');
      assertEquals(board3, expectedBoard3);

      // Move left
      board4.move('L');
      assertEquals(board4, expectedBoard4);
   }

   @Test
   void testMoveSpecial() {
      String largeBoard = "4,0,0,4,2,4,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0";
      String fullBoard = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16";
      String largeMerge = "2,2,3,1,4,4,4,4,8,8,9,3,1,1,0,1";

      Board board1 = Board.buildFromString(largeBoard);
      Board board2 = Board.buildFromString(fullBoard);
      Board board3 = Board.buildFromString(largeMerge);

      String expectedString1 = "5,2,0,0,0,4,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
      String expectedString2 = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16";

      Board expectedBoard1 = Board.buildFromString(expectedString1);
      Board expectedBoard2 = Board.buildFromString(expectedString2);

      board1.move('W');
      assertEquals(board1, expectedBoard1);

      board2.move('N');
      assertEquals(board2, expectedBoard2);

      board3.move('W');
      assertEquals(board3, board3);
   }

   @Test
   void testUpdate() {
      String stringData = "1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
      Board board = Board.buildFromString(stringData);
      board.update('W');

      int[] filledCount = { 0 };

      board.forEachCell((position) -> {
         Cell cell = board.getCell(position);
         if (!cell.isEmpty()) {
            filledCount[0]++;
         }
      });

      assertTrue(filledCount[0] == 2, "Board should contain two digits after updating");
   }
}