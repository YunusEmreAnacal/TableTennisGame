package PingPong;

import Client.Client;
import Message.Message;
import java.awt.Color;
import java.awt.Graphics;

/**
 * class for the ball in the game
 *
 * @author Yunus Emre ANAÇAL
 *
 */
public class Ball {

    public static final int SIZE = 20;

    private int x, y; // position of top left corner of square
    private int xV, yV; // either 1 or -1
    private int speed = 5; // speed of the ball

    /**
     * constructor
     */
    public Ball() {
        reset();
    }

    /**
     * setup initial position and velocity
     */
    private void reset() {
        // initial position
        x = Game.WIDTH / 2 - SIZE / 2;
        y = Game.HEIGHT / 2 - SIZE / 2;

        // initial velocity
        xV = Game.sign(Math.random() * 2.0 - 1);
        yV = Game.sign(Math.random() * 2.0 - 1);
    }

    
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillRoundRect(x, y, SIZE, SIZE, 50, 50);
    }

    /**
     * update position AND collision tests
     *
     * @param lp: left paddle
     * @param rp: right paddle
     */
    public void update(Paddle lp, Paddle rp) {

        // update position
        x += xV * speed;
        y += yV * speed;

        // collisions
        // with ceiling and floor
        if (y + SIZE >= Game.HEIGHT || y <= 0) {
            changeYDirection();
        }

        // with walls
        if (x + SIZE >= Game.WIDTH) { // right wall
            lp.addPoint();
            Message msg = new Message(Message.Message_Type.ScoreChanged);
            msg.content = lp.score;
            Client.Send(msg);
            // send this point to the server
            reset();
        }
        if (x <= 0) { // left wall
            reset();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * switch x direction of the ball
     */
    public void changeXDirection() {
        xV *= -1;
    }

    /**
     * switch y direction of the ball
     */
    public void changeYDirection() {
        yV *= -1;
    }

}
