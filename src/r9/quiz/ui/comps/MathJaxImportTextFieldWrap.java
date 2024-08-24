package r9.quiz.ui.comps;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import r9.quiz.R9SystemSetting;

public class MathJaxImportTextFieldWrap extends JPanel{
 
	private static final long serialVersionUID = 1L;
	
	public final MathJaxImportTextField textField;

	
	public MathJaxImportTextFieldWrap() {
		this(0,0);
	}
	public MathJaxImportTextFieldWrap(int row, int col) {
		setLayout(new BorderLayout());
		textField = new MathJaxImportTextField(row, col);
		add(textField, BorderLayout.CENTER);
		ImageIcon icon = R9SystemSetting.getToolIcon("mathjax.png");
		JButton mathjaxButton = new JButton(icon);
		mathjaxButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.showPopup( );
//				  MathJaxImportDialog.MathJaxCallback callback = new MathJaxImportDialog.MathJaxCallback() {
//				 		@Override
//						public void onResult(String result) {
//				 			textField.setText(result);
//						}
//						
//						@Override
//						public String getContent() {
//							 return  textField.getText();
//						}
//					};
//					MathJaxImportDialog dialog = new MathJaxImportDialog(null, callback);
//					dialog.setVisible(true);   
			}});
		  icon = R9SystemSetting.getToolIcon("template.png");
		JButton flipButton = new JButton(icon);
		flipButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.toggle();
			}});
		JPanel apane = new JPanel();
		apane.setLayout(new GridLayout(1,2));
		apane.add(flipButton);
		apane.add(mathjaxButton);
		add(apane, BorderLayout.EAST);
	}
	public void setText(String title) {
		 this.textField.setText(title);
	}
	public String getText() {
		return this.textField.getText();
	}
}
