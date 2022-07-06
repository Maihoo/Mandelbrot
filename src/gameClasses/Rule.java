package gameClasses;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import api.Game;

public class Rule {
	
	Game game;
	private Mandelbrot mb;

	Point p = MouseInfo.getPointerInfo().getLocation();
	Point b = MouseInfo.getPointerInfo().getLocation();
	
	public int n = 0;
	
	public Rule (Game game) {
		this.game = game;
		mb = new Mandelbrot(game.width, game.height, game.maxIt, game.maxSize, this, true);
	}
	
	public void render(Graphics g) {	
		if(n == 0) {
			init(g);
			n++;
		}
		
		//MOUSE
		//zoom in 
		if(game.mouseManager.leftPressed) 	{
			mb.scale *= 1.05;
			mb.extraX -= (double) ( (double) (game.getMP().x - (game.width /2))/game.width  			* (0.3/mb.scale));
			mb.extraY -= (double) ( (double) (game.getMP().y - (game.height /2))/game.height			* (0.3/mb.scale));
		}
		//zoom out
		if(game.mouseManager.rightPressed) 	{
			mb.scale /= 1.05;
			mb.extraX += (double) ( (double) (game.getMP().x - (game.width /2))/game.width  			* (0.2/mb.scale));
			mb.extraY += (double) ( (double) (game.getMP().y - (game.height /2))/game.height			* (0.2/mb.scale));
			}
		
		game.mouseManager.reset();
		
		//KEYBOARD
		//zoom in		
		if(game.keyManager.shift) 		{mb.scale *= 1.05; 			}
		//zoom out
		if(game.keyManager.control) 	{mb.scale /= 1.05;			}
		//move up
		if(game.keyManager.up) 			{mb.extraY += 0.05/mb.scale;}
		//move down
		if(game.keyManager.down) 		{mb.extraY -= 0.05/mb.scale;}
		//move left
		if(game.keyManager.left) 		{mb.extraX += 0.05/mb.scale;}
		//move right
		if(game.keyManager.right) 		{mb.extraX -= 0.05/mb.scale;}
		
		mb.render(g);
		game.keyManager.reset();
		game.mouseManager.reset();
	}
	
	public void init(Graphics g) {	
	}
}
