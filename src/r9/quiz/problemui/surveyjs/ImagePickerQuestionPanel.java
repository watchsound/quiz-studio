package r9.quiz.problemui.surveyjs;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import r9.quiz.component.ImportedImagesPanel;
import r9.quiz.surveyjs.BooleanQuestion;
import r9.quiz.surveyjs.CheckboxesQuestion;
import r9.quiz.surveyjs.DropdownQuestion;
import r9.quiz.surveyjs.ImagePickerQuestion;
import r9.quiz.surveyjs.ImagePickerQuestion.ImageLink;
import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.surveyjs.Question.ChoicesOrderType;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.R9Exception;

public class ImagePickerQuestionPanel extends QuestionCommonPanel{
 
	private static final long serialVersionUID = 1L;
	private JComboBox<ChoicesOrderType> choicesOrderCombo;
	private JTextField colCountField; 
	private ItemCollectionPanel<ImageLink> choicesPanel;
	private JCheckBox showLabelBox;
	public ImagePickerQuestionPanel(){
		 
		choicesOrderCombo = new JComboBox<ChoicesOrderType>(ChoicesOrderType.values());
		colCountField = new JTextField();
		choicesOrderCombo.setPreferredSize(new Dimension( 100,32));
		choicesOrderCombo.setMaximumSize(new Dimension( 100,32));
		colCountField.setPreferredSize(new Dimension( 100,32));
		colCountField.setMaximumSize(new Dimension( 100,32));
	 
		
		
		showLabelBox = new JCheckBox(rb.getString("ShowNote"));
		
		choicesPanel = new ItemCollectionPanel<ImageLink>(rb.getString("Value"), rb.getString("Link")){ 
			private static final long serialVersionUID = 1L;
			@Override
			public ImageLink updateData(ImageLink t, String f1, String f2, boolean c1) {
				 t.value = f1;
				 t.imageLink = f2;
				 t.correct = c1;
				 return t;
			}

			@Override
			public void selectData(ImageLink d, JTextComponent f1, JTextComponent f2, JCheckBox c1) {
				 f1.setText(d.value);
				 f2.setText(d.imageLink);
				 c1.setSelected(d.correct);
			}
			
			protected void configureFields() { 
				super.field2Field.addMouseListener(new MouseAdapter() {
					 public void mousePressed(MouseEvent e) {
						 if( !e.isPopupTrigger())
							 return;
						 showPopup(e);
					 }
					 public void mouseReleased(MouseEvent e) {
						 if( !e.isPopupTrigger())
							 return;
						 showPopup(e);
					 }
					 private void showPopup(MouseEvent e) {
						 final JPopupMenu menu = new JPopupMenu();
						 ImportedImagesPanel.CALLBACK callback = new ImportedImagesPanel.CALLBACK() { 
							public void onSelection(File file) { 
								field2Field.setText(file.getName());
								menu.setVisible(false);
							}};
							
					    ImportedImagesPanel panel = new ImportedImagesPanel(callback);	 
						 menu.add(panel);
						 menu.show(field2Field, e.getX(), e.getY());
					 }
				});
			}

			@Override
			public ImageLink create() {
				return new ImageLink("", "");
			}};
		  
			 createCommonPanel(   );
			 contentPanel.add(PropertyUIHelper.createRow(rb.getString("OptionsInOneLine"), colCountField, 35));
			 contentPanel.add(PropertyUIHelper.createRow(rb.getString("OptionsOrder"), choicesOrderCombo, 35));
			 contentPanel.add(PropertyUIHelper.createRow(showLabelBox, true));
			 contentPanel.add(choicesPanel);
			 createCorrectAnswerRow9();
				createCommonNavigation( );
	}
	
	@Override
	public void setupUI(Question question, Quiz quiz){
		 if(!(question instanceof ImagePickerQuestion))
			 return;
		 ImagePickerQuestion q = (ImagePickerQuestion)question;
		 super.setupUI(q,quiz);
		 
		 this.colCountField.setText(q.getColCount() +"");
		 this.choicesOrderCombo.setSelectedItem(q.getChoicesOrder());
		 showLabelBox.setSelected(q.isShowLabel());
		 choicesPanel.setData(q.getChoices());
	}
	@Override
	public void fromUI(Question question, Quiz quiz) throws R9Exception{
		 if(!(question instanceof ImagePickerQuestion))
			 return;
		 ImagePickerQuestion q = (ImagePickerQuestion)question;
		 super.fromUI(q, quiz);
		 q.setColCount(Integer.parseInt( this.colCountField.getText()));
		 q.setShowLabel(showLabelBox.isSelected());
		 q.setChoicesOrder( (ChoicesOrderType) choicesOrderCombo.getSelectedItem());
	}
}
