package api;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener{

	private boolean[] keys;
	public boolean shift, control, up, down, left, right;
	
	public KeyManager() {
		keys = new boolean[256];
	}

	public void tick(){
		shift 	= keys[KeyEvent.VK_SHIFT];
		control = keys[KeyEvent.VK_CONTROL];
		up 		= keys[KeyEvent.VK_W];
		down 	= keys[KeyEvent.VK_S];
		left 	= keys[KeyEvent.VK_A];
		right 	= keys[KeyEvent.VK_D];
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}
	
	public void reset() {
		shift	= false;
		control = false;
		up 		= false;
		down 	= false;
		left 	= false;
		right 	= false;
	}
}
