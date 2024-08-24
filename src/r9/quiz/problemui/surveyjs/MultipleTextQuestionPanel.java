package r9.quiz.problemui.surveyjs;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import r9.quiz.surveyjs.MultipleTextQuestion;
import r9.quiz.surveyjs.MultipleTextQuestion.TextItem;
import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.R9Exception;

public class MultipleTextQuestionPanel extends QuestionCommonPanel{
 
	private static final long serialVersionUID = 1L;
	private JTextField colCountField; 
	private ItemCollectionPanel<TextItem> choicesPanel;
	public MultipleTextQuestionPanel(){
		 
		colCountField = new JTextField();
		colCountField.setPreferredSize(new Dimension( 100,32));
		colCountField.setMaximumSize(new Dimension( 100,32));
	 
		
		choicesPanel = new ItemCollectionPanel<TextItem>(rb.getString("Name"), rb.getString("Title"),false){ 
			private static final long serialVersionUID = 1L;
			@Override
			public TextItem updateData(TextItem t, String f1, String f2, boolean c1) {
				t.name = f1;
				t.title = f2;
				t.correct = c1;
				return t;
			}

			@Override
			public void selectData(TextItem d, JTextComponent f1, JTextComponent f2, JCheckBox c1) {
				 f1.setText(d.name);
				 f2.setText(d.title);
				 c1.setSelected(d.correct);
			}

			@Override
			public TextItem create() {
				 return new TextItem("","");
			}};
		  
			 createCommonPanel(  );
			 contentPanel.add(PropertyUIHelper.createRow(rb.getString("OptionsInOneLine"), colCountField, 35));
	 	 
			 contentPanel.add(choicesPanel);
			 createCorrectAnswerRow9();
				createCommonNavigation( );
	}
	
	@Override
	public void setupUI(Question question, Quiz quiz){
		 if(!(question instanceof MultipleTextQuestion))
			 return;
		 MultipleTextQuestion q = (MultipleTextQuestion)question;
		 super.setupUI(q,quiz);
		 
		 this.colCountField.setText(q.getColCount() +"");
	 	 
		 choicesPanel.setData(q.getItems());
	}
	@Override
	public void fromUI(Question question, Quiz quiz) throws R9Exception{
		 if(!(question instanceof MultipleTextQuestion))
			 return;
		 MultipleTextQuestion q = (MultipleTextQuestion)question;
		 super.fromUI(q, quiz);
		 try{
			 q.setColCount(Integer.parseInt( this.colCountField.getText())); 
		 }catch(Exception e){}
		 
	}
}
