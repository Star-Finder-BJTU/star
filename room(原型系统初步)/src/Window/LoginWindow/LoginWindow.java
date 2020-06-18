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
	
	JLabel bacgrangd,jan,bi,logo,qq,tu;//gif,��С�����رգ�logo��QQ��ͷ��
	JLabel an1,an2,lie1,lie2;//��ɫ��|��
	JTextField user;
	JPasswordField pass;
	JPanel bgcolor;//��
	JLabel su1,mi1,ku1,ku2,gou1,gou2;//����ͼ
	JLabel text1,text2,text3,text4,text5;//�Զ���¼����ס���룬�һ����룬ע���˺ţ���¼
	static Point origin = new Point();//���������ڿ��϶�����
	int a=0,b=0,c=0,d=0;//������
	int f=0,g=0,h=0,j=0;//���ơ�
	JLabel submit,ma;
	
	java.net.URL Url_1 = getClass().getResource("/res/2.gif");
	java.net.URL Url_minize = getClass().getResource("/res/��С��.png");
	java.net.URL Url_close = getClass().getResource("/res/�ر�.png");
	java.net.URL Url_logo = getClass().getResource("/res/qq.png");
	java.net.URL Url_head = getClass().getResource("/res/ͷ��.png");
	java.net.URL Url_qq_1 = getClass().getResource("/res/qq (1).png");
	java.net.URL Url_qq_2 = getClass().getResource("/res/qq (2).png");
	java.net.URL Url_pwd = getClass().getResource("/res/����.png");
	java.net.URL Url_pwd_1 = getClass().getResource("/res/���� (1).png");
	java.net.URL Url_line_2 = getClass().getResource("/res/ֱ��2.png");
	java.net.URL Url_line_3 = getClass().getResource("/res/ֱ��3.png");
	java.net.URL Url_line_4 = getClass().getResource("/res/ֱ��4.png");
	java.net.URL Url_frame = getClass().getResource("/res/���.png");
	java.net.URL Url_bingo = getClass().getResource("/res/�Թ�.png");
	java.net.URL Url_ercode = getClass().getResource("/res/��ά��.png");		
	java.net.URL Url_ercode_2 = getClass().getResource("/res/��ά��2.png");		
	java.net.URL Url_transparent = getClass().getResource("/res/͸����Ƭ.png");	
	
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
		an2 = new JLabel();//����
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
		text1 = new JLabel("�Զ���¼");
		text2 = new JLabel("��ס����");
		text3 = new JLabel("�һ�����");
		text4 = new JLabel("ע���˺�");
		text5 = new JLabel("��¼");
		submit = new JLabel();
		ma = new JLabel(new ImageIcon(Url_ercode));
		
		//λ��
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
		//����
		qq.setFont(new Font("΢���ź�", 1, 25));
		qq.setForeground(Color.white);
		an1.setBackground(new Color(0,0,0,0.3f));
		an2.setBackground(new Color(0,0,0,0.3f));
		bgcolor.setBackground(new Color(255, 255, 255));
		
		user.setForeground(Color.gray);
		user.setText("DL\u7528\u6237\u540D");
		user.setOpaque(false);//͸������
		user.setBorder(null);//ȥ���߿�
		user.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		pass.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		pass.setBorder(null);//ȥ���߿�
		
		pass.setOpaque(false);
		pass.setForeground(Color.gray);
		pass.setText("����");
		pass.setEchoChar((char)0);
		
		text1.setFont(new Font("΢���ź�", 0, 12));
		text2.setFont(new Font("΢���ź�", 0, 12));
		text3.setFont(new Font("΢���ź�", 0, 12));
		text4.setFont(new Font("΢���ź�", 0, 12));
		text5.setFont(new Font("΢���ź�", 0, 15));
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
		
		
		//�¼�����
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
					user.setText("DL�û���");
			}
		}
		
		public void focusGained(FocusEvent e) {
			user.setForeground(Color.black);
			lie1.setIcon(new javax.swing.ImageIcon(Url_line_3));
			a=1;c=1;b=0;
			su1.setIcon(new javax.swing.ImageIcon(Url_qq_2));
			if(user.getText().equals("DL�û���")) {
				user.setText("");
			}else {
				user.setText(user.getText());
				user.selectAll();
			}
			}
		});
		
		pass.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {//ʧȥ����
				lie2.setIcon(new javax.swing.ImageIcon(Url_line_2));//ʧȥ���㻻ͼƬ
				mi1.setIcon(new javax.swing.ImageIcon(Url_pwd));
				d=0;
			if(String.valueOf(pass.getPassword()).isEmpty()) {
				pass.setForeground(Color.gray);
				pass.setText("����");
				pass.setEchoChar((char)0);//��������ʾ����
			}
		}
		
		
		public void focusGained(FocusEvent e) {//�õ�����
			mi1.setIcon(new javax.swing.ImageIcon(Url_pwd_1));
			lie2.setIcon(new javax.swing.ImageIcon(Url_line_3));
			b=1;a=0;d=1;
			pass.setForeground(Color.black);
			pass.setEchoChar('*');//���û����뿴����
			if(String.valueOf(pass.getPassword()).equals("����")) {
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
					text5.setFont(new Font("΢���ź�", 0, 14));
					dispose();
				
					String users = user.getText();
					String password = String.valueOf(pass.getPassword());
					
					if(users.equals("admin")&&password.equals("666666")) {
						MyName = users;
						try {
					        socket = new Socket("127.0.0.1", 8888);
					        System.out.println("�Ѿ����Ϸ�������");
					    } catch (Exception e1) {
					        e1.printStackTrace();
					    }
				        dispose();
						new FriendListWindow(MyName, socket).setVisible(true);
					}else {
						System.out.print(users + password);
						JOptionPane.showMessageDialog(null, "Username doesn't match with password��");
						new LoginWindow();
					}
				}
			}
			
		});
			getContentPane().setLayout(null);//����
			
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
			this.setIconImage(Toolkit.getDefaultToolkit().createImage(Url_transparent));//����ͼ��
			this.setLocationRelativeTo(null);//���־���
			this.setUndecorated(true);//ȥ����
			this.setFocusable(true);//������Ȼ�ý���
			this.setBackground(new Color(255,255,255));//������ɫ
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setAlwaysOnTop(true);//���
			this.setVisible(true);//��ʾ
			
		}
		
		
		public static void main(String[] args) {
		
			new LoginWindow();
		
		}
		
		public void mouseClicked(MouseEvent e) {//������ָ�
		}
		
		public void mousePressed(MouseEvent e) {//�����
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
				text5.setFont(new Font("΢���ź�", 0, 14));
				dispose();
			
				String users = user.getText();
				String password = String.valueOf(pass.getPassword());
				
				
				//���ݿ�����
				if((users.equals("king")&&password.equals("666666")) || (users.equals("sun")) && password.equals("111111")) {
					//�����Ϸ�����
					MyName = users;
					try {
				        socket = new Socket("127.0.0.1", 8888);
				        PrintWriter out = new PrintWriter(socket.getOutputStream());
				        out.println(Constants.Constant.Login_Signal + MyName);
				        out.flush();
				        System.out.println("�Ѿ����Ϸ�������");
				    } catch (Exception e1) {
				        e1.printStackTrace();
				    }
			        dispose();
					new FriendListWindow(MyName, socket).setVisible(true);
				}else {
					System.out.print(users + password);
					JOptionPane.showMessageDialog(null, "Username doesn't match with password��");
					new LoginWindow();
				}
			}
		}
		
		public void mouseReleased(MouseEvent e) {//���ʱ
			if(e.getSource()==submit||e.getSource()==text5) {
				text5.setFont(new Font("΢���ź�", 0, 15));
			}
		}
		
		public void mouseEntered(MouseEvent e) {//��ͣ
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
		
		public void mouseExited(MouseEvent e) {//��ͣ��
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