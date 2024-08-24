//package r9.quiz;
// 
//
//
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.Frame;
//import java.awt.Graphics;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.awt.image.BufferedImage;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
////import java.beans.PropertyChangeListener;
//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//import java.util.Vector;
//
//import javafx.application.Platform;
//import javafx.embed.swing.JFXPanel;
//import javafx.geometry.HPos;
//import javafx.geometry.VPos;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.Region;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
//
//import javax.swing.BoxLayout;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JDialog;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.SwingUtilities;
//
//import r9.quiz.cards.CardFillInBlankQuestion;
//import r9.quiz.cards.CardHtmlPage;
//import r9.quiz.util.ImageUtil;
//import r9.quiz.util.Utils;
//import r9.quiz.*;
////import javax.swing.event.ChangeListener;
////import com.sun.webpane.webkit.JSObject;
////import com.sun.webkit.dom.JSObject; 
//
///** 
//  */
//public class ProductInfoDialog extends JDialog  
//{ 
// 
//	final JFXPanel fxPanel;
//    
//    private Browser browser; 
//      
//	public ProductInfoDialog(Frame parent )
//	{
//		super(parent);
//		  ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
//		  
//		final Object[] buttonLabels =   { rb.getString("Close") };
//		JPanel panelContents =  new JPanel();
//		panelContents.setLayout(new BorderLayout());
//		
//		fxPanel = new JFXPanel();
//		panelContents.add(fxPanel, BorderLayout.CENTER); 
//		
//		JOptionPane jOptionPane = new JOptionPane(panelContents, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
//
//		setContentPane(jOptionPane);
//		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//
//		addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent we)
//			{
//				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
//			}
//		});
//
//		jOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
//			public void propertyChange(PropertyChangeEvent e)
//			{
//				String prop = e.getPropertyName();
//				if(isVisible() 
//					&& (e.getSource() == jOptionPane)
//					&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
//				{
//					Object value = jOptionPane.getValue();
//					if(value == JOptionPane.UNINITIALIZED_VALUE)
//					{
//						return;
//					}
//					if(value.equals(buttonLabels[0]))
//					{  
// 					    setVisible(false);
//					}
//					 
//					else
//					{
//						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
//					}
//				}
//			}
//		});
//		 
//         
//	    Platform.runLater(new Runnable() {
//		      @Override public void run() {
//		    	  browser = new Browser(); 
//		  	    //   loadContent();   
//		  	     Scene  scene = new Scene(browser, Constants.CARD_WIDTH,
//		  	        		Constants.CARD_HEIGHT  , javafx.scene.paint.Color.web("#FFFFFF"));
//		  	        
//		  	    fxPanel.setScene( scene ); 
//		  	    loadContent(getHtml()); 
//		      }
//		 }); 
//	    
//	    pack();
//	}
//	
//	  
//	  
//	private void loadContent(String content){
//		R9SystemSetting assetDatabase = R9SystemSetting.getInstance();
//		File dir = assetDatabase.getTempDir();
//	 	File  htmlEditor = new File( dir, "info.html");
//        if( htmlEditor.exists() ){
//        	htmlEditor.delete();
//        }
//        try {
//			Utils.copyStringToFile(content, htmlEditor);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//         try { 
// 
//				 browser.webEngine.load(htmlEditor.toURI().toURL().toString());
//				
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				 
//			}
//	}
//	private String getHtml( ){
//		if( R9SystemSetting.LOCALE.getLanguage().equals("zh"))
//			return this.getHtml_zh();
//		return this.getHtml_en();
//	}
//	private String getHtml_en( ){
//		return "<!DOCTYPE html> " +
//				"<html>" +
//				"<head>" +
//				"  <meta charset=\"UTF-8\">" +
//				"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" />" +
//				"  <title>产品介绍</title>" +
//		 
//				"</head>" +
//				"<body>" +
//				"<h4 style='color:red;'>How to Import Image？</h4>" +
//				"<div>Under【Resource Setting】 ，Click:【Image Setting】，then import new image, or delete existing image. <div>" +
//				"<ol><li>On webpage editor, click 'image' button, you can pick and insert image into html page. </li>" +
//				"<li>Under Image-Selection Problem, righ-click mouse buttona and bring up popupmenu, then select images </li></ol>" +
//				
//				"<h4 style='color:red;'>How to insert quiz on existing video file ？</h4>" +
//				"<div>Under【Resource Setting】，Click:【Course Video】，then you can import video file。<div>" +
//				"<ol><li>Authoring problem</li>" +
//				"<li>Under【Resource Setting】，Click:【Video Insert Points】， set up quiz playing time。</li></ol>" +
//				
//				"<h4 style='color:red;'>How to use SurveyJS style quiz？</h4>" +
//				"<div>SurveyJS style quiz can be used as independent quiz unit, it can also be mixed with noraml quiz problem."
//				+ " <div>" +
//				"<ol><li>Under【Creating New Problem】, select【SurveyJS】</li>" +
//				"<li> Click 【SurveyJS】， open SurveyJS quiz editor dialog。</li></ol>" +
//				
//		"<h4 style='color:red;'>How to Use MathJax to Support Math Formula？</h4>" +
//		"<div>Both Rich-Text Editor and Green-Border-Colored Text Input support MathJax <div>" +
//		"<ol><li>Math formula should be enclosed by \\( and \\) in order to show math inline with text content.</li>" +
//		"<li> In Course Meta-Data Setting Dialog (click 'Course Meta-Data' button), check 'Use MathJax'. </li> " +
//		"<li> Right click mouse to bring up MathJax Editor window on green-border text input field</li></ol>" +
//		
//				"<h4 style='color:red;'>How to edit quiz template, how to create new quiz template？</h4>" +
//				"<div>The UI for quiz problem is editable, you can aslo design new UI for quiz problem 。<div>" +
//				"<ol><li>If you need to import new CSS/Javascript file for template, "
//				+ "from 【File】- 【Import/edit JS File】 or 【Import/edit CSS File】</li>" +
//				"<li> Click【File】- 【Edit Problem Template】 ，edit existing template, or create new template </li> " +
//				"<li> ${} in template is reserved for dynamic content or variable used in problem.  </li> " +
//				"<li>  ${questionBody} is used for problem content</li> " +
//				"<li>  ${name} is used for problem title/name</li> " +
//				"<li>  ${msgInfo} is used for feedback on correct answer </li> " +
//				"<li>  ${msgError} is used for feedback on incorrect answer</li> " +
//				"  </ol>" +
//				"</body>" +
//				"</html>"  ;
//	} 
//	private String getHtml_zh( ){
//		return "<!DOCTYPE html> " +
//				"<html>" +
//				"<head>" +
//				"  <meta charset=\"UTF-8\">" +
//				"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" />" +
//				"  <title>产品介绍</title>" +
//		 
//				"</head>" +
//				"<body>" +
//				"<h4 style='color:red;'>如何引入图片？</h4>" +
//				"<div>在【资源设置和管理】下，点击【图片管理】，导入新图片，删除已经导入的图片。<div>" +
//				"<ol><li>在网页文本编辑界面， 点击【图片】，可以在页面插入图片</li>" +
//				"<li>在图片选择题中，鼠标右键打开菜单， 可以选择图片</li></ol>" +
//				
//				"<h4 style='color:red;'>如何在已有的视频上插入测试页面？</h4>" +
//				"<div>在【资源设置和管理】下，点击【课件视频】，导入视频文件。<div>" +
//				"<ol><li>编写试题</li>" +
//				"<li>在【资源设置和管理】下，点击【视频插入点设置】，设置各个试题页面的播放时间点。</li></ol>" +
//				
//				"<h4 style='color:red;'>如何使用SurveyJS风格的测试题？</h4>" +
//				"<div>SurveyJS风格的测试题可以单独使用， 也可以和基础题型混合使用<div>" +
//				"<ol><li>在【创建新问题-题型】下， 选择【新文本页】</li>" +
//				"<li> 点击【关联SurveyJS问卷】， 打开SurveyJS问卷编辑界面。</li></ol>" +
//				
//				"<h4 style='color:red;'>如何使用MathJax数学公式？</h4>" +
//				"<div>富文本编辑区，和绿色边框区都支持MathJax<div>" +
//				"<ol><li>为了和文字混合使用数学公式，数学公式要用\\(和 \\)隔开</li>" +
//				"<li> 对于绿色的输入框，鼠标点击右键可以打开MathJax编辑界面</li></ol>" +
//
//				"<h4 style='color:red;'>如何编辑网页模板， 如何创建新的网页模板？</h4>" +
//				"<div>基础题型的页面风格是可以重新设计的。<div>" +
//				"<ol><li>如果需要使用新的CSS文件，或者JS文件，从【File】- 【导入&管理JS文件】 或者 【导入&管理CSS文件】， 导入新文件</li>" +
//				"<li> 点击从【File】- 【编辑网页模板】 ， 编辑已有的模板，或者创建新的模板</li> " +
//				"<li> 模板中的${} 表示试题问卷里的动态内容。 </li> " +
//				"<li>  ${questionBody} 表示问题内容</li> " +
//				"<li>  ${name} 表示题目标题</li> " +
//				"<li>  ${msgInfo} 表示回答正确时的提示</li> " +
//				"<li>  ${msgError} 表示回答正确时的提示</li> " +
//				"  </ol>" +
//				"</body>" +
//				"</html>"  ;
//	}
//	 
//
//	class Browser extends Region {
//		 
//	    final WebView browser = new WebView();
//	    final WebEngine webEngine = browser.getEngine();
//	     
//	    public Browser() {
//	        //apply the styles
//	        getStyleClass().add("browser");
//	        // load the web page
//	      //  webEngine.load("http://www.oracle.com/products/index.html");
//	        //add the web view to the scene
//	        getChildren().add(browser);
//	 
//	    }
//	    
//	    public void loadContent(String content){
//	    	System.out.println( content );
//	    	webEngine.loadContent(content);
//	   // 	 JSObject jsobj = (JSObject) webEngine.executeScript("window");
//		  //    jsobj.setMember("r9gifstyle", new Bridge());
//	    }
//	    
//	    private Node createSpacer() {
//	        Region spacer = new Region();
//	        HBox.setHgrow(spacer, Priority.ALWAYS);
//	        return spacer;
//	    }
//	 
//	    @Override protected void layoutChildren() {
//	        double w = getWidth();
//	        double h = getHeight();
//	        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
//	    }
//	 
//	    @Override protected double computePrefWidth(double height) {
//	        return Constants.CARD_WIDTH ;
//	    }
//	 
//	    @Override protected double computePrefHeight(double width) {
//	        return Constants.CARD_HEIGHT;
//	    }
// 	} 
// 
//	class Bridge {
//	    public void invokeGifAction(String actionid) {
//	         System.out.println( actionid );
//	    }
//	}
// 
//}
//
