package dev.raghav.flood.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import dev.raghav.flood.Handler;

public class UICloseButton extends UIObject {

	private ClickListener clicker;
	private Handler handler;
	private Color color;
	private int alpha;
	
	public UICloseButton(double x, double y, int width, int height, Handler handler, ClickListener clicker) {
		super(x, y, width, height);
		this.clicker = clicker;
		this.handler = handler;
		alpha = 255;
		color = new Color(255, 255, 255, alpha);
	}

	@Override
	public void tick() {
		if (distance(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), x, y) < width / 2 + 5) {
			hovering = true;
			color = new Color(255, 255, 0, alpha);
		}
		else {
			hovering = false;
			color = new Color(255, 255, 255, alpha);
		}
	}

	@Override
	public void render(Graphics2D g2) {
		g2.setColor(color);
	    g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	    g2.translate(x, y);
	    g2.drawLine(-width / 2, -height / 2, width / 2, height / 2);
	    g2.drawLine(-width / 2, height / 2, width / 2, -height / 2);
	    g2.translate(-x, -y);
	}

	@Override
	public void onClick() {
		clicker.onClick();
	}

	@Override
	protected void setTransparency(int infoAlpha) {
		alpha = infoAlpha;
	}
}