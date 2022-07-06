package api;
public class LauncherMB {

	public static void main(String[] args) {
		
		Game game = new Game("Mandelbrot", 1000, 1000, 200, 128);
		game.start();
	}
}