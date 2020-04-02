package dev.raghav.flood.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import dev.raghav.flood.Handler;

public class Text {
	public static void drawString(Graphics2D g2, String text, int xPos, int yPos, String align, String alignV, Color c, Font font, Handler handler) {
		g2.setColor(c);
		g2.setFont(font);
		int x = xPos;
		int y = yPos;
		FontMetrics fm = g2.getFontMetrics(font);
		if (align == "c") {
			x = xPos - fm.stringWidth(text) / 2;
			y = yPos - fm.getHeight() / 2 + fm.getAscent();
		}
		if (align == "r") {
			x = handler.getWidth() - fm.stringWidth(text) - 10;
		}
		if (alignV == "u") {
			y = fm.getHeight() - 5;
		}
		g2.drawString(text, x, y); 
	}
}
