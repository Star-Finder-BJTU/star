package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import bean.Message;
import bean.TmpMsg;
import bean.User;

/**
 * @author 22591
 */
public class DBUtils {
    public static final String BASE_PATH = DBUtils.class.getClassLoader().getResource(".").getPath().substring(1);
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    public  DBUtils(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/msg?serverTimezone=Asia/Shanghai","root","lx2000");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return conn;
    }

    private void insert(TmpMsg msg){
        try {
            String insertSql = "insert into tmp_msg (userName,hostName,macAddress,hostAddress,localUserName,localHostName,"
                    + " localHostAddress,localMacAddress,type,messageType,messageText,sendTime,receiveTime)  "
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(insertSql);
            ps.setString(1, msg.getUserName());
            ps.setString(2, msg.getHostName());
            ps.setString(3, msg.getMacAddress());
            ps.setString(4, msg.getHostAddress());
            ps.setString(5, msg.getLocalUserName());
            ps.setString(6, msg.getLocalHostName());
            ps.setString(7, msg.getLocalHostAddress());
            ps.setString(8, msg.getLocalMacAddress());
            ps.setString(9, msg.getType());
            ps.setString(10, msg.getMessageType());
            ps.setString(11, msg.getMessageText());
            ps.setString(12, msg.getSendTime());
            ps.setString(13, msg.getReceiveTime());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                if(ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 插入一条通讯信息的工具方法
     * @param msg
     */
    public static void insertMsg(TmpMsg msg){
        DBUtils dbUtil = new DBUtils();
        dbUtil.insert(msg);
        dbUtil.close();
    }

    /**
     * 将接收到的消息写入数据库（只写聊天信息和文件传送的记录）
     * @param m 消息对象
     * @param hostAddr 对方IP地址
     */
    public static void writeReceivedMsgToDB(Message m,String hostAddr){
        TmpMsg msg = new TmpMsg();
        msg.setHostAddress(hostAddr);
        msg.setHostName(m.getHostName());
        msg.setUserName(m.getUserName());
        msg.setLocalHostAddress(Config.LOCAL_HOST_ADDR);
        msg.setLocalHostName(Config.LOCAL_HOST_NAME);
        msg.setLocalMacAddress(Config.LOCAL_MAC_ADDR);
        msg.setLocalUserName(Config.LOCAL_USER_NAME_DEFAULT);
        //0表示发送的消息；1表示接收的消息
        msg.setType("1");
        //暂时没做，预留
        msg.setMacAddress("Unknown");
        msg.setMessageText(m.getData());
        msg.setMessageType(m.getHead());
        msg.setSendTime(BasicUtils.formatDate(new Date(m.getSeq()),false));
        msg.setReceiveTime(BasicUtils.formatDate(new Date(), false));
        DBUtils.insertMsg(msg);
    }

    /**
     * 将发送的消息写入数据库（只写聊天信息和文件传送记录）
     * @param m 消息对象
     * @param user 对方user对象
     */
    public static void writeSentMsgToDB(Message m,User user){
        TmpMsg msg = new TmpMsg();
        msg.setHostAddress(user.getHostAddress());
        //通过查找该用户信息得到
        msg.setHostName(user.getHostName());
        //通过查找该用户信息得到
        msg.setUserName(user.getUserName());
        msg.setLocalHostAddress(Config.LOCAL_HOST_ADDR);
        msg.setLocalHostName(m.getHostName());
        msg.setLocalMacAddress(Config.LOCAL_MAC_ADDR);
        msg.setLocalUserName(m.getUserName());
        //0表示发送的消息；1表示接收的消息
        msg.setType("0");
        //对方的Mac地址，通过查找该用户信息得到
        msg.setMacAddress(user.getMacAddress());
        msg.setMessageText(m.getData());
        msg.setMessageType(m.getHead());
        msg.setSendTime(BasicUtils.formatDate(new Date(m.getSeq()),false));
        //发送信息时暂时是不知道对方接收时间的，以后可能通过对方确认消息得到，暂时没做，预留
        msg.setReceiveTime("Unknown");
        DBUtils.insertMsg(msg);
    }
    /**
     * 查询是否为数据库中敏感词
     */
    private  boolean isSensitive(Message m) throws SQLException {
        String selectSql = "select * from ssWords";
        ps = conn.prepareStatement(selectSql);
        try {
            rs = ps.executeQuery();
            if (rs.next()) {
                String words = rs.getString("words");
                if(m.getData().contains(words)){
                    return true;
                }
            }
        }  finally{
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally{
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public static boolean getSensitiveResult(Message m) throws SQLException {
        DBUtils dbUtil = new DBUtils();
        return dbUtil.isSensitive(m);
    }
}