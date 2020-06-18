package ui;

import msg_handler.MessageReceiver;
import msg_handler.MessageSender;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.button.StandardButtonShaper;
import org.jvnet.substance.skin.SubstanceModerateLookAndFeel;
import org.jvnet.substance.utils.SubstanceConstants.ImageWatermarkKind;
import org.jvnet.substance.watermark.SubstanceImageWatermark;

import util.Config;
import util.NetUtils;
import util.UIUtils;
import bean.User;

/**
 * @author 22591
 */
public class MainFrame extends JFrame implements ActionListener{
    //保存所有打开的聊天框

    private List<TalkFrame> talkFrames ;
    //消息接收器

    private MessageReceiver msgReceiver;

    //在线用户集合

    private ArrayList<User> users;

    //自身的信息
    private User self;


    private JLabel label;
    private JList userList;
    private JButton refresh;

    public MainFrame(){
        talkFrames = new LinkedList<TalkFrame>();
        users = new ArrayList<User>();
        self = new User();
        self.setHostName(NetUtils.getLocalHostName());
        self.setHostAddress(NetUtils.getLocalHostAddress());
        self.setMacAddress(NetUtils.getLocalHostMacAddress());
        //预留，将来通过读取配置文件加载用户的设置，如果未设置则等于loginName
        self.setUserName(Config.LOCAL_USER_NAME_DEFAULT);
        //预留，将来通过读取配置文件加载用户的设置
        self.setRemark("");
        users.add(self);

        initComponents();
        //启动接收消息线程
        msgReceiver = MessageReceiver.getInstance(this);
        new Thread(msgReceiver).start();

        //发送登陆广播消息
        MessageSender.sendLoginBroadcast();

    }
    private void initComponents() {
        userList = new JList(users.toArray());
        userList.setCellRenderer(new MyListCellRenderer());
        label = new JLabel(UIUtils.getLabelString(users.size()));
        userList.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                MyListCellRenderer rd = (MyListCellRenderer)(userList.getCellRenderer());
                rd.setBorder(new LineBorder(Color.red,4,false));
                if(e.getClickCount()==2){   //双击时
                    User user = ((User)userList.getSelectedValue());
                    TalkFrame tf = MainFrame.this.getTalkFrame(user);
                    if(tf==null){
                        TalkFrame newTF = new TalkFrame(user);
                        MainFrame.this.addTalkFrame(newTF);
                        newTF.setVisible(true);
                    }else{
                        tf.setVisible(true);
                    }

                }
            }
        });
        refresh = new JButton("刷新",UIUtils.getImageIcon("images/refresh.png"));
        refresh.setFocusable(false);
        refresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refresh.addActionListener(this);
        JToolBar toolBar = new JToolBar();
        toolBar.setBorderPainted(false);
        toolBar.add(refresh);
        JPanel center = new JPanel(new BorderLayout(0,5));
        center.add(label,BorderLayout.NORTH);
        JScrollPane userScrollPane = new JScrollPane(userList);
        center.add(userScrollPane,BorderLayout.CENTER);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(0,5));
        contentPane.add(toolBar,BorderLayout.NORTH);
        contentPane.add(center,BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                //关闭时，退出软件，此时需发送一个退出广播，通知其他在线用户
                System.out.println("closing...");
                MessageSender.sendLogoutBroadcast();
                System.exit(0);
            }
        });
        this.setTitle("欢迎来到——星宇星寻");
        this.setIconImage(UIUtils.getIconImage("images/app64.png"));
        this.setSize(new Dimension(Config.MAIN_FRAME_WIDTH,Config.MAIN_FRAME_HEIGHT));
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==refresh){
            refresh();
        }
    }

    private void refresh() {
        users.clear();
        users.add(self);
        userList.setListData(users.toArray());
        label.setText(UIUtils.getLabelString(users.size()));
        MessageSender.sendLoginBroadcast();
    }

    /**
     * 判断和hostAddress指定的ip地址的聊天框是否存在
     * @param hostAddress
     * @return
     */
    public boolean hasTalkFrame(String hostAddress){
        Iterator<TalkFrame> it = this.talkFrames.iterator();
        while(it.hasNext()){
            if(it.next().getHostAddress().equals(hostAddress)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据hostAddress返回对应的聊天框
     * @param user
     * @return
     */
    public TalkFrame getTalkFrame(User user){
        Iterator<TalkFrame> it = this.talkFrames.iterator();
        while(it.hasNext()){
            TalkFrame tf = it.next();
            if(tf.getHostAddress().equals(user.getHostAddress())) {
                return tf;
            }
        }
        return null;
    }

    public void addTalkFrame(TalkFrame tf){
        this.talkFrames.add(tf);
    }

    /**
     * 收到用户登陆广播消息时更新在线用户列表,如果用户已存在列表中则忽略
     * @param user
     */
    public void addUser(User user) {
        for(int i=0;i<users.size();i++){
            User u = users.get(i);
            if(u.getHostAddress().equals(user.getHostAddress())){
                System.out.println("该用户已存在");
                return;
            }
        }
        users.add(user);
        userList.setListData(users.toArray());
        label.setText(UIUtils.getLabelString(users.size()));
    }

    /**
     * 收到用户退出广播消息时更新在线用户列表
     */
    public void removeUser(User user) {
        for(int i=0;i<this.users.size();i++){
            User u = this.users.get(i);
            if(u.getHostAddress().equals(user.getHostAddress())){
                users.remove(i);
                break;
            }
        }
        userList.setListData(users.toArray());
        label.setText(UIUtils.getLabelString(users.size()));
    }


    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(new SubstanceModerateLookAndFeel());
            SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
            SubstanceImageWatermark watermark  =   new  SubstanceImageWatermark("images/1.jpg");
            SubstanceImageWatermark.setKind(ImageWatermarkKind.SCREEN_TILE);
            SubstanceLookAndFeel.setCurrentWatermark(watermark);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UIUtils.initGlobalFont(new Font("华文中宋", Font.PLAIN, 14));
        new MainFrame();
    }

}

class MyListCellRenderer extends JLabel implements ListCellRenderer {

    private static final long serialVersionUID = 1L;
    final ImageIcon userIcon = UIUtils.getImageIcon("images/user_48.png");
    Border unselectedBorder = null;
    Border selectedBorder = null;
    public MyListCellRenderer(){
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        User user = (User)value;
        this.setLayout(new BorderLayout());
        setText("<html><font color='blue'>"+user.getUserName()+"("+user.getHostName()+")</font><br/><font color='#A9A9A9'>"+
                user.getHostAddress()+"</font></html>");
        setIcon(userIcon);
        setToolTipText("双击开始聊天");
        if (isSelected) {
            if (selectedBorder == null) {
                selectedBorder = BorderFactory.createMatteBorder(1,1,1,1, Color.blue);
            }
            setBorder(selectedBorder);
            setBackground(Config.SYS_COLOR_SELECTED);
        } else {
            if (unselectedBorder == null) {
                unselectedBorder = BorderFactory.createMatteBorder(1,1,1,1, Color.gray);
            }
            setBorder(unselectedBorder);
            setBackground(Color.white);
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        return this;
    }

}