package dev.raghav.flood.ui;

import java.awt.Color;
import java.awt.Graphics2D;

public interface ButtonActions {

	public void render(Graphics2D g2, double x, double y, int width, int height, Color color);
	
	public void tick();
}
