package r9.quiz.problemui.surveyjs;

import java.awt.Dimension;
 
import javax.swing.JComboBox;
import javax.swing.JTextField;

import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.surveyjs.TextQuestion;
import r9.quiz.surveyjs.TextQuestion.InputType;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.R9Exception;

public class TextQuestionPanel extends QuestionCommonPanel{
 
	private static final long serialVersionUID = 1L;
	private JComboBox<InputType> inputTypeCombo;
	private JTextField placeHolderField;  
	public TextQuestionPanel(){
		 
		inputTypeCombo = new JComboBox<InputType>(InputType.values());
		placeHolderField = new JTextField();
		 
		inputTypeCombo.setPreferredSize(new Dimension( 100,32));
		inputTypeCombo.setMaximumSize(new Dimension( 100,32));
	 
		  
		 createCommonPanel( );
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("DefaultValueShown"), placeHolderField, 35));
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("InputType"), inputTypeCombo, 35)); 
	//	 contentPanel.add(Box.createVerticalStrut(400));
		 createCorrectAnswerRow9();
			createCommonNavigation( );
	}
	@Override
	public void createCorrectAnswerRow9() {
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("Key"), correctAnswerField, 32));
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("WrongFollow"), wrongFollowField, 32));
	}
	
	@Override
	public void setupUI(Question question, Quiz quiz){
		 if(!(question instanceof TextQuestion))
			 return;
		 TextQuestion q = (TextQuestion)question;
		 super.setupUI(q, quiz);
		 
		 this.placeHolderField.setText(q.getPlaceHolder());
		 this.inputTypeCombo.setSelectedItem(q.getInputType());
		 
	}
	@Override
	public void fromUI(Question question, Quiz quiz) throws R9Exception{
		 if(!(question instanceof TextQuestion))
			 return;
		 TextQuestion q = (TextQuestion)question;
		 super.fromUI(q, quiz);
		 q.setPlaceHolder(placeHolderField.getText()); 
		 q.setInputType( (InputType) inputTypeCombo.getSelectedItem());
	}
}
