package api;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

import states.State;
import states.GameState;

public class Game implements Runnable {
	
	public boolean liefSchonMal, running;
	public int width, height, maxIt, maxSize, resultFPS, ticks;
	public static double FPS = 144;
	public double ts, ts2;
	public String title;
	private Display display;
	private Thread thread;
	private BufferStrategy bs;
	private Graphics g;
	
	//States
	private State menuState;
	private State gameState;
	
	
	//Input
	public KeyManager keyManager;
	public MouseManager mouseManager;
	
	public Game(String title, int width, int height, int maxIt, int maxSize) {
		this.title 	 = title;
		this.width 	 = width;
		this.height  = height;
		this.maxIt 	 = maxIt;
		this.maxSize = maxSize;

		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}
	
	public Point getMP() {
		return display.getMP();
	}
	
	private void init() {
		if(display != null) display.close();
		display = new Display(title, width,height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		gameState = new GameState(this);
		State.setState(gameState);
		render();render();render();
	}
	
	private void tick() {
		keyManager.tick();
		mouseManager.tick();
		
		if(State.getState() != null) {
			State.getState().tick();
		}
		
		ts2 = System.currentTimeMillis();
		
		if(ticks % 50 == 0) {
			resultFPS = (int) (50000/(ts2-ts));
			ts = System.currentTimeMillis();
		}
		
		ticks++;
	}
	
	public void render() {
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null) {
			display.getCanvas().createBufferStrategy(2);
			return;
		}
		g = bs.getDrawGraphics();
		//Clear Screan
		g.clearRect(0, 0, width, height);
		//Draw Here!
		
		gameState.render(g);
		
		g.setFont(new Font("Arial", Font.PLAIN, 15)); 
		g.setColor(Color.WHITE);
		byte[] data = (resultFPS + " fps").getBytes();
	    g.drawBytes(data, 0, data.length, width-60, 20);

		//End Drawing
		bs.show();
		g.dispose();
	}
	
	public void run() {
		
		init();

		double fps = FPS;
		double timePerTick = 1000000000/fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		
		while(running) {
			
			timePerTick = 1000000000/(FPS);
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1){
			tick();
			if(keyManager.shift || keyManager.control || keyManager.up || keyManager.down || keyManager.left || keyManager.right || mouseManager.leftPressed || mouseManager.rightPressed) {
				render();
			}
			
			
			delta--;
			}
			
			if(timer >= 1000000000) {
				timer = 0;
			}
		}
		
		stop();
		
	}
	
	public void setGame() {
		State.setState(gameState);
		gameState.ret();
	}
	
	public void resetGame() {
		init();
		State.setState(gameState);
		gameState.init();
	}
	
	public void setMenu() {
		State.setState(menuState);
		menuState.init();
	}
	
	public void resetKM() {
		keyManager.reset();
	}
	
	public void resetMM() {
		mouseManager.reset();
	}
	
	public synchronized void start() {
		if(running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(!running)
			return;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}