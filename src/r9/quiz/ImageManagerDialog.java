package r9.quiz;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
 
import javax.swing.JOptionPane;

import r9.quiz.component.ImportedImagesPanel;

public class ImageManagerDialog extends QuizStyleDialog{
	 
	private static final long serialVersionUID = 1L;
	private ImportedImagesPanel importedImagesPanel;
	public ImageManagerDialog() {
		 ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		final Object[] buttonLabels = {   rb.getString("Close") };
		importedImagesPanel = new ImportedImagesPanel(null);
		JOptionPane jOptionPane = new JOptionPane(importedImagesPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);

		setContentPane(jOptionPane);
		 

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
					jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					if(value.equals(buttonLabels[0]))
					{
					 setVisible(false);
					} 
					else
					{
						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					}
				}
			}
		});
		 	
    }
}
