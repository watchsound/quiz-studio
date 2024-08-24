package r9.quiz; 

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Menu;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
 































import r9.quiz.action.HtmlTemplateSettingAction;
import r9.quiz.action.ImportCommonFileAction;
import r9.quiz.action.NewAction;
import r9.quiz.action.OpenAction;
import r9.quiz.action.ProductInfoAction;
import r9.quiz.action.SaveAsAction;
import r9.quiz.cards.Exam;
import r9.quiz.cards.CardYesNoQuestion; 
import r9.quiz.cards.CardFillInBlankQuestion;
import r9.quiz.cards.HtmlContent;
import r9.quiz.cards.CardHtmlPage;
import r9.quiz.cards.HtmlTemplate;
import r9.quiz.cards.IQuestion;
import r9.quiz.cards.CardPage;
import r9.quiz.cards.CardSurveyJsJumpPage;
import r9.quiz.cards.CardChoiceQuestion;
import r9.quiz.component.AudioControlPane;
import r9.quiz.component.ImagePicker;
import r9.quiz.component.ImportedImagesPanel;
import r9.quiz.problemui.FillInBlankCreatorPanel;
import r9.quiz.problemui.SimpleChoiceCreatorPanel;
import r9.quiz.problemui.YesNotCreatorPanel;
import r9.quiz.problemui.surveyjs.BooleanQuestionPanel;
import r9.quiz.problemui.surveyjs.CheckboxesQuestionPanel;
import r9.quiz.problemui.surveyjs.DropdownQuestionPanel;
import r9.quiz.problemui.surveyjs.HtmlQuestionPanel;
import r9.quiz.problemui.surveyjs.ImagePickerQuestionPanel;
import r9.quiz.problemui.surveyjs.MatrixQuestionPanel;
import r9.quiz.problemui.surveyjs.MultipleTextQuestionPanel;
import r9.quiz.problemui.surveyjs.QuestionCommonPanel;
import r9.quiz.problemui.surveyjs.RadioGroupQuestionPanel;
import r9.quiz.problemui.surveyjs.RatingQuestionPanel;
import r9.quiz.problemui.surveyjs.TextQuestionPanel;
import r9.quiz.surveyjs.BooleanQuestion;
import r9.quiz.surveyjs.CheckboxesQuestion;
import r9.quiz.surveyjs.DropdownQuestion;
import r9.quiz.surveyjs.HtmlQuestion;
import r9.quiz.surveyjs.ImagePickerQuestion;
import r9.quiz.surveyjs.MatrixQuestion;
import r9.quiz.surveyjs.MultipleTextQuestion;
import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.surveyjs.RadioGroupQuestion;
import r9.quiz.surveyjs.RatingQuestion;
import r9.quiz.surveyjs.TextQuestion;
import r9.quiz.util.ImageUtil;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.R9Exception;
import r9.quiz.util.Utils;
import r9.quiz.*;

/** 
  */
public class SurvyJsQuizCreatingDialog extends QuizStyleDialog implements ReloadableUI {
	 
	private static final long serialVersionUID = 1L;
	private JOptionPane jOptionPane;
	private Quiz quiz;
	private QuestionPreviewistModel quizListModel;
	private JList quizListView;
	private JScrollPane quizListScrollPane;
 

	//private  Page curPage; //FIXME
	private CardLayout mCardLayout;
	private JPanel contentPanel;
// 	private JCheckBox isBackAllowedCheckBox;
//	private JCheckBox isForwardAllowedCheckBox;
// 	private JCheckBox isMergeToPreviousCheckBox;
////	private JCheckBox timeSharedWithPreviousCheckBox;
// 	private JTextField durationField;
	
//	private AudioControlPane audioControlPane;
//	private JTextField timeInVideoField;
//	private JComboBox htmlTemplateComboBox;
	//private ImportedImagesPanel importedImagesPanel;
	
	private Question curQuestion;
	CardSurveyJsJumpPage htmlPage;
	private BooleanQuestionPanel booleanQuestionPanel;
	private CheckboxesQuestionPanel checkboxesQuestionPanel;
	private DropdownQuestionPanel dropdownQuestionPanel;
	private HtmlQuestionPanel htmlQuestionPanel;
	private ImagePickerQuestionPanel imagePickerQuestionPanel;
	private MatrixQuestionPanel matrixQuestionPanel;
	private MultipleTextQuestionPanel multipleTextQuestionPanel;
	private RadioGroupQuestionPanel radioGroupQuestionPanel;
	private RatingQuestionPanel ratingQuestionPanel;
	private TextQuestionPanel textQuestionPanel; 
	
	private QuestionCommonPanel curDetailPanel;
	final boolean surveyJsOnly;
	private ResourceBundle rb;
	private JLabel statusLabel;
	public SurvyJsQuizCreatingDialog(Frame parent, final CardSurveyJsJumpPage htmlPage, final boolean surveyJsOnly) {
		super(  );
		rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		super.setTitle(rb.getString("ASetOfProblems"));
		this.surveyJsOnly = surveyJsOnly;
		this.htmlPage = htmlPage;
		this.quiz = htmlPage.getSurveyQuiz();
		if(quiz == null)
			quiz = new Quiz();
		if( surveyJsOnly )
			QuizCreatingDialog.OPEN_INSTANCE ++;
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
					    Question q = quiz.getQuestions().get(row); 
					    try {
							setStatus("");
							populateModelFromUI(); 
						}catch(R9Exception ed) {
							setStatus(ed.getMessage());
							return;
						}
					    
					    quiz.rebuildPages();
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
		
		booleanQuestionPanel = new BooleanQuestionPanel();
		checkboxesQuestionPanel = new CheckboxesQuestionPanel();
		dropdownQuestionPanel = new DropdownQuestionPanel();
		htmlQuestionPanel = new HtmlQuestionPanel( );
		imagePickerQuestionPanel = new ImagePickerQuestionPanel();
		matrixQuestionPanel = new MatrixQuestionPanel();
		multipleTextQuestionPanel = new MultipleTextQuestionPanel();
		radioGroupQuestionPanel = new RadioGroupQuestionPanel();
		ratingQuestionPanel = new RatingQuestionPanel();
		textQuestionPanel = new TextQuestionPanel();
		
		
		mCardLayout = new CardLayout();
	 	contentPanel = new JPanel();
		contentPanel.setLayout(mCardLayout);
		contentPanel.add(booleanQuestionPanel, Question.QuestionType.bool.getName());
		contentPanel.add(checkboxesQuestionPanel, Question.QuestionType.choice.getName());
		contentPanel.add(dropdownQuestionPanel, Question.QuestionType.dropdown.getName());
		contentPanel.add(htmlQuestionPanel, Question.QuestionType.html_page.getName());
		contentPanel.add(imagePickerQuestionPanel, Question.QuestionType.imagepicker.getName());
		contentPanel.add(matrixQuestionPanel, Question.QuestionType.matrix.getName());
		contentPanel.add(multipleTextQuestionPanel, Question.QuestionType.multipletext.getName());
		contentPanel.add(radioGroupQuestionPanel, Question.QuestionType.radiogroup.getName());
		contentPanel.add(ratingQuestionPanel, Question.QuestionType.rating.getName());
		contentPanel.add(textQuestionPanel, Question.QuestionType.text.getName());
		contentPanel.add(new JPanel(), "dummy");
		
		indicatorPane.add( contentPanel, BorderLayout.CENTER );
		
		 mCardLayout.show(contentPanel, "dummy");
		 this.curDetailPanel = htmlQuestionPanel;
		 
		
// 		isBackAllowedCheckBox = new JCheckBox("显示返回");
////		isForwardAllowedCheckBox = new JCheckBox("显示下一页");
// 		isMergeToPreviousCheckBox = new JCheckBox("在上一页显示");
////		timeSharedWithPreviousCheckBox = new JCheckBox("和上一页时间共享");
// 		durationField = new JTextField();
//		 
//		
////		audioControlPane = new AudioControlPane();
// 		JPanel backPanel = new JPanel();
//		backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS)); 
// 		backPanel.add(new JLabel("时间(秒):"), Box.RIGHT_ALIGNMENT);
//		backPanel.add(durationField, Box.RIGHT_ALIGNMENT);
//	 	backPanel.add(isMergeToPreviousCheckBox, Box.RIGHT_ALIGNMENT);
//		backPanel.add(isBackAllowedCheckBox, Box.RIGHT_ALIGNMENT);
 		
	 
		JPanel southPanel =new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS)); 
     //	southPanel.add( backPanel );
		//southPanel.add(PropertyUIHelper.createLine() );
		 
       
		ImageIcon icon = R9SystemSetting.getToolIcon("color_chooser.png");
		JButton imageButton = new JButton(rb.getString("ImageManage"),icon);
		imageButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				ImageManagerDialog dialog = new ImageManagerDialog();
				dialog.setSize(450, 250);
				dialog.setVisible(true);
			}});
		
		JButton metaButton = new JButton(rb.getString("SurveyJsMetaData") );
		metaButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 SurveyQuizSettingDialog dialog = new SurveyQuizSettingDialog(null, quiz);
				 dialog.setSize(600, 500); 
				 dialog.setVisible(true);
				 
			}});
		
		JButton projectButton = new JButton( rb.getString("CourseMetaData"));
		projectButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				 ProjectMetaDataDialog dialog = new ProjectMetaDataDialog(null);
				 dialog.setSize(550, 400);
				 dialog.pack();
				 dialog.setVisible(true);
				 
			}});
		
		 	
		
		List<JComponent> pageLevelButtons = new ArrayList<JComponent>();
		 
		JButton b1 = new JButton(Question.QuestionType.bool.getDescription());
		pageLevelButtons.add(b1);
		b1.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				try {
					setStatus("");
					populateModelFromUI(); 
				}catch(R9Exception ed) {
					setStatus(ed.getMessage());
					return;
				}
				quiz.rebuildPages();
				BooleanQuestion q = new BooleanQuestion();
			     quiz.getQuestions().add(q); 
				 quizListModel.rebuildList(); 
				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		b1 = new JButton(Question.QuestionType.choice.getDescription());
		pageLevelButtons.add(b1);
		b1.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				try {
					setStatus("");
					populateModelFromUI(); 
				}catch(R9Exception ed) {
					setStatus(ed.getMessage());
					return;
				}
				quiz.rebuildPages();
				CheckboxesQuestion q = new CheckboxesQuestion();
			     quiz.getQuestions().add(q); 
				 quizListModel.rebuildList(); 
				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		b1 = new JButton(Question.QuestionType.dropdown.getDescription());
		pageLevelButtons.add(b1);
		b1.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				try {
					setStatus("");
					populateModelFromUI(); 
				}catch(R9Exception ed) {
					setStatus(ed.getMessage());
					return;
				}
				quiz.rebuildPages();
				DropdownQuestion q = new DropdownQuestion();
			     quiz.getQuestions().add(q); 
				 quizListModel.rebuildList(); 
				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		b1 = new JButton(Question.QuestionType.html_page.getDescription());
		pageLevelButtons.add(b1);
		b1.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				try {
					setStatus("");
					populateModelFromUI(); 
				}catch(R9Exception ed) {
					setStatus(ed.getMessage());
					return;
				}
				quiz.rebuildPages();
				HtmlQuestion q = new HtmlQuestion();
			     quiz.getQuestions().add(q); 
				 quizListModel.rebuildList(); 
				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		b1 = new JButton(Question.QuestionType.imagepicker.getDescription());
		pageLevelButtons.add(b1);
		b1.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				try {
					setStatus("");
					populateModelFromUI(); 
				}catch(R9Exception ed) {
					setStatus(ed.getMessage());
					return;
				}
				quiz.rebuildPages();
				ImagePickerQuestion q = new ImagePickerQuestion();
			     quiz.getQuestions().add(q); 
				 quizListModel.rebuildList(); 
				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		b1 = new JButton(Question.QuestionType.matrix.getDescription());
		pageLevelButtons.add(b1);
		b1.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				try {
					setStatus("");
					populateModelFromUI(); 
				}catch(R9Exception ed) {
					setStatus(ed.getMessage());
					return;
				}
				quiz.rebuildPages();
				MatrixQuestion q = new MatrixQuestion();
			     quiz.getQuestions().add(q); 
				 quizListModel.rebuildList(); 
				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		b1 = new JButton(Question.QuestionType.multipletext.getDescription());
		pageLevelButtons.add(b1);
		b1.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				try {
					setStatus("");
					populateModelFromUI(); 
				}catch(R9Exception ed) {
					setStatus(ed.getMessage());
					return;
				}
				quiz.rebuildPages();
				MultipleTextQuestion q = new MultipleTextQuestion();
			     quiz.getQuestions().add(q); 
				 quizListModel.rebuildList(); 
				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		b1 = new JButton(Question.QuestionType.radiogroup.getDescription());
		pageLevelButtons.add(b1);
		b1.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				try {
					setStatus("");
					populateModelFromUI(); 
				}catch(R9Exception ed) {
					setStatus(ed.getMessage());
					return;
				}
				quiz.rebuildPages();
				RadioGroupQuestion q = new RadioGroupQuestion();
			     quiz.getQuestions().add(q); 
				 quizListModel.rebuildList(); 
				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
//		b1 = new JButton(Question.QuestionType.rating.getName());
//		pageLevelButtons.add(b1);
//		b1.addActionListener(new ActionListener(){ 
//			public void actionPerformed(ActionEvent e) {
//				populateModelFromUI();
//				RatingQuestion q = new RatingQuestion();
//			     quiz.getQuestions().add(q); 
//				 quizListModel.rebuildList(); 
//				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
//			}});
		b1 = new JButton(Question.QuestionType.text.getDescription());
		pageLevelButtons.add(b1);
		b1.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				try {
					setStatus("");
					populateModelFromUI(); 
				}catch(R9Exception ed) {
					setStatus(ed.getMessage());
					return;
				}
				
				
				quiz.rebuildPages();
				TextQuestion q = new TextQuestion();
			     quiz.getQuestions().add(q); 
				 quizListModel.rebuildList(); 
				 quizListView.setSelectedIndex( quizListModel.getSize() - 1); 
			}});
		
		ImageIcon icon2 = R9SystemSetting.getToolIcon("_button_cancel.png");
		JButton deletePage = new JButton(rb.getString("DeleteCurrentProblem"),icon2);
		pageLevelButtons.add(deletePage);
		deletePage.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				deleteCurProblem(); 
			}});
		//southPanel.add(PropertyUIHelper.createLine());
		
		southPanel.add(PropertyUIHelper.createTitleRow(rb.getString("CreationTitle") ,true));
		southPanel.add(PropertyUIHelper.createRow( pageLevelButtons, 5, false, Color.WHITE));
		 
		 southPanel.add(PropertyUIHelper.createTitleRow(rb.getString("ResourceManage"), true));
		 southPanel.add(PropertyUIHelper.createRow("",  imageButton,   metaButton, projectButton));
			
		 statusLabel = new JLabel();
		 statusLabel.setForeground(Color.RED);
	    southPanel.add(PropertyUIHelper.createRow(statusLabel, true));
			
		indicatorPane.add( southPanel, BorderLayout.SOUTH );
		
		indicatorPane.setBorder(BorderFactory.createEtchedBorder());
		if( surveyJsOnly ) {
		   WorkspaceMenuHandler menuHandler = new WorkspaceMenuHandler(this);
		   this.setJMenuBar(menuHandler.setupMenu());
		}
		
		if( quiz != null && quiz.getQuestions().size() > 0) {
			SwingUtilities.invokeLater(new Runnable() { 
				public void run() {
					 Question question = quiz.getQuestions().get(0);
					 switchView(question);
				}});
		}

		final Object[] buttonLabels = { rb.getString("Save"), rb.getString("SaveClose"), rb.getString("DeleteAll"), rb.getString("Preview") };
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
					 if (value.equals(buttonLabels[0])) {//save  
						 jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
						  try {
								setStatus("");
								populateModelFromUI(); 
							}catch(R9Exception ed) {
								setStatus(ed.getMessage());
								return;
							}
							quiz.rebuildPages();
							if( quiz.isEmpty()) {
								htmlPage.setSurveyQuiz(null);
							} else {
								htmlPage.setSurveyQuiz(quiz); 
							}
							CourseCreatingManager.sharedInstance.markDirty();
							CourseCreatingManager.sharedInstance.save();  
							
					 } else  if (value.equals(buttonLabels[1])) {//save all
						 jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
						 try {
								setStatus("");
								populateModelFromUI(); 
							}catch(R9Exception ed) {
								setStatus(ed.getMessage());
								return;
							}
						quiz.rebuildPages();
						if( quiz.isEmpty()) {
							htmlPage.setSurveyQuiz(null);
						} else {
							htmlPage.setSurveyQuiz(quiz); 
						}
						CourseCreatingManager.sharedInstance.markDirty();
						CourseCreatingManager.sharedInstance.save(); 
						setVisible(false); 
					 }else if (value.equals(buttonLabels[2])) { //"删除全部试题"
						 int op = JOptionPane.showConfirmDialog(SurvyJsQuizCreatingDialog.this, rb.getString("DeleteConfirm") + "？");
						 if( op == JOptionPane.CANCEL_OPTION)
							 return;
						 
						     htmlPage.setSurveyQuiz(null);
						     CourseCreatingManager.sharedInstance.markDirty();
								CourseCreatingManager.sharedInstance.save(); 
							setVisible(false);
							if( surveyJsOnly ) {
								QuizCreatingDialog.OPEN_INSTANCE --;
								if( QuizCreatingDialog.OPEN_INSTANCE <=0 )
									System.exit(0);
							}
					} else if (value.equals(buttonLabels[3])) {
						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
						try {
							setStatus("");
							populateModelFromUI(); 
						}catch(R9Exception ed) {
							setStatus(ed.getMessage());
							return;
						}
						htmlPage.setSurveyQuiz(quiz);
						
					    Exam exam = new Exam();
					    CardSurveyJsJumpPage page = new CardSurveyJsJumpPage();
					    exam.getPages().add(page);
					    page.setSurveyQuiz(quiz);
						new HtmlCodeRender( ).preview(exam, false);
					}
				}
			}
		});
		 
		this.pack();
	  
	}
	
	  
	
	public void deleteCurProblem(){
	 
		if( curQuestion == null )
			return;
		int index = quiz.getQuestions().indexOf(curQuestion);
		if( index < 0)
			return;
		quiz.getQuestions().remove(curQuestion);
	    quizListModel.rebuildList(); 
	    curQuestion = null;
		    
	    if ( index <  quiz.getQuestions().size() ){
	    	quizListView.setSelectedIndex(index); 
	    } else {
	    	if ( index -1 >= 0 )
	    		quizListView.setSelectedIndex(index -1);
	    }
	    curQuestion = (Question) quizListView.getSelectedValue();
	}
	
	public void reload(){
	 	quizListModel.rebuildList();
		if( !quiz.getQuestions().isEmpty() ){
			quizListView.setSelectedIndex(0);
		} 
	}
	
	public void switchView(Question page){
		curQuestion = page;
		 mCardLayout.show(contentPanel, page.getType().getName());
		 if( page.getType() == Question.QuestionType.bool ){
			 curDetailPanel =  booleanQuestionPanel; 
		 }else if( page.getType() == Question.QuestionType.choice )
			 curDetailPanel= checkboxesQuestionPanel;
		 else if( page.getType() == Question.QuestionType.dropdown )
			 curDetailPanel=  dropdownQuestionPanel;
		 else if( page.getType() == Question.QuestionType.html_page )
			 curDetailPanel= htmlQuestionPanel;
		 else if( page.getType() == Question.QuestionType.imagepicker )
			 curDetailPanel= imagePickerQuestionPanel;
		 else if( page.getType() == Question.QuestionType.matrix )
			 curDetailPanel=  matrixQuestionPanel;
		 else if( page.getType() == Question.QuestionType.multipletext )
			 curDetailPanel=  multipleTextQuestionPanel;
		 else if( page.getType() == Question.QuestionType.radiogroup )
			 curDetailPanel= radioGroupQuestionPanel;
		 else if( page.getType() == Question.QuestionType.rating )
			 curDetailPanel= ratingQuestionPanel;
		 else if( page.getType() == Question.QuestionType.text )
			 curDetailPanel= textQuestionPanel;
	 
		 if( curDetailPanel != null )
			 curDetailPanel.setupUI(curQuestion, quiz);
	}
	public void populateModelFromUI( ) throws R9Exception{
		if( this.curQuestion == null ) return;
		 if( curDetailPanel == null ) return;
		 curDetailPanel.fromUI(this.curQuestion, quiz); 
	}
        
    public void setStatus(String message) {
    	this.statusLabel.setText(message);
    }
	
	

	class QuestionPreviewistModel extends AbstractListModel {

		private static final long serialVersionUID = 1L;

		public QuestionPreviewistModel() {
	 	}

		public Quiz getCardset() {
			return quiz;
		}
		

		private void rebuildList() {
			this.fireContentsChanged(this, 0, getSize());
		}
		
		public void add(int index,Object data){
			if( data instanceof Question){
				Question q = (Question)data;
			   quiz.getQuestions().add(index,  q);
			}
		}
		
		public void remove(int index){
			quiz.getQuestions().remove(index);
		}

		public int getSize() {
			if (quiz == null)
				return 0;
			return quiz.getQuestions().size();
		}

		public Question getElementAt(int index) {
			if (quiz == null || quiz.getQuestions().isEmpty())
				return null;

			return quiz.getQuestions().get(index);
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
			private Question question;
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
				this.question = (Question) value;
				this.index = index;
				this.isSelected = isSelected;
				this.cellHasFocus = cellHasFocus;
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(isSelected ? list.getSelectionBackground() : list
						.getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				String content = question.toStringDetail();
			 
					Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
					g.setFont(f);
					g.setColor(Color.BLACK);
					int ypos = 14;
					for(String line : content.split("\n")){
						g.drawString(line == null ? "" : line, 14, ypos);
						ypos += 14;
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
