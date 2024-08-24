package r9.quiz.problemui.surveyjs;

import javax.swing.Box;
import javax.swing.JTextField;

import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.surveyjs.RatingQuestion;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.R9Exception;

public class RatingQuestionPanel extends QuestionCommonPanel{
 
	private static final long serialVersionUID = 1L;
	private JTextField minRateDescription;
	private JTextField maxRateDescription;
	public RatingQuestionPanel(){
		 
		minRateDescription = new JTextField();
		maxRateDescription = new JTextField();
		
		 createCommonPanel(   );
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("MinScoreDescription"), minRateDescription, 35));
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("MaxScoreDescription"), maxRateDescription, 35));
		 contentPanel.add(Box.createVerticalStrut(400));
		 createCorrectAnswerRow9();
			createCommonNavigation( );
	}
	
	@Override
	public void setupUI(Question question, Quiz quiz){
		 if(!(question instanceof RatingQuestion))
			 return;
		 RatingQuestion q = (RatingQuestion)question;
		 super.setupUI(q,quiz);
		 this.maxRateDescription.setText(  q.getMaxRateDescription() ); 
		 this.minRateDescription.setText(q.getMinRateDescription());
	}
	@Override
	public void fromUI(Question question, Quiz quiz) throws R9Exception{
		 if(!(question instanceof RatingQuestion))
			 return;
		 RatingQuestion q = (RatingQuestion)question;
		 super.fromUI(q, quiz);
		 q.setMinRateDescription(this.minRateDescription.getText());
		 q.setMaxRateDescription(this.maxRateDescription.getText());
	}
}
