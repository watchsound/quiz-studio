package r9.quiz;
 

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import r9.quiz.action.NewAction;
import r9.quiz.action.OpenAction;
import r9.quiz.util.PropertyUIHelper;
 

/** Class for providing a dialog that lets the user load a local image and specify its attributes
  */
public class ProjectLoadingDialog extends JDialog  implements ActionListener
{
	  
	private static final long serialVersionUID = 1L;
	QuizCreatingDialog mainPane;
	private JOptionPane jOptionPane;
    
	public ProjectLoadingDialog(JDialog parent, final QuizCreatingDialog mainPane)
	{
		super(parent);
		setUndecorated(true);
		
		this.mainPane = mainPane;
		  ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
	 
		final Object[] buttonLabels =   { rb.getString("Close") };
		JPanel panelContents =  new JPanel();
		panelContents.setLayout(new BoxLayout(panelContents, BoxLayout.Y_AXIS));
		JButton newButton = new JButton( rb.getString("New"));
		newButton.setActionCommand("New");;
		newButton.addActionListener(this);
		panelContents.add(PropertyUIHelper.createRow(rb.getString("CreateQuiz"), newButton));
		
		JButton newButton2 = new JButton(rb.getString("New"));
		newButton2.setActionCommand("New-surveyjs");;
		newButton2.addActionListener(this);
		panelContents.add(PropertyUIHelper.createRow(rb.getString("CreateSurveyJsQuiz"), newButton2));
		
		
		panelContents.add(PropertyUIHelper.createLine(1));
		
		JButton openButton = new JButton(rb.getString("Open"));
		openButton.setActionCommand("Open");;
		openButton.addActionListener(this); 
		panelContents.add(PropertyUIHelper.createRow(rb.getString("OpenExisting"), openButton));
		 
		String lastOpened = R9SystemSetting.sharedInstance.getLastProjectName();
		if( lastOpened != null) {
			JButton lastButton = new JButton( lastOpened );
			lastButton.setActionCommand("Last"); 
			lastButton.addActionListener(this); 
			panelContents.add(PropertyUIHelper.createRow(rb.getString("OpenLastOne"), lastButton));
		}
		
		panelContents.add(PropertyUIHelper.createLine(2));
		
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
 					    System.exit(0);
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
	
	public void actionPerformed(ActionEvent e) {
		 if (e.getActionCommand().equals("Open")){
			 new OpenAction(mainPane, new OpenAction.OpenActionCallback() {
			 	@Override
				public void onResult(boolean success) {
					 if (success)
						 setVisible(false);
					 else {
						 
					 }
				}
			}).actionPerformed(e);
		 }
		 if (e.getActionCommand().equals("New")){
			 
			 new NewAction( mainPane, false, new NewAction.NewActionCallback() {
				 	@Override
					public void onResult(boolean success) {
					    if (success)
					    	 ProjectLoadingDialog.this.setVisible(false);
					}
				} ).actionPerformed(e);
		 }
         if (e.getActionCommand().equals("New-surveyjs")){
			 
			 new NewAction( mainPane,true, new NewAction.NewActionCallback() {
				 	@Override
					public void onResult(boolean success) {
						  if (success)
						 	 ProjectLoadingDialog.this.setVisible(false);
					}
				} ).actionPerformed(e);
		 }
		 
		 
		 
         if (e.getActionCommand().equals("Last")){
        	 String lastOpened = R9SystemSetting.sharedInstance.getLastProjectName(); 
        	 boolean success = CourseCreatingManager.sharedInstance.load(lastOpened);
			 if ( success ){
				 mainPane.reload();
				 setVisible(false);
			 }
		 }
		 
		 
	}
}
