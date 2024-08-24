package r9.quiz.ui.comps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import r9.quiz.R9SystemSetting;  

public class MathJaxImportButton extends JButton{
 
	private static final long serialVersionUID = 1L;
	MathJaxImportDialog.MathJaxCallback callback;
	public MathJaxImportButton(String title, final MathJaxImportDialog.MathJaxCallback callback) {
		super(title);
		ImageIcon icon = R9SystemSetting.getToolIcon("mathjax.png");
		super.setIcon(icon);
		this.callback = callback;
		this.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				MathJaxImportDialog dialog = new MathJaxImportDialog(null, callback);
				dialog.setVisible(true);
			}});
	}
}
