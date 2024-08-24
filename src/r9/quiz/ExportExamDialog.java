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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import r9.quiz.action.NewAction;
import r9.quiz.action.OpenAction;
import r9.quiz.cards.Exam;
import r9.quiz.component.ImageFileChooser;
import r9.quiz.component.MutableFilter;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.Utils;
 

/** Class for providing a dialog that lets the user load a local image and specify its attributes
  */
public class ExportExamDialog extends QuizStyleDialog  
{
	 
	private static final long serialVersionUID = 1L;
	private JOptionPane jOptionPane;
	private JTextField libDirField;
	private JButton currentDirButton;
	private JButton parentDirButton;
	private JButton parent2DirButton;
	private Exam exam;
	 
    
	public ExportExamDialog(Frame parent )
	{
		super(parent); 
		
		exam = CourseCreatingManager.sharedInstance.getExam();
		  ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
	 
		final Object[] buttonLabels =   { rb.getString("Close"), rb.getString("Export") };
		JPanel panelContents =  new JPanel();
		panelContents.setLayout(new BoxLayout(panelContents, BoxLayout.Y_AXIS));
		 
		panelContents.add(PropertyUIHelper.createTitleRow(rb.getString("ExportLibDir")));
		libDirField = new JTextField(exam.getLibDirectory());
		
		
		panelContents.add(PropertyUIHelper.createRow(libDirField, false));
		currentDirButton = new JButton(rb.getString("CurrentDir"));
		currentDirButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				libDirField.setText("");
			}});
		parentDirButton = new JButton(rb.getString("ParentDir"));
		parentDirButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				libDirField.setText("../");
			}});
		parent2DirButton = new JButton(rb.getString("GrandParentDir"));
		parent2DirButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				libDirField.setText("../../");
			}});
		panelContents.add(PropertyUIHelper.createRow("", currentDirButton,parentDirButton,parent2DirButton));
		 
		
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
						 setVisible(false);
					}
					else if(value.equals(buttonLabels[1]))
					{  
						export();
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
    public void export() {
    	String libdir = this.libDirField.getText().trim();
    	exam.setLibDirectory(libdir);
    	File courseDir = new File( CourseCreatingManager.sharedInstance.getCourseFolder() );
    	File out = new File( courseDir, "export");
		if( out.exists() ) out.delete();
		out.mkdir();
		for(File f : courseDir.listFiles()) {
			if( f.isDirectory())
				continue;
			String name = f.getName();
			if( name.endsWith(".r9") || name.endsWith(".r9json") || name.endsWith("json") || name.endsWith(".html") )
				continue;
			try {
				Utils.copyFileToFile(f, new File(out, name));
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}
		String libDir = exam.getLibDirectory();
		if( libDir == null || libDir.length() == 0) {
			File systemDir = R9SystemSetting.sharedInstance.getThirdPartyDir();
			List<String> libs = HtmlCodeRender.getLibs(exam);
			for(String lib : libs) {
				File s = new File(systemDir, lib);
				File o = new File(out, lib);
				try {
					Utils.copyFileToFile(s,o);
				} catch (IOException e) { 
					e.printStackTrace();
				}
			}
		}
		String htmlCode = new HtmlCodeRender().renderHtmlCode( exam, true);
		File out2 = new File( out, "index.html"); 
		try {
			R9SystemSetting.stringToFile(htmlCode, out2); 
		} catch (IOException e) {
			 e.printStackTrace();
		}
		Utils.setClipboardString(out.getAbsolutePath());
		 ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		JOptionPane.showMessageDialog(this, rb.getString("ExportPath") + " ；\n " + out.getAbsolutePath() 
		+ "\n " + rb.getString("SavedToClipboard"));
    }
}
