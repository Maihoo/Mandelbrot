package states;


import java.awt.Graphics;

import api.Game;
import gameClasses.Rule;

public class GameState extends State {
	
	public Rule rule;
	
	public GameState(Game game) {
		super(game);
		rule = new Rule(game);
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void ret() {
		rule.n = 1;
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		rule.render(g);
	}	
}
