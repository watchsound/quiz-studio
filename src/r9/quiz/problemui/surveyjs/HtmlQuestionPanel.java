package r9.quiz.problemui.surveyjs;

import r9.quiz.WebEditor;
import r9.quiz.surveyjs.HtmlQuestion;
import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.util.R9Exception;

public class HtmlQuestionPanel extends QuestionCommonPanel{
 
	private static final long serialVersionUID = 1L;
	private WebEditor webEditor;
	public HtmlQuestionPanel(){
		  
		 webEditor = new WebEditor(null); 
		 createCommonPanel(  );
		 contentPanel.add( webEditor); 
		 createCommonNavigation( );
		 
		 correctAnswerField.setVisible(false);
	}
	
	@Override
	public void setupUI(Question question, Quiz quiz){
		 if(!(question instanceof HtmlQuestion))
			 return;
		 HtmlQuestion q = (HtmlQuestion)question;
		 super.setupUI(q,quiz);
		  
		 webEditor.resetUIStates(q );
	}
	@Override
	public void fromUI(Question question, Quiz quiz) throws R9Exception{
		 if(!(question instanceof HtmlQuestion))
			 return;
		 HtmlQuestion q = (HtmlQuestion)question;
		 super.fromUI(q, quiz);
		 webEditor.populateModelFromUI();
	}
}
