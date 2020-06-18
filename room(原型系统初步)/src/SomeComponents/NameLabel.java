package SomeComponents;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class NameLabel extends JLabel{

	/**
	 * 
	 */
	private int thickness = 2;
	private Color[] colors = {
			Color.BLACK,Color.BLUE,Color.CYAN,Color.DARK_GRAY,Color.GRAY,Color.GREEN,Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,Color.PINK,Color.RED,Color.WHITE,Color.YELLOW
	};
	private int index = 0;
	private static final long serialVersionUID = -5387085985298481370L;
	public NameLabel(String name) {
		super(name,JLabel.CENTER);
		start s = new start();
		Thread t = new Thread(s);
		t.start();
	}
	class start implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setBorder(BorderFactory.createLineBorder(colors[index], thickness, true));
				index = (index + 1)%colors.length;
			}
		}
		
	}
}

