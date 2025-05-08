package com.jonataneckeskog.game._2048.game;

public enum Direction {

   UP(1, 0, "UP"),
   DOWN(-1, 0, "DOWN"),
   RIGHT(0, 1, "RIGHT"),
   LEFT(0, -1, "LEFT");

   private final int rowDelta;
   private final int colDelta;
   private final String name;

   Direction(int rowDelta, int colDelta, String name) {
      this.rowDelta = rowDelta;
      this.colDelta = colDelta;
      this.name = name;
   }

   public int getRowDelta() {
      return rowDelta;
   }

   public int getColumnDelta() {
      return colDelta;
   }

   public static Direction fromChar(char c) {
      return switch (Character.toUpperCase(c)) {
         case 'N', 'U' -> UP;
         case 'S', 'D' -> DOWN;
         case 'E', 'R' -> RIGHT;
         case 'W', 'L' -> LEFT;
         default -> null;
      };
   }

   public boolean isVertical() {
      return this == UP || this == DOWN;
   }

   public boolean isHorizontal() {
      return this == RIGHT || this == LEFT;
   }

   @Override
   public String toString() {
      return name;
   }
}