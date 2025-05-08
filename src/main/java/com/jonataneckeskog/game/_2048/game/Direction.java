package com.jonataneckeskog.game._2048.game;

public class Direction {

   public static final Direction UP = new Direction(1, 0, "UP");
   public static final Direction DOWN = new Direction(-1, 0, "DOWN");
   public static final Direction LEFT = new Direction(0, 1, "LEFT");
   public static final Direction RIGHT = new Direction(0, -1, "RIGHT");

   private final int rowDelta;
   private final int colDelta;
   private final String name;

   private Direction(int rowDelta, int colDelta, String name) {
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
         case 'W', 'L' -> LEFT;
         case 'E', 'R' -> RIGHT;
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