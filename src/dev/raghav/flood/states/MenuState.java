package dev.raghav.flood.states;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import dev.raghav.flood.Handler;
import dev.raghav.flood.entities.Player;
import dev.raghav.flood.gfx.FontLoader;
import dev.raghav.flood.gfx.ImageLoader;
import dev.raghav.flood.gfx.Text;
import dev.raghav.flood.ui.ClickListener;
import dev.raghav.flood.ui.UIManager;
import dev.raghav.flood.ui.UISideButton;


public class MenuState extends State {
	
	private UIManager uiManager;
	private BufferedImage flood;
	private BufferedImage modeImage;
	public static int mode = 0;
	public static int highscore;
	private boolean back;
	private boolean forward;
	private static Font font;
	
	public MenuState(Handler handler) {
		super(handler);
		flood = ImageLoader.loadImage("/textures/flood.png");
		modeImage = ImageLoader.loadImage("/textures/mode" + (mode + 1) + ".png");
		highscore = getHighscore();
		font = FontLoader.loadFont("fonts/NotoSans-Medium.ttf", 20);
		back = false;
		forward = false;
		
		uiManager = new UIManager(handler);
		uiManager.addImageButtons(this);
		
		uiManager.addObject(new UISideButton(handler.getWidth() / 2 + 110, 160, 20, 20, 1, false, handler, new ClickListener() {
			public void onClick() {
				forward = true;
			}
		}));
		
		uiManager.addObject(new UISideButton(handler.getWidth() / 2 - 110, 160, 20, 20, -1, false, handler, new ClickListener() {
			public void onClick() {
				back = true;
			}
		}));

		handler.getMouseManager().setUIManager(uiManager);
	}

	@Override
	public void tick() {
		checkMode();
		uiManager.tick();
	}

	@Override
	public void render(Graphics2D g2) {
		uiManager.render(g2);
		g2.drawImage(flood, handler.getWidth() / 2 - 150, -105, 300, 300, null);
		g2.drawImage(modeImage, handler.getWidth() / 2 - 80, 100, 160, 120, null);
		if (highscore == -1)
			Text.drawString(g2, "HIGHSCORE: ???", handler.getWidth() / 2, 240, "c", "", new Color(255, 255, 255), font, handler);
		else
			Text.drawString(g2, "HIGHSCORE: " + highscore, handler.getWidth() / 2, 240, "c", "", new Color(255, 255, 255), font, handler);
		drawTutorial(g2, uiManager);
	}
	
	public void resetGame() {
		Player.endScreen = false;
		info = false;
		infoAlpha = 0;
		uiManager.reset();
		State.setState(new GameState(handler));
	}
	
	public void checkMode() {
		if (handler.getKeyManager().left) {
			back = true;
		}
		if (handler.getKeyManager().right) {
			forward = true;
		}
		if (!(handler.getKeyManager().left) && back && mode > 0) {
			mode--;
			back = false;
			forward = false;
			modeImage = ImageLoader.loadImage("/textures/mode" + (mode + 1) + ".png");
			highscore = getHighscore();
		}
		if (!(handler.getKeyManager().right) && forward && mode < 2) {
			mode++;
			forward = false;
			back = false;
			modeImage = ImageLoader.loadImage("/textures/mode" + (mode + 1) + ".png");
			highscore = getHighscore();
		}
	}
	
	public int getHighscore() {
		FileReader fReader = null;
		BufferedReader bReader = null;
		try {
			fReader = new FileReader("res/highscore/mode" + (mode + 1) + ".dat");
			bReader = new BufferedReader(fReader);
			int score = Integer.parseInt(bReader.readLine());
			bReader.close();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(score);
			byte[] data = baos.toByteArray();
			
			fReader = new FileReader(System.getProperty("user.home") + File.separator + "flood" + File.separator + "mode" + (MenuState.mode + 1) + ".dat");
			bReader = new BufferedReader(fReader);
			if (getTaco(data).equals(bReader.readLine())) {
				bReader.close();
				return score;
			}
			else {
				bReader.close();
				return -1;
			}
		} catch (IOException | NoSuchAlgorithmException e) {
			return -1;
		}
	}
}
