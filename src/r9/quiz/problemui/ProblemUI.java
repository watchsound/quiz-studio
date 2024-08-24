package r9.quiz.problemui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;

import r9.quiz.CourseCreatingManager;
import r9.quiz.R9SystemSetting;
import r9.quiz.cards.CardYesNoQuestion;
import r9.quiz.cards.CardPage;
import r9.quiz.component.ImagePicker;
import r9.quiz.surveyjs.Question;

public class ProblemUI extends JPanel{
 
	private static final long serialVersionUID = 1L;

	public static JButton createImageButton(final int order){
		final ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.problemui.message",R9SystemSetting.LOCALE);
		CardPage page = CourseCreatingManager.sharedInstance.getCurrentPage();
		String title = rb.getString("Setup");
		
		final JButton image1Button = new JButton(title);
		if( page != null ) {
			String name = page.getImage(order)  ;
			 image1Button.putClientProperty("r9imagename",name);
			if( name != null && name.length() > 0 )
				image1Button.setText(name);
			
		}
		
		image1Button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CardPage page = CourseCreatingManager.sharedInstance.getCurrentPage();
				ImagePicker.Callback callback = new ImagePicker.Callback() { 
					@Override
					public void select(File file) { 
					     page.setImage(order, file == null ? null : file.getName()); 
					     image1Button.setText( file == null ? rb.getString("Setup") : file.getName());
					     if( file == null )
					    	 image1Button.putClientProperty("r9imagename",null);
					     else
					         image1Button.putClientProperty("r9imagename", file.getName());
					}
					
					@Override
					public void delete() {
						String imageName = page.getImage(order);
						CourseCreatingManager.sharedInstance.deleteImage(imageName);
						page.deleteImage(order);
						image1Button.setText(rb.getString("Setup"));
						image1Button.putClientProperty("r9imagename",null);
					}
				};
				ImagePicker.showPopupMenu(image1Button, callback); 
				
			}});
		return image1Button;
	}
	public void populateModelFromUI( ){}
	
	public void resetUIStates(Question question ){}
}
