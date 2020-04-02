package dev.raghav.flood.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import dev.raghav.flood.Handler;
import dev.raghav.flood.states.MenuState;
import dev.raghav.flood.states.State;

public class UISideButton extends UIObject {

	private ClickListener clicker;
	private Handler handler;
	private Color color;
	private int side;
	private boolean info;
	private int alpha;
	
	public UISideButton(double x, double y, int width, int height, int side, boolean info, Handler handler, ClickListener clicker) {
		super(x, y, width, height);
		this.clicker = clicker;
		this.handler = handler;
		this.side = side;
		this.info = info;
		alpha = 255;
		color = new Color(255, 255, 255, alpha);
	}

	@Override
	public void tick() {
		if (MenuState.info && !info)
			hovering = false;
		else {
			if (distance(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), x, y) < width / 2 + 5) {
				hovering = true;
				color = new Color(255, 255, 0, alpha);
			}
			else {
				hovering = false;
				color = new Color(255, 255, 255, alpha);
			}
			if ((!info && (MenuState.mode + side > 2 || MenuState.mode + side < 0)) || (info && (State.slide + side > 1 || State.slide + side < 0)))
				color = new Color(150, 150, 150);
		}
	}

	@Override
	public void render(Graphics2D g2) {
		g2.setColor(color);
	    g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	    g2.translate(x, y);
	    g2.drawLine(side * -width / 2, -height / 2, 0, 0);
	    g2.drawLine(0, 0, side * -width / 2, height / 2);
	    g2.drawLine(0, height / 2, side * width / 2, 0);
	    g2.drawLine(side * width / 2, 0, 0, -height / 2);
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