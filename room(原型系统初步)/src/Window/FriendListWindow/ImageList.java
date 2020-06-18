package Window.FriendListWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
 
import javax.swing.AbstractListModel;
 
/**
 * ��swing��JList�ؼ�ʹ�á�
 * ÿһ���� ͼƬ+���֣�������ͼƬ�����棩����ʽ��ʾ������ˮƽ��������
 * 
 * ʵ��������ܵ��ص������¼������棺
 * ��1����Ҫ�ı�JList��cellRender��֧�ֶ�ͼƬ����ʾ��Ĭ�ϵ�cellRenderֻ����ʾ�ı��ַ���
 * ��2������JList��ÿһ�����ʾ����(���ֺ�ͼƬ�����й�ϵ)
 * ��3������ÿһ��ѡ�к�ı���
 * 
 * @author Silly
 */
 
/**
 * 1.����һ��ImageListModel���̳С�AbstractListModel��������ΪJList���б�ģ��
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