package r9.quiz.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;

import r9.quiz.FileResourceManagerDialog;
import r9.quiz.QuizCreatingDialog;

public class ImportCommonFileAction extends AbstractAction{
    
	private static final long serialVersionUID = 1L;
	JDialog parent;
	private boolean isJsFile;
	 public ImportCommonFileAction( JDialog dialog, boolean isJsFile){
			this.parent = dialog;
			this.isJsFile = isJsFile;
	 }
	 
	 
	@Override
	public void actionPerformed(ActionEvent e) { 
		FileResourceManagerDialog adialog = new FileResourceManagerDialog(parent,  isJsFile);
		adialog.setSize(450, 450);
		adialog.setVisible(true);
	}
	
	 

}
