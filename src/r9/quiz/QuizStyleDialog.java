package r9.quiz;

import java.awt.Frame;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class QuizStyleDialog extends JDialog{
 
	private static final long serialVersionUID = 1L;

	public QuizStyleDialog() {
		init();
	}
	public QuizStyleDialog(JDialog parent) {
		super(parent);
		init();
	}
	public QuizStyleDialog(Frame parent) {
		super(parent);
		init();
	}
	private void init() {
		ImageIcon icon = R9SystemSetting.getToolIcon("mathjax.png");
		List<Image> images = new ArrayList<Image>();
		images.add(icon.getImage());
		setIconImages(images);
	}
}
