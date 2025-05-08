package com.jonataneckeskog.game._2048.gui;

import javax.swing.*;
import java.awt.*;

public class CellGUI extends JPanel {

   private static final Color MAX_COLOR = new Color(0xedc22e);

   private int value;

   public CellGUI(int value) {
      this.value = value;
   }

   public void update(int value) {
      if (this.value != value) {
         this.value = value;
         repaint();
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;

      // Cell background
      g2.setColor(getColor());
      g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

      // Draw value
      if (value != 0) {
         g2.setColor(getTextColor());
         g2.setFont(new Font("Arial", Font.BOLD, 24));
         String text = String.valueOf(value);
         FontMetrics fm = g2.getFontMetrics();
         int textWidth = fm.stringWidth(text);
         int textHeight = fm.getAscent();
         int x = (getWidth() - textWidth) / 2;
         int y = (getHeight() + textHeight) / 2 - 4;
         g2.drawString(text, x, y);
      }
   }

   private Color getTextColor() {
      return value <= 4 ? new Color(0x776e65) : Color.WHITE;
   }

   private Color getColor() {
      switch (value) {
         case 0:
            return new Color(0xcdc1b4);
         case 2:
            return new Color(0xeee4da);
         case 4:
            return new Color(0xede0c8);
         case 8:
            return new Color(0xf2b179);
         case 16:
            return new Color(0xf59563);
         case 32:
            return new Color(0xf67c5f);
         case 64:
            return new Color(0xf65e3b);
         case 128:
            return new Color(0xedcf72);
         case 256:
            return new Color(0xedcc61);
         case 512:
            return new Color(0xedc850);
         case 1024:
            return new Color(0xedc53f);
         case 2048:
            return new Color(0xedc22e);
         default: {
            int power = (int) (Math.log(value) / Math.log(2)) - 11;
            Color color = MAX_COLOR;
            for (int i = 0; i < power; i++)
               color = color.darker();
            return color;
         }
      }
   }

}
