package util;

import java.awt.Color;

public class Config {
    //窗体宽高默认值
    public static final int TALK_FRAME_WIDTH = 500;
    public static final int TALK_FRAME_HEIGHT = 550;
    public static final int MAIN_FRAME_HEIGHT = 600;
    public static final int MAIN_FRAME_WIDTH = 280;

    //广播地址
    public static final String BROADCAST_ADDR = "255.255.255.255";

    public static final int MSG_UDP_PORT = 9876; //基本UDP通信端口
    public static final int FILE_TCP_PORT = 6789; //传送文件的TCP消息端口
    public static final int VOICE_UDP_PORT = 8765 ; //语音聊天的UDP消息端口

    public static final Color DEFAULT_MSG_COLOR = Color.black; //消息内容默认字体颜色
    public static final Color DEFAULT_INFO_COLOR = Color.blue; //消息附加信息默认字体颜色
    public static final Color SYS_COLOR_WEAK= Color.gray; //系统信息默认字体颜色
    public static final Color SYS_COLOR_COMMON = new Color(75, 0, 130); //系统信息默认字体颜色
    public static final Color SYS_COLOR_STRONG = Color.red; //系统信息默认字体颜色
    public static final Color SYS_COLOR_MOUSEOVER = new Color(183,243,155); //鼠标进入时组件默认背景颜色
    public static final Color SYS_COLOR_SELECTED = UIUtils.getColorFromHex("#fcf0c1"); //组件选中时的默认背景颜色
    public static final int DEFAULT_MSG_FONT_SIZE = 14; //消息内容默认字体大小
    public static final int DEFAULT_INFO_FONT_SIZE = 14; //消息附加信息默认字体大小

    //消息头部的取值
    public static final String HEAD_LOGIN_MSG = "01"; //登陆时广播消息头部
    public static final String HEAD_TALK_MSG = "02"; //聊天信息
    public static final String HEAD_LOGOUT_MSG = "03"; //退出时广播消息头部
    public static final String HEAD_LOGIN_REPLY_MSG = "04"; //回应登陆广播的消息头部
    public static final String HEAD_REQUEST_SEND_FILE = "05"; //请求传送文件的消息头部
    public static final String HEAD_REFUSE_FILE = "07"; //拒绝接收文件的消息头部
    public static final String HEAD_SHAKE = "08"; //发送窗口抖动的消息头部
    public static final String HEAD_REQUEST_VOICE = "09"; //请求语音聊天的消息头部
    public static final String HEAD_ACCEPT_VOICE = "10"; //接收语音聊天的消息头部
    public static final String HEAD_REFUSE_VOICE = "11"; //拒绝语音聊天的消息头部
    public static final String HEAD_STOP_VOICE = "12"; //中断语音聊天的消息头部

    //本机信息
    public static final String LOCAL_HOST_ADDR = NetUtils.getLocalHostAddress(); //本地主机地址
    public static final String LOCAL_HOST_NAME = NetUtils.getLocalHostName(); //本地主机名
    public static final String LOCAL_MAC_ADDR = NetUtils.getLocalHostMacAddress(); //	本机硬件地址
    public static final String LOCAL_USER_NAME_DEFAULT = SysUtils.getLoginUserName(); //默认的用户名为本机登录名，如果有配置项则取配置项

    //系统提示
    public static final String SHAKE_TIP = "对方给您发送了一个抖动"; //抖动提示
    public static final String SHAKE_TIP_SELF = "您给对方发送了一个抖动"; //抖动提示
    public static final String SEND_FILE_MSG_TIP="对方发来了文件，请在右边文件传送面板接收或拒绝！"; //文件传送消息提示
    public static final String REFUSE_FILE_MSG_TIP = "对方放弃了接收您将要传送的文件！"; // 拒绝接收文件消息的提示
    public static final String REFUSE_FILE_MSG_TIP_SELF = "您放弃了接收对方将要传送的文件！"; // 拒绝接收文件消息的提示
    public static final String FILE_SEND_FINISHED = "文件发送完毕"; //
    public static final String FILE_ACCEPT_FINISHED="文件接收完毕"; //
    public static final String REQUEST_VOICE_TIP = "对方请求语音聊天，是否接受？";
    public static final String REQUEST_VOICE_TIP_SELF = "您请求与语音聊天，正在等待对方回应，请稍候...";
    public static final String REFUSE_VOICE_TIP_SELF = "您拒绝了与对方的语音聊天请求！";
    public static final String REFUSE_VOICE_TIP = "对方拒绝了您的语音聊天请求！";
    public static final String ACCEPT_VOICE_TIP = "对方接收了您的语音聊天请求，语音聊天开始";
    public static final String ACCEPT_VOICE_TIP_SELF = "您接收了对方的语音聊天请求，语音聊天开始";
    public static final String STOP_VOICE_TIP = "对方中断了和您的语音聊天！";
    public static final String STOP_VOICE_TIP_SELF = "您中断了和对方的语音聊天";

    //文件传送状态
    public static final String STATE_WAITING = "01";   //等待传送或接收
    public static final String STATE_SENDING = "02";  //正在发送
    public static final String STATE_ACCEPTING = "03";  //正在接收
    public static final String STATE_COMPLETED = "04"; //传送或接收完成
    public static final String STATE_CANCEL_SEND = "05"; //已取消发送
    public static final String STATE_CANCEL_ACCEPT = "06"; //已取消接收

    //文件传送状态描述
    public static final String TEXT_STATE_WAITING = "等待";   //等待传送或接收
    public static final String TEXT_STATE_SENDING = "正在发送";  //正在发送
    public static final String TEXT_STATE_ACCEPTING = "正在接收";  //正在接收
    public static final String TEXT_STATE_COMPLETED = "已完成"; //传送或接收完成
    public static final String TEXT_STATE_CANCEL_SEND = "已取消发送"; //已取消发送
    public static final String TEXT_STATE_CANCEL_ACCEPT = "已取消接收"; //已取消接收
}
