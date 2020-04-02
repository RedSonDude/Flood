package dev.raghav.flood;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import dev.raghav.flood.entities.Player;
import dev.raghav.flood.input.KeyManager;
import dev.raghav.flood.input.MouseManager;
import dev.raghav.flood.world.Map;
import dev.raghav.flood.world.Path;

public class Handler {
	
	private Game game;
	private Map map;
	private Player player;
	
	public Handler(Game game) {
		this.game = game;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public KeyManager getKeyManager() {
		return game.getKeyManager();
	}
	
	public MouseManager getMouseManager() {
		return game.getMouseManager();
	}
	
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public double getxAccel() {
		if (player == null)
 			return 0;
		return player.getxAccel();
	}
	
	public double getyAccel() {
		if (player == null)
 			return 0;
		return player.getyAccel();
	}
	
	public void setxAccel(double d) {
		player.setxAccel(d);
	}
	
	public void setyAccel(double d) {
		player.setyAccel(d);
	}
	
	public void setScrx(double d) {
		player.setScrx(d);
	}
	
	public void setScry(double d) {
		player.setScry(d);
	}
	
 	public double getScrx() {
 		if (player == null)
 			return 0;
		return player.getScrx();
	}
	
	public double getScry() {
		if (player == null)
 			return 0;
		return player.getScry();
	}
	
	public Polygon getArrow() {
		return player.getArrow();
	}
	
	public int getWidth() {
		return game.getWidth();
	}
	
	public int getHeight() {
		return game.getHeight();
	}
	
	public Path getP() {
		return map.getP();
	}
	
	public void setP(Path p) {
		map.setP(p);
	}
	
	public double getSpeed() {
		return player.getSpeed();
	}

	public void setSpeed(double speed) {
		player.setSpeed(speed);
	}
	
	public double getDir() {
		return player.getDir();
	}

	public void setDir(double dir) {
		player.setDir(dir);
	}
	
	public double getDirAccel() {
		return player.getDirAccel();
	}

	public void setDirAccel(double dir) {
		player.setDirAccel(dir);
	}
	
	public double getTopSpeed() {
		return player.getTopSpeed();
	}
	
	public void fixCollision() {
		map.getP().fixCollision();
	}
	
	public Point2D getFloodPoint() {
		return map.getP().getFloodPoint();
	}
}
