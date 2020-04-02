package dev.raghav.flood.ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public abstract class UIObject {
	
	protected double x, y;
	protected int width, height;
	protected boolean hovering;
	
	public UIObject(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		hovering = false;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics2D g2);
	
	public abstract void onClick();
	
	public void onMouseMove(MouseEvent e) {
		
	}
	
	public void onMouseRelease(MouseEvent e) {
		if (hovering)
			onClick();
	}
	
	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
	}

	//getters and setters
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isHovering() {
		return hovering;
	}

	public void setHovering(boolean hovering) {
		this.hovering = hovering;
	}

	protected abstract void setTransparency(int infoAlpha);
}
