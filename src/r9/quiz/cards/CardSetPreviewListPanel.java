package r9.quiz.cards;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import r9.quiz.component.ImageFileChooser;
import r9.quiz.component.MutableFilter;
import r9.quiz.util.Translatrix;

 

public class CardSetPreviewListPanel extends JPanel {
	private final String[] extsIMG = {"jpg"};  // { "gif", "jpg", "jpeg", "png" };
 	private Logger logger;
	private JScrollPane cardListScrollPane;
	private JList cardListView;
	private CardPreviewListModel cardListModel;
	private JButton projectSettingPicker; 
	private JButton quizCreator;
	//private JCheckBox accessSettor;

	public CardSetPreviewListPanel( ) {
		super();
	 
		cardListModel = new CardPreviewListModel();
		cardListView = new JList(cardListModel);
		cardListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cardListView.setCellRenderer(new CardPreviewCellRenderer());
		cardListView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (!lsm.isSelectionEmpty()) {
						int row = lsm.getLeadSelectionIndex();

						CardPage card = cardListModel.getElementAt(row);
					 
					}
				}
			}
		});
		cardListScrollPane = new JScrollPane(cardListView);
		cardListScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		cardListScrollPane.getVerticalScrollBar().setUnitIncrement(100);
		this.setLayout(new BorderLayout());
		this.add(cardListScrollPane, BorderLayout.CENTER);

		projectSettingPicker = new JButton("课件设置");
		projectSettingPicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//			      ProjectSettingDialog dialog = new ProjectSettingDialog(null);
//			      dialog.setVisible(true);
			}
		});

		 

		quizCreator = new JButton("试题");
		quizCreator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				QuizCreatingDialog dialog = new QuizCreatingDialog(null);
//				dialog.setVisible(true);
			}
		});
		
//		accessSettor = new JCheckBox("共享");
//		accessSettor.addItemListener(new ItemListener() {
//        	@Override
//			public void itemStateChanged(ItemEvent e) {
//        		CourseCreatingManager.sharedInstance.getCardSet()
//        		   .setAccessLevelCode(e.getStateChange() == ItemEvent.DESELECTED ? "PRIVATE" : "PUBLIC"); 
//            	} 
//		});

		JPanel indicatorPane = new JPanel();
		indicatorPane.setLayout(new BoxLayout(indicatorPane,
				BoxLayout.LINE_AXIS));
		indicatorPane.add(this.projectSettingPicker); 
		indicatorPane.add(Box.createRigidArea(new Dimension(0, 5)));
		indicatorPane.add(quizCreator);
		//indicatorPane.add(accessSettor);
		this.add(indicatorPane, BorderLayout.NORTH);
	}
	
	public void hookUIStatus(){
	//	accessSettor.setSelected( CourseCreatingManager.sharedInstance.getCardSet()
	//	   .getAccessLevelCode().equals( "PUBLIC" ) ); 
 	 
	}

	 

	public File browseForImage(String startDir, int dialogType, String[] exts,
			String desc) {
		ImageFileChooser jImageDialog = new ImageFileChooser(startDir);
		jImageDialog.setDialogType(JFileChooser.CUSTOM_DIALOG);
		jImageDialog.setFileFilter(new MutableFilter(exts, desc));
		jImageDialog.setDialogTitle(Translatrix
				.getTranslationString("ImageDialogTitle"));
		int optionSelected = JFileChooser.CANCEL_OPTION;
		optionSelected = jImageDialog.showDialog(this,
				Translatrix.getTranslationString("Insert"));
		if (optionSelected == JFileChooser.APPROVE_OPTION) {
			return jImageDialog.getSelectedFile();
		}
		return (File) null;
	}
}
