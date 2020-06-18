package net.Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.activation.CommandInfo;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import Window.FriendListWindow.ImageCellRender;
import Window.FriendListWindow.ImageList;

import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;


public class server extends JFrame {

	private JPanel contentPane;
	static String temp1 = "/ + username + command";
	static String temp2 = "默认为8888";
	static int port = 8888;
	static HashMap<String,Socket> Name_Socket = new HashMap<String,Socket>();
	//Socket s_socket = null;
	//ServerSocket serverSocket = null;
	  
	JTextField ip_text;
	static JTextField port_text;
	static JTextArea user_info;
	JList<File> list;
	JTextArea command_area;
	JTextField command;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					server frame = new server();
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
	public server() {
		setResizable(false);
		setTitle("                                                                                          DL Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 852, 777);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.setLayout(new BorderLayout());
		
		JPanel north = new JPanel();
		north.setBorder(BorderFactory.createLineBorder(Color.CYAN, 5, true));
		north.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));
		JLabel ip_label = new JLabel("ip address:");
		ip_text = new JTextField(20);
		ip_text.setText("127.0.0.1");
		ip_text.setHorizontalAlignment(JTextField.CENTER);
		ip_text.setEditable(false);
		JLabel port_label = new JLabel("port number:");
		port_text = new JTextField(20);
		port_text.setText(temp2);
		port_text.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(port_text.getText().equals((String)temp2)){
					port_text.setText(null);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(port_text.getText().isEmpty()) {
					port_text.setText(temp2);
				}
			}
			
		});
		JButton start = new JButton("Start!");
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// TODO Auto-generated method stub
				start s = new start();
				Thread th = new Thread(s);
				th.start();
			}
		});
		north.add(ip_label);
		north.add(ip_text);
		north.add(port_label);
		north.add(port_text);
		north.add(start);
		
		user_info = new JTextArea(25,55);
		user_info.setBorder(BorderFactory.createTitledBorder("用户指令"));
		user_info.setEditable(false);
		list = new JList<File>();
		ImageList listModel = new ImageList();		
		File[] files = new File("D:\\ChatRoom\\room\\bin\\res\\head".trim()).listFiles(new FileFilter(){
				public boolean accept(File file){
					return file.getName().endsWith("jpg");
				}
		});
		for(File file : files){
			listModel.addElement(file);
		}
		list.setModel(listModel);
		list.setCellRenderer(new ImageCellRender());
		JScrollPane left = new JScrollPane();
		left.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
		left.setViewportView(user_info);
		JScrollPane right = new JScrollPane(list);
		
		JPanel south = new JPanel();
		south.setLayout(new BorderLayout());
		command_area = new JTextArea(5,0);
		command_area.setEditable(false);
		command_area.setBorder(BorderFactory.createTitledBorder("操作用户"));
		JScrollPane ca = new JScrollPane(command_area);
		south.add(ca,BorderLayout.NORTH);
		command = new JTextField(30);
		south.add(command,BorderLayout.SOUTH);
		command.setText(temp1);
		command.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(command.getText().equals((String)temp1)){
					command.setText(null);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(command.getText().isEmpty()) {
					command.setText(temp1);
				}
			}
			
		});
		command.addKeyListener(new KeyListener() {

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
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					if(!(command.getText().isEmpty())) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						command_area.append(command.getText() + "\t\t\t\t" + df.format(new Date()) + "\r\n");
						command.setText(null);
						//对某用户具体操作
					}
				}
			}
			
		});
		south.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
		
		this.add(left,BorderLayout.WEST);
		this.add(right,BorderLayout.EAST);
		this.add(north,BorderLayout.NORTH);
		this.add(south,BorderLayout.SOUTH);
	}
}
class SServer implements Runnable {// 服务端
	  static List<Socket> socketList=new ArrayList<Socket>();
	//读取 In
	  static Socket socket = null;
	  static ServerSocket serverSocket = null;
	  public SServer() {// 构造方法
	      try {
	          String temp = server.port_text.getText();
	    	  if((!temp.isEmpty()) && (!temp.equals((String)server.temp2))) {
	    		  server.port = Integer.parseInt(temp);
	    	  }
	          serverSocket = new ServerSocket(server.port);
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	  }
	  @Override
	  public void run() {
	      try {
	          Thread.sleep(1000);
	          BufferedReader in = new BufferedReader(new InputStreamReader(socket
	                  .getInputStream()));
	          while (true) {
	              String receive = in.readLine();
	              server.user_info.append(receive + "\r\n");
	              //处理登录信息
	              if(receive.startsWith(Constants.Constant.Login_Signal)) {
	            	  String[] Signal_Name = receive.split("###");
	            	  if(Signal_Name != null && Signal_Name.length != 0) {
	            		  if(!server.Name_Socket.containsKey(Signal_Name[1])){
	            			  server.Name_Socket.put(Signal_Name[1], socket);
	            		  }
	            	  }
	              }else {
		              //切割信息串
//	            	  for (int i = 0; i < socketList.size(); i++) {
//		                  Socket socket=socketList.get(i);
//		                  PrintWriter out = new PrintWriter(socket.getOutputStream());
//		                  if (socket!=this.socket) {
//		                      out.println(receive);
//		                  }else{
//		                      out.println("(你)"+receive);
//		                  }
//		                  out.flush();
//		              }
	            	  String[] from_msg_to = receive.split("####");
	            	  if(from_msg_to != null && from_msg_to.length != 0) {
	            		  if(server.Name_Socket.containsKey(from_msg_to[2])) {
	            			  Socket ss = server.Name_Socket.get(from_msg_to[2]);
	            			  PrintWriter out = new PrintWriter(ss.getOutputStream());
	            			  out.println(from_msg_to[1]);
	            			  out.flush();
	            		  }
	            	  }
	              } 	 
	          }
	      } catch (Exception e) {

	          e.printStackTrace();
	      }
	  }
	}
class Print implements Runnable {
	  static List<Socket> socketList=new ArrayList<Socket>();
	  Scanner input = new Scanner(System.in);
	  public Print(Socket s) {
	      try {
	          socketList.add(s);
	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	  }
	  @Override
	  public void run() {
	      try {
	          Thread.sleep(1000);
	          while (true) {
	              String msg = input.next();
	          for (int i = 0; i < socketList.size(); i++) {
	              Socket socket=socketList.get(i);
	              PrintWriter out = new PrintWriter(socket.getOutputStream());
	              System.out.println("对客户端说：");
	              out.println("服务端说："+msg);
	              out.flush();
	          }
	          }
	      } catch (Exception e) {
	          // TODO: handle exception
	          e.printStackTrace();
	      }
	  }
}
class start implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
	    server.user_info.append("************服务端*************\r\n");
	    SServer t = new SServer();
	    int count = 0;
	    while (true) {          
	        try {
	            SServer.socket = SServer.serverSocket.accept();
	            count++;
	            server.user_info.append("第" + count + "个客户已连接\r\n");
	            SServer.socketList.add(SServer.socket);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        Print p = new Print(SServer.socket);
	        Thread read = new Thread(t);
	        Thread print = new Thread(p);
	        read.start();
	        print.start();
	    }
	}
}



