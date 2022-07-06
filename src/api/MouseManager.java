package api;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener {

	public boolean leftPressed, rightPressed;
	public boolean[] keys;
	
	public MouseManager(){
		keys = new boolean[256];
	}
	
	public void tick(){
		leftPressed  = keys[MouseEvent.BUTTON1];
		rightPressed = keys[MouseEvent.BUTTON3];
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		keys[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		keys[e.getButton()] = false;
	}
	
	public void reset() {
		leftPressed  = false;
		rightPressed = false;
	}

	public void mouseMoved(MouseEvent e) {
	}
	
	public void mouseDragged(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {		
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}