package r9.quiz.problemui.surveyjs;

import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import r9.quiz.surveyjs.BooleanQuestion;
import r9.quiz.surveyjs.CheckboxesQuestion;
import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.surveyjs.StringChoice;
import r9.quiz.surveyjs.Question.ChoicesOrderType;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.R9Exception;

public class CheckboxesQuestionPanel extends QuestionCommonPanel{
 
	private static final long serialVersionUID = 1L;
	private JComboBox<ChoicesOrderType> choicesOrderCombo;
	private JTextField colCountField;
	private JCheckBox hasNoneBox;
	private ItemCollectionPanel<StringChoice> choicesPanel;
	public CheckboxesQuestionPanel(){
	 
		choicesOrderCombo = new JComboBox<ChoicesOrderType>(ChoicesOrderType.values());
		choicesOrderCombo.setPreferredSize(new Dimension( 100,32));
		choicesOrderCombo.setMaximumSize(new Dimension( 100,32));
		colCountField = new JTextField();
		colCountField.setPreferredSize(new Dimension( 100,32));
		colCountField.setMaximumSize(new Dimension( 100,32));
		hasNoneBox = new JCheckBox();
		
		choicesPanel = new ItemCollectionPanel<StringChoice>(rb.getString("Value"), null){ 
			private static final long serialVersionUID = 1L;
			@Override
			public StringChoice updateData(StringChoice t, String f1, String f2, boolean c1) { 
				t.correct = c1;
				t.value = f1;
				return t;
			}

			@Override
			public void selectData(StringChoice d, JTextComponent f1, JTextComponent f2, JCheckBox c1) {
				 f1.setText(d.value);
				 c1.setSelected(d.correct);
			}

			@Override
			public StringChoice create() {
				 return new StringChoice("",false);
			}};
		  
		 createCommonPanel(    );
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("OptionsInOneLine"), colCountField, 35));
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("OptionsOrder"), choicesOrderCombo, 35));
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("IncludeNone"), hasNoneBox ));
		 contentPanel.add(choicesPanel);
		 createCorrectAnswerRow9();
			createCommonNavigation( );
		}
		 
	@Override	 
	public void setupUI(Question question, Quiz quiz){
		 if(!(question instanceof CheckboxesQuestion))
			 return;
		 CheckboxesQuestion q = (CheckboxesQuestion)question;
		 super.setupUI(q,quiz);
		 
		 this.colCountField.setText(q.getColCount() +"");
		 this.choicesOrderCombo.setSelectedItem(q.getChoicesOrder());
		 this.hasNoneBox.setSelected(q.isHasNone());
		 choicesPanel.setData(q.getChoices());
	}
	@Override
	public void fromUI(Question question, Quiz quiz) throws R9Exception{
		 if(!(question instanceof CheckboxesQuestion))
			 return;
		 CheckboxesQuestion q = (CheckboxesQuestion)question;
		 super.fromUI(q,quiz);
		 try{
			 q.setColCount(Integer.parseInt( this.colCountField.getText()));
		 }catch(Exception e){}
		 
		 
		 q.setHasNone(hasNoneBox.isSelected());
		 q.setChoicesOrder( (ChoicesOrderType) choicesOrderCombo.getSelectedItem());
	}
}
