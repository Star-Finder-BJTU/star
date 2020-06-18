package ui;

import msg_handler.MessageSender;
import voice_handler.VoiceReceiver;
import voice_handler.VoiceSender;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import p2p.FileClient;
import p2p.FileServer;
import util.BasicUtils;
import util.Config;
import util.DBUtils;
import util.SysUtils;
import util.UIUtils;
import bean.Message;
import bean.User;


/**
 * @author 22591
 */
public class TalkFrame extends JFrame implements ActionListener{
    private User user; //对方用户对象
    private String savePath; // 全部保存时用户选择的路径
    private String fileNames[]; //接收文件的文件名数组
    private long fileLengths[]; //接收文件的长度数组
    private FileServer server; //传送文件时的TCP服务器
    private boolean filePanelOpen = false;
    private boolean voiceTalkFlag = false;


    /*分割面板，左边为主面板（聊天面板），右边为文件传送面板，
       默认右边面板隐藏，发送文件时才显示*/
    private JSplitPane jsp;
    private JPanel mainPanel;  // 主面板（聊天面板）
    private FileSelectPanel filePanel;  // 文件传送面板

    private JButton sendBtn; //发送聊天内容按钮
    private JButton cancelBtn; //取消按钮
    private JButton openFilePaneBtn; //打开传送文件面板的按钮
    private JButton shakeBtn; //抖动按钮
    private JButton voiceBtn; //语音聊天

    private JTextArea msgToSendTA; //要发送的内容文本域
    private JScrollPane msgRecordSP;
    private JScrollPane msgToSendSP;

    private JTextPane textPane;
    private JPopupMenu menu;
    private JMenuItem menuItemClear;

    public TalkFrame(User user){
        this.user = user;
        initComponents();
    }

    private void initComponents() {
        this.setIconImage(UIUtils.getIconImage("images/user_24.png"));
        this.setTitle(user.getUserName()+"("+user.getHostName()+")");
        this.setSize(new Dimension(Config.TALK_FRAME_WIDTH,Config.TALK_FRAME_HEIGHT));

        JToolBar toolBar = new JToolBar();
        toolBar.setBorderPainted(false);
        openFilePaneBtn = new JButton("传送文件",UIUtils.getImageIcon("images/file24.png"));
        openFilePaneBtn.setFocusable(false);
        openFilePaneBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openFilePaneBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(openFilePaneBtn);
        voiceBtn = new JButton("语音聊天",UIUtils.getImageIcon("images/mic2_24.png"));
        voiceBtn.setFocusable(false);
        voiceBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        voiceBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(voiceBtn);
        textPane = new JTextPane();
        textPane.setEditable(false);
        msgRecordSP = new JScrollPane(textPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        msgToSendTA = new JTextArea();
        msgToSendTA.setRows(5);
        msgToSendTA.setLineWrap(true);
        msgToSendTA.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    sendTalkMsg();
                }
            }
        });
        msgToSendSP = new JScrollPane(msgToSendTA,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel south = new JPanel(new BorderLayout());
        JPanel sthBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sendBtn = new JButton("发送",UIUtils.getImageIcon("images/send.png"));
        cancelBtn = new JButton("关闭",UIUtils.getImageIcon("images/cancel.png"));
        sthBtnPanel.add(sendBtn);
        sthBtnPanel.add(cancelBtn);
        JToolBar toolBar2 = new JToolBar();
        toolBar2.setBorderPainted(false);
        shakeBtn = new JButton(UIUtils.getImageIcon("images/bell24.png"));
        shakeBtn.setToolTipText("抖动");
        shakeBtn.setFocusable(false);

        toolBar2.add(shakeBtn);
        south.add(toolBar2,BorderLayout.NORTH);
        south.add(msgToSendSP,BorderLayout.CENTER);
        south.add(sthBtnPanel,BorderLayout.SOUTH);

        mainPanel = new JPanel(new BorderLayout(0,2));
        mainPanel.add(msgRecordSP, BorderLayout.CENTER);
        mainPanel.add(south, BorderLayout.SOUTH);

        filePanel = new FileSelectPanel(this);
        jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,mainPanel,filePanel);
        jsp.setDividerLocation(Config.TALK_FRAME_WIDTH);
        jsp.setDividerSize(5);
        jsp.setOneTouchExpandable(false);
        jsp.setEnabled(false); //设置分割线不能移动

        menu = new JPopupMenu();
        menuItemClear = new JMenuItem("清屏",UIUtils.getImageIcon("images/book_go.png"));
        menu.add(menuItemClear);
        textPane.setComponentPopupMenu(menu);
        menuItemClear.addActionListener(this);

        openFilePaneBtn.addActionListener(this);
        sendBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        shakeBtn.addActionListener(this);
        voiceBtn.addActionListener(this);
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(toolBar,BorderLayout.NORTH);
        content.add(jsp,BorderLayout.CENTER);
        this.setResizable(false);
        UIUtils.setLocationCenter(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==this.openFilePaneBtn){
            if(filePanelOpen){
                hideFilePanel();
            }else{
                openFilePanel();
            }
        }else if(e.getSource()==this.sendBtn){
            sendTalkMsg();
        }else if(e.getSource()==this.cancelBtn){
            this.dispose();
        }else if(e.getSource()==this.shakeBtn){
            this.sendShakeMsg();
        }else if(e.getSource()==this.menuItemClear){
            this.clear();
        }else if(e.getSource()==this.voiceBtn){
            if(this.voiceTalkFlag){
                this.stopVoice();
                this.sendStopVoiceMsg();
            }else{
                this.sendRequestVoiceMsg();
            }
        }
    }


    public void updateSendProgress(int value,String info,String toolTip){
        this.filePanel.updateSendProgress(value,info,toolTip);
    }

    public void updateReceiveProgress(int value,String info, String toolTip){
        this.filePanel.updateReceiveProgress(value,info,toolTip);
    }
    /*	public void changeSendPanel(){
            this.filePanel.showSendProgressBar();
        }*/
    public void changeReceivePanel(){
        this.filePanel.showReceiveProgressBar();
    }
    public void recoverSendPanel(){
        this.filePanel.recoverSendPanel();
    }

    public void recoverReceivePanel(){
        this.filePanel.recoverReceivePanel();
    }

    public void startFileServer(String[] fileNames,long[] fileLengths){
        server = null;
        server = new FileServer(this,fileNames,fileLengths);
        server.start();
    }

    public void stopFileServer(){
        if(server != null){
            if(server.isAlive()){
                server.stopServer();
            }
            server = null;
        }
    }

    private void clear() {
        textPane.setText("");
    }

    /**
     * 发送聊天内容
     */
    private void sendTalkMsg() {

        String content = msgToSendTA.getText().trim();
        if(!content.equals("")){
            try {
                String userName = SysUtils.getLoginUserName();
                Message msg = new Message(userName,Config.LOCAL_HOST_NAME,Config.HEAD_TALK_MSG,content);
                String name = Config.LOCAL_USER_NAME_DEFAULT;
                String sendTime = BasicUtils.formatDate(new Date());
                String info = name+" "+sendTime;
                this.appendMsg(info,content);
                msgToSendTA.setText("");
                MessageSender.send(msg, user.getHostAddress());
                DBUtils.writeSentMsgToDB(msg, user);
            } catch (Exception ex) {
                ex.printStackTrace();
                UIUtils.showErroMessage(this, "发送失败！错误信息："+ex.getMessage());
            }
        }

    }

    /**
     * 发送抖动
     */
    private void sendShakeMsg() {
        try {
            String sendTime = BasicUtils.formatDate(new Date());
            this.appendSysMsg(Config.SHAKE_TIP_SELF+" "+sendTime);
            MessageSender.sendShakeMsg(user.getHostAddress());
        } catch (Exception ex) {
            UIUtils.showErroMessage(this, "发送失败");
        }
    }

    private void sendRequestVoiceMsg(){
        try {
            String sendTime = BasicUtils.formatDate(new Date());
            this.appendSysMsg(Config.REQUEST_VOICE_TIP_SELF+" "+sendTime);
            MessageSender.sendRequestVoiceMsg(user.getHostAddress());
        } catch (Exception ex) {
            UIUtils.showErroMessage(this, "发送失败");
        }
    }

    private void sendStopVoiceMsg() {
        try {
            String sendTime = BasicUtils.formatDate(new Date());
            this.appendSysMsg(Config.STOP_VOICE_TIP_SELF+" "+sendTime);
            MessageSender.sendStopVoiceMsg(user.getHostAddress());
        } catch (Exception ex) {
            UIUtils.showErroMessage(this, "发送失败");
        }
    }

    /**
     * 隐藏传送文件面板
     */
    public void hideFilePanel(){
        this.setSize(Config.TALK_FRAME_WIDTH, Config.TALK_FRAME_HEIGHT);
        jsp.setDividerLocation(Config.TALK_FRAME_WIDTH);
        filePanelOpen = false;
        this.openFilePaneBtn.setIcon(UIUtils.getImageIcon("images/file24.png"));
        this.openFilePaneBtn.setText("文件传送");
    }

    /**
     * 打开传送文件面板
     */
    public void openFilePanel(){
        this.setSize(Config.TALK_FRAME_WIDTH+370, Config.TALK_FRAME_HEIGHT);
        jsp.setDividerLocation(Config.TALK_FRAME_WIDTH-15);
        filePanelOpen = true;
        this.openFilePaneBtn.setIcon(UIUtils.getImageIcon("images/forbiden24.png"));
        this.openFilePaneBtn.setText("关闭文件");
    }

    /**
     *
     * @param info 聊天记录的附属信息：发送人+发送时间
     * @param msg 聊天记录的消息内容
     */
    public synchronized void appendMsg(String info,String msg){
        updateMsgInfo(info+"\n",Config.DEFAULT_INFO_COLOR,false,Config.DEFAULT_INFO_FONT_SIZE,null);
        updateMsgInfo("  "+msg+"\n",Config.DEFAULT_MSG_COLOR,false,Config.DEFAULT_MSG_FONT_SIZE,null);
        textPane.setCaretPosition(textPane.getDocument().getLength()); //设置自动滚动到最后
    }

    public synchronized void  appendSysMsg(String msg){
        updateMsgInfo(msg+"\n",Config.SYS_COLOR_COMMON,false,Config.DEFAULT_MSG_FONT_SIZE,UIUtils.getImageIcon("images/information.png"));
        textPane.setCaretPosition(textPane.getDocument().getLength()); //设置自动滚动到最后
    }

    /**
     * 用来更新消息区域的普通格式消息的方法
     * @param str 设置消息文字内容
     * @param col 设置字体颜色
     * @param isBold 设置是否为粗体
     * @param fontSize 设置字体大小
     * @param icon 设置在消息首部要插入的图标，如果为null则表示不插入图标
     */
    private void updateMsgInfo(String str, Color col, boolean isBold, int fontSize, Icon icon ) {
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attrSet, col); // 颜色
        if (isBold) {
            StyleConstants.setBold(attrSet, true);
        }// 字体类型
        StyleConstants.setFontSize(attrSet, fontSize); // 字体大小
        Document doc = textPane.getDocument();
        if(icon != null){
            textPane.setCaretPosition(doc.getLength());
            textPane.insertIcon(icon);
        }
        try {
            doc.insertString(doc.getLength(), str, attrSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


    public void shake(){
        this.requestFocus(); // 先获得焦点
        UIUtils.setLocationCenter(this);//然后放置到屏幕正中
        try {
            UIUtils.playSound("sounds/alarm.au");  //播放抖动音效
        } catch (IOException e) {
            e.printStackTrace();
        }
        //最后实现在屏幕中央的抖动效果
        Point origiLocation = this.getLocationOnScreen();
        for(int i=0;i<15;i++){
            int shakeX = (int)((Math.random()-0.5)*50);
            int shakeY = (int)((Math.random()-0.5)*50);
            this.setLocation(origiLocation.x+shakeX, origiLocation.y+shakeY);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setLocation(origiLocation);
    }

    public void receiveRequestVoiceMsg() {
        if(JOptionPane.showConfirmDialog(this, Config.REQUEST_VOICE_TIP, "语音确认",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
            startVoice();
            this.appendSysMsg(Config.ACCEPT_VOICE_TIP_SELF);
            MessageSender.sendAcceptVoiceMsg(this.user.getHostAddress());
        }else{
            this.appendSysMsg(Config.REFUSE_VOICE_TIP_SELF);
            MessageSender.sendRefuseVoiceMsg(this.user.getHostAddress());
        }
    }

    public void startVoice(){
        System.out.println("开始启动语音。。。");
        this.startVoiceSender();
        this.startVoiceReceiver();
        this.voiceBtn.setText("关闭语音");
        this.voiceBtn.setIcon(UIUtils.getImageIcon("images/closemic24.png"));
        this.voiceTalkFlag = true;
    }

    public void stopVoice(){
        System.out.println("开始关闭语音");
        VoiceSender voiceSender  = VoiceSender.getInstance();
        if(voiceSender.isFlag()){
            System.out.println("开始关闭语音发送线程。。。");
            voiceSender.shutDown();
        }
        VoiceReceiver voiceReceiver = VoiceReceiver.getInstance();
        if(voiceReceiver.isFlag()){
            System.out.println("开始关闭语音接收线程。。。");
            voiceReceiver.shutDown();
        }
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TalkFrame.this.voiceBtn.setText("语音聊天");
                TalkFrame.this.voiceBtn.setIcon(UIUtils.getImageIcon("images/mic2_24.png"));
                TalkFrame.this.voiceTalkFlag = false;
            }
        });
    }

    public void startVoiceReceiver(){
        VoiceReceiver voiceReceiver = VoiceReceiver.getInstance();
        if(!voiceReceiver.isFlag()){
            System.out.println("开始启动语音接收线程。。。");
            new Thread(voiceReceiver).start();
        }
    }

    public void startVoiceSender(){
        VoiceSender voiceSender  = VoiceSender.getInstance();
        voiceSender.setHostAddress(this.getHostAddress());
        if(!voiceSender.isFlag()){
            System.out.println("开始启动语音发送线程。。。");
            new Thread(voiceSender).start();
        }
    }
    /**
     * 接收到传送文件的消息时，将对方要传送的文件显示在表格中
     */
    public void updateReceiveTable() {
        this.filePanel.updateReceiveTable(this.fileNames,this.fileLengths);
    }

    public void recoverReceiveTable(){
        this.filePanel.recoverReceiveTable();
    }

    /**
     * 更新发送文件表格的状态字段
     * index : 行号
     * state ：状态值
     */
    public void updateSendTableState(int index,String state){
        this.filePanel.updateSendTableState(index, state);
    }

    /**
     * 更新接收文件表格的状态字段
     * index : 行号
     * state ：状态值
     */
    public void updateReceiveTableState(int index,String state){
        this.filePanel.updateReceiveTableState(index, state);
    }

    public String getHostAddress(){
        return this.user.getHostAddress();
    }

    public void startAcceptFile(){
        new FileClient(this).start();
    }

    public FileSelectPanel getFilePanel(){
        return this.filePanel;
    }

    public void setSavePath(String savePath){
        this.savePath = savePath;
    }

    public String getSavePath(){
        return this.savePath;
    }

    public FileServer getServer() {
        return server;
    }

    public void setServer(FileServer server) {
        this.server = server;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public long[] getFileLengths() {
        return fileLengths;
    }

    public void setFileLengths(long[] fileLengths) {
        this.fileLengths = fileLengths;
    }

}
