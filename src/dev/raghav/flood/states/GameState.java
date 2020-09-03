package dev.raghav.flood.states;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import dev.raghav.flood.Handler;
import dev.raghav.flood.entities.Player;
import dev.raghav.flood.gfx.FontLoader;
import dev.raghav.flood.gfx.ImageLoader;
import dev.raghav.flood.gfx.Text;
import dev.raghav.flood.ui.ClickListener;
import dev.raghav.flood.ui.UIImageButton;
import dev.raghav.flood.ui.UIManager;
import dev.raghav.flood.world.Map;
import dev.raghav.flood.world.Path;

public class GameState extends State {
  
	private Player player;
	private Map map;
	private static Font font;
	private static Font font2;
	private DecimalFormat df;
	private static double startTime;
	public static double elapsed;
	private int countdown;
	private double textSize;
	private double yPos;
	private int alpha;
	private UIManager uiManager;
	
	public GameState(Handler handler) {
		super(handler);
		startTime = -1;
		elapsed = 0;
		countdown = 3;
		textSize = -1;
		df = new DecimalFormat("##.#");
		df.setRoundingMode(RoundingMode.DOWN);
		uiManager = new UIManager(handler);
		
		map = new Map(handler);
		handler.setMap(map);
		
		player = new Player(handler);
		handler.setPlayer(player);
		
		font = FontLoader.loadFont("fonts/NotoSans-Medium.ttf", 20);
	}
	
	public void tick() {
		if (State.getState() == this && startTime == -1)
			startTime = System.nanoTime();
		if (!Player.dead)
			elapsed = (System.nanoTime() - startTime) / 1000000000;
		map.tick();
		player.tick();
		if (Player.endScreen) {
			if (Path.score > MenuState.highscore) {
				MenuState.highscore = Path.score;
				File dir = new File(System.getProperty("user.home") + "/flood");
				File scoreFile = new File(System.getProperty("user.home") + "/flood/mode" + (MenuState.mode + 1) + ".dat");
				File tortilla = new File(System.getProperty("user.home") + "/flood/.data" + (MenuState.mode + 1) + ".dat");
				if (!dir.exists()) {
					dir.mkdir();
				}
				if (!scoreFile.exists()) {
					try {
						scoreFile.createNewFile();
						tortilla.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				FileWriter fWriter;
				BufferedWriter bWriter;
				try {
					fWriter = new FileWriter(scoreFile);
					bWriter = new BufferedWriter(fWriter);
					bWriter.write(Integer.toString(MenuState.highscore));
					bWriter.close();
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					DataOutputStream dos = new DataOutputStream(baos);
					dos.writeInt(MenuState.highscore);
					byte[] data = baos.toByteArray();
					
					fWriter = new FileWriter(tortilla);
					bWriter = new BufferedWriter(fWriter);
					bWriter.write(getTaco(data));
					bWriter.close();
				} catch (IOException | NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
			if (handler.getMouseManager().getUIManager() != uiManager) {
				uiManager.addImageButtons(this);
				uiManager.addObject(new UIImageButton(handler.getWidth() - 30, 30, 40, 40, ImageLoader.loadImage("/textures/home.png"), handler, new ClickListener() {
					public void onClick() {
						resetMenu();
					}
				}));
				handler.getMouseManager().setUIManager(uiManager);
			}
			uiManager.tick();
		}
	}

	public void render(Graphics2D g2) {
		map.render(g2, handler);  
		player.render(g2);
		if (!Player.dead) {
			Text.drawString(g2, "Score: " + Path.score, 0, 0, "r", "u", Color.WHITE, font, handler);
			int time;
			if (GameState.elapsed < 3)
				time = 0;
			else
				time = (int) GameState.elapsed - 3;
			Text.drawString(g2, "Time: " + time / 60 + ":" + String.format("%02d", time % 60), 10, 0, "", "u", Color.WHITE, font, handler);
		}
		if (elapsed > 0 && countdown == 3) {
			if (textSize == -1) {
				textSize = 100;
				yPos = 50;
				alpha = 255;
			}
			else if (textSize > 60){
				textSize -= 4;
				yPos += 2;
			}
			else {
				alpha -= 10;
				if (alpha < 0) {
					alpha = 0;
					countdown = 2;
					textSize = -1;
				}
			}
			font2 = FontLoader.loadFont("fonts/NotoSans-Medium.ttf", (float) textSize);
			Text.drawString(g2, "3", 240, (int) yPos, "c", "", new Color(255, 255, 255, alpha), font2, handler);
		}
		if (elapsed > 1 && countdown == 2) {
			if (textSize == -1) {
				textSize = 100;
				yPos = 50;
				alpha = 255;
			}
			else if (textSize > 60){
				textSize -= 4;
				yPos += 2;
			}
			else {
				alpha -= 10;
				if (alpha < 0) {
					alpha = 0;
					countdown = 1;
					textSize = -1;
				}
			}
			font2 = FontLoader.loadFont("fonts/NotoSans-Medium.ttf", (float) textSize);
			Text.drawString(g2, "2", 240, (int) yPos, "c", "", new Color(255, 255, 255, alpha), font2, handler);
		}
		if (elapsed > 2 && countdown == 1) {
			if (textSize == -1) {
				textSize = 100;
				yPos = 50;
				alpha = 255;
			}
			else if (textSize > 60){
				textSize -= 4;
				yPos += 2;
			}
			else {
				alpha -= 10;
				if (alpha < 0) {
					alpha = 0;
					countdown = 0;
					textSize = -1;
				}
			}
			font2 = FontLoader.loadFont("fonts/NotoSans-Medium.ttf", (float) textSize);
			Text.drawString(g2, "1", 240, (int) yPos, "c", "", new Color(255, 255, 255, alpha), font2, handler);
		}
		if (elapsed > 3 && countdown == 0) {
			if (textSize == -1) {
				textSize = 100;
				yPos = 50;
				alpha = 255;
			}
			else if (textSize > 60){
				textSize -= 4;
				yPos += 2;
			}
			else {
				alpha -= 10;
				if (alpha < 0) {
					alpha = 0;
					countdown = -1;
					textSize = -1;
				}
			}
			font2 = FontLoader.loadFont("fonts/NotoSans-Medium.ttf", (float) textSize);
			Text.drawString(g2, "GO!", 240, (int) yPos, "c", "", new Color(255, 255, 255, alpha), font2, handler);
		}
		if (Player.endScreen) {
			alpha += 10;
			font2 = FontLoader.loadFont("fonts/NotoSans-Medium.ttf", 25);
			if (alpha > 255)
				alpha = 255;
			Text.drawString(g2, "SCORE", 240, 25, "c", "", new Color(255, 255, 255, alpha), font2, handler);
			int time;
			if (GameState.elapsed < 3)
				time = 0;
			else
				time = (int) GameState.elapsed - 3;
			Text.drawString(g2, "TIME", handler.getWidth() / 2, 105, "c", "", new Color(255, 255, 255, alpha), font2, handler);
			Text.drawString(g2, "HIGHSCORE", handler.getWidth() / 2, 185, "c", "", new Color(255, 255, 255, alpha), font2, handler);
			font2 = FontLoader.loadFont("fonts/NotoSans-ExtraBold.ttf", 40);
			Text.drawString(g2, Path.score + "", handler.getWidth() / 2, 60, "c", "", new Color(255, 255, 255, alpha), font2, handler);
			Text.drawString(g2, time / 60 + ":" + String.format("%02d", time % 60), 240, 140, "c", "", new Color(255, 255, 255, alpha), font2, handler);
			Text.drawString(g2, MenuState.highscore + "", handler.getWidth() / 2, 220, "c", "", new Color(255, 255, 255, alpha), font2, handler);
			uiManager.render(g2);
			drawTutorial(g2, uiManager);
		}
	}
	
	public void resetMenu() {
		State.setState(new MenuState(handler));
		uiManager.reset();
	}

	public void resetGame() {
		player.setAlpha(255);
		Player.endScreen = false;
		State.setState(new GameState(handler));
		uiManager.reset();
	}
}
