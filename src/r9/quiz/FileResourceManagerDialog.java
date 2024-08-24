package r9.quiz;
 

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener; //property change stuff
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.AbstractListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileFilter;

 

public class FileResourceManagerDialog extends QuizStyleDialog implements
		PropertyChangeListener {
   
	private static final long serialVersionUID = 1L;

	private JOptionPane optionPane;

//	private String btnString1 = "保存&关闭";
//	private String btnString2 = "删除选择的JS文件";
//	private String btnString3 =  "引入新JS文件"; 
	
	Object[] options;
	private JList variableListView;
	private FileListModel variableListModel;
 	
	 
	boolean isJsFile;
	/** Creates the reusable dialog. */
	public FileResourceManagerDialog(JDialog dialog, final boolean isJsFile ) {
		super( dialog ); 
	    this.isJsFile = isJsFile;
	    ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
	    
		 options =new String[] { rb.getString("SaveClose") ,rb.getString("DeleteJSFile")  , rb.getString("ImportJsFile") };
	 
		// Create the JOptionPane.
	
		optionPane = new JOptionPane(getContentDetailPane( ),
				JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null,
				options, options[0]);

		// Make this dialog display it.
		setContentPane(optionPane);

		// Handle window closing correctly.
		//setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				 
				optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		 

		// Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
	}

	private JPanel getContentDetailPane( ) { 
		variableListModel = new FileListModel();
		variableListView = new JList(variableListModel);
		  
			variableListView.setTransferHandler(new ListItemTransferHandler());
			variableListView.setDropMode(DropMode.INSERT);
			variableListView.setDragEnabled(true);
			
			variableListView
			.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 
	   
		variableListView.setVisibleRowCount(-1);
		JScrollPane variableListScrolPane = new JScrollPane(
				variableListView);
		variableListScrolPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		variableListScrolPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		 JPanel mainPanel = new JPanel();
		 mainPanel.setLayout(new BorderLayout());
		 mainPanel.add(variableListScrolPane, BorderLayout.CENTER);
        mainPanel.add(new JLabel("提示： 可通过拖拉改变变量声明的顺序"), BorderLayout.SOUTH);
		return mainPanel;
	}

	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY
						.equals(prop))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				// ignore reset
				return;
			}
			 optionPane.setValue(
	                    JOptionPane.UNINITIALIZED_VALUE);
	 
	            if (options[0].equals(value) || value.equals( JOptionPane.CLOSED_OPTION )) { 
	            	variableListModel.save(); 
	                setVisible(false);
	            }  
	            else if (options[1].equals(value)) { 
	            	 int row = variableListView.getSelectedIndex();
	            	 if( row < 0) return;
	            	 variableListModel.optionList.remove(row);
	            	 variableListModel.rebuildList();
	            }   
	            else if (options[2].equals(value)) { 
	            	JFileChooser chooser = new JFileChooser( ); 
	            	if( isJsFile) {
	            		chooser.setFileFilter(new FileFilter(){ 
		        			public boolean accept(File f) {
		                 	    return f.isDirectory() || f.getName().endsWith(".js")  ;
		        			}  
		        			public String getDescription() {
		        			   	return "Javascript  File";
		        			}});
	            	} else {
	            		chooser.setFileFilter(new FileFilter(){ 
		        			public boolean accept(File f) {
		                 	    return f.isDirectory() || f.getName().endsWith(".css")  ;
		        			}  
		        			public String getDescription() {
		        			   	return "CSS  File";
		        			}});
	            	}
	        		
	        		
	        		 int selection = chooser.showOpenDialog(null);
	        		 if ( selection == JFileChooser.APPROVE_OPTION ){
	        			  File file = chooser.getSelectedFile();
	        			  if( file == null || file.isDirectory() )
	        				 return;
	        			  String name = file.getName();
	        			  File out = new File(R9SystemSetting.getInstance().getHtmlTemplateDir(), name);
	        			  CourseCreatingManager.sharedInstance.copyFile(file, out); 
	        			  variableListModel.optionList.add(name);
	        			  variableListModel.rebuildList();
	        		 }
	            } 
		}
	}

	 
	class FileListModel extends AbstractListModel<String> {
		private static final long serialVersionUID = 1L;

		List<String> optionList;
		public FileListModel() {
			optionList = isJsFile ?  R9SystemSetting.sharedInstance.getJsFiles() : R9SystemSetting.sharedInstance.getCssFiles()  ;
		}
		public void save() {
			R9SystemSetting.sharedInstance.setJsFiles(optionList);
		}

		public void rebuildList() {
			this.fireContentsChanged(this, 0, getSize());
		}

		public int getSize() {
			return optionList.size();
		}

		public String getElementAt(int index) {
			return optionList.get(index);
		}

		public void add(int idx, String r9Variable) {
			 optionList.remove(r9Variable);
			 optionList.add(idx, r9Variable);
		}
	}
	
	 
	class ListItemTransferHandler extends TransferHandler {
		 
		private static final long serialVersionUID = 1L;
		private final DataFlavor localObjectFlavor;
		  private Object[] transferedObjects = null;
		  public ListItemTransferHandler() {
		    localObjectFlavor = new ActivationDataFlavor(
		      Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items");
		  }
		  @SuppressWarnings("deprecation")
		  @Override protected Transferable createTransferable(JComponent c) {
		    JList list = (JList) c;
		    indices = list.getSelectedIndices();
		    transferedObjects = list.getSelectedValuesList().toArray();
		    return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
		  }
		  @Override public boolean canImport(TransferSupport info) {
		    if(!info.isDrop() || !info.isDataFlavorSupported(localObjectFlavor)) {
		      return false;
		    }
		    return true;
		  }
		  @Override public int getSourceActions(JComponent c) {
		    return MOVE; //TransferHandler.COPY_OR_MOVE;
		  }
		  @SuppressWarnings("unchecked")
		  @Override public boolean importData(TransferSupport info) {
		    if(!canImport(info)) {
		      return false;
		    }
		    JList target = (JList)info.getComponent();
		    JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
		    FileListModel listModel = (FileListModel)target.getModel();
		    int index = dl.getIndex();
		    int max = listModel.getSize();
		    if(index<0 || index>max) {
		      index = max;
		    }
		    addIndex = index;
		    try {
		      Object[] values = (Object[])info.getTransferable().getTransferData(
		          localObjectFlavor);
		      addCount = values.length;
		      for(int i=0; i<values.length; i++) {
		        int idx = index++;
		        if( values[i] instanceof String){
		        	    listModel.add(idx, (String)values[i]);
				        target.addSelectionInterval(idx, idx);
		        } 
		      }
		      return true;
		    } catch(UnsupportedFlavorException ufe) {
		      ufe.printStackTrace();
		    } catch(IOException ioe) {
		      ioe.printStackTrace();
		    }
		    return false;
		  }
		  @Override protected void exportDone(
		      JComponent c, Transferable data, int action) {
		    cleanup(c, action == MOVE);
		  }
		  private void cleanup(JComponent c, boolean remove) {
		    if(remove && indices != null) {
		      JList source = (JList)c;
		      FileListModel model = (FileListModel)source.getModel();
		      if(addCount > 0) {
		        //http://java-swing-tips.googlecode.com/svn/trunk/DnDReorderList/src/java/example/MainPanel.java
		        for(int i=0; i<indices.length; i++) {
		          if(indices[i]>=addIndex) {
		            indices[i] += addCount;
		          }
		        }
		      }
		      for(int i=indices.length-1; i>=0; i--) {
		    	 //hanning  model.remove(indices[i]);
		      }
		    }
		    indices  = null;
		    addCount = 0;
		    addIndex = -1;
		  }
		  private int[] indices = null;
		  private int addIndex  = -1; //Location where items were added
		  private int addCount  = 0;  //Number of items added.
		}
}