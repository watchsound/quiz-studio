package r9.quiz.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import r9.quiz.CourseCreatingManager;
import r9.quiz.QuizCreatingDialog;
import r9.quiz.R9SystemSetting;
import r9.quiz.ReloadableUI;
import r9.quiz.SurvyJsQuizCreatingDialog;
import r9.quiz.cards.CardHtmlPage;
import r9.quiz.cards.CardPage;
import r9.quiz.cards.CardSurveyJsJumpPage;
import r9.quiz.cards.Exam;
import r9.quiz.surveyjs.Quiz;

public class OpenAction extends AbstractAction{
    public static interface OpenActionCallback{
    	void onResult(boolean success);
    }
     
     OpenActionCallback callback;
     ReloadableUI dialog;
	 public OpenAction( ReloadableUI dialog){
			this.dialog = dialog;
	 }
	 public OpenAction( ReloadableUI dialog, OpenActionCallback callback){
			this.dialog = dialog;
			this.callback = callback;
	 }
	 
	@Override
	public void actionPerformed(ActionEvent e) {
		final ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message", R9SystemSetting.LOCALE);
		Exam existing =  CourseCreatingManager.sharedInstance.getExam();
		if( existing != null  && existing.getPages().size() > 0 ){
			int option = JOptionPane.showConfirmDialog(null, rb.getString("saveProject") + "？");
			if( option == JOptionPane.YES_OPTION ){
				CourseCreatingManager.sharedInstance.save();
			}
		}
		
		JFileChooser chooser = new JFileChooser(R9SystemSetting.getInstance().getCreatedFilesDir());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		chooser.setFileFilter(new FileFilter(){
//         	@Override
//			public boolean accept(File f) {
//         	    return f.isDirectory() || f.getName().endsWith("cardset.obj") ;
//			} 
//			@Override
//			public String getDescription() {
//			   	return "Test Project Description File";
//			}});
		
		 int selection = chooser.showOpenDialog(null);
		 if ( selection == JFileChooser.APPROVE_OPTION ){
			  File file = chooser.getSelectedFile();
			  if( !file.isDirectory() )
				  file =  file.getParentFile();
			  if( file.isDirectory() ){
				 boolean success = CourseCreatingManager.sharedInstance.load(file.getName());
				 if ( success ){
					 boolean isSurveyJsWorkspace = dialog instanceof SurvyJsQuizCreatingDialog;
					 Exam exam =  CourseCreatingManager.sharedInstance.getExam();
					 if( exam.isSurveyJsOnly() == isSurveyJsWorkspace) {
						 dialog.reload(); 
					 } else {
						( (JDialog)dialog ).setVisible(false);
						if( exam.isSurveyJsOnly() ) {
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
					
					 if( callback != null)
						 callback.onResult(true);
				 } else {
					 if( callback != null)
						 callback.onResult(false);
					 JOptionPane.showMessageDialog(null, rb.getString("failedOpenFile"));
			 	 }
			  }  else {
				  if( callback != null)
						 callback.onResult(false);
				  JOptionPane.showMessageDialog(null, rb.getString("failedOpenFile"));
			  }
		 }
	}
	
	 

}
