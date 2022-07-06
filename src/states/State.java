package states;
import java.awt.Graphics;

import api.Game;

public abstract class State {
	
	private static State currentState = null;
	
	public State(Game game) {
		this.game = game;
	}
	

	
	public static void setState(State state) {
		currentState = state;
	}
	
	public static State getState() {
		return currentState;
	}
	
	protected Game game;
	
	public abstract void init();
	
	public abstract void ret();
	
	public abstract void tick();
	
	public abstract void render(Graphics g);	
}
