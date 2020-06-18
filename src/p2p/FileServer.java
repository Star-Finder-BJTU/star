package p2p;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import ui.TalkFrame;
import util.BasicUtils;
import util.Config;


/**
 * @author 22591
 */
public class FileServer extends Thread{
    private TalkFrame parentFrame;
    private static final String TAG = "SERVER INFO:";
    private ServerSocket serverSocket;
    private String[] fileNames;
    private long[] fileLengths;
    private long progress;
    private int index;
    public FileServer(TalkFrame parentFrame,String[] fileNames,long[] fileLengths){
        this.parentFrame = parentFrame;
        this.fileLengths = fileLengths;
        this.fileNames = fileNames;
        this.progress = 0;
    }
    @Override
    public void run(){
        if(fileLengths.length ==0 || fileNames.length == 0) {
            return;
        }
        try {
            serverSocket = new ServerSocket(Config.FILE_TCP_PORT);
            System.out.println(TAG+"服务器已经启动！");
            Socket socket = serverSocket.accept();
            System.out.println(TAG+"对方连接成功！");
            BufferedOutputStream socketOS = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream socketIS = new BufferedInputStream(socket.getInputStream());
            // 读入缓存字节数组的数据长度
            int len = 0;
            byte[] buffer =  new byte[1024];
            //总耗时
            long totalCostTime = 0;
            //总文件数目
            int totalFileCount = 0;
            //总文件大小
            long totalFileLength = 0;

            while( ((len=socketIS.read(buffer))!=-1)){
                String msg = new String(buffer,0,len);
                //对方发送来的信息是个整数，表示将要接收的文件的编号；
                index = Integer.parseInt(msg);
                System.out.println("对方发来的序号是："+index);
                //当传送完最后一个文件会再发送一个整数信息，其值等于文件个数，表明已全部接收完毕，则关闭服务器
                if(index==(fileNames.length)){
                    System.out.println("即将退出循环");
                    break;
                }
                System.out.println(TAG+"============================================================");
                System.out.println(TAG+"开始传送第【"+index+"】号文件："+fileNames[index]+"，请等待...");
                long startTime = System.currentTimeMillis();
                this.parentFrame.updateSendTableState(index, Config.STATE_SENDING);
                File fileToSend = new File(fileNames[index]);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));
                this.progress = 0;
                long fileLength = fileLengths[index];
                while((len=bis.read(buffer))!=-1){
                    socketOS.write(buffer, 0, len);
                    //进度信息
                    progress += len;
                    String lengthShow = BasicUtils.convertByte(fileLength);
                    String info = "("+(index+1)+"/"+fileNames.length+")  "+BasicUtils.convertByte(progress)+"/"+lengthShow+"  "+progress*100/fileLength+"%";
                    int pValue = (int)(progress*100/fileLength);
                    parentFrame.updateSendProgress(pValue,info,"正在发送："+fileNames[index]);
                }
                socketOS.flush();
                bis.close();
                System.out.println(TAG+"第【"+index+"】号文件传送完毕～");
                long costTime = (System.currentTimeMillis()-startTime);
                totalCostTime += costTime;
                totalFileCount++;
                totalFileLength += fileLengths[index];
                parentFrame.updateSendTableState(index, Config.STATE_COMPLETED);
                parentFrame.appendSysMsg("文件["+fileNames[index]+"]发送完毕，文件大小["+BasicUtils.convertByte(fileLengths[index])+"]，耗时["
                        +BasicUtils.convertTime(costTime/1000)+"] "+BasicUtils.formatDate(new Date()));
            }
            socketOS.close();
            socketIS.close();
            socket.close();
            serverSocket.close();
            parentFrame.appendSysMsg("所有文件发送完毕，文件总数目["+totalFileCount+"]个，文件总大小["+BasicUtils.convertByte(totalFileLength)+"]，总耗时["+BasicUtils.convertTime(totalCostTime/1000)+"]");
            parentFrame.recoverSendPanel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer(){
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
