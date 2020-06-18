package bean;

import org.apache.commons.codec.DecoderException;

import util.BasicUtils;

/**
 * 包序号+命令字+用户名+':'+主机名+':'+消息正文
 * 包序号：占8Byte，采用当前毫秒数转换为字节数组，这也是包的发送时间
 * 命令字：即head字段，是长度为2的字符串；每个命令字在Global类中定义
 * @author 22591
 */
public class Message {
    //包序号

    private long seq;
    private String userName;
    private String hostName;

    //消息头，表明消息的类型

    private String head;

    //消息正文

    private String data;

    public Message(){

    }

    public Message(String userName,String hostName,String head, String message) {
        this.seq = System.currentTimeMillis();
        this.head = head;
        this.data = message;
        this.hostName = hostName;
        this.userName = userName;
        System.out.println("====发送消息："+seq+this.head+this.userName+":"+this.hostName+":"+this.data);
    }

    public Message(long seq, String userName,String hostName,String head, String message) {
        this.seq = seq;
        this.head = head;
        this.data = message;
        this.hostName = hostName;
        this.userName = userName;
    }

    public String getHead() {
        return head;
    }
    public void setHead(String head) {
        this.head = head;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public byte[] toBytes() throws DecoderException{
        byte[] seqBytes = BasicUtils.longToByteArray(this.seq);
        byte[] otherBytes = (this.head+this.userName+":"+this.hostName+":"+this.data).getBytes();
        byte[] msgBytes = new byte[seqBytes.length+otherBytes.length];
        for(int i=0;i<msgBytes.length;i++){
            if(i<seqBytes.length) {
                msgBytes[i] = seqBytes[i];
            } else {
                msgBytes[i] = otherBytes[i-seqBytes.length];
            }
        }
        return msgBytes;
    }
}
