package r9.quiz.component;
 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import r9.quiz.CourseCreatingManager;
import r9.quiz.R9SystemSetting;
import r9.quiz.util.Utils;
 

public class ImportedImagesPanel extends JPanel{
    
	private static final long serialVersionUID = 1L;

	public static   interface CALLBACK {
    	void onSelection(File file);
    }
	List<File> files;
	private FileListModel fileListModel;
	private JList fileListView;
	private JScrollPane fileScrolPane;
	private JButton addButton;
	private JButton deleteButton;
	private ResourceBundle rb;
	public ImportedImagesPanel(final CALLBACK callback){
		setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
		files = CourseCreatingManager.sharedInstance.getAllImageFiles( );
		
		rb = ResourceBundle.getBundle("r9.quiz.message", R9SystemSetting.LOCALE);
		fileListModel = new FileListModel();
		fileListView = new JList(fileListModel);
		fileListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileListView.setCellRenderer(new FileCellRenderer());
		fileListView.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							ListSelectionModel lsm = (ListSelectionModel) e
									.getSource();
							if (!lsm.isSelectionEmpty()) {
								if( callback != null)
									callback.onSelection((File)fileListView.getSelectedValue());
							}
						}
					}
				});
	//	fileListView.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	//	fileListView.setVisibleRowCount(1);
		fileScrolPane = new JScrollPane(fileListView);
		fileScrolPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		fileScrolPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		addButton = new JButton(rb.getString("Add"));
		addButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				File whatFile = Utils.browseForImage(".", JFileChooser.OPEN_DIALOG,
						new String[] {"jpg", "png"}, rb.getString("selectimage"));
				if (whatFile != null) {
					File f = CourseCreatingManager.sharedInstance.importImageFile(whatFile, false);
					if( f != null ){
						files.add(f);
						fileListModel.rebuildList();
					}
				}
				 
			}});
		deleteButton = new JButton(rb.getString("Delete"));
		deleteButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				File file = (File)	fileListView.getSelectedValue();
				if( file == null) return;
				CourseCreatingManager.sharedInstance.deleteImage(file.getName());
				files.remove(file);
				fileListModel.rebuildList();
			}});
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(addButton);
		p.add(deleteButton);
		this.setLayout(new BorderLayout());
		this.add(fileScrolPane, BorderLayout.CENTER);
		if( callback == null ) {
			this.add(p, BorderLayout.SOUTH);
			this.add(new JLabel(rb.getString("allimportedimage") + "   "), BorderLayout.NORTH);
		} 
	}
	
	
	
	class FileListModel extends AbstractListModel  {
		private static final long serialVersionUID = 1L;
  
		public FileListModel() {
			 
		}
  
		private void rebuildList() {  
			this.fireContentsChanged(this, 0, getSize());
		}

		public int getSize() {
			return files.size();
		}

		public File getElementAt(int index) {
			 return files.get(index);
		}
 
	}
	
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
			private File page;

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
				this.page = (File) value;
				this.index = index;
				this.isSelected = isSelected;
				this.cellHasFocus = cellHasFocus;
				this.setText(page.getName());
			} 
		//	public Dimension getPreferredSize() {
		//		return new Dimension(60, 30);
		//	}
		}
	}
}
