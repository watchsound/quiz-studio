package r9.quiz; 

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import r9.quiz.cards.CardChoiceQuestion;
import r9.quiz.cards.CardFillInBlankQuestion;
import r9.quiz.cards.CardHtmlPage;
import r9.quiz.cards.CardImgChoiceQuestion;
import r9.quiz.cards.CardPage;
import r9.quiz.cards.CardSurveyJsJumpPage;
import r9.quiz.cards.CardYesNoQuestion;
import r9.quiz.cards.Exam;
import r9.quiz.cards.HtmlContent;
import r9.quiz.cards.HtmlTemplate;
import r9.quiz.problemui.FillInBlankCreatorPanel;
import r9.quiz.problemui.ImageChoiceCreatorPanel;
import r9.quiz.problemui.SimpleChoiceCreatorPanel;
import r9.quiz.problemui.YesNotCreatorPanel;
import r9.quiz.ui.comps.video.VideoBasedTimelineSettingDialog;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.Utils;

/** 
  */
public class QuizCreatingDialog extends QuizStyleDialog implements   ReloadableUI{
	 
	private static final long serialVersionUID = 1L;

	public static int OPEN_INSTANCE = 0;
	
	private JOptionPane jOptionPane;
	private Exam exam;
	private QuestionPreviewistModel quizListModel;
	private JList quizListView;
	private JScrollPane quizListScrollPane;
	private YesNotCreatorPanel createYesNoPane;
	private SimpleChoiceCreatorPanel createSimpleChoicePane;
	private FillInBlankCreatorPanel creatorFillinPane;
	private WebEditor creatorWebPane;

	//private  Page curPage; //FIXME
	private CardLayout mCardLayout;
	private JPanel contentPanel;
//	private JCheckBox isBackAllowedCheckBox;
//	private JCheckBox isForwardAllowedCheckBox;
//	private JCheckBox isMergeToPreviousCheckBox;
////	private JCheckBox timeSharedWithPreviousCheckBox;
//	private JTextField durationField;
	
//	private AudioControlPane audioControlPane;
//	private JTextField timeInVideoField;
	private JComboBox htmlTemplateComboBox;
 
 	private JButton  suerveyQuizButton;
	private JButton videoButton;
	private ImageChoiceCreatorPanel createImageChoicePane;

	private JPanel surveyJsJumpPane;
	final ResourceBundle rb;
	public QuizCreatingDialog(Frame parent) {
		super(  );
		exam = CourseCreatingManager.sharedInstance.getExam();
		rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		OPEN_INSTANCE++;
		JPanel indicatorPane = new JPanel(new BorderLayout());

		quizListModel = new QuestionPreviewistModel();
		quizListView = new JList(quizListModel);
		quizListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		quizListView.setCellRenderer(new QuestionPreviewCellRenderer());
		quizListView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
				     ListSelectionModel lsm = (ListSelectionModel)e.getSource();
					 if (! lsm.isSelectionEmpty())  {
				    	int row = lsm.getLeadSelectionIndex();
					    CardPage q = exam.getPages().get(row); 
					    switchView(q);
					 }
				}
			}
		});
		quizListView.setTransferHandler(new ListItemTransferHandler());
		quizListView.setDropMode(DropMode.INSERT);
		quizListView.setDragEnabled(true);
		    
		quizListScrollPane = new JScrollPane(quizListView);
		quizListScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		quizListScrollPane.getVerticalScrollBar().setUnitIncrement(100);
		indicatorPane.add(quizListScrollPane, BorderLayout.WEST);
		
		createYesNoPane = new YesNotCreatorPanel();
		createSimpleChoicePane = new SimpleChoiceCreatorPanel();
		createImageChoicePane = new ImageChoiceCreatorPanel();
		creatorFillinPane = new FillInBlankCreatorPanel();
		creatorWebPane = new WebEditor(null);
		 
		surveyJsJumpPane = new JPanel();
		surveyJsJumpPane.setLayout(new BoxLayout(surveyJsJumpPane, BoxLayout.Y_AXIS));
		surveyJsJumpPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
		surveyJsJumpPane.add(PropertyUIHelper.createTitleRow(rb.getString("SurveyJsNotShown"), true));
		 
		ImageIcon icon = R9SystemSetting.getToolIcon("template.png");
 		suerveyQuizButton = new JButton(rb.getString("OpenSurveyJs"),icon);
		suerveyQuizButton.setPreferredSize(new Dimension(218,35));
		suerveyQuizButton.setMaximumSize(new Dimension(218,35));
		suerveyQuizButton.addActionListener(new ActionListener(){ 
			@Override
			public void actionPerformed(ActionEvent e) {
				 CardPage existing = CourseCreatingManager.sharedInstance.getCurrentPage( );
				 if( existing == null) return;
				 if( existing instanceof CardSurveyJsJumpPage){
					 SurvyJsQuizCreatingDialog dialog = 
							 new SurvyJsQuizCreatingDialog(null, (CardSurveyJsJumpPage)existing, false);
					 dialog.setSize(1100, 920);
					 dialog.setVisible(true);
				 }
			}});
		
		surveyJsJumpPane.add( PropertyUIHelper.createRow(suerveyQuizButton, false) );
		surveyJsJumpPane.add( PropertyUIHelper.createVerticalFill() );
		
		mCardLayout = new CardLayout();
	 	contentPanel = new JPanel();
		contentPanel.setLayout(mCardLayout);
		contentPanel.add(createYesNoPane, "yesno");
		contentPanel.add(createSimpleChoicePane, "choice");
		contentPanel.add(createImageChoicePane, "imagechoice");
		contentPanel.add(creatorFillinPane, "fillin");
		contentPanel.add(creatorWebPane, "webview");
		contentPanel.add(surveyJsJumpPane, "surveyjs");
			
		indicatorPane.add( contentPanel, BorderLayout.CENTER );
		
//		isBackAllowedCheckBox = new JCheckBox("显示返回");
//		isForwardAllowedCheckBox = new JCheckBox("显示下一页");
//		isMergeToPreviousCheckBox = new JCheckBox("在上一页显示");
//		timeSharedWithPreviousCheckBox = new JCheckBox("和上一页时间共享");
//		durationField = new JTextField();
		
		
	//	timeInVideoField = new JTextField();
	//	timeInVideoField.setPreferredSize(new Dimension(65,35));
	//	timeInVideoField.setMaximumSize(new Dimension(65,35));
		htmlTemplateComboBox = new JComboBox();
		htmlTemplateComboBox.setPreferredSize(new Dimension(256,35));
		htmlTemplateComboBox.setMaximumSize(new Dimension(256,35));

//		audioControlPane = new AudioControlPane();
//		JPanel backPanel = new JPanel();
//		backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS)); 
//		backPanel.add(audioControlPane, Box.LEFT_ALIGNMENT);
//		backPanel.add(Box.createHorizontalStrut(100),  Box.LEFT_ALIGNMENT);
//		backPanel.add(new JLabel("时间(秒):"), Box.RIGHT_ALIGNMENT);
//		backPanel.add(durationField, Box.RIGHT_ALIGNMENT);
//		backPanel.add(timeSharedWithPreviousCheckBox, Box.RIGHT_ALIGNMENT);
//		backPanel.add(isMergeToPreviousCheckBox, Box.RIGHT_ALIGNMENT);
//		backPanel.add(isBackAllowedCheckBox, Box.RIGHT_ALIGNMENT);
//		backPanel.add(isForwardAllowedCheckBox, Box.RIGHT_ALIGNMENT);
		
		//JPanel videoAndTemplatePanel = new JPanel();
		//videoAndTemplatePanel = new JPanel();
		//videoAndTemplatePanel.setLayout(new BoxLayout(videoAndTemplatePanel, BoxLayout.X_AXIS)); 
		//videoAndTemplatePanel.add(Box.createHorizontalGlue());
		List<JComponent> videoAndTemplatePanel = new ArrayList<JComponent>();
	//	videoAndTemplatePanel.add(suerveyQuizButton ); 
	//	videoAndTemplatePanel.add(new JLabel("在视频中的显示时间") );
	//	videoAndTemplatePanel.add(timeInVideoField );
		videoAndTemplatePanel.add(new JLabel(rb.getString("PageTemplate")) );
		videoAndTemplatePanel.add(htmlTemplateComboBox );
	//	suerveyQuizButton.setVisible(false);
		JPanel southPanel =new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS)); 
	//	southPanel.add( backPanel );
		//southPanel.add(PropertyUIHelper.createLine() );
 
		
		southPanel.add(PropertyUIHelper.createRow( videoAndTemplatePanel, 5, false, null));
		
		
		List<JComponent> pageLevelButtons = new ArrayList<JComponent>();
		 
		JButton newTextChoicePage = new JButton(rb.getString("TextChoiceProblem"));
		pageLevelButtons.add(newTextChoicePage);
		newTextChoicePage.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				populateModelFromUI();
				CardChoiceQuestion q = new CardChoiceQuestion();
			 	exam.addPage(q); 
				quizListModel.rebuildList(); 
				quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		JButton newImgChoicePage = new JButton(rb.getString("ImageChoiceProblem"));
		pageLevelButtons.add(newImgChoicePage);
		newImgChoicePage.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				populateModelFromUI();
				CardImgChoiceQuestion q = new CardImgChoiceQuestion();
			 	exam.addPage(q); 
				quizListModel.rebuildList(); 
				quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		JButton newChoicePage = new JButton(rb.getString("YesNoProblem"));
		pageLevelButtons.add(newChoicePage);
		newChoicePage.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				populateModelFromUI();
				CardYesNoQuestion q = new CardYesNoQuestion();
			 	exam.addPage(q); 
				quizListModel.rebuildList(); 
				quizListView.setSelectedIndex( quizListModel.getSize() - 1);
				 
			}});
		JButton newFillinPage = new JButton(rb.getString("FillinProblem"));
		pageLevelButtons.add(newFillinPage);
		newFillinPage.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				populateModelFromUI();
				CardFillInBlankQuestion q = new CardFillInBlankQuestion();
			 	exam.addPage(q); 
				quizListModel.rebuildList(); 
				quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		
		JButton newTextPage = new JButton(rb.getString("TextPage"));
		pageLevelButtons.add(newTextPage);
		newTextPage.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				populateModelFromUI();
				CardHtmlPage q = new CardHtmlPage();
			 	exam.addPage(q); 
				quizListModel.rebuildList(); 
				quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		 icon = R9SystemSetting.getToolIcon("template.png");
		JButton newSurveyJsPage = new JButton(rb.getString("SurveyJsProblem"),icon);
		pageLevelButtons.add(newSurveyJsPage);
		newSurveyJsPage.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				populateModelFromUI();
				CardSurveyJsJumpPage q = new CardSurveyJsJumpPage();
			 	exam.addPage(q); 
				quizListModel.rebuildList(); 
				quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		 icon = R9SystemSetting.getToolIcon("_button_cancel.png");
		JButton deletePage = new JButton(rb.getString("DeletePage"),icon);
		pageLevelButtons.add(deletePage);
		deletePage.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				 int op = JOptionPane.showConfirmDialog(QuizCreatingDialog.this, rb.getString("DeleteConfirm"));
				 if( op == JOptionPane.CANCEL_OPTION)
					 return;
				deletePage(); 
			}});
		//southPanel.add(PropertyUIHelper.createLine());
		southPanel.add(PropertyUIHelper.createTitleRow(rb.getString("CreationTitle"), true));
		southPanel.add(PropertyUIHelper.createRow( pageLevelButtons, 5, false, Color.WHITE));
		 
		southPanel.add(PropertyUIHelper.createTitleRow(rb.getString("ResourceManage"), true));
		
		  icon = R9SystemSetting.getToolIcon("color_chooser.png");
		JButton imageButton = new JButton(rb.getString("ImageManage"),icon);
		imageButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				ImageManagerDialog dialog = new ImageManagerDialog();
				dialog.setSize(450, 250);
				dialog.setVisible(true);
			}});
		  icon = R9SystemSetting.getToolIcon("video.png");
		 videoButton = new JButton(rb.getString("CourseVideo"), icon);
		videoButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				if( exam == null) return;
				JPopupMenu menu = new JPopupMenu();
				JMenuItem item1 = new JMenuItem(rb.getString("UseVideoFile"));
				item1.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						  showVideoPicker(); 
					}});
			    menu.add(item1);
				JMenuItem item2 = new JMenuItem( rb.getString("SetVideoLink") );
				item2.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						setVideoLink(); 
					}});
			    menu.add(item2);
			    JMenuItem item3 = new JMenuItem( rb.getString("Delete") );
				item3.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						exam.setVideoLink("");
						exam.setVideoName("");
						videoButton.setText("");
					}});
			    menu.add(item3);
			    menu.show(videoButton, 5, 5);
			}});
		icon = R9SystemSetting.getToolIcon("tree_structure.png");
		JButton videoTimeButton = new JButton(rb.getString("VideoInsertPos"),icon);
		videoTimeButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				if( exam.getVideoName()== null || exam.getVideoName().length() == 0)
					return;
				VideoBasedTimelineSettingDialog g = new VideoBasedTimelineSettingDialog(  );
				g.setSize(Constants.CARD_WIDTH, Constants.CARD_HEIGHT + 200);
				g.setVisible(true);
			}});
		
		JButton metaButton = new JButton(rb.getString("CourseMetaData"));
		metaButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 ProjectMetaDataDialog dialog = new ProjectMetaDataDialog(null);
				 dialog.setSize(550, 400);
				 dialog.pack();
				 dialog.setVisible(true);
			}});
	
		
		southPanel.add(PropertyUIHelper.createRow("",  imageButton, videoButton, videoTimeButton, metaButton));
		
		
		indicatorPane.add( southPanel, BorderLayout.SOUTH );
		
		indicatorPane.setBorder(BorderFactory.createEtchedBorder());

		final Object[] buttonLabels = {  rb.getString("Save"), rb.getString("Exit"),  rb.getString("Preview")};
		Object[] panelContents = { indicatorPane };
		jOptionPane = new JOptionPane(panelContents, JOptionPane.PLAIN_MESSAGE,
				JOptionPane.DEFAULT_OPTION, null, buttonLabels, null);

		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				//jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		jOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();
				if (isVisible()
						&& (e.getSource() == jOptionPane)
						&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop
								.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
					Object value = jOptionPane.getValue();
					if (value == JOptionPane.UNINITIALIZED_VALUE) {
						return;
					}
					 if (value.equals(buttonLabels[0])) {
						populateModelFromUI();
						CourseCreatingManager.sharedInstance.markDirty();
						CourseCreatingManager.sharedInstance.save();
						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					} else if (value.equals(buttonLabels[1])) { 
						int option = JOptionPane.showConfirmDialog(null, rb.getString("Save") +"？");
						if (option == JOptionPane.CANCEL_OPTION ){
							jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
							return;
						}
						if (option == JOptionPane.YES_OPTION ){
							populateModelFromUI();
							CourseCreatingManager.sharedInstance.markDirty();
							CourseCreatingManager.sharedInstance.save();
						}  
				        setVisible(false);
				        OPEN_INSTANCE--;
				        if( OPEN_INSTANCE <= 0)
				            System.exit(0); 
					} else if (value.equals(buttonLabels[2])) { //preview
						if( exam == null) return;
						new HtmlCodeRender( ).preview(exam, false);
						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					} else {
						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					}
				}
			}
		});
		
		WorkspaceMenuHandler menuHandler = new WorkspaceMenuHandler(this);
		this.setJMenuBar(menuHandler.setupMenu());
		this.pack();
		
		 SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() { 
					ProjectLoadingDialog dialog = new ProjectLoadingDialog(QuizCreatingDialog.this, QuizCreatingDialog.this);
					dialog.setSize(450, 180);
					dialog.setLocation(250, 350);
					 
					dialog.setVisible(true);
//					if ( exam.getPages().size() > 0 ){
//						quizListView.setSelectedIndex(0);
//					} else {
//				    	ChoiceQuestion q = new ChoiceQuestion();
//				       exam.getPages().add(q);
//				       quizListModel.rebuildList();
//				       quizListView.setSelectedIndex(0);
//					}
					 
				}}); 
		
	}
	
	protected void setVideoLink() {
		String info = rb.getString("SetVideoLink") + "___________________________________________";
		String link = JOptionPane.showInputDialog(this, info, exam.getVideoLink());
		exam.setVideoLink(link);
	}
	 
	protected void showVideoPicker() {
		JFileChooser chooser = new JFileChooser( );
		chooser.setFileFilter(new FileFilter(){
        	@Override
			public boolean accept(File f) {
			 	return  f.isDirectory() || f.getName().endsWith(".mp4")
			 			|| f.getName().endsWith(".mpg") || f.getName().endsWith(".avi")
			 			|| f.getName().endsWith(".3gp") || f.getName().endsWith(".flv")
			 			|| f.getName().endsWith(".mov");
			} 
			@Override
			public String getDescription() {
			   	return "Video Description File";
			}});
		
		 int selection = chooser.showOpenDialog(null);
		 if ( selection == JFileChooser.APPROVE_OPTION ){
			  final File importedFile = chooser.getSelectedFile();
			  if( importedFile.isDirectory() )
					  return;
		       String fname = importedFile.getName();
		       try {
				  R9SystemSetting.copyFileToFile(importedFile, 
						   new File(CourseCreatingManager.sharedInstance.getCourseFolder(), fname));
			      exam.setVideoName(fname);
			      videoButton.setText( rb.getString("CourseVideo") + "[" + fname + "]");
		       } catch (IOException e1) {
				 e1.printStackTrace();
			   }
		       
		 }
	}



	public void deletePage(){
		CardPage curPage = CourseCreatingManager.sharedInstance.getCurrentPage();
		if( curPage == null )
			return;
		int index = exam.getPages().indexOf(curPage);
		if( index < 0)
			return;
		exam.deletePage(curPage);
	    quizListModel.rebuildList(); 
		    
	    if ( index <  exam.getPages().size() ){
	    	quizListView.setSelectedIndex(index);
	    } else {
	    	if ( index -1 >= 0 )
	    		quizListView.setSelectedIndex(index -1);
	    }
		 
	}
	
	public void reload(){
		exam = CourseCreatingManager.sharedInstance.getExam();
		quizListModel.rebuildList();
		if( !exam.getPages().isEmpty() ){
			quizListView.setSelectedIndex(0);
		} 
		String videoDesc = exam.getVideoLink();
		if( videoDesc == null || videoDesc.length() == 0) {
			videoDesc = exam.getVideoName() ;
		} else {
			if( videoDesc.length() > 10 ) {
				videoDesc = videoDesc.substring(videoDesc.length()-10);
			}
		}
	    videoButton.setText(rb.getString("CourseVideo") + "[" + videoDesc + "]");
	}
	
	public void switchView(CardPage page){
	  CardPage existing = CourseCreatingManager.sharedInstance.getCurrentPage( );
	  if( existing != null){
		//  existing.setBackAllowed(isBackAllowedCheckBox.isSelected());
		//  existing.setForwardAllowed(isForwardAllowedCheckBox.isSelected());
		//  try{
		//	  double t = Double.parseDouble( timeInVideoField.getText() );
		//	  existing.setTimeInVideo( t);
		//  }catch(Exception ex){ 	
		//	  ex.printStackTrace();
		//  }
          if( htmlTemplateComboBox.getSelectedItem() != null)
		      existing.setHtmlTemplateId( 
		    		 ( (HtmlTemplate) htmlTemplateComboBox.getSelectedItem()).getFileName() );
		  ;
		  
		//  existing.setMergeToPreviousPage(isMergeToPreviousCheckBox.isSelected());
		//  existing.setTimeSharedWithPrevious(timeSharedWithPreviousCheckBox.isSelected());
		//  existing.setDuration(Utils.parseInt(durationField.getText(), 0));  
		  
	  }
//	  this.isMergeToPreviousCheckBox.setEnabled(true);
//	  this.timeSharedWithPreviousCheckBox.setEnabled(true);
//	  this.isBackAllowedCheckBox.setEnabled(true);
//	  this.isForwardAllowedCheckBox.setEnabled(true);
//	  this.timeInVideoField.setEnabled(true);
		 
	  CourseCreatingManager.sharedInstance.setCurrentPage(page);
	  htmlTemplateComboBox.setVisible(! (page instanceof CardSurveyJsJumpPage ) );
	  htmlTemplateComboBox.setModel(
			  new DefaultComboBoxModel( 
					  R9SystemSetting.sharedInstance.getHtmlTemplateMetaData().filter(page.getType()) ) );
	  htmlTemplateComboBox.setSelectedItem( 
			  R9SystemSetting.sharedInstance.getHtmlTemplateMetaData().getTemplate(page.getHtmlTemplateId()) );
	  
	  CardPage previousPage = CourseCreatingManager.sharedInstance.getPreviousPage();
	  if( previousPage == null ){
//		    this.isBackAllowedCheckBox.setSelected(false);
//		    this.isBackAllowedCheckBox.setEnabled(false);
//		    this.timeSharedWithPreviousCheckBox.setSelected(false);
//		    this.timeSharedWithPreviousCheckBox.setEnabled(false);
//		    this.isMergeToPreviousCheckBox.setSelected( false );
//		    this.isMergeToPreviousCheckBox.setEnabled( false );
	  } else {
//		  if ( previousPage instanceof HtmlContent || page instanceof HtmlContent ){
//			  this.isMergeToPreviousCheckBox.setSelected( false );
//			  this.isMergeToPreviousCheckBox.setEnabled( false );
//		  }
//		  if ( previousPage.hasImages() || page.hasImages() ){
//			  this.isMergeToPreviousCheckBox.setSelected( false );
//			  this.isMergeToPreviousCheckBox.setEnabled( false );
//		  }
		  
//		  if( timeSharedWithPreviousCheckBox.isEnabled() )
//			  this.timeSharedWithPreviousCheckBox.setSelected(page.isTimeSharedWithPrevious());
//		  
//		 
//		  if( isForwardAllowedCheckBox.isEnabled() )
//		      this.isForwardAllowedCheckBox.setSelected(page.isForwardAllowed());
//		  if( isBackAllowedCheckBox.isEnabled() )
//		      this.isBackAllowedCheckBox.setSelected(page.isBackAllowed());
//		  if( isMergeToPreviousCheckBox.isEnabled() )
//		      this.isMergeToPreviousCheckBox.setSelected( page.isMergeToPreviousPage());
	  }
//	  timeInVideoField.setText( page.getTimeInVideo() + "");
//	    suerveyQuizButton.setVisible(false);
		// this.durationField.setText(page.getDuration() +"");
		if( page instanceof CardHtmlPage){
//			suerveyQuizButton.setVisible(true);
			 mCardLayout.show(contentPanel, "webview");
			 creatorWebPane.resetUIStates((HtmlContent)page ); 
		} else if ( page instanceof CardSurveyJsJumpPage){
			 mCardLayout.show(contentPanel, "surveyjs"); 
		} else if ( page instanceof CardYesNoQuestion){
			 mCardLayout.show(contentPanel, "yesno");
			 createYesNoPane.resetUIStates((CardYesNoQuestion)page); 
		} else if ( page instanceof CardChoiceQuestion){
			 mCardLayout.show(contentPanel, "choice");
			 createSimpleChoicePane.resetUIStates((CardChoiceQuestion)page); 
		} else if ( page instanceof CardImgChoiceQuestion){
			 mCardLayout.show(contentPanel, "imagechoice");
			 createImageChoicePane.resetUIStates((CardImgChoiceQuestion)page); 
		} else {
			 mCardLayout.show(contentPanel, "fillin");
			 creatorFillinPane.resetUIStates((CardFillInBlankQuestion)page); 
		}  
		//audioControlPane.nodeChanged();
	}
	public void populateModelFromUI( ){
	//	boolean canBack = this.isBackAllowedCheckBox.isSelected();
		CardPage page =  CourseCreatingManager.sharedInstance.getCurrentPage( );
	//	page.setBackAllowed(canBack);
	//	page.setForwardAllowed(isForwardAllowedCheckBox.isSelected());
//		 try{
//			  double t = Double.parseDouble( timeInVideoField.getText() );
//			  page.setTimeInVideo( t);
//		  }catch(Exception ex){ 	  
//		  }
		 
		 if( htmlTemplateComboBox.getSelectedItem() != null)
			 page.setHtmlTemplateId( ((HtmlTemplate)htmlTemplateComboBox.getSelectedItem() ).getFileName());
		     
		// page.setMergeToPreviousPage(isMergeToPreviousCheckBox.isSelected());
		// page.setTimeSharedWithPrevious(this.timeSharedWithPreviousCheckBox.isSelected());
		// page.setDuration(Utils.parseInt(durationField.getText(), 0));
		 creatorWebPane.populateModelFromUI( );
		 createYesNoPane.populateModelFromUI( );
		 createSimpleChoicePane.populateModelFromUI( );
		 creatorFillinPane.populateModelFromUI( );
	}
        

	
	

	class QuestionPreviewistModel extends AbstractListModel {

		private static final long serialVersionUID = 1L;

		public QuestionPreviewistModel() {
	 	}

		public Exam getCardset() {
			return exam;
		}
		

		private void rebuildList() {
			this.fireContentsChanged(this, 0, getSize());
		}
		
		public void add(int index,Object data){
			if( data instanceof CardPage){
				CardPage page = (CardPage)data;
			   exam.getPages().add(index,  page);
			}
		}
		
		public void remove(int index){
			exam.getPages().remove(index);
		}

		public int getSize() {
			if (exam == null)
				return 0;
			return exam.getPages().size();
		}

		public CardPage getElementAt(int index) {
			if (exam == null)
				return null;

			return exam.getPages().get(index);
		}
	}

	class QuestionPreviewCellRenderer implements ListCellRenderer {


		public Component getListCellRendererComponent(final JList list,
				final Object value, final int index, final boolean isSelected,
				final boolean cellHasFocus) {
			return new CellPreviewPanel(list, value, index, isSelected,
					cellHasFocus);
		}

		private class CellPreviewPanel extends JPanel {
			private static final long serialVersionUID = -4624762713662343786L;
			private JList list;
			private CardPage question;
			private int index;
			private boolean isSelected;
			private boolean cellHasFocus;

			public CellPreviewPanel(final JList list, final Object value,
					final int index, final boolean isSelected,
					final boolean cellHasFocus) {
				super(new BorderLayout());
				setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(2, 2, 2, 2),
						BorderFactory.createLineBorder(Color.darkGray, 1)));
				this.list = list;
				this.question = (CardPage) value;
				this.index = index;
				this.isSelected = isSelected;
				this.cellHasFocus = cellHasFocus;
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(isSelected ? list.getSelectionBackground() : list
						.getBackground());
				
				g.fillRect(0, 0, getWidth(), getHeight());
				BufferedImage image = question.getPreviewImage();
				if (image == null || question instanceof HtmlContent) {
					String content = ""; 
					if( question instanceof HtmlContent){
						content +=  Utils.conpressHtml(((HtmlContent)question).getContent());
					} else if (question instanceof CardFillInBlankQuestion){
						content += ((CardFillInBlankQuestion)question).getQuestionBody();
					} 
					String qname = question.getName();
					if( qname != null && qname.trim().length()>0 ) 
					   content =  qname + "<br/>" + content;
					
					Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
					g.setFont(f);
					g.setColor(Color.BLACK);
					int ypos = 20;
					for(String line : content.split("<br/>")){
						if( line.trim().length() == 0 ) continue;
						g.drawString(line == null ? "" : line, 15, ypos);
						ypos += 15;
					}
					
			 	} else {
					int width = this.getWidth();
					int height = this.getHeight();
					int imagewidth = image.getWidth();
					int imageheight = image.getHeight();
					g.drawImage(image, (width - imagewidth) / 2,
							(height - imageheight) / 2, null);
				}
				int ypos = 25;
				if( question instanceof CardSurveyJsJumpPage  ) {
					Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
					g.setFont(f);
					g.setColor(Color.blue);
				 	g.drawString(rb.getString("SurveyJSNode"), 20, ypos);
				 	ypos += 25;
				}
					 
				if( exam != null ){
					String error = question.getErrorMessage(exam);
					if( error != null && error.length() > 0 ){ 
						for(String line : error.split("\n")){
							Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
							g.setFont(f);
							g.setColor(Color.RED);
						 	g.drawString(line, 20, ypos);
						 	ypos += 25;
						}
					}
					if( question instanceof CardSurveyJsJumpPage  ) {
						
					} else {
						if( question.getHtmlTemplateId()==null || question.getHtmlTemplateId().length()==0) {
							String error2 = rb.getString("NoTemplateError");
							Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
							g.setFont(f);
							g.setColor(Color.RED);
						 	g.drawString(error2, 20, ypos);
						 	ypos += 25;
						}
					} 
				} 
			}

			public Dimension getPreferredSize() {
				return new Dimension(Constants.CARD_PREVIEW_WIDTH + 4,
						Constants.CARD_PREVIEW_HEIGHT + 4);
			}
		}
	}
	
	

	class ListItemTransferHandler extends TransferHandler {
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
		    QuestionPreviewistModel listModel = (QuestionPreviewistModel)target.getModel();
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
		        listModel.add(idx, values[i]);
		        target.addSelectionInterval(idx, idx);
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
		      QuestionPreviewistModel model = (QuestionPreviewistModel)source.getModel();
		      if(addCount > 0) {
		        //http://java-swing-tips.googlecode.com/svn/trunk/DnDReorderList/src/java/example/MainPanel.java
		        for(int i=0; i<indices.length; i++) {
		          if(indices[i]>=addIndex) {
		            indices[i] += addCount;
		          }
		        }
		      }
		      for(int i=indices.length-1; i>=0; i--) {
		        model.remove(indices[i]);
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
