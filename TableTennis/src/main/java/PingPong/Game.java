package PingPong;

import Client.Client;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.Socket;

/**
 * 
 *
 * @author Yunus Emre ANAÇAL
 *
 */
public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    private boolean isGameFinished = false;

    public final static int WIDTH = 1000;
    public final static int HEIGHT = WIDTH * 9 / 16; // 16:9

    public boolean running = false; // true if the game is running
    private Thread gameThread; // thread where the game is updated AND drawn 

    // ball object 
    private Ball ball;
    // the paddles
    private Paddle leftPaddle;
    public static Paddle rightPaddle;

    private MainMenu menu; // Main Menu object

    private Rectangle gamerName;
    public static String gamerNameTxt = "";

    //User Name Label
    private Rectangle rivalName;
    public static String rivalNameTxt = "Waiting second players";
    
    private Rectangle winnerName;
    public static String winnerNameTxt = "";
    
    Font fontUser; // for user names
    Font fontWinner; // for winner name
    /**
     * constructor
     */
    public Game() {

        canvasSetup();

        new Window("Table Tennis Game", this);

        initialize();

        // position and dimensions of each button
        int x, y, w, h;

        w = 100;
        h = 40;

        y = h / 2;
        
        int x_first = w / 2;
        gamerName = new Rectangle(x_first, y, w, h);

        int x_second = Game.WIDTH - 3 * w / 2;
        rivalName = new Rectangle(x_second, y, w, h);
        
        int x_third = WIDTH / 2;
        int y_third = HEIGHT / 2;
        int w1 = 200;
        int h1 = 80;
        winnerName = new Rectangle(x_third, y_third, w1, h1);
                
        fontUser = new Font("TimesRoman", Font.PLAIN, 10);
        fontWinner = new Font("TimesRoman", Font.PLAIN, 30);
        
        this.addMouseListener(menu);
        this.addMouseMotionListener(menu);
        this.setFocusable(true);

    }

    /**
     * initialize all our game objects
     */
    private void initialize() {
        // Initialize Ball object
        ball = new Ball();

        // Initialize paddle objects
        Color leftPaddleColor = new Color(0, 0, 0);
        Color rightPaddleColor = new Color(0, 0, 0);
        leftPaddle = new Paddle(leftPaddleColor, true);
        rightPaddle = new Paddle(rightPaddleColor, false);

        // initialize main menu
        menu = new MainMenu(this);

        this.addKeyListener(new KeyInput(leftPaddle, menu));

    }

    private void canvasSetup() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
    }

    /**
     * Game loop
     */
    @Override
    public void run() {

        this.requestFocus();

        // game timer
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                update();
                delta--;
                draw();
                frames++;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }

        stop();
    }

    /**
     * start the thread and the game
     */
    public synchronized void start() {
        gameThread = new Thread(this);
        gameThread.start(); // start thread
        running = true;
    }

    /**
     * Stop the thread and the game
     */
    public void stop() {
        try {
            gameThread.join(); // waits for this thread to finish
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * draw all the objects in the game panel
     */
    public void draw() {
        // Initialize drawing tools first before drawing

        BufferStrategy buffer = this.getBufferStrategy(); // Blank canvas to draw on

        // control if it is blank or not
        if (buffer == null) {
            this.createBufferStrategy(3); // Creating a Triple Buffer
            /*
                * triple buffering basically means we have 3 different canvases this is used to
                * improve performance but the drawbacks are the more buffers, the more memory
                * needed so if you get like a memory error or something, put 2 instead of 3.
                * 
                * BufferStrategy:
                * https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferStrategy.html
             */

            return;
        }
        // extract drawing tool from the buffers
        Graphics g = buffer.getDrawGraphics();

        /*
            * Graphics is class used to draw rectangles, ovals and all sorts of shapes and
            * pictures so it's a tool used to draw on a buffer
            * 
            * Graphics: https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
         */
        drawBackground(g);

        // draw main menu contents
        if (menu.active) {
            menu.draw(g);
        }
        gamerNameTxt = "PLAYER 1:" + menu.userNameTxtContent;

        // draw ball
        ball.draw(g);
        // draw paddles (score will be drawn with them)
        leftPaddle.draw(g);
        rightPaddle.draw(g);
        //real draw part
        g.dispose(); // Disposes of this graphics context and releases any system resources that it
        buffer.show(); // actually shows the 3 rectangles we drew

    }

  
    private void drawBackground(Graphics g) {

        // green background
        Color table = new Color(0, 128, 0);
        g.setColor(table);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // line in the middle
        g.setColor(Color.white);
        Graphics2D g2d = (Graphics2D) g; 
        Stroke defaultStroke = g2d.getStroke();
        Stroke dashed = new BasicStroke(5);
        
        g.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2); // horizontal line
        
        g2d.setStroke(dashed);
       
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        g.drawLine(0, 0, WIDTH, 0); // top edge
        g.drawLine(0, HEIGHT - 2, WIDTH, HEIGHT - 2); // bottom edge
        g.drawLine(0, 0, 0, HEIGHT); // left edge
        g.drawLine(WIDTH - 2, 0, WIDTH - 2, HEIGHT); // right edge
        
        if (!menu.active) {
            // After connecting to the server
            g.setFont(fontUser);
            
            // draw user name spaces
            g.setColor(Color.white);
            g2d.fill(gamerName);
            g.setColor(Color.white);
            g2d.fill(rivalName);
            
            // draw user names borders
            g.setColor(Color.black);
            g2d.draw(gamerName);
            g2d.draw(rivalName);
            
            int strWidth, strHeight;
            strWidth = g.getFontMetrics(fontUser).stringWidth(gamerNameTxt);
            strHeight = g.getFontMetrics(fontUser).getHeight();

            g.setColor(Color.blue);
            g.drawString(gamerNameTxt, (int) (gamerName.getX() + gamerName.getWidth() / 2 - strWidth / 2),
                    (int) (gamerName.getY() + gamerName.getHeight() / 2 + strHeight / 4));

            strWidth = g.getFontMetrics(fontUser).stringWidth(rivalNameTxt);
            strHeight = g.getFontMetrics(fontUser).getHeight();

            g.setColor(Color.red);
            g.drawString(rivalNameTxt, (int) (rivalName.getX() + rivalName.getWidth() / 2 - strWidth / 2),
                    (int) (rivalName.getY() + rivalName.getHeight() / 2 + strHeight / 4));
            
            if(isGameFinished){
                g.setFont(fontWinner);
                g.setColor(Color.WHITE);
                g2d.fill(winnerName);
                
                int strWidth1, strHeight1;
                strWidth1 = g.getFontMetrics(fontUser).stringWidth(winnerNameTxt);
                strHeight1 = g.getFontMetrics(fontUser).getHeight();

                g.setColor(Color.BLUE);
                g.drawString(winnerNameTxt, (int) (winnerName.getX() + winnerName.getWidth() / 2 - strWidth1 / 2),
                        (int) (winnerName.getY() + winnerName.getHeight() / 2 + strHeight1 / 4));
            }
        }
    }

    /**
     * update settings and move all objects
     */
    public void update() {

        if (!menu.active) {
            if (Client.isOpponentFound) {
                if (isGameFinished == false) {
                    ball.update(leftPaddle, rightPaddle);
                    leftPaddle.update(ball);
                    rightPaddle.update(ball);
                    if (leftPaddle.score > 20) {
                        isGameFinished = true;
                        winnerNameTxt = "YOU WIN!!";
                    } else if (rightPaddle.score > 20) {
                        isGameFinished = true;
                        winnerNameTxt = "WINNER IS "+rivalNameTxt;
                        
                    }
                }
            }
        }
    }

    public static int ensureRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    
    public static int sign(double d) {
        if (d <= 0) {
            return -1;
        }
        return 1;
    }

    /**
     * start of the program
     */
    public static void main(String[] args) {
        new Game();
    }

}
