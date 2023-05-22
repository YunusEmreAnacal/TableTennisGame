package PingPong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * class for the paddle
 * 
 * @author Yunus Emre ANAÇAL
 *
 */
public class Paddle {

	private int x, y; // position
        
        private int speed = 10; // speed of the paddle movement
        
	private int vel = 0; // speed and direction of the paddle
	
	private int width = 20, height = 80; // dimensions
        
	public int score = 0; // score for the player
	
	private boolean left; // true if it's the left paddle
        
        private Color color; // color of the paddle

	/**
	 * create initial properties for the paddle
	 * 
	 */
	public Paddle(Color clr, boolean left) {
		// initial properties
		color = clr;
		this.left = left;

		if (left) // different x values if right or left paddle
			x = 0;
		else
			x = Game.WIDTH - width;

		y = Game.HEIGHT / 2 - height / 2;

	}

	
	public void addPoint() {
		score++;
	}

	/**
	 * Draw paddle (a rectangle), Draw score too
	 * 
	 * @param g - Graphics object used to draw everything
	 */
	public void draw(Graphics g) {

		// draw paddle
		g.setColor(color);
		g.fillRect(x, y, width, height);

		// draw score
		int sx; // x position of the string
		int padding = 25; // space between line and score
		String scoreText = Integer.toString(score);
		Font font = new Font("TimesRoman", Font.PLAIN, 50);

		if (left) {
			int strWidth = g.getFontMetrics(font).stringWidth(scoreText); 
																			// center it properly (for perfectionists)
			sx = Game.WIDTH / 2 - padding - strWidth;
		} else {
			sx = Game.WIDTH / 2 + padding;
		}

		g.setFont(font);
		g.drawString(scoreText, sx, 50);
	}

	/**
	 * update position AND collision tests
	 */
	public void update(Ball ball) {

		// update position
		y = Game.ensureRange(y + vel, 0, Game.HEIGHT - height);

		// collisions
		int ballX = ball.getX();
		int ballY = ball.getY();

		if (left) {

			if (ballX <= width + x && ballY + Ball.SIZE >= y && ballY <= y + height)
				ball.changeXDirection();

		} else {

			if (ballX + Ball.SIZE >= x && ballY + Ball.SIZE >= y && ballY <= y + height)
				ball.changeXDirection();

		}

	}

	public void switchDirections(int direction) {
		vel = speed * direction;
	}

	/**
	 * stop moving the paddle
	 */
	public void stop() {
		vel = 0;
	}

}
