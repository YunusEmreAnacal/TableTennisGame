package PingPong;


import Message.Message;
import Client.Client;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
/**
 * window class
 * 
 * @author Yunus Emre ANAÇAL
 *
 */
public class Window extends JFrame implements WindowListener{

	/**
	 * Create the frame.
	 * 
	 */
	public Window(String title, Game game) {
		JFrame frame = new JFrame(title);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		add(game); 
		pack();
                
		setLocationRelativeTo(null); 
                
		setVisible(true);
                
                addWindowListener(this);
	}
        

    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("Window Opened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        
        System.out.println("Window Closed");
        if(Client.socket != null){
            Message msg = new Message(Message.Message_Type.ExitServer);
            msg.content = "exited";
            Client.Send(msg);
            System.out.println("Connection disconnected : "+ Client.socket.isClosed());
            Client.Stop();
        }
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }

}
