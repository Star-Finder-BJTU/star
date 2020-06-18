package voice_handler;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import msg_handler.MessageReceiver;
import ui.MainFrame;
import util.Config;
import util.UIUtils;

/**
 * 接收语音的线程
 * @author 22591
 */
public class VoiceReceiver implements Runnable {
    private static VoiceReceiver instance = null;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private boolean flag = false;
    final int bufSize = 16384;
    SourceDataLine sourceDataLine;
    private VoiceReceiver()  {

    }

//    public static VoiceReceiver getInstance(){
//        if(instance == null) {
//            instance = new VoiceReceiver();
//        }
//        return instance;
//    }

    public static VoiceReceiver getInstance(){
        if(instance == null){
            synchronized (VoiceReceiver.class){
                if(instance == null){
                    instance = new VoiceReceiver();
                }
            }
        }
        return instance;
    }
    @Override
    public void run() {
        flag = true;
        try {
            socket = new DatagramSocket(Config.VOICE_UDP_PORT);
        }catch(BindException e){
            e.printStackTrace();
            flag=true;
            System.out.println("socket被占用，说明语音接收线程正阻塞在最后一次循环正准备退出，\n"
                    + "此时，本线程只需要设置flag的值为true，让该线程继续运行，而本线程直接退出即可");
            return;
        }catch (SocketException e) {
            e.printStackTrace();
            UIUtils.showErroMessage(null, "语音接收线程启动失败！");
            flag = false;
            return;
        }
        byte[] data = new byte[512];
        packet = new DatagramPacket(data, data.length);

        AudioFormat format =new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 1, 2, 44100.0f, false);
        //AudioFormat format = new AudioFormat(44100F,16,2,true,true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);
        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(format, bufSize);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            flag = false;
            return;
        }
        sourceDataLine.start();
        while (flag) {
            try{
                socket.receive(packet);
                sourceDataLine.write(packet.getData(), 0, packet.getLength());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        if (flag) {
            sourceDataLine.drain();
        }
        sourceDataLine.stop();
        sourceDataLine.close();
        sourceDataLine = null;
        socket.close();
        socket = null;
        flag = false;
        System.out.println("退出receiver run");
    }

    public boolean isFlag() {
        return flag;
    }

    public void shutDown() {
        this.flag = false;
    }

}
