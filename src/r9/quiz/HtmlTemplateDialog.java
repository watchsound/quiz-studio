package r9.quiz;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import r9.quiz.cards.HtmlTemplate;
import r9.quiz.cards.HtmlTemplateList;
import r9.quiz.cards.QuestionType;
import r9.quiz.component.MultiComboBox;
import r9.quiz.util.PropertyUIHelper;
 

public class HtmlTemplateDialog extends QuizStyleDialog{
 
	private static final long serialVersionUID = 1L;
	private TemplateFileModel templateFileModel;
	private JList templateFileModelListView;
	private JScrollPane templateListScrolPane;
	public HtmlTemplateList metadata;
	private JTextField templateNameField;
	private JComboBox templateTypesCombox;
	private RSyntaxTextArea templatContentArea;
	protected HtmlTemplate curSelected;
	private MultiComboBox jsCombobox;
	private MultiComboBox cssCombobox;
	private JScrollPane templateContentScrolPane;


	public HtmlTemplateDialog(){
		 ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		 
		templateFileModel = new TemplateFileModel();
		templateFileModelListView = new JList(templateFileModel);
		templateFileModelListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		templateFileModelListView.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
								ListSelectionModel lsm = (ListSelectionModel) e
										.getSource();
								if (!lsm.isSelectionEmpty()) {
 								int row = lsm.getLeadSelectionIndex();
 								     if(!updateFromUI())
 									     JOptionPane.showMessageDialog(HtmlTemplateDialog.this, rb.getString("EmptyName"));;
 								     curSelected = templateFileModel.getElementAt(row);
 								     updateFromData();
								}
							}
						}
					}); 
			
		   templateFileModelListView.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		   templateFileModelListView.setVisibleRowCount(-1);
			
			templateListScrolPane = new JScrollPane(templateFileModelListView);
			templateListScrolPane
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			templateListScrolPane
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			templateListScrolPane.setPreferredSize(new Dimension(600,150));
			
			templateNameField = new JTextField(); 
			
			templateTypesCombox = new JComboBox(QuestionType.values());
			
			templatContentArea = new RSyntaxTextArea(10,20);
			templatContentArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
			templatContentArea.setCodeFoldingEnabled(true);
			
			templateContentScrolPane = new JScrollPane(templatContentArea);
			templateContentScrolPane
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			templateContentScrolPane
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			templateContentScrolPane.setPreferredSize(new Dimension(600,350));
			
			
			JButton saveChangeButton = new JButton(rb.getString("Save"));
			saveChangeButton.addActionListener(new ActionListener(){ 
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!updateFromUI()){
						JOptionPane.showMessageDialog(HtmlTemplateDialog.this,  rb.getString("EmptyName"));;
					}
					templateFileModel.rebuildList();
					}});
			JButton addNewButton = new JButton("添加");
			addNewButton.addActionListener(new ActionListener(){ 
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!updateFromUI())
						JOptionPane.showMessageDialog(HtmlTemplateDialog.this,  rb.getString("EmptyName"));;
				    curSelected = new HtmlTemplate();
					metadata.getTemplates().add(curSelected);
					updateFromData();
					templateFileModel.rebuildList();
				}});
			JPanel panelContents = new JPanel();
			panelContents.setLayout(new BoxLayout(panelContents, BoxLayout.Y_AXIS));
			panelContents.add(templateListScrolPane);
			 
			JPanel metaRow = new JPanel();
			metaRow.setLayout(new GridLayout(1,4));
			metaRow.add(new JLabel(rb.getString("TemplateName")));
			metaRow.add( templateNameField );
			metaRow.add(new JLabel(rb.getString("ProblemType")));
			metaRow.add( templateTypesCombox );
			 
			jsCombobox = new MultiComboBox(R9SystemSetting.sharedInstance.getJsFilesInArray(), new String[]{ "" });  
			cssCombobox = new MultiComboBox(R9SystemSetting.sharedInstance.getCssFilesInArray(), new String[]{ "" });  
			JPanel metaRow2 = new JPanel();
			metaRow2.setLayout(new GridLayout(1,4));
			metaRow2.add(new JLabel(rb.getString("JSFile")));
			metaRow2.add( jsCombobox );
			metaRow2.add(new JLabel(rb.getString("CSSFile")));
			metaRow2.add( cssCombobox );
	          
			panelContents.add( metaRow );
			panelContents.add( metaRow2 );
			panelContents.add( templateContentScrolPane );
			panelContents.add(PropertyUIHelper.createRow(saveChangeButton, addNewButton, false, null));
			panelContents.setBorder(BorderFactory.createLoweredBevelBorder());
			
			if( metadata.getTemplates().isEmpty() ){
				curSelected = new HtmlTemplate();
				metadata.getTemplates().add(curSelected);
			} else {
				templateFileModelListView.setSelectedIndex(0);
			}
			
			final Object[] buttonLabels = { rb.getString("Save"), rb.getString("Close") };
			
			JOptionPane jOptionPane = new JOptionPane(panelContents, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);

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
						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
						if(value.equals(buttonLabels[0]))
						{
							if(!updateFromUI()){
								JOptionPane.showMessageDialog(HtmlTemplateDialog.this,  rb.getString("EmptyName"));
								return;
							}
						    R9SystemSetting.getInstance().saveHtmlTemplateMetaData(metadata);  
						 	setVisible(false);
						}
						else if(value.equals(buttonLabels[1]))
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
			 pack();		
	} 
	
	public boolean updateFromUI(){
		if( curSelected == null) return true;
		if( templateNameField.getText().length() == 0)
			return false;
		if( templatContentArea.getText().length() == 0)
			return false;
		curSelected.setName( templateNameField.getText() );
		curSelected.setJsFiles( jsCombobox.getSelectedValues());
		curSelected.setCssFiles( this.cssCombobox.getSelectedValues());
		curSelected.setProblemType(( (QuestionType)templateTypesCombox.getSelectedItem()));
	    File f =   new File(R9SystemSetting.getInstance().getHtmlTemplateDir(), curSelected.getFileName());
	    if( f.exists() )
	    	f.delete();
	    try {
			R9SystemSetting.stringToFile( templatContentArea.getText(), f);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return false;
	}
	public void updateFromData(){
		if( curSelected == null) return;
		templateNameField.setText( curSelected.getName( ) );
		jsCombobox.rebuild( R9SystemSetting.sharedInstance.getJsFilesInArray(), 
				curSelected.getJsFilesInArray()  );
		cssCombobox .rebuild( R9SystemSetting.sharedInstance.getCssFilesInArray(), 
				curSelected.getCssFilesInArray()  );
		
		curSelected.setJsFiles( jsCombobox.getSelectedValues());
		curSelected.setCssFiles( this.cssCombobox.getSelectedValues());
	
		
		templateTypesCombox.setSelectedItem(  curSelected.getProblemType()   ); 
	    File f =   new File(R9SystemSetting.getInstance().getHtmlTemplateDir(), curSelected.getFileName());
	    if( !f.exists() ) {
	    	templatContentArea.setText("");
	    	return ;
	    }
	    try {
			String content = R9SystemSetting.fileToString(f);
			templatContentArea.setText(content);
		} catch (Exception e) {
		 	e.printStackTrace();
		}
		 
	}
	
	
	  public  class TemplateFileModel extends AbstractListModel {
	 		private static final long serialVersionUID = 1L;
	 		 
	 		public TemplateFileModel() {
	 			metadata = R9SystemSetting.getInstance().getHtmlTemplateMetaData();
	             
	 		}
	 		 
	 		
	 		 
	 		private void rebuildList() {
	 			this.fireContentsChanged(this, 0, getSize());
	 		}

	 		 
	 		
	 		public void delete(int index ){
	 			if ( metadata.getTemplates().size() == 0 || index < 0 || index >= metadata.getTemplates().size())
	 				return;
	 			HtmlTemplate t = metadata.getTemplates().get(index);
	 			File f = new File(R9SystemSetting.getInstance().getHtmlTemplateDir(), t.getFileName());
	 			f.delete();
	 			 metadata.getTemplates().remove(index);
	 	 		rebuildList();
	 		} 
	 		
	 		public int getSize() {
	 			return metadata.getTemplates().size();
	 		}
	 		 

	 		public HtmlTemplate getElementAt(int index) {
	 			return   metadata.getTemplates().get(index);
	 		}
	 	}
}
