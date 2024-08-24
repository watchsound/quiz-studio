package r9.quiz;
 

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
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import r9.quiz.cards.Exam;
import r9.quiz.util.Utils;
 

/** Class for providing a dialog that lets the user load a local image and specify its attributes
  */
public class ProjectMetaDataDialog extends QuizStyleDialog  
{
	 
	private static final long serialVersionUID = 1L;
	JOptionPane jOptionPane;
	JTextArea   descriptionTextArea;
	JTextField  taglistField;
	JTextField  levelField;
	JTextField  subjectField;
	JTextField  durationField; 
	JCheckBox   accessCheckBox;
	JCheckBox    mathjaxCheckBox;
	Exam    exam;
	private JTextField titleField;
    
	public ProjectMetaDataDialog(Frame parent )
	{
		super( parent );
		exam =  CourseCreatingManager.sharedInstance.getExam();
		final ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		 
		final Object[] buttonLabels = { rb.getString("Accept") };
		JPanel panelContents = this.createContentPanel();
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
						exam.setDescription( descriptionTextArea.getText());
						String tagstr =  taglistField.getText();
//					    List<String> tags = new ArrayList<String>();
//						 if ( tagstr != null && tagstr.length() > 0 ){
//						     
//							 String[] ts = tagstr.split(" ");
//							 for(String t : ts){
//								 if (t.trim().length()>0){
//									 tags.add(t);
//								 }
//							 }
//							 
//						 }
//						 exam.setTags(tags);
						 exam.setTagList(tagstr);
						  
						 String title  = titleField.getText().trim();
						 if (   title.length() == 0 )
							 exam.setLevel(" ");
						 else
							  exam.setLevel(title);
						 
						 String level  = levelField.getText().trim();
						 if (   level.length() == 0 )
							 exam.setLevel(rb.getString("Unknown"));
						 else
							  exam.setLevel(level);
						 
						 String subject  = subjectField.getText().trim();
						 if (   subject.length() == 0 )
							 exam.setSubject(rb.getString("Unknown"));
						 else
							  exam.setSubject(subject);
						  
						 String duration  = durationField.getText().trim();
						 if (   level.length() == 0 )
							 exam.setDuration(0);
						 else{
							 try{
								 int dn = Integer.parseInt(duration);
								 exam.setDuration(dn);
							 }catch(Exception ex){
								 exam.setDuration(0);
							 } 
						 }
						  
						 
						
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
	
	private JPanel createContentPanel(){
		final ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		 JPanel contentPanel = new JPanel();
		 contentPanel.setBorder(BorderFactory.createTitledBorder(rb.getString("QuizSystemSetting")));
		 contentPanel.setLayout(new GridBagLayout()); 
			  
			  GridBagConstraints c = new GridBagConstraints();
			  c.fill = GridBagConstraints.HORIZONTAL;
              c.anchor = GridBagConstraints.NORTHWEST;
            //  c.weightx = 1.0;
             // c.gridwidth = GridBagConstraints.REMAINDER;
              c.insets = new Insets(1, 1, 1, 1);
			  
              c.gridx = 0;
 			 c.gridy = 1; 
 			 contentPanel.add(new JLabel(rb.getString("Title")), c);
 			 
 			 c.gridx = 1;
 			 c.gridy = 1; 
 			 this.titleField = new JTextField();
 			 if ( exam.getTitle() != null && exam.getTitle().length() > 0)
 				 this.titleField.setText( exam.getTitle()  );
 			 else
 				 this.titleField.setText( ""  ); 
 			 contentPanel.add(titleField, c);
 			 
 			 
 			 
 
			 c.gridx = 0;
			 c.gridy = 1;
			 c.weightx = 0;
			   
			 contentPanel.add(new JLabel(rb.getString("Description")), c);
			//add(Box.createRigidArea(new Dimension(0, 10)));

			 c.gridx = 0;
			 c.gridy = 2;
			 c.weightx = 1;
			 c.gridwidth = 2;
			 descriptionTextArea  = new JTextArea(15,4);
			 descriptionTextArea.setText( exam.getDescription() );
			 contentPanel.add(descriptionTextArea, c);
		 
			 
			 c.gridwidth = 1;
			 c.gridx = 0;
			 c.gridy = 3;
			 c.weightx = 0.5;
			 JButton forgroundButton = new JButton(rb.getString("CoverImage"));
			 forgroundButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						File whatFile = Utils.browseForImage(".", JFileChooser.OPEN_DIALOG,
								new String[] {"jpg"} , "选择封面图像");
						if (whatFile != null) {
							 
							String filename = CourseCreatingManager.sharedInstance
									.saveCoverOrBackgrdImage(  
											whatFile.getAbsolutePath(), true);
							if (filename == null) {
								JOptionPane.showMessageDialog(null, "引入文件： "
												+ whatFile.getName() + " 失败");
							}
						}
					}
				});
			 contentPanel.add(forgroundButton, c);
			 
			 c.gridx = 1;
			 c.gridy = 3;
			 c.weightx = 0.5;
			 JButton backgroundButton = new JButton(rb.getString("BgImage"));
			 backgroundButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						File whatFile = Utils.browseForImage(".", JFileChooser.OPEN_DIALOG,
								new String[] {"jpg"}, "选择背景图像");
						if (whatFile != null) {
							 
							String filename = CourseCreatingManager.sharedInstance
									.saveCoverOrBackgrdImage(  
											whatFile.getAbsolutePath(), false);
							if (filename == null) {
								JOptionPane.showMessageDialog(null, "引入文件： " + whatFile.getName() + " 失败");
							}
						}
					}
				});
			 contentPanel.add(backgroundButton, c);
			 backgroundButton.setEnabled(false);
			 
			 c.gridx = 0;
			 c.gridy = 4;
			  
			 mathjaxCheckBox  = new JCheckBox(rb.getString("UseMathJax"));
			 mathjaxCheckBox.setSelected(exam.isUseMathJax());
		 
		     mathjaxCheckBox.addActionListener(new ActionListener() { 
				@Override
				public void actionPerformed(ActionEvent e) {
					exam.setUseMathJax(mathjaxCheckBox.isSelected());
				}});
			 contentPanel.add(mathjaxCheckBox, c);
			 
			  
			 c.gridx = 0;
			 c.gridy = 5; 
			 contentPanel.add(new JLabel(rb.getString("Subject")), c);
			 
			 c.gridx = 1;
			 c.gridy = 5; 
			 this.subjectField = new JTextField();
			 if ( exam.getSubject() != null && exam.getSubject().length() > 0)
				 this.subjectField.setText( exam.getSubject()  );
			 else
				 this.subjectField.setText( rb.getString("Unknown") ); 
			 contentPanel.add(subjectField, c);
			 
			 
			 c.gridx = 0;
			 c.gridy =6; 
			 contentPanel.add(new JLabel(rb.getString("Grader")), c);
			 
			 c.gridx = 1;
			 c.gridy = 6; 
			 this.levelField = new JTextField();
			 if ( exam.getLevel() != null && exam.getLevel().length() > 0)
				 this.levelField.setText( exam.getLevel()  );
			 else
				 this.levelField.setText(  rb.getString("Unknown") ); 
			 contentPanel.add(levelField, c);
			 
			 c.gridx = 0;
			 c.gridy = 7; 
			 contentPanel.add(new JLabel(rb.getString("TotalTime")), c);
			 
			 c.gridx = 1;
			 c.gridy = 7; 
			 this.durationField = new JTextField();
			 this.durationField.setText( exam.getDuration()  + "" ); 
			 contentPanel.add(durationField, c);
			 
			 
			 c.gridx = 0;
			 c.gridy = 8; 
			 contentPanel.add(new JLabel(rb.getString("TagsWithTip")), c);
			 
			 c.gridx = 1;
			 c.gridy = 8; 
			 this.taglistField = new JTextField();
//			 List<String> tags = exam.getTags();
//			 if ( tags != null && tags.size() > 0 ){
//				 String tagstr = "";
//				 for(String t : tags){
//					 tagstr += t  +  " " ;
//				 }
//				 taglistField.setText( tagstr );
//			 }
			 taglistField.setText( exam.getTagList() == null ?  "" :  exam.getTagList() );
			 contentPanel.add(taglistField, c);
			 
		 return contentPanel;
	}

	
}
