package r9.quiz.problemui.surveyjs;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import r9.quiz.surveyjs.BooleanQuestion;
import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.R9Exception;

public class BooleanQuestionPanel extends QuestionCommonPanel{
 
	private static final long serialVersionUID = 1L;
	private JTextField labelLabel;
	private JCheckBox correctAnswerCheckBox;
 
	public BooleanQuestionPanel(){
		 
		labelLabel = new JTextField();
		correctAnswerCheckBox = new JCheckBox();
		 createCommonPanel( );
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("Note"), labelLabel, 35));
		createCorrectAnswerRow9();
		createCommonNavigation( );
	}
	@Override
	public void createCorrectAnswerRow9() {
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("Key"), correctAnswerCheckBox ));
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("WrongFollow"), wrongFollowField, 35));
	}
	@Override
	public void setupUICorrectField(Question question, Quiz quiz){
		correctAnswerCheckBox.setSelected(Boolean.parseBoolean(question.getCorrectAnswer()));
	}
	@Override
	protected void fromUICorrectField(Question question) {
		question.setCorrectAnswer(correctAnswerCheckBox.isSelected() + "");
	}
	@Override
	public void setupUI(Question question, Quiz quiz){
		 if(!(question instanceof BooleanQuestion))
			 return;
		 BooleanQuestion q = (BooleanQuestion)question;
		 super.setupUI(q, quiz);
		 
		 this.labelLabel.setText(q.getLabel());
	}
	@Override
	public void fromUI(Question question, Quiz quiz) throws R9Exception{
		 if(!(question instanceof BooleanQuestion))
			 return;
		 BooleanQuestion q = (BooleanQuestion)question;
		 super.fromUI(q, quiz);
		 q.setLabel(this.labelLabel.getText());
	}
}
