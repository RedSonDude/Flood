 package dev.raghav.flood.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import dev.raghav.flood.Handler;
import dev.raghav.flood.states.GameState;
import dev.raghav.flood.world.Path;

public class Player {

	private double scrx;
	private double scry;
	private double xAccel;
	private double yAccel;
	private double dir;
	private double dirAccel;
	private double speed;
	private double topSpeed;
	private Polygon arrow;
	private Polygon trail;
	private int tick;
	private int alpha;
	private ArrayList<Point2D> positions;
	private ArrayList<Double> dirs;
	public static boolean dead;
	public static boolean endScreen;

	private Handler handler;

	public Player(Handler handler) {
		this.handler = handler;
		dir = 0;
		dirAccel = 0;
		speed = 0;
		topSpeed = 5.0;
		scrx = 0;
		scry = 20;
		xAccel = 0;
		yAccel = 0;
		tick = 0;
		alpha = 255;
		arrow = new Polygon();
		trail = new Polygon();
		positions = new ArrayList<Point2D>();
		dirs = new ArrayList<Double>();
		dead = false;
		endScreen = false;
	}

	public void move() {
		dir = (dir + dirAccel) % (2 * Math.PI);
		scrx += xAccel;
		scry += yAccel;
		handler.getP().fixCollision();
	}

	public void tick() {
		tick++;
		if (tick % 600 == 0)
			topSpeed += 0.25;
		if (GameState.elapsed > 3.0) {
			getInput();
			move();
		}
		if (!dead)
			dead();
		if (tick % 5 == 0) {
			if (tick > 25) {
				positions.remove(4);
				dirs.remove(4);
			}
			if (dead) {
				positions.add(0, null);
				dirs.add(0, null);
			}
			else {
				positions.add(0, new Point2D.Double(-scrx + 240, -scry + 180));
				dirs.add(0, dir);
			}
		}
	}
	
	public void render(Graphics2D g2) {
		if (alpha > 0)
			drawPlayer(g2);
		else {
			endScreen = true;
		}
	}

	private void getInput() {
	
		if (handler.getKeyManager().up && !dead) {
			if (speed < topSpeed) {
				speed += 0.2;
			}
			else {
				speed = topSpeed;
			}
		}
		if (handler.getKeyManager().down && !dead) {
			if (speed > -topSpeed) {
				speed -= 0.2;
			}
			else {
				speed = -topSpeed;
			}
		}
		if (handler.getKeyManager().left && !dead) {
			dirAccel = Math.PI / 128 * speed / topSpeed;
		}
		if (handler.getKeyManager().right && !dead) {
			dirAccel = -Math.PI / 128 * speed / topSpeed;
		}
		if (!handler.getKeyManager().up && !handler.getKeyManager().down || dead) {
			speed *= 0.99;
		}
		if (!handler.getKeyManager().left && !handler.getKeyManager().right || dead) {
			dirAccel *= 0.8;
		}
		
		xAccel = speed * Math.sin(dir);
		yAccel = speed * Math.cos(dir);
	}

	public void drawPlayer(Graphics2D g2) {
		if (dead)
			alpha -= 5;
		g2.setColor(new Color(255, 255, 255, alpha));
	    g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	    g2.translate(240, 180);
	    g2.rotate(-dir);
	    arrow.reset();
	    arrow.addPoint(0, -20);
	    arrow.addPoint((int) (18 * Math.sin(0.75 * Math.PI)), (int) (-18 * Math.cos(0.75 * Math.PI)));
	    arrow.addPoint(0, 0);
	    arrow.addPoint((int) (-18 * Math.sin(0.75 * Math.PI)), (int) (-18 * Math.cos(0.75 * Math.PI)));
	    g2.fillPolygon(arrow);
	    g2.drawPolygon(arrow);
	    g2.rotate(dir);
	    g2.translate(-240, -180);
	    for (int i = 0; i < positions.size(); i++) {
	    	if (!(positions.get(i) == null)) {
		    	g2.setColor(new Color(255, 255, 255, (int) (255 * (5 - i) / 6.0)));
		    	g2.translate(positions.get(i).getX() + handler.getScrx(), positions.get(i).getY() + handler.getScry());
		    	g2.rotate(-dirs.get(i));
		    	trail.reset();
		    	trail.addPoint(0, -20);
			    trail.addPoint((int) (18 * Math.sin(0.75 * Math.PI)), (int) (-18 * Math.cos(0.75 * Math.PI)));
			    trail.addPoint(0, 0);
			    trail.addPoint((int) (-18 * Math.sin(0.75 * Math.PI)), (int) (-18 * Math.cos(0.75 * Math.PI)));
			    g2.drawPolygon(trail);
			    g2.rotate(dirs.get(i));
			    g2.translate(-positions.get(i).getX() - handler.getScrx(), -positions.get(i).getY() - handler.getScry());
	    	}
	    }
	}
	
	public void dead() {
		double x;
		double y;
		
		Area arr = new Area(arrow);
		AffineTransform t = new AffineTransform();
		t.translate(240, 180);
		t.rotate(-dir);
		arr.transform(t);
		
		for (double i = 0; i < 2 * Math.PI; i += 0.1) {
			x = handler.getFloodPoint().getX() + handler.getScrx();
			y = handler.getFloodPoint().getY() + handler.getScry();
			x += (Path.width / 2 + 3) * Math.cos(i);
			y += (Path.width / 2 + 3) * Math.sin(i);
			if (arr.contains(x, y))
				dead = true;
		}
	}
	
	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
	}
	
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	//getters and setters
	public double getScrx() {
		return scrx;
	}

	public void setScrx(double scrx) {
		this.scrx = scrx;
	}

	public double getScry() {
		return scry;
	}

	public void setScry(double scry) {
		this.scry = scry;
	}

	public double getDir() {
		return dir;
	}

	public double getDirAccel() {
		return dirAccel;
	}

	public void setDirAccel(double dirAccel) {
		this.dirAccel = dirAccel;
	}

	public void setDir(double dir) {
		this.dir = dir;
	}

	public double getxAccel() {
		return xAccel;
	}

	public void setxAccel(double xMove) {
		this.xAccel = xMove;
	}

	public double getyAccel() {
		return yAccel;
	}

	public void setyAccel(double yMove) {
		this.yAccel = yMove;
	}
	
	public Polygon getArrow() {
		return arrow;
	}
	
	public double getTopSpeed() {
		return topSpeed;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
}
