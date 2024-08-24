package r9.quiz.problemui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import r9.quiz.Constants;
import r9.quiz.R9SystemSetting;
import r9.quiz.WebEditor;
import r9.quiz.cards.CardYesNoQuestion;
import r9.quiz.util.ImageUtil;
import r9.quiz.util.Utils;

public class YesNotCreatorPanel extends ProblemUI {
 
	private static final long serialVersionUID = 1L;

	CardYesNoQuestion question;
	
	WebEditor questionBody;
 

	JCheckBox  answerBox;
	
	JTextField tagLevel1Field;
	JTextField tagLevel2Field;
	JTextField scoreField; 
	private JTextField nameField;

	public YesNotCreatorPanel() {

		super();
		final ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.problemui.message",R9SystemSetting.LOCALE);
		
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
		
		questionBody = new WebEditor( null, Constants.CARD_WIDTH, 380); 
		
		row++;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 4; 
		c.gridheight = 10; 
		add(questionBody, c);
		 
		row  += 9;
		this.answerBox = new JCheckBox(rb.getString("CorrectOnCheck"));
 
		c.ipady =1;
		c.insets = new Insets(2,2,2,2);
	 
		row++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		add( this.answerBox, c);
  
		 
		
		row++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0 ;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		add(new JLabel( rb.getString("CorrectFollow") ), c);

		c.weightx =1;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		tagLevel1Field = new JTextField();
		add( tagLevel1Field, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0 ;
		c.weighty = 0;
		c.gridx = 2;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		add(new JLabel( rb.getString("WrongFollow") ), c);

		c.weightx =1;
		c.weighty = 0;
		c.gridx = 3;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		tagLevel2Field = new JTextField();
		add( tagLevel2Field, c);
		
		row++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		add(new JLabel(rb.getString("Score") ), c);

		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = row;
		c.gridwidth = 1; 
		c.gridheight = 1; 
		scoreField = new JTextField();
		 add( scoreField, c);

		
		 row++; 
	}
	
	public void resetUIStates(CardYesNoQuestion question ){//, final boolean isBackAllowed){
		if ( this.question != null ){
			populateModelFromUI( );
		}
		 this.question = question;
		 questionBody.resetUIStates(question);
		 this.answerBox.setSelected( question.isCorrect() );
		  
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
		question.setCorrect( this.answerBox.isSelected() );
	  
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