package r9.quiz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import r9.quiz.action.ExportExamAction;
import r9.quiz.action.HtmlTemplateSettingAction;
import r9.quiz.action.ImportCommonFileAction;
import r9.quiz.action.NewAction;
import r9.quiz.action.OpenAction;
import r9.quiz.action.ProductInfoAction;
import r9.quiz.action.SaveAsAction;

public class WorkspaceMenuHandler implements ActionListener{
	ReloadableUI workspace;
	private ResourceBundle rb;
	public WorkspaceMenuHandler(ReloadableUI workspace) {
		this.workspace = workspace;
		rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
	}
	
	public JMenuBar setupMenu(){
	 	JMenu fileMenu = new JMenu();
		 fileMenu.setText("File");
		 JMenuItem openAction = new JMenuItem(rb.getString("Open"));
		 openAction.setActionCommand("Open"); 
	 	 openAction.addActionListener(this);
		 fileMenu.add(openAction);
		 JMenuItem newAction = new JMenuItem(rb.getString("CreateQuizIncludeVideo"));
		 newAction.setActionCommand("New"); 
		 newAction.addActionListener(this);
		 fileMenu.add(newAction);
		 JMenuItem newAction2 = new JMenuItem(rb.getString("CreateSurveyJsQuiz"));
		 newAction2.setActionCommand("New-surveyjs"); 
		 newAction2.addActionListener(this);
		 fileMenu.add(newAction2);
		 JMenuItem saveAsAction = new JMenuItem(rb.getString("SaveAs"));
		 saveAsAction.setActionCommand("SaveAs"); 
		 saveAsAction.addActionListener(this);
		 fileMenu.add(saveAsAction);
		 fileMenu.addSeparator();
		 
		 JMenuItem exportAction = new JMenuItem(rb.getString("ExportExam"));
		 exportAction.setActionCommand("ExportExam"); 
		 exportAction.addActionListener(this);
		 fileMenu.add(exportAction);
		 
		 
		 
		 fileMenu.addSeparator();
		 
		 JMenuItem importJsAction = new JMenuItem(rb.getString("ImportManagerJS"));
		 importJsAction.setActionCommand("ImportJsFile"); 
		 importJsAction.addActionListener(this);
		 fileMenu.add(importJsAction);
		 JMenuItem importCssAction = new JMenuItem(rb.getString("ImportManagerCSS"));
		 importCssAction.setActionCommand("ImportCssFile"); 
		 importCssAction.addActionListener(this);
		 fileMenu.add(importCssAction);
		 
		 fileMenu.addSeparator();
		 
		 JMenuItem templateAction = new JMenuItem(rb.getString("EditPageTemplate"));
		 templateAction.setActionCommand("HtmleTemplate"); 
		 templateAction.addActionListener(this);
		 fileMenu.add(templateAction);
		 
		 fileMenu.addSeparator();
		 
		 JMenuItem infoAction = new JMenuItem(rb.getString("About"));
		 infoAction.setActionCommand("Info"); 
		 infoAction.addActionListener(this);
		 fileMenu.add(infoAction);
		 
		 JMenuBar bar = new JMenuBar();
		 bar.add(fileMenu);
		 return bar; 
	}
	
	public void actionPerformed(ActionEvent e) {
		 if (e.getActionCommand().equals("Open")){
			 new OpenAction(workspace).actionPerformed(e);
		 }
		 if (e.getActionCommand().equals("New")){
			 new NewAction( workspace, false ).actionPerformed(e);
		 }
		 if (e.getActionCommand().equals("New-surveyjs")){
			 new NewAction( workspace, true ).actionPerformed(e);
		 }
		 if (e.getActionCommand().equals("ExportExam")){
			 new ExportExamAction(   ).actionPerformed(e);
		 }
		 
		 
		 
		 if (e.getActionCommand().equals("Info")){
			 new ProductInfoAction(   ).actionPerformed(e);
		 }
		 if (e.getActionCommand().equals("SaveAs")){
			 new SaveAsAction(   ).actionPerformed(e);
		 }
		 if (e.getActionCommand().equals("ImportJsFile")){
			 new ImportCommonFileAction( (JDialog) workspace, true ).actionPerformed(e);
		 }  
		 if (e.getActionCommand().equals("ImportCssFile")){
			 new ImportCommonFileAction( (JDialog)workspace, false ).actionPerformed(e);
		 }  
		 if (e.getActionCommand().equals("HtmleTemplate")){
			 new HtmlTemplateSettingAction(   ).actionPerformed(e);
		 }
	}
}
