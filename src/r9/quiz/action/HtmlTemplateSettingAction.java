package r9.quiz.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import r9.quiz.CourseCreatingManager;
import r9.quiz.HtmlTemplateDialog;
import r9.quiz.QuizCreatingDialog;
import r9.quiz.R9SystemSetting;
import r9.quiz.cards.Exam;

public class HtmlTemplateSettingAction extends AbstractAction{
   
	 
	 public HtmlTemplateSettingAction(  ){
			 
	 }
	 
	 
	@Override
	public void actionPerformed(ActionEvent e) { 
		HtmlTemplateDialog newDialog = new HtmlTemplateDialog();
		newDialog.setSize(850, 650);
		newDialog.setVisible(true);
	}
	
	 

}
