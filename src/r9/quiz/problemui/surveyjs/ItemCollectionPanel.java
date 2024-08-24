package r9.quiz.problemui.surveyjs;
 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import r9.quiz.R9SystemSetting;
import r9.quiz.ui.comps.MathJaxImportTextField;
import r9.quiz.ui.comps.MathJaxImportTextFieldWrap;
import r9.quiz.util.PropertyUIHelper;
 

public abstract class ItemCollectionPanel<T> extends JPanel{
 
	private static final long serialVersionUID = 1L;
	List<T> data;
	private CollectionModel collectionModel;
	private JList collectionView;
	private JScrollPane fileScrolPane;
	private JButton addButton;
	private JButton deleteButton;
	protected MathJaxImportTextFieldWrap field1Field;
	protected JTextField field2Field;
	final String f1; final String f2;
	private T curSelected;
	//private int curSelectedIndex;
	private JButton saveButton;
	private JCheckBox correctCheckBox;
	public ItemCollectionPanel(final String f1, final String f2){
		this(f1, f2, true);
	}
	public ItemCollectionPanel(final String f1, final String f2, final boolean showCorrectCheck){
		this.f1 = f1;
		this.f2 = f2;
		ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.problemui.surveyjs.message",R9SystemSetting.LOCALE);
		data = new ArrayList<T>();
		field1Field = new MathJaxImportTextFieldWrap();
		field2Field = new JTextField();
		
		correctCheckBox = new JCheckBox(rb.getString("Key"));
		
		collectionModel = new CollectionModel();
		collectionView = new JList(collectionModel);
		collectionView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		collectionView.setCellRenderer(new FileCellRenderer());
		collectionView.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							ListSelectionModel lsm = (ListSelectionModel) e
									.getSource();
							if (!lsm.isSelectionEmpty()) {
								if( curSelected != null)
								     saveDataFromUI() ;
								 
								curSelected = (T)collectionView.getSelectedValue();
								//curSelectedIndex = collectionView.getSelectedIndex();
								selectData(curSelected, field1Field.textField, field2Field, correctCheckBox);
							}
						}
					}
				});
		collectionView.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		collectionView.setVisibleRowCount(1);
		fileScrolPane = new JScrollPane(collectionView);
		fileScrolPane.setMinimumSize(new Dimension(500,50));
		fileScrolPane.setPreferredSize(new Dimension(500,50));
		fileScrolPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		fileScrolPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		addButton = new JButton(rb.getString("add"));
		addButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				if( curSelected != null) {
					 saveDataFromUI() ;
					    field1Field.textField.setText("");
						field2Field.setText(""); 
						 correctCheckBox.setSelected(false);
						 curSelected = create( );
						 data.add(curSelected); 
						 collectionModel.rebuildList(); 
				} else {
					 curSelected = create( );
					 saveDataFromUI() ;
					 data.add(curSelected); 
					 collectionModel.rebuildList(); 
				} 
			}});
		saveButton = new JButton(rb.getString("save"));
		saveButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				if( curSelected != null) {
					 saveDataFromUI() ; 
					 collectionModel.rebuildList(); 
				} else {
					 curSelected = create( );
					 saveDataFromUI() ;
					 data.add(curSelected); 
					 collectionModel.rebuildList(); 
				}  
			}});
		deleteButton = new JButton(rb.getString("delete"));
		deleteButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				 if(  curSelected == null){
					 //can not happen?
					 return;
				 }  
				data.remove(curSelected);
				collectionModel.rebuildList();
				 curSelected = null;
			//	 curSelectedIndex = -1;
				 field1Field.textField.setText("");
				 field2Field.setText("");
				 correctCheckBox.setSelected(false);
			}});
		JPanel creationPanel = new JPanel();
		creationPanel.setLayout(new BoxLayout(creationPanel, BoxLayout.Y_AXIS));
		if( f2 != null && f2.length() > 0 ) {
			JPanel ap = new JPanel();
			ap.setLayout(new GridLayout(1,2));
			ap.add( PropertyUIHelper.createRow( f1 , field1Field ) );
			ap.add(PropertyUIHelper.createRow(f2, field2Field ));
			creationPanel.add(ap);
		} else {
			 creationPanel.add(PropertyUIHelper.createRow( f1 , field1Field )); 
		}
	   
		
			
		
		if( showCorrectCheck)
		    creationPanel.add(PropertyUIHelper.createRow(correctCheckBox, addButton, saveButton, deleteButton ));
		else
			creationPanel.add(PropertyUIHelper.createRow("", addButton, saveButton, deleteButton ));
		creationPanel.add(PropertyUIHelper.createDashLine(false, 5, 1));
		this.setLayout(new BorderLayout());
		this.add(fileScrolPane, BorderLayout.CENTER);
		this.add(creationPanel, BorderLayout.SOUTH);
		
		configureFields();
	}
	protected void configureFields() { 
	}
	
	public void saveDataFromUI() {
		if( curSelected == null ) {
			return;
		} 
		 String d1 = field1Field.textField.getText();
		 field1Field.textField.setText("");
		 String d2 = (f2 == null || f2.length() == 0) ? null : field2Field.getText(); 
		 field2Field.setText("");
		 if( (d1 == null || d1.length() == 0) && (d2 == null || d2.length() == 0))
			 return; 
		 curSelected = updateData(curSelected, d1, d2, this.correctCheckBox.isSelected());
		 
	}
	
	
	class CollectionModel extends AbstractListModel  {
		private static final long serialVersionUID = 1L;
  
		public CollectionModel() {
			 
		}
  
		private void rebuildList() {  
			this.fireContentsChanged(this, 0, getSize());
		}

		public int getSize() {
			return data.size();
		}

		public T getElementAt(int index) {
			 return data.get(index);
		}
 
	}
	public void setData(List<T> data){
		this.data = data;
		this.collectionModel.rebuildList();
	}
	public abstract T create( );
	public abstract T updateData(T d, String f1, String f2, boolean c1);
	public abstract void selectData(T d, JTextComponent f1, JTextComponent f2, JCheckBox c1);
	
	class FileCellRenderer implements ListCellRenderer {

		public Component getListCellRendererComponent(final JList list,
				final Object value, final int index, final boolean isSelected,
				final boolean cellHasFocus) {
			return new CellPreviewPanel(list, value, index, isSelected,
					cellHasFocus);
		}

		private class CellPreviewPanel extends JLabel {
			private static final long serialVersionUID = -4624762713662343786L;
			private JList list;
			private int index;
			private boolean isSelected;
			private boolean cellHasFocus;
			private T page;

			public CellPreviewPanel(final JList list, final Object value,
					final int index, final boolean isSelected,
					final boolean cellHasFocus) {
				 
				if (!cellHasFocus)
					setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createEmptyBorder(1, 1, 1, 1),
							BorderFactory.createLineBorder(Color.darkGray, 1)));
				else
					setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createLineBorder(Color.red, 1),
							BorderFactory.createLineBorder(Color.darkGray, 1)));

				this.list = list;
				this.page = (T) value;
				this.index = index;
				this.isSelected = isSelected;
				this.cellHasFocus = cellHasFocus;
				this.setText(page.toString());
			} 
			public Dimension getPreferredSize() {
				return new Dimension(60, 30);
			}
		}
	}
}
