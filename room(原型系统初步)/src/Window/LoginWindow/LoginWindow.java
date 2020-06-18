package Window.LoginWindow;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Window.FriendListWindow.FriendListWindow;
import Window.MainWindow.MainWindow;


public class LoginWindow extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1586744099236751336L;
	
	JLabel bacgrangd,jan,bi,logo,qq,tu;//gif,最小化，关闭，logo，QQ，头像
	JLabel an1,an2,lie1,lie2;//暗色块|线
	JTextField user;
	JPasswordField pass;
	JPanel bgcolor;//白
	JLabel su1,mi1,ku1,ku2,gou1,gou2;//缩略图
	JLabel text1,text2,text3,text4,text5;//自动登录，记住密码，找回密码，注册账号，登录
	static Point origin = new Point();//变量，用于可拖动窗体
	int a=0,b=0,c=0,d=0;//控制线
	int f=0,g=0,h=0,j=0;//控制√
	JLabel submit,ma;
	
	java.net.URL Url_1 = getClass().getResource("/res/2.gif");
	java.net.URL Url_minize = getClass().getResource("/res/最小化.png");
	java.net.URL Url_close = getClass().getResource("/res/关闭.png");
	java.net.URL Url_logo = getClass().getResource("/res/qq.png");
	java.net.URL Url_head = getClass().getResource("/res/头像.png");
	java.net.URL Url_qq_1 = getClass().getResource("/res/qq (1).png");
	java.net.URL Url_qq_2 = getClass().getResource("/res/qq (2).png");
	java.net.URL Url_pwd = getClass().getResource("/res/密码.png");
	java.net.URL Url_pwd_1 = getClass().getResource("/res/密码 (1).png");
	java.net.URL Url_line_2 = getClass().getResource("/res/直线2.png");
	java.net.URL Url_line_3 = getClass().getResource("/res/直线3.png");
	java.net.URL Url_line_4 = getClass().getResource("/res/直线4.png");
	java.net.URL Url_frame = getClass().getResource("/res/框框.png");
	java.net.URL Url_bingo = getClass().getResource("/res/对勾.png");
	java.net.URL Url_ercode = getClass().getResource("/res/二维码.png");		
	java.net.URL Url_ercode_2 = getClass().getResource("/res/二维码2.png");		
	java.net.URL Url_transparent = getClass().getResource("/res/透明照片.png");	
	
	Socket socket = null;
	private String MyName = null;
	public LoginWindow() {
		setResizable(false);
		ImageIcon ii = new ImageIcon(Url_1);
		ii.setImage(ii.getImage().getScaledInstance(450, 300, Image.SCALE_DEFAULT));
		bacgrangd = new JLabel(new ImageIcon(Url_1));
		jan = new JLabel(new ImageIcon(Url_minize));
		bi = new JLabel(new ImageIcon(Url_close));
		logo = new JLabel(new ImageIcon(Url_logo));
		qq = new JLabel("DL");
		an1 = new JLabel();
		an2 = new JLabel();//暗调
		tu = new JLabel(new ImageIcon(Url_head));
		user = new JTextField();
		pass = new JPasswordField();
		su1 = new JLabel(new ImageIcon(Url_qq_1));
		mi1 = new JLabel(new ImageIcon(Url_pwd));
		lie1 = new JLabel(new ImageIcon(Url_line_2));
		lie2 = new JLabel(new ImageIcon(Url_line_3));
		bgcolor = new JPanel();
		ku1 = new JLabel(new ImageIcon(Url_frame));
		ku2 = new JLabel(new ImageIcon(Url_frame));
		gou1 = new JLabel(new ImageIcon(Url_bingo));
		gou2 = new JLabel(new ImageIcon(Url_bingo));
		text1 = new JLabel("自动登录");
		text2 = new JLabel("记住密码");
		text3 = new JLabel("找回密码");
		text4 = new JLabel("注册账号");
		text5 = new JLabel("登录");
		submit = new JLabel();
		ma = new JLabel(new ImageIcon(Url_ercode));
		
		//位置
		bacgrangd.setBounds(-35, -123, 500, 250);
		jan.setBounds(364, 2, 32, 32);
		bi.setBounds(396, 3, 32, 32);
		logo.setBounds(10, 10, 32, 32);
		qq.setBounds(50, 5, 45, 45); 
		an1.setBounds(361, 0, 35, 35);
		an2.setBounds(395, 0, 35, 35);
		tu.setBounds(170, 80, 90, 85);
		user.setBounds(130, 160, 180, 40);
		pass.setBounds(130, 200, 180, 40);
		su1.setBounds(100, 170, 20, 20);
		mi1.setBounds(100, 210, 20, 20);
		lie1.setBounds(100, 190, 240, 10);
		lie2.setBounds(100, 230, 240, 10);
		bgcolor.setBounds(0, 125, 500, 300);
		ku1.setBounds(100, 250, 20, 20);
		ku2.setBounds(190, 250, 20, 20);
		gou1.setBounds(106, 255, 10, 10);
		gou2.setBounds(196, 255, 10, 10);
		text1.setBounds(125, 250, 80, 20);
		text2.setBounds(215, 250, 80, 20);
		text3.setBounds(288, 250, 80, 20);
		text4.setBounds(15, 300, 80, 20);
		text5.setBounds(206, 285, 80, 20);
		submit.setBounds(100, 280, 242, 35);
		ma.setBounds(385, 290, 30, 30);
		//属性
		qq.setFont(new Font("微软雅黑", 1, 25));
		qq.setForeground(Color.white);
		an1.setBackground(new Color(0,0,0,0.3f));
		an2.setBackground(new Color(0,0,0,0.3f));
		bgcolor.setBackground(new Color(255, 255, 255));
		
		user.setForeground(Color.gray);
		user.setText("DL\u7528\u6237\u540D");
		user.setOpaque(false);//透明背景
		user.setBorder(null);//去掉边框
		user.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		pass.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		pass.setBorder(null);//去掉边框
		
		pass.setOpaque(false);
		pass.setForeground(Color.gray);
		pass.setText("密码");
		pass.setEchoChar((char)0);
		
		text1.setFont(new Font("微软雅黑", 0, 12));
		text2.setFont(new Font("微软雅黑", 0, 12));
		text3.setFont(new Font("微软雅黑", 0, 12));
		text4.setFont(new Font("微软雅黑", 0, 12));
		text5.setFont(new Font("微软雅黑", 0, 15));
		text1.setForeground(new Color(170, 170, 170));
		text2.setForeground(new Color(170, 170, 170));
		text3.setForeground(new Color(170, 170, 170));
		text4.setForeground(new Color(170, 170, 170));
		text5.setForeground(Color.white);
		
		gou1.setVisible(false);
		gou2.setVisible(false);
		
		submit.setBackground(new Color(5, 186, 251));
		submit.setOpaque(true);
		
		text3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		text4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		
		//事件区域
		jan.addMouseListener(this);
		bi.addMouseListener(this);
		user.addMouseListener(this);
		pass.addMouseListener(this);
		text1.addMouseListener(this);
		text2.addMouseListener(this);
		text3.addMouseListener(this);
		text4.addMouseListener(this);
		ku1.addMouseListener(this);
		ku2.addMouseListener(this);
		submit.addMouseListener(this);
		ma.addMouseListener(this);
		this.addMouseListener(this);
		
		
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				
			}
			public void mouseDragged(MouseEvent e) {
				Point p = getLocation(); 
				setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
			}
		});
		
		user.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				su1.setIcon(new javax.swing.ImageIcon(Url_qq_1));
				lie1.setIcon(new javax.swing.ImageIcon(Url_line_2));
				c=0;
				if(user.getText().isEmpty()) {
					user.setForeground(Color.gray);
					user.setText("DL用户名");
			}
		}
		
		public void focusGained(FocusEvent e) {
			user.setForeground(Color.black);
			lie1.setIcon(new javax.swing.ImageIcon(Url_line_3));
			a=1;c=1;b=0;
			su1.setIcon(new javax.swing.ImageIcon(Url_qq_2));
			if(user.getText().equals("DL用户名")) {
				user.setText("");
			}else {
				user.setText(user.getText());
				user.selectAll();
			}
			}
		});
		
		pass.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {//失去焦点
				lie2.setIcon(new javax.swing.ImageIcon(Url_line_2));//失去焦点换图片
				mi1.setIcon(new javax.swing.ImageIcon(Url_pwd));
				d=0;
			if(String.valueOf(pass.getPassword()).isEmpty()) {
				pass.setForeground(Color.gray);
				pass.setText("密码");
				pass.setEchoChar((char)0);//让密码显示出来
			}
		}
		
		
		public void focusGained(FocusEvent e) {//得到焦点
			mi1.setIcon(new javax.swing.ImageIcon(Url_pwd_1));
			lie2.setIcon(new javax.swing.ImageIcon(Url_line_3));
			b=1;a=0;d=1;
			pass.setForeground(Color.black);
			pass.setEchoChar('*');//让用户输入看不见
			if(String.valueOf(pass.getPassword()).equals("密码")) {
				pass.setText("");
			}else {
				pass.setText(String.valueOf(pass.getPassword()));
			}
			}
		});
		
		pass.addKeyListener(new KeyListener() {

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
					text5.setFont(new Font("微软雅黑", 0, 14));
					dispose();
				
					String users = user.getText();
					String password = String.valueOf(pass.getPassword());
					
					if(users.equals("admin")&&password.equals("666666")) {
						MyName = users;
						try {
					        socket = new Socket("127.0.0.1", 8888);
					        System.out.println("已经连上服务器了");
					    } catch (Exception e1) {
					        e1.printStackTrace();
					    }
				        dispose();
						new FriendListWindow(MyName, socket).setVisible(true);
					}else {
						System.out.print(users + password);
						JOptionPane.showMessageDialog(null, "Username doesn't match with password！");
						new LoginWindow();
					}
				}
			}
			
		});
			getContentPane().setLayout(null);//布局
			
			getContentPane().add(jan);
			getContentPane().add(bi);
			getContentPane().add(qq);
			getContentPane().add(logo);
			getContentPane().add(an1);
			getContentPane().add(an2);
			getContentPane().add(tu);
			getContentPane().add(lie1);
			getContentPane().add(lie2);
			getContentPane().add(user);
			getContentPane().add(pass);
			getContentPane().add(su1);
			getContentPane().add(mi1);
			getContentPane().add(gou1);
			getContentPane().add(gou2);
			getContentPane().add(ku1);
			getContentPane().add(ku2);
			getContentPane().add(text1);
			getContentPane().add(text2);
			getContentPane().add(text3);
			getContentPane().add(text4);
			getContentPane().add(text5);
			getContentPane().add(submit);
			getContentPane().add(ma);
			getContentPane().add(bgcolor);
			getContentPane().add(bacgrangd);
			
			this.setSize(430, 330);
			this.setIconImage(Toolkit.getDefaultToolkit().createImage(Url_transparent));//窗体图标
			this.setLocationRelativeTo(null);//保持居中
			this.setUndecorated(true);//去顶部
			this.setFocusable(true);//面板首先获得焦点
			this.setBackground(new Color(255,255,255));//背景颜色
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setAlwaysOnTop(true);//最顶层
			this.setVisible(true);//显示
			
		}
		
		
		public static void main(String[] args) {
		
			new LoginWindow();
		
		}
		
		public void mouseClicked(MouseEvent e) {//点击不恢复
		}
		
		public void mousePressed(MouseEvent e) {//点击后
			if (e.getSource() == jan) {
				setExtendedState(JFrame.ICONIFIED);
			}else if(e.getSource()== this) {
				origin.x = e.getX(); 
				origin.y = e.getY();
			}else if(e.getSource()==bi) {
				System.exit(0);
			}else if(e.getSource()==ku1||e.getSource()==text1) {
				if(f==0) {
					gou1.setVisible(true);
					g=1;f=1;
				}else if(g==1) {
					gou1.setVisible(false);
					f=0;g=0;
				}
			}else if(e.getSource()==ku2||e.getSource()==text2) {
				if(h==0) {
					gou2.setVisible(true);
					j=1;h=1;
				}else if(j==1) {
					gou2.setVisible(false);
					h=0;j=0;
				}
			}else if(e.getSource()==submit||e.getSource()==text5) {
				text5.setFont(new Font("微软雅黑", 0, 14));
				dispose();
			
				String users = user.getText();
				String password = String.valueOf(pass.getPassword());
				
				
				//数据库连接
				if((users.equals("king")&&password.equals("666666")) || (users.equals("sun")) && password.equals("111111")) {
					//连接上服务器
					MyName = users;
					try {
				        socket = new Socket("127.0.0.1", 8888);
				        PrintWriter out = new PrintWriter(socket.getOutputStream());
				        out.println(Constants.Constant.Login_Signal + MyName);
				        out.flush();
				        System.out.println("已经连上服务器了");
				    } catch (Exception e1) {
				        e1.printStackTrace();
				    }
			        dispose();
					new FriendListWindow(MyName, socket).setVisible(true);
				}else {
					System.out.print(users + password);
					JOptionPane.showMessageDialog(null, "Username doesn't match with password！");
					new LoginWindow();
				}
			}
		}
		
		public void mouseReleased(MouseEvent e) {//点击时
			if(e.getSource()==submit||e.getSource()==text5) {
				text5.setFont(new Font("微软雅黑", 0, 15));
			}
		}
		
		public void mouseEntered(MouseEvent e) {//悬停
			if (e.getSource() == jan) {
				an1.setOpaque(true);
			}else if(e.getSource()==bi) {
				an2.setOpaque(true);
			}else if(e.getSource()==user) {
				if(a==0&&c==0) {
					lie1.setIcon(new javax.swing.ImageIcon(Url_line_4));
				}
			}else if(e.getSource()==pass) {
				if(b==0&&d==0) {
					lie2.setIcon(new javax.swing.ImageIcon(Url_line_4));
				}
			}else if(e.getSource()==text3) {
				text3.setForeground(Color.GRAY);
			}else if(e.getSource()==text4) {
				text4.setForeground(Color.GRAY);
			}else if(e.getSource()==ma) {
				ma.setIcon(new javax.swing.ImageIcon(Url_ercode_2));
			} 
		}
		
		public void mouseExited(MouseEvent e) {//悬停后
			if (e.getSource() == jan) {
				an1.setOpaque(false);
			}else if(e.getSource()==bi) {
				an2.setOpaque(false);
			}else if(e.getSource()==user) {
				if(a==0) {
					lie1.setIcon(new javax.swing.ImageIcon(Url_line_2));
				}
			}else if(e.getSource()==pass) {
				if(b==0) {
					lie2.setIcon(new javax.swing.ImageIcon(Url_line_2));
				}
			}else if(e.getSource()==text3) {
				text3.setForeground(new Color(170, 170, 170));
			}else if(e.getSource()==text4) {
				text4.setForeground(new Color(170, 170, 170));
			}else if(e.getSource()==ma) {
				ma.setIcon(new javax.swing.ImageIcon(Url_ercode));
			}
		}

}