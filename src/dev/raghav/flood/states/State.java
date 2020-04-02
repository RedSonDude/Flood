package dev.raghav.flood.states;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import dev.raghav.flood.Handler;
import dev.raghav.flood.gfx.ImageLoader;
import dev.raghav.flood.ui.UIManager;

public abstract class State {

	private static State currentState = null;
	public static boolean info;
	private BufferedImage tutorial;
	public static int slide; 
	protected int infoAlpha;
	protected double infoSize;
	private boolean added;
	public static boolean back;
	public static boolean forward;

	public State(Handler handler) {
	  	this.handler = handler;
		slide = 0;
		info = false;
		added = false;
		back = false;
		forward = false;
		infoAlpha = 0;
		infoSize = 4.0 / 3;
	}
	
	public static void setState(State state) {
		currentState = state;
	}
	
	public static State getState() {
		return currentState;
	}

	protected void drawTutorial(Graphics2D g2, UIManager uiManager) {
		if (info) {
			if (!added) {
				tutorial = ImageLoader.loadImage("/textures/tutorial1.png");
				uiManager.addRenderedButtons();
				added = true;
			}
			checkSlide();
			infoAlpha += (int) (255.0 / 10);
			infoSize -= 1.0 / 30;
			if (infoAlpha > 255)
				infoAlpha = 255;
			if (infoSize < 1.0)
				infoSize = 1.0;
			AlphaComposite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) infoAlpha / 255);
			g2.setComposite(alphaComp);
			g2.drawImage(tutorial, (int) (handler.getWidth() / 2 - 180 * infoSize), (int) (handler.getHeight() / 2 - 135 * infoSize), (int) (360 * infoSize), (int) (270 * infoSize), null);
			uiManager.setTransparency(infoAlpha);
			uiManager.renderRenderedButtons(g2);
		}
		else if (added) {
			uiManager.removeRenderedButtons();
			added = false;
			slide = 0;
			back = false;
			forward = false;
			infoAlpha = 0;
			infoSize = 4.0 / 3;
		}
	}
	
	protected String getTaco(byte[] salsa) throws NoSuchAlgorithmException {
		MessageDigest tomatoes = MessageDigest.getInstance("SHA-256");
		byte[] jalopeños = tomatoes.digest(salsa);
		return Base64.getEncoder().encodeToString(jalopeños);
	}
	
	public void checkSlide() {
		if (handler.getKeyManager().left) {
			back = true;
		}
		if (handler.getKeyManager().right) {
			forward = true;
		}
		if (!(handler.getKeyManager().left) && back && slide == 1) {
			slide--;
			back = false;
			forward = false;
			tutorial = ImageLoader.loadImage("/textures/tutorial" + (slide + 1) + ".png");
		}
		if (!(handler.getKeyManager().right) && forward && slide == 0) {
			slide++;
			forward = false;
			back = false;
			tutorial = ImageLoader.loadImage("/textures/tutorial" + (slide + 1) + ".png");
		}
	}

	//class
  
	protected Handler handler;
  
	public abstract void tick();
	public abstract void render(Graphics2D g2);
	public abstract void resetGame();

}
