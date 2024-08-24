package r9.quiz.problemui.surveyjs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import r9.quiz.CourseCreatingManager;
import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.util.Utils;

public class IfVisibleField extends JTextField{
 
	private static final long serialVersionUID = 1L;
	private Question question;
	private Question lastSelectedQuestion;
	private Quiz quiz;
	public IfVisibleField() {
		this.addMouseListener(new MouseAdapter() {
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
				 String text = Utils.trimTrailing( getText() );
				 if( text .length() == 0   ) {
					   showNames(e);
					   return;
				 }
				 char lastchar = text.charAt( text.length()-1 );
				 if( lastchar == '='  || lastchar == '>' || lastchar == '<' ) {
					   showValues(e);
					   return;
				 }
				  
				 if( lastSelectedQuestion != null) {
					 showOps(e);
					 return;
				 }
				 if( text.endsWith("and") || text.endsWith("or") )
				     showNames(e); 
				 else
					 showAndOr(e);
			 }
		});
		setPreferredSize(new Dimension(500,32));
		setMinimumSize(new Dimension(500,32));
	}
	public void setQuestion(Question question, Quiz quiz) {
		this.question = question;
		this.quiz = quiz;
		this.setText(question.getVisibleIf());
		lastSelectedQuestion = null;
	}
	public void append(String text) {
		this.setText( this.getText() + " " + text);
	}
	protected void showOps(MouseEvent e) {
		 JPopupMenu menu = new JPopupMenu();
		 final JMenuItem item1 = new JMenuItem("=");
		 item1.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 		append("=");
			}}); 
		menu.add(item1); 
		
		 final JMenuItem item2 = new JMenuItem("!=");
		 item2.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 		append("!=");
			}}); 
		menu.add(item2); 
		
		 final JMenuItem item3 = new JMenuItem(">");
		 item3.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 		append(">");
			}}); 
		menu.add(item3); 
		
		 final JMenuItem item4 = new JMenuItem("<");
		 item4.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 		append("<");
			}}); 
		menu.add(item4); 
		
		 final JMenuItem item5 = new JMenuItem(">=");
		 item5.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 		append(">=");
			}}); 
		menu.add(item5); 
		
		 final JMenuItem item6 = new JMenuItem("<=");
		 item6.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 		append("<=");
			}}); 
		menu.add(item6); 
		
		
		menu.show(this, e.getX(), e.getY());
		
	}
	protected void showAndOr(MouseEvent e) {
		 JPopupMenu menu = new JPopupMenu();
		 final JMenuItem item1 = new JMenuItem("and");
		 item1.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 		append(" and ");
			}}); 
		menu.add(item1); 
		
		 final JMenuItem item2 = new JMenuItem("or");
		 item2.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 		append(" or ");
			}}); 
		menu.add(item2);  
		menu.show(this, e.getX(), e.getY()); 
	}
	protected void showValues(MouseEvent e) {
		if( lastSelectedQuestion == null)
			return;
		List<String> names =  lastSelectedQuestion.getValues();
		 JPopupMenu menu = new JPopupMenu();
		 for(int i = 0; i < names.size(); i++) {
			 final String name = names.get(i);
			 final JMenuItem item1 = new JMenuItem(name);
			 item1.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					 try {
						 if( "true".equals(name) || "false".equals(name)) {
							 setText(  getText() + " {" + name + "} ");
						 } else {
							 Double.parseDouble(name);
							 setText(  getText() + " {" + name + "} ");
						 } 
					 }catch(Exception ex) {
						 setText(  getText() + " {\"" + name + "\"} "); 
					 } 	 
					 lastSelectedQuestion = null;
				}}); 
			menu.add(item1);  
		 }
		 menu.show(this, e.getX(), e.getY());
	}
	protected void showNames(MouseEvent e) {
		List<String> names =  quiz.getPageNames(question);
		 JPopupMenu menu = new JPopupMenu();
		 for(int i = 0; i < names.size(); i++) {
			 final String name = names.get(i);
			 final JMenuItem item1 = new JMenuItem(name);
			 item1.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						 append( name );
						 lastSelectedQuestion = quiz.getByName(name);
					}}); 
				menu.add(item1);   
		 }
		 menu.show(this, e.getX(), e.getY());
	}
}
