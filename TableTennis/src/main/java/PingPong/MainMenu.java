package PingPong;

import Client.Client;
import Message.Message;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * the main menu at the start of the game
 *
 * @author Yunus Emre ANAÇAL
 *
 */
public class MainMenu extends MouseAdapter {

    private final int PORT_NUMBER = 5000;

    public static boolean active; // true if main menu is displaying

    // Play button
    private Rectangle playBtn;
    private String playTxt = "Play";
    private boolean pHL = false; 

    // Quit button
    private Rectangle quitBtn;
    private String quitTxt = "Quit";
    private boolean qHL = false;

    //Player Name Label
    private Rectangle playerNameLbl;
    private String playerNameLblTxt = "Player Name:";
    private boolean lHighlight = false;

    //Player Name Label
    private Rectangle userNameTxt;
    public String userNameTxtContent = "";
    private boolean tHighlight = false;

    private boolean getUserName = false;
    private Font font;

  
    public MainMenu(Game game) {

        active = true;
        game.start();

        // position and dimensions
        int x, y, w, h;

        w = 400;
        h = 100;

        y = Game.HEIGHT / 2 + h / 2;

        // First Button (Play)
        x = Game.WIDTH / 2 - w / 2;
        y = Game.HEIGHT / 2 - h / 2;
        playBtn = new Rectangle(x, y, w, h);
        
        // Second Button (Quit)
        x = Game.WIDTH / 2 - w / 2;
        y = Game.HEIGHT / 2 + h / 2 + 20; // Add a vertical offset for spacing
        quitBtn = new Rectangle(x, y, w, h);

        // Label for Player Name
        x = Game.WIDTH / 4 - w / 2;
        y = Game.HEIGHT / 4 - h / 2;
        playerNameLbl = new Rectangle(x, y, w, h);

        // Text Field for User Name
        x = Game.WIDTH * 3 / 4 - w / 2;
        y = Game.HEIGHT / 4 - h / 2;
        userNameTxt = new Rectangle(x, y, w, h);
        font = new Font("TimesRoman", Font.PLAIN, 50);

    }

   
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(font);

        // draw buttons
        Color buttonColor = new Color(0, 0, 0);
        Color highLightColor = new Color(0, 0, 0);
        Color labelColor = new Color(0, 0, 0);

        g.setColor(buttonColor);
        if (pHL) {
            g.setColor(highLightColor);
        }
        g2d.fill(playBtn);

        g.setColor(buttonColor);
        if (qHL) {
            g.setColor(highLightColor);
        }
        g2d.fill(quitBtn);
        // draw labels
        g.setColor(labelColor);
        g2d.fill(playerNameLbl);

        g.setColor(labelColor);
        g2d.fill(userNameTxt);

        // draw borders
        g.setColor(Color.white);
        g2d.draw(playBtn);
        g2d.draw(quitBtn);
        g2d.draw(playerNameLbl);
        g2d.draw(userNameTxt);

        // draw text in buttons
        int strWidth, strHeight;

        // Play Button text
        strWidth = g.getFontMetrics(font).stringWidth(playTxt);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.blue);
        g.drawString(playTxt, (int) (playBtn.getX() + playBtn.getWidth() / 2 - strWidth / 2),
                (int) (playBtn.getY() + playBtn.getHeight() / 2 + strHeight / 4));

        // Quit Button text
        strWidth = g.getFontMetrics(font).stringWidth(quitTxt);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.red);
        g.drawString(quitTxt, (int) (quitBtn.getX() + quitBtn.getWidth() / 2 - strWidth / 2),
                (int) (quitBtn.getY() + quitBtn.getHeight() / 2 + strHeight / 4));

        // User Name Label text
        strWidth = g.getFontMetrics(font).stringWidth(playerNameLblTxt);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.WHITE);
        g.drawString(playerNameLblTxt, (int) (playerNameLbl.getX() + playerNameLbl.getWidth() / 2 - strWidth / 2),
                (int) (playerNameLbl.getY() + playerNameLbl.getHeight() / 2 + strHeight / 4));

        // User Name Text Content
        strWidth = g.getFontMetrics(font).stringWidth(userNameTxtContent);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.WHITE);
        g.drawString(userNameTxtContent, (int) (userNameTxt.getX() + userNameTxt.getWidth() / 2 - strWidth / 2),
                (int) (userNameTxt.getY() + userNameTxt.getHeight() / 2 + strHeight / 4));

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Point p = e.getPoint();

        if (playBtn.contains(p)) {
            active = false;
            Client.Start("127.0.0.1", PORT_NUMBER);
            Message msg = new Message(Message.Message_Type.JoinServer);
            msg.content = userNameTxtContent;
            Client.Send(msg);
        } else if (quitBtn.contains(p)) {
            System.exit(0);
        } else if (userNameTxt.contains(p)) {
            getUserName = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        Point point = e.getPoint();
        // determine if mouse is hovering inside one of the buttons
        pHL = playBtn.contains(point);
        qHL = quitBtn.contains(point);

    }

}
