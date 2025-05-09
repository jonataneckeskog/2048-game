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
   void testEquals() {
      String stringData = "2,2,0,0,0,0,0,0,3,0,0,0,0,1,0,0";
      Board board1 = Board.buildFromString(stringData);
      Board board2 = Board.buildFromString(stringData);

      assertEquals(board1, board2);
   }

   @Test
   void testMoveDirections() {
      String stringData = "2,2,0,0,0,0,0,0,3,0,0,0,0,1,0,0";
      Board board1 = Board.buildFromString(stringData);
      Board board2 = Board.buildFromString(stringData);
      Board board3 = Board.buildFromString(stringData);
      Board board4 = Board.buildFromString(stringData);

      String upString = "2,2,0,0,3,1,0,0,0,0,0,0,0,0,0,0";
      String downString = "0,0,0,0,0,0,0,0,2,2,0,0,3,1,0,0";
      String rightString = "0,0,0,3,0,0,0,0,0,0,0,3,0,0,0,1";
      String leftString = "3,0,0,0,0,0,0,0,3,0,0,0,1,0,0,0,";

      Board expectedBoard1 = Board.buildFromString(upString);
      Board expectedBoard2 = Board.buildFromString(downString);
      Board expectedBoard3 = Board.buildFromString(rightString);
      Board expectedBoard4 = Board.buildFromString(leftString);

      // Test illegal direction
      assertThrows(IllegalArgumentException.class, () -> board1.move('X'));

      // Move up
      boolean moved1 = board1.move('U');
      assertTrue(moved1, "Move should return true since the board state changed");
      assertEquals(expectedBoard1, board1);

      // Move down
      boolean moved2 = board2.move('D');
      assertTrue(moved2, "Move should return true since the board state changed");
      assertEquals(expectedBoard2, board2);

      // Move right
      boolean moved3 = board3.move('R');
      assertTrue(moved3, "Move should return true since the board state changed");
      assertEquals(expectedBoard3, board3);

      // Move left
      boolean moved4 = board4.move('L');
      assertTrue(moved4, "Move should return true since the board state changed");
      assertEquals(expectedBoard4, board4);
   }

   @Test
   void testMoveSpecial() {
      String largeBoard = "4,0,0,4,2,4,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0";
      String largeMerge = "2,2,3,1,4,4,4,4,8,8,9,3,1,1,0,1";
      String fullBoard = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16";
      String invalidMove = "0,2,3,0,0,0,0,0,0,0,0,0,0,0,0,0";

      Board board1 = Board.buildFromString(largeBoard);
      Board board2 = Board.buildFromString(largeMerge);
      Board board3 = Board.buildFromString(fullBoard);
      Board board4 = Board.buildFromString(invalidMove);

      String expectedString1 = "5,2,0,0,0,4,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
      String expectedString2 = "3,3,1,0,5,5,0,0,9,9,3,0,2,1,0,0";

      Board expectedBoard1 = Board.buildFromString(expectedString1);
      Board expectedBoard2 = Board.buildFromString(expectedString2);

      board1.move('W');
      assertEquals(expectedBoard1, board1);

      board2.move('W');
      assertEquals(expectedBoard2, board2);

      boolean moved1 = board3.move('N');
      assertFalse(moved1, "Move should return false when the board state is unchanged");
      assertEquals(board3, board3);

      boolean moved2 = board4.move('N');
      assertFalse(moved2, "Move should return false when the board state is unchanged");
   }

   @Test
   void testUpdate() {
      String stringData1 = "1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
      String stringData2 = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16";

      Board board1 = Board.buildFromString(stringData1);
      boolean updated1 = board1.update('W');

      int[] filledCount = { 0 };

      board1.forEachCell((position) -> {
         Cell cell = board1.getCell(position);
         if (!cell.isEmpty()) {
            filledCount[0]++;
         }
      });

      assertTrue(updated1, "Update should return true for a position that isn't over");
      assertTrue(filledCount[0] == 2, "Board should contain two digits after updating");

      Board board2 = Board.buildFromString(stringData2);
      boolean updated2 = board2.update('S');

      assertFalse(updated2, "Should return false because the board is full with no legal moves");
   }
}