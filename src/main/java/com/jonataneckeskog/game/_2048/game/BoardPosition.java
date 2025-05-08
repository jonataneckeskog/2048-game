package com.jonataneckeskog.game._2048.game;

public class BoardPosition {

   public int row;
   public int column;

   public BoardPosition(int row, int column) {
      this.row = row;
      this.column = column;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      BoardPosition that = (BoardPosition) obj;
      return row == that.row && column == that.column;
   }

   @Override
   public int hashCode() {
      return 31 * row + column;
   }

   @Override
   public String toString() {
      return "(" + row + ", " + column + ")";
   }

}
