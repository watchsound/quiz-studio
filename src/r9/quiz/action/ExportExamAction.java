package r9.quiz.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import r9.quiz.CourseCreatingManager;
import r9.quiz.ExportExamDialog;
import r9.quiz.QuizCreatingDialog; 
import r9.quiz.cards.Exam;
import r9.quiz.cards.CardHtmlPage;
import r9.quiz.cards.CardPage;
import r9.quiz.util.ImageUtil;
import r9.quiz.util.Utils;

public class ExportExamAction extends AbstractAction{
	public static interface NewActionCallback{
	    	void onResult(boolean success);
	}
	      
	public ExportExamAction(  ){ 
	}
	 
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ExportExamDialog dialog = new ExportExamDialog(null);
		dialog.setAlwaysOnTop(true);
		dialog.setSize(450, 250);
		dialog.setVisible(true); 
	}

}
