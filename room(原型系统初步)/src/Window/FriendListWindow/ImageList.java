package Window.FriendListWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
 
import javax.swing.AbstractListModel;
 
/**
 * 【swing中JList控件使用】
 * 每一项以 图片+文字（文字在图片的下面）的形式显示并且在水平方向排序
 * 
 * 实现这个功能的重点在以下几个方面：
 * （1）需要改变JList的cellRender来支持对图片的显示，默认的cellRender只会显示文本字符串
 * （2）设置JList的每一项的显示方向(文字和图片的排列关系)
 * （3）设置每一项选中后的背景
 * 
 * @author Silly
 */
 
/**
 * 1.创建一个ImageListModel，继承【AbstractListModel】，设置为JList的列表模型
 * @author Silly
 *
 */
public class ImageList extends AbstractListModel<File> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -83519007330122378L;
	private List<File> imageFile = new ArrayList<File>();
	
	public void addElement(File file){
		this.imageFile.add(file);
	}
	public int getSize(){
		return imageFile.size();
	}
	public File getElementAt(int index){
		return imageFile.get(index);
	}
}