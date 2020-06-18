package Window.FriendListWindow;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
 
/**
 * DefaultListCellRenderer是Swing JList的渲染器
 * ImageCellRender继承DefaultListCellRender,DefaultListCellRender继承于JLabel，所以只要将图片赋给JLabel就可以更改显示样式
 */
public class ImageCellRender extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2746069683064696124L;
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int index, boolean isSelected, boolean cellHasFocus){
		// instanceof 判断其左边对象是否为其右边类的实例
		if(value instanceof File){
			File imageFile = (File) value;
			try{
				//拿到当前文件的URI再拿到URL
				ImageIcon icon = new ImageIcon(imageFile.toURI().toURL());
				//设置图标大小
				icon.setImage(icon.getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT));
				setIcon(icon);
				//a.这里可以用html来控制字体的颜色
				//b.也可以通过设置前景色来改变字体颜色
				setForeground(Color.blue);
				this.setBackground(Color.LIGHT_GRAY);
				String text = imageFile.getName();
				int IN = 0;
				for(int i = text.length() - 1;i>=0;i--) {
					if(text.charAt(i) == '.') {
						IN = i;
						break;
					}
				}
				char[] temp  = text.toCharArray();
				temp[IN] = '\0';
				char[] temp1 = new char[IN];
				for(int i = 0;i<IN;i++) {
					temp1[i] = temp[i];
				}
				text = String.valueOf(temp1);
				setText("                        " + text);
				setVerticalTextPosition(SwingConstants.CENTER);
				setHorizontalTextPosition(SwingConstants.RIGHT);
				setBorder(BorderFactory.createLineBorder(Color.GREEN,4));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(isSelected){
			this.setBackground(Color.CYAN);
		}
		return this;
	}
}