package r9.quiz.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import r9.quiz.CourseCreatingManager;
import r9.quiz.QuizCreatingDialog;
import r9.quiz.R9SystemSetting;
import r9.quiz.ReloadableUI;
import r9.quiz.SurvyJsQuizCreatingDialog;
import r9.quiz.cards.Exam;
import r9.quiz.cards.CardHtmlPage;
import r9.quiz.cards.CardPage;
import r9.quiz.cards.CardSurveyJsJumpPage;

public class NewAction extends AbstractAction{
	public static interface NewActionCallback{
	    	void onResult(boolean success);
	}
	     
	 
	NewActionCallback callback;
    ReloadableUI dialog;
    boolean surveyJsOnly;
	public NewAction( ReloadableUI dialog, boolean surveyJsOnly){
		this.dialog = dialog;
		this.surveyJsOnly = surveyJsOnly;
	}
	public NewAction( ReloadableUI dialog,  boolean surveyJsOnly,  NewActionCallback callback){
		this.dialog = dialog;
		this.surveyJsOnly= surveyJsOnly;
		this.callback = callback;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Exam existing =  CourseCreatingManager.sharedInstance.getExam();
		 final ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		if( existing != null && existing.getPages().size() > 0){
			int option = JOptionPane.showConfirmDialog(null, rb.getString("SaveCurrent") + "？");
			if( option == JOptionPane.YES_OPTION ){
				CourseCreatingManager.sharedInstance.save();
			} else {
				
			}
		}
		
		String s = (String)JOptionPane.showInputDialog(null,
				rb.getString("InputNewName"),
                "... ", 
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "Quiz－");
		if (s == null){
			 if( callback != null)
				 callback.onResult(false);
			return;
		}
		String homedir = CourseCreatingManager.sharedInstance.getCourseHome(s);
		File f = new File(homedir);
		if( f.exists() ){  
			 boolean success = CourseCreatingManager.sharedInstance.load(s);
			 if( success ) {
				 dialog.reload();
				 if( callback != null )
					 callback.onResult(true);
				 return;
			 } else {
				 
			 } 
		}
		
		
        Exam exam = new Exam();
        exam.setName(s);
        if( surveyJsOnly)
            exam.addPage(  new CardSurveyJsJumpPage() ); 
        else
        	exam.addPage(  new CardHtmlPage() ); 
        
        CourseCreatingManager.sharedInstance.setExam(exam);
        
   	    boolean isSurveyJsWorkspace = dialog instanceof SurvyJsQuizCreatingDialog;
        if( isSurveyJsWorkspace == surveyJsOnly) {
			 dialog.reload(); 
		 } else {
		 	( (JDialog)dialog ).setVisible(false);
			if(  surveyJsOnly ) {
				 CardPage card = exam.getPages().get(0);
				 if( card instanceof CardSurveyJsJumpPage){
					 SurvyJsQuizCreatingDialog dialog = 
							 new SurvyJsQuizCreatingDialog(null, (CardSurveyJsJumpPage)card, true);
					 dialog.setSize(1100, 920);
					 dialog.setVisible(true);
				 }
			} else {
				QuizCreatingDialog dialog = new QuizCreatingDialog(null);
				dialog.setSize(1050, 850);
				dialog.setVisible(true);
			}
		 }
        
       
   	    if( callback != null )
		    callback.onResult(true);
		
	}

}
