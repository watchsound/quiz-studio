package r9.quiz.problemui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import r9.quiz.Constants;
import r9.quiz.R9SystemSetting;
import r9.quiz.WebEditor;
import r9.quiz.cards.CardChoiceQuestion;
import r9.quiz.ui.comps.MathJaxImportTextFieldWrap;
import r9.quiz.util.ImageUtil;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.Utils;

public 	class SimpleChoiceCreatorPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CardChoiceQuestion question;
	WebEditor questionBody;

	MathJaxImportTextFieldWrap[] optionsList;
	String[] optionLabelList = { "A", "B", "C", "D", "E" };

	JLabel  answerBox;
	
	JTextField tagLevel1Field;
	JTextField tagLevel2Field;
	JTextField scoreField; 
	JCheckBox[]  correctList;
	private JTextField nameField;
	private ResourceBundle rb;

	public SimpleChoiceCreatorPanel() {

		super();
	    rb = ResourceBundle.getBundle("r9.quiz.problemui.message",R9SystemSetting.LOCALE);
		
		setBorder( BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.darkGray, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10) 
				 ));
		
		setLayout(new GridBagLayout());
		int row = -1;
		GridBagConstraints c = new GridBagConstraints();
		row++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		add(new JLabel(rb.getString("Title")), c);
		
		nameField = new JTextField();
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = row;
		c.gridwidth = 3; 
		c.gridheight = 1; 
		add(nameField, c);
		  
		questionBody = new WebEditor( null, Constants.CARD_WIDTH, 240); 
		  
		row++;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 6; 
		c.gridheight = 7; 
		add(questionBody, c);
		 
		row  += 6;
	//	JPanel optionPanel = new JPanel(new GridBagLayout());
		
		optionsList = new MathJaxImportTextFieldWrap[optionLabelList.length];
		for (int i = 0; i < optionLabelList.length; i++)
			optionsList[i] = new MathJaxImportTextFieldWrap();
		 
		correctList = new JCheckBox[optionLabelList.length];
		for (int i = 0; i < optionLabelList.length; i++)
			correctList[i] = new JCheckBox();
		
		
		c.ipady =1;
		c.insets = new Insets(2,2,2,2);
		for (int i = 0; i < optionLabelList.length; i++) {
			row++;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0;
			c.weighty = 0;
			c.gridx = 0;
			c.gridy = row;
			c.gridwidth = 1; 
			c.gridheight = 1; 
			 add(new JLabel(optionLabelList[i] + ") : "), c);

			 
				c.weightx = 0;
				c.weighty = 0;
				c.gridx = 1;
				c.gridy = row;
				c.gridwidth = 1; 
				c.gridheight = 1; 
				add(correctList[i], c); 
			 
			c.weightx = 1;
			c.weighty = 0;
			c.gridx = 2;
			c.gridy = row;
			c.gridwidth = 3; 
			c.gridheight = 1; 
			add(optionsList[i], c);
			
		
			
			 
		}
		row++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0 ;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		add(new JLabel( rb.getString("Key") ), c);

		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		answerBox = new JLabel ( );
		add( answerBox, c);
		
		tagLevel1Field = new JTextField();
		tagLevel2Field = new JTextField();
		JPanel feedbackRow = new JPanel();
		feedbackRow.setLayout(new GridLayout(1,2));
		feedbackRow.add(PropertyUIHelper.createRow(rb.getString("CorrectFollow"), tagLevel1Field ));
		feedbackRow.add(PropertyUIHelper.createRow(rb.getString("WrongFollow"), tagLevel2Field ));
		
		row++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1 ;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 8; 
		c.gridheight = 1; 
		add(feedbackRow, c);

	    
		row++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		add(new JLabel( rb.getString("Score") ), c);

		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		scoreField = new JTextField();
		 add( scoreField, c);

	 
		
	}
	
	public void resetUIStates(CardChoiceQuestion question ){//, final boolean isBackAllowed){
		if ( this.question != null ){
			populateModelFromUI( );
		}
		 this.question = question;
		 questionBody.resetUIStates(question);
		 String[] options = question.getOptions();
		 for (int i = 0; i < optionsList.length; i++ ){
			 if ( options != null && i < options.length ){
				 optionsList[i].setText(options[i]);
			 } else {
				 optionsList[i].setText("");
			 }
			 correctList[i].setSelected( question.getAnswers().indexOf(this.optionLabelList[i])>=0);
			 
		 }
 	     answerBox.setText( question.getAnswers() );
 	     tagLevel1Field.setText( question.getCorrectFollow() );
 	     tagLevel2Field.setText( question.getWrongFollow() );
 	     scoreField.setText( question.getScore() + "" );
 	     nameField.setText( question.getName() ); 
	}
	public BufferedImage getImagePreview(){
		    BufferedImage myImage = (BufferedImage) this. createImage(
		    		this.getWidth(),  this.getHeight());
        	Graphics g = myImage.getGraphics();
        	this.print(g); 
			//resize 
			BufferedImage thumbImage =  ImageUtil.resizeImage(myImage, Constants.CARD_PREVIEW_WIDTH, Constants.CARD_PREVIEW_HEIGHT, false, false);
		 	return thumbImage;
	}
	
	public void populateModelFromUI( ){
		if( question == null)
			return;
		questionBody.populateModelFromUI( );
		
		question.setAnswers( Utils.fromCheckBox( correctList ) );
		List<String> options = new ArrayList<String>();
		for(int i = 0; i < optionsList.length; i++){
			String option = optionsList[i].getText();
			if ( option != null && option.length() > 0 )
				options.add(option); 
		}
		question.setOptions( options.toArray(new String[]{}));
		question.setPreviewImage(getImagePreview());
		question.setCorrectFollow( tagLevel1Field.getText() );
		question.setWrongFollow( tagLevel2Field.getText() );
		try{
			int level = Integer.parseInt( scoreField.getText() );
			question.setScore(level);
		}catch(Exception ex){
			question.setLevel( 0 );
		}
		question.setName( nameField.getText() );
		//question.setBackAllowed(canBack);
		
	}
	public Dimension getPreferredSize() {
		return new Dimension(Constants.CARD_PREVIEW_WIDTH * 2,
				Constants.CARD_PREVIEW_HEIGHT * 4);
	}
}