package r9.quiz.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import r9.quiz.CourseCreatingManager;
import r9.quiz.QuizCreatingDialog; 
import r9.quiz.cards.Exam;
import r9.quiz.cards.CardHtmlPage;
import r9.quiz.cards.CardPage;
import r9.quiz.util.ImageUtil;
import r9.quiz.util.Utils;

public class SaveAsAction extends AbstractAction{
	public static interface NewActionCallback{
	    	void onResult(boolean success);
	}
	     
	 
	NewActionCallback callback; 
	public SaveAsAction(  ){ 
	}
	public SaveAsAction(   NewActionCallback callback){ 
		this.callback = callback;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = (String)JOptionPane.showInputDialog(null,
                "英语考试程名 ",
                "请输入英语考试名称",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "英语考试－");
		if (s == null){
			 if( callback != null)
				 callback.onResult(false);
			return;
		}
		
		String homedir = CourseCreatingManager.sharedInstance.getCourseHome(s);
		File f = new File(homedir);
		if( f.exists() ){
			 JOptionPane.showMessageDialog(null, "文件已经存在");
			 CourseCreatingManager.sharedInstance.load(s);
			 if( callback != null )
				 callback.onResult(false);
			 return;
		}
		String curhomedir = CourseCreatingManager.sharedInstance.getCourseFolder( );
		 
		try {
			Utils.copyFolder(new File(curhomedir), f);
			Exam existing =  CourseCreatingManager.sharedInstance.getExam();
			if( existing != null ){
				existing.setName(s);
				existing.setUuid(ImageUtil.createUUID());
				CourseCreatingManager.sharedInstance.save();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
   	    if( callback != null )
		    callback.onResult(true);
		
	}

}
