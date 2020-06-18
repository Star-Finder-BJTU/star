package msg_handler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Date;

import ui.MainFrame;
import ui.TalkFrame;
import util.BasicUtils;
import util.DBUtils;
import util.Config;
import util.NetUtils;
import util.UIUtils;
import bean.Message;
import bean.User;

/**
 * 接收聊天信息的线程，当接收到信息就显示在主窗口的聊天信息面板上
 * @author 22591
 */
public class MessageReceiver implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private MainFrame mf;
    private static MessageReceiver instance = null;

    private MessageReceiver(MainFrame mf) {
        try {
            this.mf = mf;
            byte[] data = new byte[512];
            packet = new DatagramPacket(data, data.length);
            socket = new DatagramSocket(Config.MSG_UDP_PORT);
        } catch (SocketException e) {
            UIUtils.showErroMessage(mf, "消息接收线程启动失败！");
        }
    }

//    public static synchronized MessageReceiver getInstance(MainFrame mf){
//        if(instance == null){
//            instance = new MessageReceiver(mf);
//        }
//        instance.mf = mf;
//        return instance;
//    }


    public static MessageReceiver getInstance(MainFrame mf){
        if(instance == null){
            synchronized (MessageReceiver.class){
                if(instance == null){
                    instance = new MessageReceiver(mf);
                }
            }
        }
        instance.mf = mf;
        return instance;
    }

    @Override
    public void run() {
        try {
            while (true) {
                socket.receive(packet);
                String hostAddress = packet.getAddress().getHostAddress();
                Message msg = parseMessage(packet.getData(), packet
                        .getLength());
                String msgHead = msg.getHead();
                String msgData = msg.getData();
                String userName = msg.getUserName();
                String hostName = msg.getHostName();
                User user = new User();
                user.setHostAddress(hostAddress);
                user.setHostName(hostName);
                user.setUserName(userName);
                String sendTime = BasicUtils.formatDate(new Date(msg.getSeq()));
                String info = userName + " " + sendTime;
                boolean isSelf = hostAddress.equals(NetUtils.getLocalHostAddress());
                if (msgHead.equals(Config.HEAD_TALK_MSG)) {
                    receiveTalkMessage(user, info, msgData);
//                    UIUtils.playSound("sounds/msg.wav");
                    //将信息封装写入数据库
                    DBUtils.writeReceivedMsgToDB(msg, hostAddress);
                } else if (msgHead.equals(Config.HEAD_LOGIN_MSG) && !isSelf) {
                    // 登陆时发送的广播消息
                    receiveLoginMessage(user);
                }else if(msgHead.equals(Config.HEAD_LOGIN_REPLY_MSG)){
                    // 回复登陆广播的消息，不用考虑是否为本机发送，因为本机收不到自身的登陆包
                    receiveLoginReplyMessage(user);
                }else if (msgHead.equals(Config.HEAD_LOGOUT_MSG) && !isSelf) {
                    // 退出时发送的广播消息
                    receiveLogoutMessage(user);
                }else if(msgHead.equals(Config.HEAD_REQUEST_SEND_FILE)){
                    //请求传送文件的消息
                    receiveSendFileMessage(user, msgData, sendTime);
                }else if(msgHead.equals(Config.HEAD_SHAKE)){
                    //窗口抖动消息
                    receiveShakeMessage(user, sendTime);
                }else if(msgHead.equals(Config.HEAD_REFUSE_FILE)){
                    //拒绝接收文件的消息
                    receiveRefuseFileMessage(user, sendTime);
                }else if(msgHead.equals(Config.HEAD_REQUEST_VOICE)){
                    //请求语音聊天
                    receiveRequestVoiceMessage(user,sendTime);
                }else if(msgHead.equals(Config.HEAD_ACCEPT_VOICE)){
                    //接受语音聊天
                    receiveAcceptVoiceMessage(user,sendTime);
                }else if(msgHead.equals(Config.HEAD_REFUSE_VOICE)){
                    //拒绝语音聊天
                    receiveRefuseVoiceMessage(user,sendTime);
                }else if(msgHead.equals(Config.HEAD_STOP_VOICE)){
                    //中断语音聊天
                    receiveStopVoiceMessage(user,sendTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveStopVoiceMessage(User user, String sendTime) {
        TalkFrame tf = mf.getTalkFrame(user);
        if(tf==null){
            tf = new TalkFrame(user);
            mf.addTalkFrame(tf);
        }
        tf.setVisible(true);
        tf.appendSysMsg(Config.STOP_VOICE_TIP + " " + sendTime);
        tf.stopVoice();
    }

    private void receiveAcceptVoiceMessage(User user, String sendTime) {
        TalkFrame tf = mf.getTalkFrame(user);
        if(tf==null){
            tf = new TalkFrame(user);
            mf.addTalkFrame(tf);
        }
        tf.setVisible(true);
        tf.appendSysMsg(Config.ACCEPT_VOICE_TIP+" "+sendTime);
        tf.startVoice();
    }


    private void receiveRefuseVoiceMessage(User user, String sendTime) {
        TalkFrame tf = mf.getTalkFrame(user);
        if(tf==null){
            tf = new TalkFrame(user);
            mf.addTalkFrame(tf);
        }
        tf.setVisible(true);
        tf.appendSysMsg(Config.REFUSE_VOICE_TIP+" "+sendTime);
    }


    private void receiveRequestVoiceMessage(User user, String sendTime) {
        TalkFrame tf = mf.getTalkFrame(user);
        if(tf==null){
            tf = new TalkFrame(user);
            mf.addTalkFrame(tf);
        }
        tf.setVisible(true);
        tf.appendSysMsg(Config.REQUEST_VOICE_TIP+" "+sendTime);
        tf.receiveRequestVoiceMsg();
    }


    private void receiveRefuseFileMessage(User user, String sendTime) {
        TalkFrame tf = mf.getTalkFrame(user);
        if(tf==null){
            tf = new TalkFrame(user);
            mf.addTalkFrame(tf);
        }
        tf.setVisible(true);
        tf.appendSysMsg(Config.REFUSE_FILE_MSG_TIP+" "+sendTime);
        tf.recoverSendPanel();
        tf.stopFileServer();
    }

    private void receiveSendFileMessage(User user, String msgData, String sendTime) {
        int splitIndex = msgData.indexOf("*");
        String fileNames = msgData.substring(0,splitIndex);
        String fileLengths = msgData.substring(splitIndex+1);
        String[] names = fileNames.split("\\|");
        String[] lengthStrs = fileLengths.split("\\|");
        long[] lengths = new long[lengthStrs.length];
        for(int i=0;i<lengthStrs.length;i++){
            lengths[i] = Long.parseLong(lengthStrs[i]);
        }
        TalkFrame tf = mf.getTalkFrame(user);
        if(tf==null){
            tf = new TalkFrame(user);
            mf.addTalkFrame(tf);
        }
        tf.setVisible(true);
        tf.appendSysMsg(Config.SEND_FILE_MSG_TIP+" "+sendTime);
        tf.getFilePanel().enableOperateBtn();
        tf.setFileNames(names);
        tf.setFileLengths(lengths);
        tf.updateReceiveTable();
        tf.openFilePanel();
    }

    private void receiveLogoutMessage(User user) {
        mf.removeUser(user);
    }

    private void receiveShakeMessage(User user, String sendTime) {
        TalkFrame tf = mf.getTalkFrame(user);
        if(tf==null){
            tf = new TalkFrame(user);
            mf.addTalkFrame(tf);
        }
        tf.setVisible(true);
        tf.appendSysMsg(Config.SHAKE_TIP+" "+sendTime);
        tf.shake();
    }

    private void receiveLoginReplyMessage(User user) {
        mf.addUser(user);
    }

    private void receiveLoginMessage(User user) {
        mf.addUser(user);
        // 收到登陆包后需发送一个登陆回应包，告诉对方自己在线
        MessageSender.sendLoginReplyMsg(user.getHostAddress());
    }

    private void receiveTalkMessage(User user, String info, String msgData) {
        // 如果接收到的是聊天消息
        TalkFrame tf = mf.getTalkFrame(user);
        if(tf==null){
            tf = new TalkFrame(user);
            mf.addTalkFrame(tf);
        }
        tf.setVisible(true);
        tf.appendMsg(info, msgData);
    }

    public  Message parseMessage(byte[] msgBytes, int length) throws Exception{
        byte[] seqBytes = Arrays.copyOfRange(msgBytes, 0, 8);
        byte[] otherBytes = Arrays.copyOfRange(msgBytes, 8, length);
        String otherStr = new String(otherBytes);
        Message msg = new Message();
        msg.setSeq(BasicUtils.byteArrayToLong(seqBytes));
        msg.setHead(otherStr.substring(0,2));
        otherStr = otherStr.substring(2);
        int index = otherStr.indexOf(":");
        String userName = otherStr.substring(0,index);
        msg.setUserName(userName);
        otherStr = otherStr.substring(index+1);
        index = otherStr.indexOf(":");
        msg.setHostName(otherStr.substring(0,index));
        msg.setData(otherStr.substring(index+1));
        System.out.println("====收到消息："+msg.getUserName()+" "+msg.getHostName()+" "+msg.getData());
        return msg;
    }


}
