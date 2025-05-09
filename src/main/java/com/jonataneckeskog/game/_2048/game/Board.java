package com.jonataneckeskog.game._2048.game;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Consumer;

public class Board {

   private Cell[][] board;
   private HashSet<BoardPosition> emptyCells;
   private int sidelength;

   public Board() {
      this(4);
   }

   public Board(int sidelength) {
      this.sidelength = sidelength;
      board = new Cell[sidelength][sidelength];
      emptyCells = new HashSet<>();
      initializeBoard();
      fillRandomEmptyCell();
   }

   private void initializeBoard() {
      for (int row = 0; row < sidelength; row++) {
         for (int column = 0; column < sidelength; column++) {
            board[row][column] = Cell.emptyCell();
            emptyCells.add(new BoardPosition(row, column));
         }
      }
   }

   public boolean update(char directionChar) {
      boolean moved = move(directionChar);
      if (moved)
         fillRandomEmptyCell();
      return !isGameOver();
   }

   public boolean isGameOver() {
      if (!emptyCells.isEmpty())
         return false;

      for (int row = 0; row < sidelength; row++) {
         for (int column = 0; column < sidelength; column++) {
            BoardPosition position = new BoardPosition(row, column);
            Cell cell = getCell(position);

            for (Direction direction : Direction.values()) {
               BoardPosition newPosition = position.getAssociatedPosition(direction);
               if (!isPositionInBounds(newPosition))
                  continue;
               if (cell.canMerge(getCell(newPosition)))
                  return false;
            }
         }
      }

      return true;
   }

   public boolean move(char c) {
      Direction dir = Direction.fromChar(c);
      if (dir == null) {
         throw new IllegalArgumentException("Invalid direction: " + c);
      }
      return slideAndMerge(dir);
   }

   private boolean slideAndMerge(Direction dir) {
      boolean moved = false;
      boolean horizontal = dir.isHorizontal();
      boolean forward = (dir == Direction.DOWN || dir == Direction.RIGHT);

      for (int line = 0; line < sidelength; line++) {
         List<Cell> values = new ArrayList<>();

         // Extract non-empty cells in swipe order
         for (int i = 0; i < sidelength; i++) {
            int row = horizontal ? line : (forward ? sidelength - 1 - i : i);
            int column = horizontal ? (forward ? sidelength - 1 - i : i) : line;
            Cell cell = board[row][column];
            if (!cell.isEmpty())
               values.add(cell);
         }

         // Merge adjacent equal cells
         for (int i = 0; i < values.size() - 1; i++) {
            Cell current = values.get(i);
            Cell next = values.get(i + 1);
            if (current.canMerge(next)) {
               values.set(i, current.merge());
               values.remove(i + 1);
               moved = true;
            }
         }

         // Rebuild line back into board using setCell
         for (int i = 0; i < sidelength; i++) {
            int row = horizontal ? line : (forward ? sidelength - 1 - i : i);
            int column = horizontal ? (forward ? sidelength - 1 - i : i) : line;
            BoardPosition position = new BoardPosition(row, column);
            Cell newCell = (i < values.size()) ? values.get(i) : Cell.emptyCell();

            if (!getCell(position).equals(newCell)) {
               setCell(newCell, position);
               moved = true;
            }
         }
      }

      return moved;
   }

   public void setCell(Cell cell, BoardPosition position) {
      if (!cell.isEmpty())
         emptyCells.remove(position);
      else
         emptyCells.add(position);
      board[position.row][position.column] = cell;
   }

   public Cell getCell(BoardPosition position) {
      if (!isPositionInBounds(position))
         throw new IndexOutOfBoundsException(
               "Position" + position.toString() + "is out of bounds");
      return board[position.row][position.column];
   }

   public int getSidelength() {
      return sidelength;
   }

   public boolean isPositionInBounds(BoardPosition position) {
      int row = position.row;
      int column = position.column;
      return (row < sidelength &&
            row >= 0 &&
            column < sidelength &&
            column >= 0);
   }

   private void fillRandomEmptyCell() {
      if (emptyCells.isEmpty())
         return;

      List<BoardPosition> list = new ArrayList<>(emptyCells);
      BoardPosition randomPosition = list.get(new Random().nextInt(list.size()));
      setCell(Cell.defaultValueCell(), randomPosition);
   }

   public void forEachCell(Consumer<BoardPosition> action) {
      for (int row = 0; row < sidelength; row++) {
         for (int column = 0; column < sidelength; column++) {
            action.accept(new BoardPosition(row, column));
         }
      }
   }

   public static Board buildFromString(String boardString) {
      String[] powersList = boardString.split(",");
      int length = powersList.length;
      double sidelengthDouble = Math.sqrt(length);
      int sidelength = 0;

      if (sidelengthDouble != (int) sidelengthDouble)
         throw new IllegalArgumentException(
               "The length of string should be a square number, with entires separated by ','");
      else
         sidelength = (int) sidelengthDouble;

      Board board = new Board(sidelength);

      for (int row = 0; row < sidelength; row++) {
         for (int column = 0; column < board.sidelength; column++) {
            int index = row * sidelength + column;
            String powerString = powersList[index];

            int power = 0;
            try {
               power = Integer.parseInt(powerString);
               if (power < 0 || power > 30)
                  throw new NumberFormatException();
            } catch (NumberFormatException e) {
               throw new IllegalArgumentException(
                     "String contained an invalid number (must be a whole number 0-30): " + powerString);
            }

            BoardPosition position = new BoardPosition(row, column);
            if (power == 0)
               board.setCell(Cell.emptyCell(), position);
            else
               board.setCell(new Cell((int) Math.pow(2, power)), position);
         }
      }
      return board;
   }

   public int getNumberOfEmptyCells() {
      return emptyCells.size();
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;

      Board otherBoard = (Board) obj;
      for (int row = 0; row < sidelength; row++) {
         for (int column = 0; column < sidelength; column++) {
            BoardPosition position = new BoardPosition(row, column);
            if (!getCell(position).equals(otherBoard.getCell(position)))
               return false;
         }
      }

      return true;
   }

   @Override
   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("\n");
      for (int row = 0; row < sidelength; row++) {
         for (int column = 0; column < sidelength; column++) {
            BoardPosition position = new BoardPosition(row, column);
            String string = getCell(position).toString();
            int stringLength = string.length();

            stringBuilder.append(getCell(position));
            for (int i = 0; i < 4 - stringLength; i++) {
               stringBuilder.append(" ");
            }
         }
         stringBuilder.append("\n");
      }
      return stringBuilder.toString();
   }

}