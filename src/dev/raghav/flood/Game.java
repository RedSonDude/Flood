package dev.raghav.flood;

import java.awt.Graphics2D;

import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.net.URL;

import dev.raghav.flood.display.Display;
import dev.raghav.flood.input.KeyManager;
import dev.raghav.flood.input.MouseManager;
import dev.raghav.flood.states.*;
import kuusisto.tinysound.*;

public class Game implements Runnable {
	private Display display;
	public int width, height;
	public String title;
	private Music music;
	public static boolean mute;

	private boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics2D g2;
  
	//input
	private KeyManager keyManager;
	private MouseManager mouseManager;
  
	//handler
	private Handler handler;
  
	//player

	public Game(String title, int width, int height) {
		this.width = width;
		this.height = height;
		this.title = title;
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
		
		TinySound.init();
		URL url = this.getClass().getResource("/sound/TheFatRat - Time Lapse.wav");
		music = TinySound.loadMusic(url);
		music.setVolume(0.2);
		mute = false;
		
		handler = new Handler(this);
		State.setState(new MenuState(handler));
	}

	private void init() {
		TinySound.setGlobalVolume(1.0);
		music.play(true);
		display = new Display(title, width, height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
	}

	private void tick() {
		if (mute)
			TinySound.setGlobalVolume(0);
		else
			TinySound.setGlobalVolume(1.0);
		
		keyManager.tick();
		if (State.getState() != null)
			State.getState().tick();
	}

	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g2 = (Graphics2D) bs.getDrawGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		//clear
		g2.clearRect(0, 0, width, height);
		//draw here
    
		if (State.getState() != null) {
			State.getState().render(g2);
		}
		//end here
		bs.show();
		g2.dispose();
	}

	public void run() {
		init();

		//in nanoseconds
		double timePerTick = 1000000000 / 60;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				ticks++;
				delta = 0;
			}
			if (timer >= 1000000000) {
				System.out.println("FPS: " + ticks);
				ticks = 0;
				timer = 0;
			}
		}
		TinySound.shutdown();
		stop();
	}
  
	public KeyManager getKeyManager() {
		return keyManager;
	}
  
	public MouseManager getMouseManager() {
		return mouseManager;
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start(); //runs the run method
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
