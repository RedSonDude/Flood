package dev.raghav.flood.world;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import dev.raghav.flood.Handler;
import dev.raghav.flood.entities.Player;
import dev.raghav.flood.states.MenuState;

public class Path {
	private Handler handler;
	private Point2D[] points;
	private ArrayList<Point2D> bezier;
	private ArrayList<Point2D> flood;
	private GeneralPath path;
	public static int width;
	private double invSlope;
	private int side;
	private double t;
	public static int score;
	private double dirWall;
	private double dirBez;
	private boolean traveled;
	public boolean start;
	private int floodStart;
	
	public Path(Handler handler) {
		points = new Point2D[7];
		path = new GeneralPath();
		width = 60;
		this.handler = handler;
		initArray();
		t = 0;
		flood = new ArrayList<Point2D>();
		flood.add(points[0]);
		traveled = false;
		start = true;
		score = 0;
	}
	
	public void flood() {
		
		double change;
		if (distance(flood.get(flood.size() - 1).getX() + handler.getScrx(), flood.get(flood.size() - 1).getY() + handler.getScry(), 240, 180) > 200)
			change = handler.getTopSpeed();
		else
			change = handler.getTopSpeed() - 0.25;
		double bezX = flood.get(flood.size() - 1).getX();
		double bezY = flood.get(flood.size() - 1).getY();
		
		while (distance(flood.get(flood.size() - 1).getX(), flood.get(flood.size() - 1).getY(), bezX, bezY) < change) {
			t += 0.001;
			if (t > 1.999) {
				t = 1.999;
				break;
			}
			int i = (int) (t / 1) * 3;
			bezX = Math.pow(1 - t % 1, 3) * points[i].getX();
			bezX += 3 * Math.pow(1 - t % 1, 2) * (t % 1) * points[i + 1].getX();
			bezX += 3 * (1 - t % 1) * Math.pow(t % 1, 2) * points[i + 2].getX();
			bezX += Math.pow(t % 1, 3) * points[i + 3].getX();
			bezY = Math.pow(1 - t % 1, 3) * points[i].getY();
			bezY += 3 * Math.pow(1 - t % 1, 2) * (t % 1) * points[i + 1].getY();
			bezY += 3 * (1 - t % 1) * Math.pow(t % 1, 2) * points[i + 2].getY();
			bezY += Math.pow(t % 1, 3) * points[i + 3].getY();
		}
		while (distance(flood.get(flood.size() - 1).getX(), flood.get(flood.size() - 1).getY(), bezX, bezY) > change) {
			t -= 0.00001;
			int i = (int) (t / 1) * 3;
			bezX = Math.pow(1 - t % 1, 3) * points[i].getX();
			bezX += 3 * Math.pow(1 - t % 1, 2) * (t % 1) * points[i + 1].getX();
			bezX += 3 * (1 - t % 1) * Math.pow(t % 1, 2) * points[i + 2].getX();
			bezX += Math.pow(t % 1, 3) * points[i + 3].getX();
			bezY = Math.pow(1 - t % 1, 3) * points[i].getY();
			bezY += 3 * Math.pow(1 - t % 1, 2) * (t % 1) * points[i + 1].getY();
			bezY += 3 * (1 - t % 1) * Math.pow(t % 1, 2) * points[i + 2].getY();
			bezY += Math.pow(t % 1, 3) * points[i + 3].getY();
		}
		if ((bezY < 180 || !start) && !Player.dead)
			score += (int) (change * 10);
		flood.add(new Point2D.Double(bezX, bezY));
	}
	
	public Point2D getFloodPoint() {
		return flood.get(flood.size() - 1);
	}
	
	public void initArray() {
		points[0] = new Point2D.Double(handler.getWidth() / 2, handler.getHeight());
		points[1] = new Point2D.Double(handler.getWidth() / 2, handler.getHeight());
		points[2] = new Point2D.Double(handler.getWidth() / 2, 0);
		points[3] = new Point2D.Double(handler.getWidth() / 2, 0);
		points[4] = new Point2D.Double(handler.getWidth() / 2, -Math.random() * 240 - 360);
		double distance = Math.random() * 240 + 240;
		double dir = ((int) (Math.random() + 0.5) * 2 - 1) * (Math.random() * Math.PI / 8 + Math.PI / 4);
		points[5] = new Point2D.Double(points[4].getX() - Math.sin(dir) * distance, points[4].getY() - Math.cos(dir) * distance);
		distance = Math.random() * 240 + 360;
		dir += ((int) (Math.random() + 0.5) * 2 - 1) * (Math.random() * Math.PI / 8 + Math.PI / 4);
		points[6] = new Point2D.Double(points[5].getX() - Math.sin(dir) * distance, points[5].getY() - Math.cos(dir) * distance);
		initPath();
	}
	
	public void newBezier() {
		start = false;
		dirBez = -Math.atan(getInvSlope(bezier.size() - 2, bezier.size() - 1)) + ((goingDown(bezier.size() - 2, bezier.size() - 1))? 1 : 0) * Math.PI;
		points[0] = points[3];
		points[1] = points[4];
		points[2] = points[5];
		points[3] = points[6];
		double distance = Math.random() * 240 + 360;
		points[4] = new Point2D.Double(points[3].getX() - Math.sin(dirBez) * distance, points[3].getY() - Math.cos(dirBez) * distance);
		distance = Math.random() * 240 + 240;
		double dir = dirBez + ((int) (Math.random() + 0.5) * 2 - 1) * (Math.random() * Math.PI / 8 + Math.PI / 4);
		points[5] = new Point2D.Double(points[4].getX() - Math.sin(dir) * distance, points[4].getY() - Math.cos(dir) * distance);
		distance = Math.random() * 240 + 360;
		dir += ((int) (Math.random() + 0.5) * 2 - 1) * (Math.random() * Math.PI / 8 + Math.PI / 4);
		points[6] = new Point2D.Double(points[5].getX() - Math.sin(dir) * distance, points[5].getY() - Math.cos(dir) * distance);
		initPath();
		traveled = false;
	}
	
	public GeneralPath getPath() {
		boolean xOut = (points[3].getX() + handler.getScrx() > handler.getWidth() && points[0].getX() + handler.getScrx() > handler.getWidth()) || (points[3].getX() + handler.getScrx() < 0 && points[0].getX() + handler.getScrx() < 0);
		boolean yOut = (points[3].getY() + handler.getScry() > handler.getHeight() && points[0].getY() + handler.getScry() > handler.getHeight()) || (points[3].getY() + handler.getScry() < 0 && points[0].getY() + handler.getScry() < 0);
		if (distance(points[3].getX() + handler.getScrx(), points[3].getY() + handler.getScry(), 240, 180) < 120)
			traveled = true;
		if ((xOut || yOut) && traveled) {
			newBezier();
		}
		return path;
	}
	
	public void initPath() {
		path.reset();
		path.moveTo(points[0].getX(), points[0].getY());
		path.curveTo(points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY(), points[3].getX(), points[3].getY());
		path.curveTo(points[4].getX(), points[4].getY(), points[5].getX(), points[5].getY(), points[6].getX(), points[6].getY());
		bezier = getAllPoints();
	}
	
	public void fixCollision() {
		if (getCollision()) {
			Area arrow = new Area(handler.getArrow());
			AffineTransform t = new AffineTransform();
			t.translate(240, 180);
			t.rotate(-handler.getDir());
			arrow.transform(t);
			if (getCollisionWalls()) {
				dirWall = -Math.atan(invSlope) + Math.PI / 2 + Math.PI * side;
				while (getCollisionWalls()) {
				//for (int i = 0; i < 1; i++) {
					handler.setScrx(handler.getScrx() - Math.sin(dirWall) / 4);
					handler.setScry(handler.getScry() - Math.cos(dirWall) / 4);
				}
			}
			//floor
			if (start) {
				if (arrow.intersects(240 + handler.getScrx() - width / 2, 180 + handler.getScry(), width, 20)) {
					dirWall = Math.PI;
					while (getCollision()) {
						handler.setScrx(handler.getScrx() - Math.sin(dirWall) / 4);
						handler.setScry(handler.getScry() - Math.cos(dirWall) / 4);
						if (getCollisionWalls()) {
							fixCollision();
							break;
						}
					}
				}
			}
			//ceiling
			if (arrow.intersects(bezier.get(bezier.size() - 1).getX() + handler.getScrx() - width / 2, bezier.get(bezier.size() - 1).getY() + handler.getScry() - 20, width, 20)) {
				dirWall = 0;
				while (getCollision()) {
					handler.setScrx(handler.getScrx() - Math.sin(dirWall) / 4);
					handler.setScry(handler.getScry() - Math.cos(dirWall) / 4);
					if (getCollisionWalls()) {
						fixCollision();
						break;
					}
				}
			}
			if (MenuState.mode == 0)
				handler.setSpeed(handler.getSpeed() * 0.95);
			if (MenuState.mode == 1)
				handler.setSpeed(handler.getSpeed() * 0.75);
			if (MenuState.mode == 2)
				handler.setSpeed(-handler.getSpeed() * 0.75);
		}
	}
	
	public GeneralPath getDirBez() {
		GeneralPath dir = new GeneralPath();
		dir.moveTo(240, 180);
		dir.lineTo(240 + Math.sin(dirBez) * 50, 180 + Math.cos(dirBez) * 50);
		return dir;
	}
	
	public GeneralPath getBez() {
		GeneralPath bez = new GeneralPath();
		bez.moveTo(points[3].getX() + handler.getScrx(), points[3].getY() + handler.getScry());
		bez.lineTo(points[4].getX() + handler.getScrx(), points[4].getY() + handler.getScry());
		bez.lineTo(points[5].getX() + handler.getScrx(), points[5].getY() + handler.getScry());
		bez.lineTo(points[6].getX() + handler.getScrx(), points[6].getY() + handler.getScry());
		return bez;
	}

	public ArrayList<Point2D> getKeyPoints(int d) {
		ArrayList<Point2D> bound = new ArrayList<Point2D>();
		boolean down = false;
		boolean startAdded = false;
		double bezX = bezier.get(0).getX();
		double bezY = bezier.get(0).getY();
		double distance1 = Integer.MAX_VALUE;
		double distance2 = Integer.MAX_VALUE;
		double distance3 = Integer.MAX_VALUE;
		for (int i = 0; i < bezier.size() - 1; i++) {
			if (distance(bezier.get(i).getX() + handler.getScrx(), bezier.get(i).getY() + handler.getScry(), 240, 180) < d) {
				if (bezier.get(i).getY() <= handler.getHeight() / 2 || !start) {
					double sl = getInvSlope(i, i + 1);
					if (sl == Double.POSITIVE_INFINITY)
						sl = Double.MAX_VALUE;
					if (sl == Double.NEGATIVE_INFINITY)
						sl = Double.MIN_VALUE;
					int sign = 1;
					if (goingDown(i, i + 1))
						sign = -1;
					if (i == bezier.size() - 2) {
						bezX = bezier.get(i + 1).getX() + sign * width / 2;
						bezY = bezier.get(i + 1).getY();
					}
					else {
						bezX = bezier.get(i).getX() + sign * Math.cos(Math.atan(sl)) * width / 2;
						bezY = bezier.get(i).getY() + sign * Math.sin(Math.atan(sl)) * width / 2;
					}
					bound.add(0, new Point2D.Double(bezX, bezY));
					if (distance(bezX + handler.getScrx(), bezY + handler.getScry(), 240, 180) < distance1)
						distance1 = distance(bezX + handler.getScrx(), bezY + handler.getScry(), 240 ,180);
	
					if (i == bezier.size() - 2) {
						bezX = bezier.get(i + 1).getX() - sign * width / 2;
						bezY = bezier.get(i + 1).getY();
					}
					else {
						bezX = bezier.get(i).getX() - sign * Math.cos(Math.atan(sl)) * width / 2;
						bezY = bezier.get(i).getY() - sign * Math.sin(Math.atan(sl)) * width / 2;
					}
					bound.add(new Point2D.Double(bezX, bezY));
					if (distance(bezX + handler.getScrx(), bezY + handler.getScry(), 240, 180) < distance2)
						distance2 = distance(bezX + handler.getScrx(), bezY + handler.getScry(), 240, 180);
				
					if (distance(bezier.get(i).getX() + handler.getScrx(), bezier.get(i).getY() + handler.getScry(), 240, 180) < distance3) {
						distance3 = distance(bezier.get(i).getX() + handler.getScrx(), bezier.get(i).getY() + handler.getScry(), 240, 180);
						invSlope = sl;
						down = goingDown(i, i +1);
					}
				}
				else if (!startAdded) {
					bound.add(0, new Point2D.Double(bezier.get(i).getX() - width / 2, handler.getHeight() / 2));
					bound.add(0, new Point2D.Double(bezier.get(i).getX() + width / 2, handler.getHeight() / 2));
					startAdded = true;
				}
			}
		}
		if (distance1 > distance2)
			side = 0;
		else
			side = 1;
		if (down) {
			side = (side + 1) % 2;
		}
		return bound;
	}
	
	public ArrayList<Point2D> getWalls(int d) {
		ArrayList<Point2D> bound = new ArrayList<Point2D>();
		double bezX = bezier.get(0).getX();
		double bezY = bezier.get(0).getY();
		for (int i = 0; i < bezier.size() - 1; i++) {
			if (distance(bezier.get(i).getX() + handler.getScrx(), bezier.get(i).getY() + handler.getScry(), 240, 180) < d) {
				double sl = getInvSlope(i, i + 1);
				if (sl == Double.POSITIVE_INFINITY)
					sl = Double.MAX_VALUE;
				if (sl == Double.NEGATIVE_INFINITY)
					sl = Double.MIN_VALUE;
				int sign = 1;
				if (goingDown(i, i + 1))
					sign = -1;
				if (i == bezier.size() - 2) {
					bezX = bezier.get(i + 1).getX() + sign * width / 2;
					bezY = bezier.get(i + 1).getY();
				}
				else {
					bezX = bezier.get(i).getX() + sign * Math.cos(Math.atan(sl)) * width / 2;
					bezY = bezier.get(i).getY() + sign * Math.sin(Math.atan(sl)) * width / 2;
				}
				bound.add(0, new Point2D.Double(bezX, bezY));
				
				if (i == bezier.size() - 2) {
					bezX = bezier.get(i + 1).getX() - sign * width / 2;
					bezY = bezier.get(i + 1).getY();
				}
				else {
					bezX = bezier.get(i).getX() - sign * Math.cos(Math.atan(sl)) * width / 2;
					bezY = bezier.get(i).getY() - sign * Math.sin(Math.atan(sl)) * width / 2;
				}
				bound.add(new Point2D.Double(bezX, bezY));
				
				if (i == bezier.size() - 2) {
					bound.add(0, new Point2D.Double(bound.get(0).getX(), bound.get(0).getY() - 40));
					bound.add(new Point2D.Double(bound.get(bound.size() - 1).getX(), bound.get(bound.size() - 1).getY() - 40));
				}
			}
		}
		return bound;
	}
	
	public ArrayList<Point2D> getAllPoints() {
		ArrayList<Point2D> bez = new ArrayList<Point2D>();
		
		int i = 0;
		double t = 0;
		double bezX = points[0].getX();
		double bezY = points[0].getY();
		while (true) {
			if (1.0 - t < 0.01) {
				if (i == 3) {
					return bez;
				}
				else {
					i += 3;
					t = 0;
				}
			}
			else {
				t += 0.01;
			}
			bez.add(new Point2D.Double(bezX, bezY));
			bezX = Math.pow(1 - t, 3) * points[i].getX();
			bezX += 3 * Math.pow(1 - t, 2) * t * points[i + 1].getX();
			bezX += 3 * (1 - t) * Math.pow(t, 2) * points[i + 2].getX();
			bezX += Math.pow(t, 3) * points[i + 3].getX();
			bezY = Math.pow(1 - t, 3) * points[i].getY();
			bezY += 3 * Math.pow(1 - t, 2) * t * points[i + 1].getY();
			bezY += 3 * (1 - t) * Math.pow(t, 2) * points[i + 2].getY();
			bezY += Math.pow(t, 3) * points[i + 3].getY();
		}
	}
	
	public GeneralPath getBoundPath(ArrayList<Point2D> l) {
		GeneralPath apprx = new GeneralPath();
		apprx.moveTo(l.get(0).getX(), l.get(0).getY());
		for (int i = 1; i < l.size(); i++) {
			apprx.lineTo(l.get(i).getX(), l.get(i).getY());
		}
		apprx.closePath();
		return apprx;
	}
	
	public double getInvSlope(int ind1, int ind2) {
		return -(bezier.get(ind1).getX() - bezier.get(ind2).getX()) / (bezier.get(ind1).getY() - bezier.get(ind2).getY());
	}
	
	public boolean goingDown(int ind1, int ind2) {
		return bezier.get(ind2).getY() > bezier.get(ind1).getY();
	}
	
	public boolean getCollision() {
		Area arrow = new Area(handler.getArrow());
		AffineTransform t = new AffineTransform();
		t.translate(240, 180);
		t.rotate(-handler.getDir());
		arrow.transform(t);
		
		if (getKeyPoints(120).size() == 0)
			return true;
		Area box = new Area();
		box = new Area(getBoundPath(getKeyPoints(120)));
		AffineTransform t2 = new AffineTransform();
		t2.translate(handler.getScrx(), handler.getScry());
		box.transform(t2);
		
		Area prevBox = (Area) box.clone();
		box.add(arrow);
		return !box.equals(prevBox);
	}
	
	public boolean getCollisionWalls() {
		Area arrow = new Area(handler.getArrow());
		AffineTransform t = new AffineTransform();
		t.translate(240, 180);
		t.rotate(-handler.getDir());
		arrow.transform(t);
		
		if (getWalls(120).size() == 0)
			return true;
		Area box = new Area();
		box = new Area(getBoundPath(getWalls(120)));
		AffineTransform t2 = new AffineTransform();
		t2.translate(handler.getScrx(), handler.getScry());
		box.transform(t2);
		
		Area prevBox = (Area) box.clone();
		box.add(arrow);
		return !box.equals(prevBox);
	}

	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
	}

	public GeneralPath getFlood() {
		GeneralPath fl = new GeneralPath();
		int i;
		int j;
		if (Player.dead) {
			i = floodStart;
			j = flood.size();
			if (j > floodStart + 400)
				j = floodStart + 400;
		}
		else {
			i = flood.size() - 100;
			j = flood.size();
			floodStart = i;
		}
		if (i < 0)
			i = 0;
		fl.moveTo(flood.get(i).getX(), flood.get(i).getY());
		for (;i < j; i++) {
			fl.lineTo(flood.get(i).getX(), flood.get(i).getY());
		}
		return fl;
	}
}
