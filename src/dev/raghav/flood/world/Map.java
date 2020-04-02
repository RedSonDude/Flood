package dev.raghav.flood.world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import dev.raghav.flood.Handler;
import dev.raghav.flood.states.GameState;

public class Map {
	private Path p;
	
	public Map(Handler handler) {
		p = new Path(handler);
	}
	public void tick() {

	}
	public void render(Graphics2D g2, Handler handler) {
		g2.setColor(new Color(150, 150, 150));
	    g2.setStroke(new BasicStroke(Path.width + 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	    
	    g2.translate(handler.getScrx(), handler.getScry());
	    g2.draw(p.getPath());
	    g2.translate(-handler.getScrx(), -handler.getScry());
	    
	    g2.setColor(new Color(0, 0, 0));
	    g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.translate(handler.getScrx(), handler.getScry());
		//g2.draw(p.getBoundPath(p.getKeyPoints(120)));
		//g2.draw(p.getBoundPath(p.getWalls(120)));
		if (GameState.elapsed > 2.75) {
			p.flood();
			g2.setColor(new Color(0, 175, 200));
			g2.setStroke(new BasicStroke(Path.width + 3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.draw(p.getFlood());
		}
		g2.translate(-handler.getScrx(), -handler.getScry());
		
		if (p.start) {
			g2.setColor(new Color(50, 50, 50));
			g2.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
			g2.translate(handler.getScrx() + 240, handler.getScry() + 187);
			g2.drawLine(-Path.width / 2 - 2, 0, Path.width / 2 + 3, 0);
			g2.translate(-handler.getScrx() - 240, -handler.getScry() - 187);
		}
	    
	    g2.setColor(new Color(0, 0, 0));
	    //g2.draw(p.getDirBez());
	    //g2.draw(p.getBez());
	}
	
	public Path getP() {
		return p;
	}
	
	public void setP(Path p) {
		this.p = p;
	}
}