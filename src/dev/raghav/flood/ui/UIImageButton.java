package dev.raghav.flood.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.net.URL;

import dev.raghav.flood.Handler;
import dev.raghav.flood.gfx.ImageLoader;
import dev.raghav.flood.states.State;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class UIImageButton extends UIObject {

	private BufferedImage image;
	private ClickListener clicker;
	private Sound sound;
	private double size;
	private boolean played;
	private Handler handler;
	BufferedImage test;
	
	public UIImageButton(double x, double y, int width, int height, BufferedImage image, Handler handler, ClickListener clicker) {
		super(x, y, width, height);
		this.image = image;
		this.clicker = clicker;
		this.handler = handler;
		size = 1.0;
		URL url = this.getClass().getResource("/sound/click.wav");
		sound = TinySound.loadSound(url);
		test = ImageLoader.loadImage("/textures/test.png");
		played = false;
	}

	@Override
	public void tick() {
		if (State.info) {
			hovering = false;
			size = 1.0;
		}
		else {
			if (distance(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), x, y) < width / 2 * size) {
				hovering = true;
				if (!played) {
					sound.play();
					played = true;
				}
				if (size < 1.1)
					size += 0.02;
			}
			else {
				hovering = false;
				played = false;
				if (size > 1.0)
					size -= 0.02;
			}
		}
	}

	@Override
	public void render(Graphics2D g2) {
		RescaleOp rescaleOp = new RescaleOp(1.0f * (float) (1.2 * size), 0, null);
		rescaleOp.filter(image, test);
		g2.translate(x - width * size / 2.0, y - height * size / 2.0);
		g2.drawImage(test, 0, 0, (int) (width * size), (int) (height * size), null);
		g2.translate(-(x - width * size / 2.0), -(y - height * size / 2.0));
	}

	@Override
	public void onClick() {
		clicker.onClick();
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	protected void setTransparency(int infoAlpha) {

	}
}
