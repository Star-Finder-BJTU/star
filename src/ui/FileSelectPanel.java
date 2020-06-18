package ui;

import msg_handler.MessageSender;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import util.BasicUtils;
import util.Config;
import util.UIUtils;
import exception.MessageOverflowException;

@SuppressWarnings("serial")
public class FileSelectPanel extends JPanel implements ActionListener {

    private JButton sendFileBtn;
    private JButton cancelBtn;
    private JButton addBtn;
    private JButton delBtn;
    private JButton receiveBtn;
    private JButton refuseBtn;
    private JButton cancelSendBtn;
    private JButton cancelReceiveBtn;
    private JTable fileSendTable;
    private JTable fileReceiveTable;
    private JPopupMenu popMenu;
    private JProgressBar fileSendProgressBar;
    private JProgressBar fileReceiveProgressBar;
    private JToolBar receiveToolBar;
    private JToolBar sendToolBar;
    private JPanel sendCardPanel ;
    private JPanel receiveCardPanel ;
    private JPanel sendProgressPanel ;
    private JPanel receiveProgressPanel ;
    private CardLayout sendCardLayout;
    private CardLayout receiveCardLayout;
    private TalkFrame parentFrame;


    public FileSelectPanel(TalkFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
    }

    private void initComponents() {
        this.fileSendProgressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
        this.fileSendProgressBar.setStringPainted(true);
        this.fileReceiveProgressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
        this.fileReceiveProgressBar.setStringPainted(true);
        sendFileBtn = new JButton("发送",UIUtils.getImageIcon("images/accept.png"));
        sendFileBtn.setToolTipText("发送");
        cancelBtn = new JButton("取消",UIUtils.getImageIcon("images/cancel2.png"));
        cancelBtn.setToolTipText("关闭");
        cancelSendBtn = new JButton(UIUtils.getImageIcon("images/cancel2.png"));
        cancelSendBtn.setToolTipText("取消传送");
        cancelReceiveBtn = new JButton(UIUtils.getImageIcon("images/cancel2.png"));
        cancelReceiveBtn.setToolTipText("取消接收");
        addBtn = new JButton("添加",UIUtils.getImageIcon("images/add.png"));
        addBtn.setToolTipText("添加文件");
        delBtn = new JButton("删除",UIUtils.getImageIcon("images/delete.png"));
        delBtn.setToolTipText("删除文件");
        receiveBtn = new JButton("接收",UIUtils.getImageIcon("images/accept.png"));
        receiveBtn.setToolTipText("接收");
        refuseBtn = new JButton("拒绝",UIUtils.getImageIcon("images/cancel2.png"));
        refuseBtn.setToolTipText("拒绝");
        sendFileBtn.setFocusable(false);
        addBtn.setFocusable(false);
        delBtn.setFocusable(false);
        cancelBtn.setFocusable(false);
        cancelSendBtn.setFocusable(false);
        cancelReceiveBtn.setFocusable(false);
        receiveBtn.setFocusable(false);
        refuseBtn.setFocusable(false);
        receiveBtn.setEnabled(false);
        refuseBtn.setEnabled(false);

        JLabel fileSendLabel = new JLabel("发送文件：");
        JLabel fileReceiveLabel = new JLabel("接收文件：");
        fileSendTable = new JTable(new MyTableModel(new Object[][]{},new String[]{"状态","文件名","文件大小","文件路径","隐藏列_文件真实大小"}));
        fileSendTable.setSelectionBackground(Config.SYS_COLOR_SELECTED);
        fileSendTable.setSelectionForeground(fileSendTable.getForeground());
        TableColumn c = fileSendTable.getColumnModel().getColumn(0);
        c.setCellRenderer(new StateRenderer());
        fileSendTable.setRowHeight(fileSendTable.getRowHeight()+5);
        fileSendTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //关闭自动调整大小
        fileSendTable.getColumnModel().getColumn(0).setPreferredWidth(85);
        fileSendTable.getColumnModel().getColumn(1).setPreferredWidth(198);
        fileSendTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        //下面两行代码实现隐藏列
        fileSendTable.getColumnModel().getColumn(3).setMinWidth(0);
        fileSendTable.getColumnModel().getColumn(3).setMaxWidth(0);
        fileSendTable.getColumnModel().getColumn(4).setMinWidth(0);
        fileSendTable.getColumnModel().getColumn(4).setMaxWidth(0);

        fileReceiveTable = new JTable(new MyTableModel(new Object[][]{},new String[]{"状态","文件名","文件大小","存放路径"}));
        fileReceiveTable.setSelectionBackground(Config.SYS_COLOR_SELECTED);
        fileReceiveTable.setSelectionForeground(fileSendTable.getForeground());
        TableColumn sc = fileReceiveTable.getColumnModel().getColumn(0);
        sc.setCellRenderer(new StateRenderer());
        fileReceiveTable.setRowHeight(fileReceiveTable.getRowHeight()+5);
        fileReceiveTable.getColumnModel().getColumn(0).setPreferredWidth(85);
        fileReceiveTable.getColumnModel().getColumn(1).setPreferredWidth(198);
        fileReceiveTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        fileReceiveTable.getColumnModel().getColumn(3).setMinWidth(0);
        fileReceiveTable.getColumnModel().getColumn(3).setMaxWidth(0);
        fileReceiveTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //关闭自动调整大小

        JScrollPane fileSendScrollPane = new JScrollPane(fileSendTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane fileReceiveScrollPane = new JScrollPane(fileReceiveTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        receiveToolBar = new JToolBar();
        receiveToolBar.setBorderPainted(false);
        receiveToolBar.add(receiveBtn);
        receiveToolBar.add(refuseBtn);
        //receiveToolBar.add(cancelBtn);
        receiveCardLayout = new CardLayout();
        receiveCardPanel = new JPanel(receiveCardLayout);
        receiveCardPanel.add(receiveToolBar, "receiveToolBar");
        receiveProgressPanel = new JPanel(new BorderLayout());
        receiveProgressPanel.add(fileReceiveProgressBar,BorderLayout.CENTER);
        receiveProgressPanel.add(cancelReceiveBtn,BorderLayout.EAST);
        receiveCardPanel.add(receiveProgressPanel,"receiveProgressBar");
        JPanel fileReceiveNorthPanel = new JPanel(new BorderLayout());
        fileReceiveNorthPanel.add(fileReceiveLabel,BorderLayout.WEST);
        fileReceiveNorthPanel.add(receiveCardPanel,BorderLayout.CENTER);
        JPanel fileReceivePanel = new JPanel(new BorderLayout(0,0));
        fileReceivePanel.add(fileReceiveNorthPanel,BorderLayout.NORTH);
        fileReceivePanel.add(fileReceiveScrollPane,BorderLayout.CENTER);

        sendToolBar = new JToolBar();
        sendToolBar.setBorderPainted(false);
        sendToolBar.add(addBtn);
        sendToolBar.add(delBtn);
        sendToolBar.add(sendFileBtn);
        sendCardLayout = new CardLayout();
        sendCardPanel = new JPanel(sendCardLayout);
        sendCardPanel.add(sendToolBar, "sendToolBar");
        sendProgressPanel = new JPanel(new BorderLayout());
        sendProgressPanel.add(fileSendProgressBar,BorderLayout.CENTER);
        sendProgressPanel.add(cancelSendBtn,BorderLayout.EAST);
        sendCardPanel.add(sendProgressPanel,"sendProgressBar");
        JPanel fileSendNorthPanel = new JPanel(new BorderLayout());
        fileSendNorthPanel.add(fileSendLabel,BorderLayout.WEST);
        fileSendNorthPanel.add(sendCardPanel,BorderLayout.CENTER);
        JPanel fileSendPanel = new JPanel(new BorderLayout(0,0));
        fileSendPanel.add(fileSendNorthPanel,BorderLayout.NORTH);
        fileSendPanel.add(fileSendScrollPane,BorderLayout.CENTER);


        sendFileBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        cancelSendBtn.addActionListener(this);
        cancelReceiveBtn.addActionListener(this);
        addBtn.addActionListener(this);
        delBtn.addActionListener(this);
        receiveBtn.addActionListener(this);
        refuseBtn.addActionListener(this);

        popMenu = new JPopupMenu();
        JMenuItem addFile = new JMenuItem("增加");
        popMenu.add(addFile);
        this.add(popMenu);

        fileSendTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    //先选中右击时鼠标所在的行
                    int row = fileSendTable.rowAtPoint(e.getPoint());
                    if(row>=0) {
                        fileSendTable.setRowSelectionInterval(row,row);
                    }
                    //再弹出右击菜单
                    popMenu.show(fileSendTable, e.getX(), e.getY());
                    int r = fileSendTable.getSelectedRow(); // 获得当前选中的行号
                    String ID = (String) fileSendTable.getValueAt(r, 0);
                    System.out.println(ID);
                }
            }
        });

/*		JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
		south.add(cancelBtn);*/

        JPanel center = new JPanel(new GridLayout(2,1,0,3));
        center.add(fileReceivePanel);
        center.add(fileSendPanel);

        this.setLayout(new BorderLayout(0, 0));
        this.add(center, BorderLayout.CENTER);
        /*		this.add(south, BorderLayout.SOUTH);
         */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==cancelBtn){
            parentFrame.hideFilePanel();
        }else if(e.getSource()==addBtn){
            addFile();
        }else if(e.getSource()==delBtn){
            delFile();
        }else if(e.getSource()==sendFileBtn){
            sendFile();
        }else if(e.getSource()==receiveBtn){
            receiveFile();
        }else if(e.getSource()==refuseBtn){
            refuseFile();
        }
    }

    private void receiveFile() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode ( JFileChooser.DIRECTORIES_ONLY );
        if ( fc.showOpenDialog ( this ) == JFileChooser.CANCEL_OPTION ) {
            return;
        }
        //此处不应该再用udp发送给对方信息，因为这样可能导致udp信息比tcp连接晚到而造成不同步和逻辑混乱
        //需要发送交互信息也应该通过tcp连接发送
        //MessageSender.sendAcceptFileMsg(this.parentFrame.getHostAddress());
        File file = fc.getSelectedFile();
        String path = file.getAbsolutePath();
        this.parentFrame.setSavePath(path);
        this.parentFrame.startAcceptFile();
        this.disableOperateBtn();
        this.showReceiveProgressBar();
    }

    private void refuseFile() {
        String sendTime = BasicUtils.formatDate(new Date());
        this.parentFrame.appendSysMsg(Config.REFUSE_FILE_MSG_TIP_SELF+" "+sendTime);
        this.disableOperateBtn();
        this.recoverReceiveTable();
        MessageSender.sendRefuseFileMsg(this.parentFrame.getHostAddress());
    }

    public void enableOperateBtn(){
        this.receiveBtn.setEnabled(true);
        this.refuseBtn.setEnabled(true);
    }

    private void disableOperateBtn(){
        this.receiveBtn.setEnabled(false);
        this.refuseBtn.setEnabled(false);
    }

    public void showSendProgressBar(){
        System.out.println("showing send progress bar....");
        this.sendCardLayout.show(this.sendCardPanel,"sendProgressBar");
    }

    public void recoverSendPanel(){
        System.out.println("recovering send panel...");
        this.fileSendProgressBar.setValue(0);
        this.fileSendProgressBar.setString("");
        this.fileSendProgressBar.setToolTipText("");
        this.sendCardLayout.show(this.sendCardPanel,"sendToolBar");
        ((MyTableModel)(fileSendTable.getModel())).setRowCount(0);
    }

    public void showReceiveProgressBar(){
        this.receiveCardLayout.show(this.receiveCardPanel, "receiveProgressBar");
    }

    public void recoverReceivePanel(){
        System.out.println("recovering receive panel...");
        this.fileReceiveProgressBar.setValue(0);
        this.fileReceiveProgressBar.setString("");
        this.fileReceiveProgressBar.setToolTipText("");
        this.receiveCardLayout.show(this.receiveCardPanel,"receiveToolBar");
        ((MyTableModel)(fileReceiveTable.getModel())).setRowCount(0);
    }

    public void updateSendProgress(int value,String info,String toolTip){
        this.fileSendProgressBar.setToolTipText(toolTip);
        this.fileSendProgressBar.setString(info);
        this.fileSendProgressBar.setValue(value);
    }

    public void updateReceiveProgress(int value,String info, String toolTip){
        this.fileReceiveProgressBar.setToolTipText(toolTip);
        this.fileReceiveProgressBar.setString(info);
        this.fileReceiveProgressBar.setValue(value);
    }

    public void recoverReceiveTable(){
        ((MyTableModel)(fileReceiveTable.getModel())).setRowCount(0);
    }

    private void sendFile() {
        int count = fileSendTable.getRowCount(); //文件数目
        if(count==0){
            UIUtils.showAlertMessage(this, "没有要发送的文件，请先添加！");
            return;
        }
        StringBuilder fileNamesStr = new StringBuilder();
        StringBuilder fileLengthsStr = new StringBuilder();
        MyTableModel tm = (MyTableModel)(fileSendTable.getModel());
        String fileNames[] = new String[count];
        long fileLengths[] = new long[count];
        for(int i=0;i<count;i++){
            String fileName = (String) tm.getValueAt(i, 1);
            String fileLength = (String) tm.getValueAt(i, 4);
            if(i!=0){
                fileNamesStr.append("|");
                fileLengthsStr.append("|");
            }
            fileNamesStr.append(fileName); //这里仅仅是文件名
            fileLengthsStr.append(fileLength);
            fileNames[i] = (String) tm.getValueAt(i, 3); //文件完整路径
            fileLengths[i] = Long.parseLong(fileLength);
        }
        this.parentFrame.startFileServer(fileNames, fileLengths); // 先启动文件传送服务器
        System.out.println("发送的数据：=================");
        System.out.println(fileNamesStr);
        System.out.println(fileLengthsStr);
        System.out.println(Arrays.toString(fileNames));
        System.out.println(Arrays.toString(fileLengths));
        //再发送传送文件消息
        try {
            MessageSender.sendRequestFileSendMsg(this.parentFrame.getHostAddress(), fileNamesStr.toString(),fileLengthsStr.toString());
            this.fileSendProgressBar.setString("等待对方接收，请稍候...");
            this.showSendProgressBar();
        } catch (MessageOverflowException e) {
            e.printStackTrace();
            UIUtils.showErroMessage(this, e.getMessage());
            this.parentFrame.stopFileServer();
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.showErroMessage(this, e.toString());
            this.parentFrame.stopFileServer();
        }

    }

    private void delFile() {
        int r = fileSendTable.getSelectedRow(); // 获得当前选中的行号
        if(r==-1) {
            UIUtils.showAlertMessage(this, "请先选择要删除的行");
            return;
        }
        while(r != -1){
            ((MyTableModel)(fileSendTable.getModel())).removeRow(r);
            r =  fileSendTable.getSelectedRow();
        }

    }

    private void addFile() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled ( true );
        fc.setFileSelectionMode ( JFileChooser.FILES_ONLY );
        if ( fc.showOpenDialog ( this ) == JFileChooser.CANCEL_OPTION ) {
            return;
        }
        File []file = fc.getSelectedFiles ();
        for (int i = 0 ; i <file.length ; i ++){
            ((MyTableModel)(fileSendTable.getModel())).addRow (
                    new String [] {Config.STATE_WAITING,file[i].getName (),BasicUtils.convertByte(file[i].length()),
                            file[i].getAbsolutePath(),String.valueOf(file[i].length())});
            //设置新增的行为选中行
            int lastRowIndex = fileSendTable.getRowCount()-1;
            fileSendTable.clearSelection();
            fileSendTable.addRowSelectionInterval(lastRowIndex, lastRowIndex);
        }
    }

    public void updateReceiveTable(String[] fileNames, long[] fileLengths) {
        for (int i = 0 ; i <fileNames.length ; i ++){
            ((MyTableModel)(fileReceiveTable.getModel())).addRow(
                    new String [] {Config.STATE_WAITING, fileNames[i],BasicUtils.convertByte(fileLengths[i]),""});
        }
    }
    /**
     * 更新发送文件表格的状态字段
     * index : 行号
     * state ：状态值
     */
    public void updateSendTableState(int index,String state){
        this.fileSendTable.setValueAt(state, index, 0);
        if(state.equals(Config.STATE_SENDING)){
            this.fileSendTable.clearSelection();
            this.fileSendTable.addRowSelectionInterval(index, index);
        }
    }
    /**
     * 更新接收文件表格的状态字段
     * index : 行号
     * state ：状态值
     */
    public void updateReceiveTableState(int index,String state){
        this.fileReceiveTable.setValueAt(state, index, 0);
        if(state.equals(Config.STATE_ACCEPTING)){
            this.fileReceiveTable.clearSelection();
            this.fileReceiveTable.addRowSelectionInterval(index, index);
        }
    }




}

@SuppressWarnings("serial")
class MyTableModel extends DefaultTableModel{
    public MyTableModel(Object[][] data, Object[] columnNames){
        super(data,  columnNames) ;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}

class StateRenderer extends JLabel implements TableCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setOpaque(true);
        if(isSelected){
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }else{
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        String v = (String)value;
        if(v.equals(Config.STATE_WAITING)){
            setIcon(UIUtils.getImageIcon("images/wait.png"));
            setText(Config.TEXT_STATE_WAITING);
        }else if(v.equals(Config.STATE_SENDING)){
            setIcon(UIUtils.getImageIcon("images/basket_remove.png"));
            setText(Config.TEXT_STATE_SENDING);
        }else if(v.equals(Config.STATE_ACCEPTING)){
            setIcon(UIUtils.getImageIcon("images/basket_put.png"));
            setText(Config.TEXT_STATE_ACCEPTING);
        }else if(v.equals(Config.STATE_COMPLETED)){
            setIcon(UIUtils.getImageIcon("images/completed.png"));
            setText(Config.TEXT_STATE_COMPLETED);
        }
        return this;
    }
}
