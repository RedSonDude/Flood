package dev.raghav.flood.ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import dev.raghav.flood.Game;
import dev.raghav.flood.Handler;
import dev.raghav.flood.gfx.ImageLoader;
import dev.raghav.flood.states.State;

public class UIManager {
	
	private ArrayList<UIObject> objects;
	private Handler handler;
	
	public UIManager(Handler handler) {
		this.handler = handler;
		objects = new ArrayList<UIObject>();
	}
	
	public void reset() {
		objects = new ArrayList<UIObject>();
	}
	
	public void tick() {
		for (UIObject o : objects)
			o.tick();
	}
	
	public void render(Graphics2D g2) {
		for (UIObject o : objects)
			o.render(g2);
	}
	
	public void onMouseMove(MouseEvent e) {
		for (UIObject o : objects)
			o.onMouseMove(e);
	}
	
	public void onMouseRelease(MouseEvent e) {
		for (UIObject o : objects) {
			o.onMouseRelease(e);
		}
	}
	
	//getters and setters
	public void addObject(UIObject o) {
		objects.add(o);
	}
	
	public void removeObject(UIObject o) {
		objects.remove(o);
	}

	public ArrayList<UIObject> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<UIObject> objects) {
		this.objects = objects;
	}

	public void addImageButtons(State state) {
		addObject(new UIImageButton(handler.getWidth() / 2, 305, 80, 80, ImageLoader.loadImage("/textures/play.png"), handler, new ClickListener() {
			public void onClick() {
				state.resetGame();
			}
		}));
		
		addObject(new UIImageButton(handler.getWidth() / 2 + 120, 305, 60, 60, ImageLoader.loadImage("/textures/info.png"), handler, new ClickListener() {
			public void onClick() {
				State.info = true;
			}
		}));
		
		addObject(new UIImageButton(handler.getWidth() / 2 - 120, 305, 60, 60, ImageLoader.loadImage("/textures/audio" + ((Game.mute)? 2 : 1) + ".png"), handler, new ClickListener() {
			private int costume = (Game.mute)? 1 : 0;
			public void onClick() {
				if (Game.mute)
					Game.mute = false;
				else
					Game.mute = true;
				costume = (costume + 1) % 2;
				((UIImageButton) getObjects().get(2)).setImage(ImageLoader.loadImage("/textures/audio" + (costume + 1) + ".png"));
			}
		}));
	}

	public void addRenderedButtons() {
		addObject(new UISideButton(handler.getWidth() / 2 + 210, 180, 20, 20, 1, true, handler, new ClickListener() {
			public void onClick() {
				State.forward = true;
			}
		}));
	
		addObject(new UISideButton(handler.getWidth() / 2 - 210, 180, 20, 20, -1, true, handler, new ClickListener() {
			public void onClick() {
				State.back = true;
			}
		}));
		
		addObject(new UICloseButton(handler.getWidth() / 2 + 165, 60, 10, 10, handler, new ClickListener() {
			public void onClick() {
				State.info = false;
			}
		}));
	}
	
	public void removeRenderedButtons() {
		for (int i = 0; i < 3; i++)
			objects.remove(objects.size() - 1);
	}

	public void renderRenderedButtons(Graphics2D g2) {
		for (int i = objects.size() - 3; i < objects.size(); i++)
			objects.get(i).render(g2);
	}

	public void setTransparency(int infoAlpha) {
		for (int i = objects.size() - 3; i < objects.size(); i++)
			objects.get(i).setTransparency(infoAlpha);
	}
}
