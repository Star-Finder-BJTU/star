package Window.MainWindow;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7960264103967188915L;
	////发送消息（文本#时间）
	private JPanel contentPane;
	static String MyName;
	static String to_usr;
	static Socket socket;
	static JTextArea SenText;
	static JTextArea jta;
	static String last = "";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow(null,null, null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow(String to,String Name,Socket s) {
		setResizable(false);
		to_usr = to;
		MyName = Name;
		socket = s;
		setTitle("                                                     "+to_usr);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 815, 778);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JToolBar QuickOp = new JToolBar();
		QuickOp.setBounds(10, 527, 592, 33);
		contentPane.add(QuickOp);
		
		JScrollPane SenJsp = new JScrollPane();
		SenJsp.setBounds(10, 565, 592, 164);
		SenJsp.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
		contentPane.add(SenJsp);
		
		SenText = new JTextArea();
		SenJsp.setViewportView(SenText);
		
		
		JPanel Self = new JPanel();
		Self.setBounds(612, 62, 177, 667);
		Self.setBorder(BorderFactory.createLineBorder(Color.CYAN,5));
		contentPane.add(Self);
		
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(10, 10, 779, 43);
		contentPane.add(toolBar);
		
		jta = new JTextArea();
		JScrollPane RecJsp = new JScrollPane(jta);
		jta.setEditable(false);
		RecJsp.setBorder(BorderFactory.createLineBorder(Color.GREEN,4));
		RecJsp.setBounds(10, 62, 592, 454);
		contentPane.add(RecJsp);
		
		SenText.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyChar() == KeyEvent.VK_ENTER && e.isControlDown() && !SenText.getText().isEmpty()) {
					start Client = new start();
					Thread t = new Thread(Client);
					t.start();
				}
			}
			
		});
	}
}
class start implements Runnable {

	@Override
	public void run() {
		CClient t = new CClient();
	    Read r = new Read(MainWindow.socket);
	    Thread print = new Thread(t);
	    Thread read = new Thread(r);
	    print.start();
	    read.start();
	}
	
}
class CClient implements Runnable {// 客户端
	@Override
	public void run() {
		try {
	        Thread.sleep(1000);         
	        PrintWriter out = new PrintWriter(MainWindow.socket.getOutputStream());
	        while (!MainWindow.SenText.getText().isEmpty() && !MainWindow.SenText.getText().equals((String)MainWindow.last)) {
	            String msg = MainWindow.SenText.getText();
	            out.println(MainWindow.MyName + "####" + msg + "####" + MainWindow.to_usr);
	            out.flush();
	            MainWindow.SenText.setText(null);
	            MainWindow.last = msg;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
class Read implements Runnable {
	static Socket socket = null;
	public Read(Socket socket) {
	    this.socket = socket;
	}
	@Override
	public void run() {
	    try {
	        Thread.sleep(1000);
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket
	                .getInputStream()));
	        while (true) {
	            MainWindow.jta.append(in.readLine() + "\r\n");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
