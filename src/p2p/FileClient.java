package p2p;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

import ui.TalkFrame;
import util.BasicUtils;
import util.Config;

/**
 * @author 22591
 */
public class FileClient extends Thread{
    private static final String TAG = "CLIENT INFO:";
    private TalkFrame talkFrame;
    private String hostAddress;
    private String savePath;
    private String[] fileNames;
    private long[] fileLengths;
    public FileClient(TalkFrame talkFrame){
        this.talkFrame = talkFrame;
        this.hostAddress = talkFrame.getHostAddress();
        this.fileLengths = talkFrame.getFileLengths();
        this.fileNames = talkFrame.getFileNames();
        this.savePath = talkFrame.getSavePath();
        System.out.println(TAG+this.hostAddress+"=="+this.savePath);
        System.out.println(TAG+Arrays.toString(this.fileNames));
        System.out.println(TAG+Arrays.toString(this.fileLengths));
    }

    @Override
    public void run(){
        try {
            Socket socket = new Socket(hostAddress,Config.FILE_TCP_PORT);
            BufferedOutputStream socketOS = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream socketIS = new BufferedInputStream(socket.getInputStream());
            byte[] buffer =  new byte[1024];
            int index = 0;
            long totalCostTime = 0; //总耗时
            int totalFileCount = 0; //总文件数目
            long totalFileLength = 0; //总文件大小
            while(index<fileNames.length){
                int len = 0;
                long progress = 0;
                long fileLength = fileLengths[index];
                long startTime = System.currentTimeMillis();
                socketOS.write(String.valueOf(index).getBytes()); //先发送一条消息，通知服务器下一条要接收的文件编号
                socketOS.flush();
                this.talkFrame.updateReceiveTableState(index, Config.STATE_ACCEPTING);
                String filePath = this.savePath+"\\"+fileNames[index];
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                while(progress<fileLengths[index]  && ((len=socketIS.read(buffer))!=-1)){
                    bos.write(buffer, 0, len);
                    //进度信息
                    progress += len;
                    String lengthShow = BasicUtils.convertByte(fileLength);
                    String info = "("+(index+1)+"/"+fileNames.length+")  "+BasicUtils.convertByte(progress)+"/"+lengthShow+"  "+progress*100/fileLength+"%";
                    int pValue = (int)(progress*100/fileLength);
                    talkFrame.updateReceiveProgress(pValue,info,"正在接收："+fileNames[index]);
                }
                bos.flush();
                bos.close();
                System.out.println(TAG+"第【"+index+"】号文件接收完毕～");
                long costTime = (System.currentTimeMillis()-startTime);
                totalCostTime += costTime;
                totalFileCount++;
                totalFileLength += fileLengths[index];
                this.talkFrame.updateReceiveTableState(index, Config.STATE_COMPLETED);
                talkFrame.appendSysMsg("文件["+fileNames[index]+"]接收完毕，文件大小["+BasicUtils.convertByte(fileLengths[index])+"]，耗时["
                        +BasicUtils.convertTime(costTime/1000)+"] "+BasicUtils.formatDate(new Date()));
                index++;
            }
            talkFrame.recoverReceiveTable();
            //最后全部接收完需要再发送一条消息
            socketOS.write(String.valueOf(index).getBytes());
            socketOS.flush();
            socketIS.close();
            socketOS.close();
            socket.close();
            talkFrame.appendSysMsg("所有文件接收完毕，文件总数目["+totalFileCount+"]个，文件总大小["+BasicUtils.convertByte(totalFileLength)+"]，总耗时["+BasicUtils.convertTime(totalCostTime/1000)+"]");
            talkFrame.recoverReceivePanel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
