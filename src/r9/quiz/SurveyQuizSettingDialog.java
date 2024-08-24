package r9.quiz;
 

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import r9.quiz.cards.Exam;
import r9.quiz.component.ImageFileChooser;
import r9.quiz.component.MutableFilter;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.surveyjs.Quiz.ClearInvisibleValuesType;
import r9.quiz.surveyjs.Quiz.ShowProgressBarType;
import r9.quiz.surveyjs.Quiz.ShowQuestionNumbersType;
import r9.quiz.surveyjs.Quiz.ShowTimerPanelModeType;
import r9.quiz.surveyjs.Quiz.ShowTimerPanelType;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.Utils;
 

/** Class for providing a dialog that lets the user load a local image and specify its attributes
  */
public class SurveyQuizSettingDialog extends QuizStyleDialog  
{
	 
	private static final long serialVersionUID = -13926572251036260L;


	JOptionPane jOptionPane;
	
	 
	JTextField  titleField;
	JTextField  maxTimeField;
	JTextField  maxFimePageField;
	JTextArea  startSurveyTextField; 
	JTextArea  completedHtmlField; 
	JCheckBox   firstPageIsStartedBox;
	JCheckBox   showPrevButtonBox;
	JComboBox<ClearInvisibleValuesType> clearInvisibleValuesType;
	JComboBox<ShowQuestionNumbersType> showQuestionNumbersType;
	JComboBox<ShowTimerPanelModeType> showTimerPanelModeType;
	JComboBox<ShowProgressBarType> showProgressBarType;
	JComboBox<ShowTimerPanelType> showTimerPanelType;
	 
	
	Quiz    quiz;


	private ResourceBundle rb;
    
	public SurveyQuizSettingDialog(Frame parent, final Quiz quiz )
	{
		super( parent );
		rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		this.quiz = quiz;
	  
		final Object[] buttonLabels = { rb.getString("Accept") };
		JPanel panelContents = new JPanel();
		panelContents.setLayout(new BoxLayout(panelContents, BoxLayout.Y_AXIS));
		
		  titleField = new JTextField();
		  maxTimeField = new JTextField();
		  maxTimeField.setPreferredSize(new Dimension(100,28));
		  maxTimeField.setMaximumSize(new Dimension(100,28));
		  maxFimePageField = new JTextField();
		  maxFimePageField.setPreferredSize(new Dimension(100,28));
		  maxFimePageField.setMaximumSize(new Dimension(100,28));
		  
		  startSurveyTextField = new JTextArea(); 
		  completedHtmlField = new JTextArea(); 
		   firstPageIsStartedBox = new JCheckBox(rb.getString("FirstPageAutoStart"));
		   
		   showPrevButtonBox = new JCheckBox(rb.getString("ShowReturnPreviousPage"));
		   
		 clearInvisibleValuesType = new JComboBox<ClearInvisibleValuesType>(ClearInvisibleValuesType.values());
		    
		showQuestionNumbersType = new JComboBox<ShowQuestionNumbersType>(ShowQuestionNumbersType.values());
		 
		 showTimerPanelModeType = new JComboBox<ShowTimerPanelModeType>(ShowTimerPanelModeType.values());
 
		 showProgressBarType = new JComboBox<ShowProgressBarType>(ShowProgressBarType.values());
		 
		showTimerPanelType = new JComboBox<ShowTimerPanelType>(ShowTimerPanelType.values());
		 
		
		panelContents.add(PropertyUIHelper.createRow(rb.getString("Title"), titleField, 35));
		panelContents.add(PropertyUIHelper.createRow(rb.getString("MaxTime"), maxTimeField ));
		panelContents.add(PropertyUIHelper.createRow(rb.getString("MaxTimePerPage"), maxFimePageField ));
		panelContents.add(PropertyUIHelper.createRow(rb.getString("StartText"), startSurveyTextField, 100, 400));
	
		panelContents.add(PropertyUIHelper.createRow(rb.getString("EndText"), completedHtmlField, 100, 400));
		
		panelContents.add(PropertyUIHelper.createDashLine(false, 5, 1));
		panelContents.add(PropertyUIHelper.createRow(firstPageIsStartedBox, true));
		panelContents.add(PropertyUIHelper.createRow(showPrevButtonBox, true));
		panelContents.add(PropertyUIHelper.createRow(rb.getString("ResetUnSeenValue"), clearInvisibleValuesType, 35,120));
		panelContents.add(PropertyUIHelper.createRow(rb.getString("ShowNumProblems"), showQuestionNumbersType, 35,120));
	    panelContents.add(PropertyUIHelper.createRow(rb.getString("ShowTimeIndicator"), showTimerPanelModeType, 35,120));
	    panelContents.add(PropertyUIHelper.createRow(rb.getString("ShowTime"), showTimerPanelType, 35,120));
		panelContents.add(PropertyUIHelper.createRow(rb.getString("ShowProgress"), showProgressBarType, 35,120));
		panelContents.add(PropertyUIHelper.createLine(1));
		
		  titleField.setText(quiz.getTitle());
		  maxTimeField .setText(quiz.getMaxTimeToFinish() +"");
		  maxFimePageField .setText(quiz.getMaxTimeToFinishPage() +"");;
		  startSurveyTextField.setText(quiz.getStartSurveyText());; 
		  completedHtmlField .setText(quiz.getCompletedHtml()); 
		   firstPageIsStartedBox.setSelected(quiz.isFirstPageIsStarted());
		   showPrevButtonBox.setSelected(quiz.isShowPrevButton());
		 clearInvisibleValuesType.setSelectedItem(quiz.getClearInvisibleValues());
		showQuestionNumbersType.setSelectedItem(quiz.getShowQuestionNumbers());;
		 showTimerPanelModeType.setSelectedItem(quiz.getShowTimerPanelMode());;
		 showProgressBarType.setSelectedItem(quiz.getShowProgressBar());
		showTimerPanelType.setSelectedItem(quiz.getShowTimerPanel());
		 
		
	   jOptionPane = new JOptionPane(panelContents, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);

		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		jOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				String prop = e.getPropertyName();
				if(isVisible() 
					&& (e.getSource() == jOptionPane)
					&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
				{
					Object value = jOptionPane.getValue();
					if(value == JOptionPane.UNINITIALIZED_VALUE)
					{
						return;
					}
					if(value.equals(buttonLabels[0]))
					{
						  quiz.setTitle(titleField.getText());
						  try{
							  quiz.setMaxTimeToFinish(Integer.parseInt( maxTimeField.getText())) ;
						  }catch(Exception e1){}
                          try{
                        	  quiz.setMaxTimeToFinishPage(Integer.parseInt( maxFimePageField.getText())) ;  
						  }catch(Exception e1){}
						  
                          quiz.setStartSurveyText(startSurveyTextField.getText());
                          quiz.setCompletedHtml(completedHtmlField.getText());
                          quiz.setFirstPageIsStarted(firstPageIsStartedBox.isSelected());
                          quiz.setShowPrevButton(showPrevButtonBox.isSelected());
						 
                          quiz.setClearInvisibleValues((ClearInvisibleValuesType) clearInvisibleValuesType.getSelectedItem());
                          quiz.setShowQuestionNumbers((ShowQuestionNumbersType) showQuestionNumbersType.getSelectedItem());
                          quiz.setShowTimerPanelMode((ShowTimerPanelModeType) showTimerPanelModeType.getSelectedItem());
                          quiz.setShowProgressBar((ShowProgressBarType) showProgressBarType.getSelectedItem());
                          quiz.setShowTimerPanel((ShowTimerPanelType) showTimerPanelType.getSelectedItem());
                          
						setVisible(false);
					} 
					else
					{
						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					}
				}
			}
		});
		// pack();
		
	}
	   
	 

	
}
