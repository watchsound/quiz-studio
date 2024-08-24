/*
GNU Lesser General Public License

ImageFileDialog
Copyright (C) 2010 Howard Kistler

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package r9.quiz.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import r9.quiz.cards.CardYesNoQuestion;
import r9.quiz.cards.CardPage;
import r9.quiz.util.Translatrix;
import r9.quiz.util.Utils;
 

/**
  * input text format :
  * ##  <-  quizSymbolSelector
  * question content
  * $$ <-  nameBodySymbolSelector
  * A:
  * B:
  * C:
  * #CORRECT     <- answerSymbolSelector
  * A C
  */
public class ImportTextFileDialog extends JDialog 
{
	 
	private List<CardPage> cardListFromText;
	private JOptionPane jOptionPane;
	
	private ImportTextCardSeparator nameBodySymbolSelector;
	private ImportTextCardSeparator answerSymbolSelector;
	private ImportTextCardSeparator quizSymbolSelector;
	private JTextArea  contentArea;
	 
	public ImportTextFileDialog(final Frame parent)
	{
		super(parent, "载入卡片内容", true);
         
		nameBodySymbolSelector = new ImportTextCardSeparator("选择区分Quiz内容和选择的标记", "\t", "\\t", "|","|", "@", "@");
		answerSymbolSelector = new ImportTextCardSeparator("选择正确答案的标记", "\t", "\\t", "#",  "#","|", "|");
		quizSymbolSelector = new ImportTextCardSeparator("选择区分Quiz和Quiz的分隔符号", "\n", "\\n", "@", "@","@@","@@");
		
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.darkGray, 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
       

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createTitledBorder("剪贴卡片内容如下"));
		
		contentArea = new JTextArea(30,40);
		JScrollPane sp = new JScrollPane( contentArea );
		contentPanel.add( sp, BorderLayout.CENTER);
		
		textPanel.add( contentPanel, BorderLayout.CENTER );
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(quizSymbolSelector);
		panel.add(answerSymbolSelector); 
		panel.add(nameBodySymbolSelector);
		
	  
	 
		final Object[] buttonLabels = { Translatrix.getTranslationString("DialogAccept"), Translatrix.getTranslationString("DialogCancel") };
		Object[] panelContents = {
				panel,  textPanel
		};
		jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);

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
						 //acccept
						String errorMessage = createCardList();
						if( errorMessage == null ) {
						    setVisible(false);
						} else {
							JOptionPane.showMessageDialog(parent,errorMessage);
							jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
						}
					}
					else if(value.equals(buttonLabels[1]))
					{
					   //cancel
						setVisible(false);
					}
					else
					{
						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					}
				}
			}
		});
		 pack();
		
	}
    public Dimension getPreferredSize(){
    	return new Dimension(450, 650);
    }
 
 
	String[] cardContentList;
	int curNestLevel = 0;
	protected String createCardList() {
		String nameSep = nameBodySymbolSelector.getSelectedSymbol();
		if ( nameSep == null ||  nameSep.length() == 0 )
			return "问题和选择的分隔符号不能为空";
		String keySep = answerSymbolSelector.getSelectedSymbol();
		if ( keySep == null ||  keySep.length() == 0 )
			return "正确答案节点标识符号不能为空";
		String cardSep = quizSymbolSelector.getSelectedSymbol();
		if ( cardSep == null ||  cardSep.length() == 0 )
			return "题目之间分隔符号不能为空";
		String content = contentArea.getText();
		if ( content == null ||  content.length() == 0 )
			return "题目内容不能为空";
		 
		 
		cardListFromText = new ArrayList<CardPage>();
	 
		cardContentList = content.split("\n");
		  
		boolean afterQuizSep = false;
		boolean afterQuizBody = false;
		boolean afterQuizOptions = false;
		//FIXME
		CardYesNoQuestion card = null;
		boolean isValid = true;
		for( String cardline : cardContentList ){
			if ( cardline == null || cardline.length() == 0 )
				continue;
			try{ 
				if( cardline.startsWith(cardSep)){
					afterQuizSep = true;
					afterQuizBody = false;
					afterQuizOptions = false;
					card = new CardYesNoQuestion();
					cardListFromText.add(card);
					continue;
				}
				if( cardline.startsWith(nameSep)){
					afterQuizSep = false;
					afterQuizBody = true;
					afterQuizOptions = false;
					continue;
				}
				if( cardline.startsWith(keySep)){
					afterQuizSep = false;
					afterQuizBody = false;
					afterQuizOptions = true;
					continue;
				}
				if( card == null ){
					isValid = false;
					continue;
				}
				 
				if( afterQuizSep ){ 
					card.setQuestionBody(cardline);
					continue;
				}
				if( afterQuizBody ){
					//FIXME
				//	card.addOption( cardline );
					continue;
				}
				if( afterQuizOptions ){
					card.setAnswers( cardline );
					card = null;
					continue;
				} 
			}catch(Exception ex){
				ex.printStackTrace();
			 	break;
			}
		}
		if ( ! isValid ){
			return "卡片内容格式有错误";
		} 
		
		return null;
	}

	 
	private String toHtml(String content){
		return "<html><body>" + content + "</html></body>";
	}
	
	private int calNestLevel(String content, String nestSymbol){
		int nestLevel = 0;
		int nextIndex = 0;
		while ( content.substring(nextIndex, nextIndex + nestSymbol.length()).equals(nestSymbol) ){
			nestLevel++;
			nextIndex += nestSymbol.length();
			if ( nextIndex + nestSymbol.length() >= content.length() )
				break;
		}
		return nestLevel;
	}


	public List<CardPage> getCardListFromText() {
		return cardListFromText;
	}

	public void setCardListFromText(List<CardPage> cardListFromText) {
		this.cardListFromText = cardListFromText;
	}

	 

	public String getDecisionValue()
	{
		return jOptionPane.getValue().toString();
	}
	
	
	
	private class ImportTextCardSeparator extends JPanel{
		   
		private static final long serialVersionUID = 1L;
	    private  JTextField   customSymbol;
	    private  JRadioButton option1Radio;
	    private  JRadioButton option2Radio;
	    private  JRadioButton option3Radio;
	    private  JRadioButton option4Radio;
	    private  String option1;
	    private  String option2;
	    private  String option3;
		 
		public   ImportTextCardSeparator(String description,  String option1, String option1s, String option2,
				 String option2s, String option3, String option3s ) {
			 	super (new BorderLayout() );
			 	this.option1 = option1;
			 	this.option2 = option2;
			 	this.option3 = option3;
			 	
				setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(Color.darkGray, 1),
						BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	           
	 
				JPanel controlPanel = new JPanel();
				controlPanel.setBorder(BorderFactory.createTitledBorder(description));
			 	
				controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.LINE_AXIS));
			 	
			 	 
				option1Radio = new JRadioButton(option1s);
				controlPanel.add(option1Radio);
				option2Radio = new JRadioButton(option2s);
				controlPanel.add(option2Radio);
				option3Radio = new JRadioButton(option3s);
				controlPanel.add(option3Radio);
				option4Radio = new JRadioButton("或者");
				controlPanel.add(option4Radio);
				customSymbol = new JTextField(8);
				customSymbol.setPreferredSize(new Dimension(50,20));       
				controlPanel.add( customSymbol );
				
				 ButtonGroup group = new ButtonGroup();
				    group.add(option1Radio);
				    group.add(option2Radio);
				    group.add(option3Radio);
				    group.add(option4Radio);
			 	add( controlPanel, BorderLayout.CENTER ); 
			 	
			 	option1Radio.setSelected(true);
		}

		public String getSelectedSymbol(){
			 if ( option1Radio.isSelected() )
				 return option1;
			 if ( option2Radio.isSelected())
				 return option2;
			 if ( option3Radio.isSelected())
				 return option3;
			 return customSymbol.getText().replace("\\\\", "\\");
		}
		 
		public Dimension getMaximumSize(){
			return new Dimension(650,80);
		}
		
	}
}
