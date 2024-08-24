package r9.quiz.problemui.surveyjs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import r9.quiz.R9SystemSetting;
import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.ui.comps.MathJaxImportTextFieldWrap;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.R9Exception;
/**
 * private String type  ;
	 private String name ;
     private String title;
     
     private String correctAnswer;  
     
     private boolean isRequired;
     private String visibleIf; 
 */
public class QuestionCommonPanel extends JPanel{
 
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	//private JLabel typeLabel;
	private MathJaxImportTextFieldWrap titleField;
	protected MathJaxImportTextFieldWrap correctAnswerField;
	private JCheckBox isRequiredBox;
	private IfVisibleField visibleIfField;
	//private JCheckBox isBackAllowedCheckBox;
	private JCheckBox isMergeToPreviousCheckBox;
//	private JTextField durationField;
	protected JPanel contentPanel;
	protected MathJaxImportTextFieldWrap wrongFollowField;
	protected ResourceBundle rb;

	public QuestionCommonPanel() {
		rb = ResourceBundle.getBundle("r9.quiz.problemui.surveyjs.message",R9SystemSetting.LOCALE);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(0, 0, 4, 0),
				BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK) ) );
		add(contentPanel, BorderLayout.NORTH);
		add(new JLabel(), BorderLayout.CENTER);
		
	}
	public void createCommonPanel( ){
	//	JPanel panel = new JPanel();
		//panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		nameField = new JTextField();
		nameField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) { 
				 char c = e.getKeyChar(); 
				 if (Character.isAlphabetic(c) ) {  
					 return; 
				 }
				 if( Character.isDigit(c) && nameField.getText().trim().length()>0) {
					 return;   //first one should not be number.... 
				 }
				 e.consume();
			}
		});
	  	
		//typeLabel = new JLabel();
		titleField = new MathJaxImportTextFieldWrap(4, 20);
		correctAnswerField = new MathJaxImportTextFieldWrap();
		wrongFollowField = new MathJaxImportTextFieldWrap();
		visibleIfField = new IfVisibleField();
		isRequiredBox = new JCheckBox(rb.getString("Required"));
		List<JComponent> comps = new ArrayList<JComponent>();
		//comps.add(new JLabel(rb.getString("Type")));
		//comps.add(typeLabel);
		comps.add(new JLabel(rb.getString("Name")));
		comps.add(nameField);
		nameField.setMaximumSize(new Dimension(120,30));
		nameField.setPreferredSize(new Dimension(120,30));
		comps.add(isRequiredBox);
		
		 contentPanel.add(PropertyUIHelper.createRow(comps, 5, true, null));
	//	add(PropertyUIHelper.createRow("Name", nameField, 32));
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("Title"), titleField, 32));
		
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("ConditionForVisible"), visibleIfField, 32));
		 
		 contentPanel.add(PropertyUIHelper.createLine(2));
		//add(PropertyUIHelper.createLeftAlignment(isRequiredBox, false));
		 
		//return panel;
	}
	public void createCorrectAnswerRow9() {
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("CorrectFollow"), correctAnswerField, 32));
		 contentPanel.add(PropertyUIHelper.createRow(rb.getString("WrongFollow"), wrongFollowField, 32));
	}
	public void createCommonNavigation( ){
		//isBackAllowedCheckBox = new JCheckBox("显示返回"); 
 		isMergeToPreviousCheckBox = new JCheckBox(rb.getString("ShownOnPreviousPage")); 
 	//	durationField = new JTextField();
 	//	durationField.setMaximumSize(new Dimension(120,30));
 	//	durationField.setPreferredSize(new Dimension(120,30));
		List<JComponent> comps = new ArrayList<JComponent>();
	//	comps.add(new JLabel("时间(秒):"));
		//comps.add(durationField);
		comps.add(isMergeToPreviousCheckBox);
	//	comps.add(isBackAllowedCheckBox);  
		JPanel navpane = new JPanel();
		navpane.setLayout(new BoxLayout(navpane, BoxLayout.Y_AXIS));
		navpane.add(PropertyUIHelper.createTitleRow(rb.getString("NavigationControl"), true));
		navpane.add(PropertyUIHelper.createRow( "", comps ) );
		add(navpane, BorderLayout.SOUTH);
	}
	
	public void setupUICorrectField(Question question, Quiz quiz){
		correctAnswerField.textField.setText(question.getCorrectAnswer());
	}
	protected void fromUICorrectField(Question question) {
		question.setCorrectAnswer(this.correctAnswerField.getText());
	}
	public void setupUI(Question question, Quiz quiz){
		//typeLabel.setText(question.getType().getSurveyJsID());
		nameField.setText(question.getName());
		titleField.setText(question.getTitle());
		setupUICorrectField(question, quiz);
		visibleIfField.setQuestion(question, quiz);
		isRequiredBox.setSelected(question.isRequired());
	//	durationField.setText(question.getDuration() + "");
		isMergeToPreviousCheckBox.setSelected(question.isMergeWithPreviousOne());
	//	isBackAllowedCheckBox.setSelected(question.isBackAllowed());
		wrongFollowField.setText(question.getWrongFollow());
	}
	
	public void fromUI(Question question, Quiz quiz) throws R9Exception{
	 
		if( !quiz.isUniquePageNames(question, nameField.getText()) ) {
			throw new R9Exception( nameField.getText() + " or Other Problem Names is Not Unique.");
		}
		question.setTitle(titleField.getText());
		question.setName(nameField.getText());
		question.setVisibleIf(this.visibleIfField.getText());
		
		question.setRequired(this.isRequiredBox.isSelected());
		
		fromUICorrectField(question);
		question.setWrongFollow(wrongFollowField.getText());
		try{
		//	question.setDuration(Integer.parseInt(durationField.getText()));
		}catch(Exception ex){}  
		question.setMergeWithPreviousOne(isMergeToPreviousCheckBox.isSelected());
		//question.setBackAllowed(isBackAllowedCheckBox.isSelected());
	}
	
}
