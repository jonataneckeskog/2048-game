package com.jonataneckeskog.game._2048.game;

import java.util.Random;

public class Cell {

   private int value;

   public Cell(int value) {
      this.value = value;
   }

   public static Cell emptyCell() {
      return new Cell(0);
   }

   public static Cell defaultValueCell() {
      Random random = new Random();
      Cell cell = new Cell((random.nextInt(10) < 9) ? 2 : 4);
      return cell;
   }

   public int getValue() {
      return value;
   }

   public boolean isEmpty() {
      return value == 0;
   }

   public boolean canMerge(Cell otherCell) {
      return value == otherCell.value;
   }

   public Cell merge() {
      return new Cell(value * 2);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      Cell otherCell = (Cell) obj;
      return value == otherCell.value;
   }

   @Override
   public String toString() {
      if (value == 0)
         return "0";
      int power = (int) (Math.log(value) / Math.log(2));
      return String.valueOf(power);
   }

}
