package com.jonataneckeskog.game._2048.game;

import java.util.Iterator;
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

   public void update(char directionChar) {
      boolean moved = move(directionChar);
      if (moved)
         fillRandomEmptyCell();
   }

   public boolean move(char directionChar) {
      Direction direction = Direction.fromChar(directionChar);
      if (direction == null) {
         throw new IllegalArgumentException("Invalid direction: " + directionChar);
      }

      boolean moved = false;

      for (int index = 0; index < sidelength; index++) {
         boolean lineMoved = moveLine(index, direction);
         moved = moved || lineMoved;
      }

      return moved;
   }

   private boolean moveLine(int index, Direction direction) {
      List<BoardPosition> line = getLinePositions(index, direction);
      List<Cell> cells = new ArrayList<>();

      // Step 1: Collect non-empty cells
      for (BoardPosition pos : line) {
         Cell cell = getCell(pos);
         if (!cell.isEmpty()) {
            cells.add(cell);
         }
      }

      // Step 2: Merge adjacent same-value cells
      List<Cell> newLine = new ArrayList<>();
      int i = 0;
      while (i < cells.size()) {
         if (i + 1 < cells.size() && cells.get(i).canMerge(cells.get(i + 1))) {
            Cell merged = cells.get(i).merge();
            newLine.add(merged);
            i += 2; // skip next cell (merged)
         } else {
            newLine.add(cells.get(i));
            i++;
         }
      }

      // Step 3: Fill the rest with empty cells
      while (newLine.size() < sidelength) {
         newLine.add(Cell.emptyCell());
      }

      // Step 4: Write back and detect if anything changed
      boolean changed = false;
      for (int j = 0; j < sidelength; j++) {
         BoardPosition pos = line.get(j);
         if (!getCell(pos).equals(newLine.get(j))) {
            setCell(newLine.get(j), pos);
            changed = true;
         }
      }

      return changed;
   }

   private List<BoardPosition> getLinePositions(int index, Direction direction) {
      List<BoardPosition> positions = new ArrayList<>();

      for (int i = 0; i < sidelength; i++) {
         int row = direction.isVertical() ? i : index;
         int col = direction.isHorizontal() ? i : index;

         if (direction == Direction.UP || direction == Direction.RIGHT) {
            row = direction.isVertical() ? sidelength - 1 - i : index;
            col = direction.isHorizontal() ? sidelength - 1 - i : index;
         }

         positions.add(new BoardPosition(row, col));
      }

      return positions;
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
         throw new IllegalStateException("No empty cells available to fill");

      Random random = new Random();
      int index = random.nextInt(emptyCells.size());
      Iterator<BoardPosition> iterator = emptyCells.iterator();
      for (int i = 0; i < index; i++) {
         iterator.next();
      }
      BoardPosition randomPosition = iterator.next();
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