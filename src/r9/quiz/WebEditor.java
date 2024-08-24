package r9.quiz;
 



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
//import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.JPanel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import r9.quiz.cards.HtmlContent;
import r9.quiz.ui.comps.MathJaxImportButton;
import r9.quiz.ui.comps.MathJaxImportDialog; 
//import r9.quiz.cards.HtmlPage;
import r9.quiz.util.ImageUtil;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.Utils;
//import javax.swing.event.ChangeListener;
//import com.sun.webpane.webkit.JSObject;
//import com.sun.webkit.dom.JSObject; 

/** 
  */
public class WebEditor extends JPanel  
{ 
  
	private static final long serialVersionUID = 1L;

	final JFXPanel fxPanel;
    
    private Browser browser; 
    
    private HtmlContent question;

//	private JButton image1Button;
//
//	private JButton image2Button;
//
//	private JButton image3Button;
//
//	private JButton image4Button;
//
//	private JLabel image2Label;
//
//	private JLabel image3Label;
//
//	private JLabel image4Label;
//
//	private JLabel image1Label;

	private int fwidth;

	private int fheight;

	private MathJaxImportButton mathjaxButton;
	
	public WebEditor(Frame parent){
		this(parent, Constants.CARD_WIDTH, Constants.CARD_HEIGHT);
	}
	
     
	public WebEditor(Frame parent, int width, int height)
	{
		this.setLayout(new BorderLayout());
        this.fwidth = width;
        this.fheight = height;
        fxPanel = new JFXPanel();
        this.add(fxPanel, BorderLayout.CENTER);
        final MathJaxImportDialog.MathJaxCallback callback = new MathJaxImportDialog.MathJaxCallback() { 
			@Override
			public void onResult(String content, String imageStr) {
				loadContent(content);
			} 
			@Override
			public boolean needImageStr() {
				return false;
			}
			@Override
			public String getContent() {
				 return importHtmlContent();
			}};
        mathjaxButton = new MathJaxImportButton("MathJax",callback);
        this.add(PropertyUIHelper.createRow(mathjaxButton, false),BorderLayout.SOUTH);
         
	    Platform.runLater(new Runnable() {
		      @Override public void run() {
		    	  browser = new Browser(); 
		  	    //   loadContent();   
		  	     Scene  scene = new Scene(browser, fwidth,
		  	        		fheight -40 , javafx.scene.paint.Color.web("#FFFFFF"));
		  	        
		  	    fxPanel.setScene( scene ); 
		      }
		 }); 
	    
	    
	}
	 public void updateImageListInBrowser() {
		 browser.updateImageListInBrowser(); 
	 }
	 
	
	public String getImageFileListInHtmlCode() {
		List<File> files = CourseCreatingManager.sharedInstance.getAllImageFiles( );
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("   var r9imagelist = [];");
 		for(File f : files)
            	sb.append(" r9imagelist.push('" + f.getName() + "');");
		sb.append("</script>");
		return sb.toString();
	}
	 

	public String importHtmlContent(  ) { 
      	return (String)browser.webEngine.executeScript("exportToWorkspace( )");
	}
	
	public void loadHtmlContent(final String content){
		 Platform.runLater(new Runnable() {
		      @Override public void run() {
		    	  loadContent(content); 
		      }
		 }); 
	}
	
	 
	  
	private void loadContent(String content){
		 
		File dir = new File( CourseCreatingManager.sharedInstance.getCourseFolder() );
	 	File  htmlEditor = new File( dir, "workspac.html");
        if( htmlEditor.exists() ){
        	htmlEditor.delete();
        }
        try {
        	String imagelistcode = getImageFileListInHtmlCode();
			Utils.copyStringToFile(getHtml(content, imagelistcode), htmlEditor);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //copy images
        
        
         try { 
//				 browser.webEngine  .getLoadWorker().stateProperty() .addListener( 
//				 
//					        new ChangeListener<State>() {
//					            public void changed(ObservableValue ov, State oldState, State newState) {
//					                if (newState == State.SUCCEEDED) {
//					                	 JSObject jsobj = (JSObject) browser.webEngine.executeScript("window");
//									     jsobj.setMember("java", new Bridge());
//					                }
//					            } 
//							 
//					        });
				 browser.webEngine.load(htmlEditor.toURI().toURL().toString());
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 
			}
	}
	 
	private String getHtml(String content, String imagelistcode){
		return "<!DOCTYPE html> " +
				"<html>" +
				"<head>" +
				"  <meta charset=\"UTF-8\">" +
				"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" />" +
				"  <title>summernote</title>" +
				"  <!-- include jquery -->" +
				"  <script src=\"../../third_party/jquery-1.11.3.min.js\"></script>" +

				"  <!-- include libraries BS3 -->" +
				"  <link rel=\"stylesheet\" href=\"../..//third_party/bootstrap.min.css\" />" +
				"  <script type=\"text/javascript\" src=\"../../third_party/bootstrap.min.js\"></script>" +

				"  <link rel=\"stylesheet\" href=\"../../third_party/summernote.css\">" +
				"  <script type=\"text/javascript\" src=\"../../third_party/summernote.js\"></script>" +

				"  <script type=\"text/javascript\">" +
				"    $(function() {" +
				"      $('.summernote').summernote({" +
				"        height: " +  (fheight -60)  +
				"      });" +

	//			"      $('form').on('submit', function (e) {" +
	//			"        e.preventDefault();" +
	//			"        alert($('.summernote').val());" +
	//			"      });" +
				"    });" +
				"  function exportToWorkspace(){ return $('.summernote').summernote('code'); } "+
		       
				"  </script>" +
				"</head>" +
				"<body>" +
				imagelistcode + 
				"<form action=\"#\" novalidate>" +
				"  <div class=\"form-group\">" +
				"    <textarea name=\"text\" class=\"summernote\" id=\"contents\" title=\"Contents\">" + content + "</textarea>" +
				" </div>" +
				"</form>" +
				"</body>" +
				"</html>"  ;
	}
	
	public void resetUIStates(final HtmlContent nquestion ){ //, final boolean isBackAllowed){
		 Platform.runLater(new Runnable() {
		      @Override public void run() {
		    	  if (  question != null ){ 
		    		try {
		    			String content =  importHtmlContent();
				    	question.setContent(content);
		    		} catch(Exception ex) { 
		    		} 
		  		  }
		  		  question = nquestion;
		  		  loadHtmlContent(question.getContent()); 
		      }
		 });  
	}
	public BufferedImage getImagePreview(){
		    BufferedImage myImage = (BufferedImage) this. createImage(
		    		this.getWidth(),  this.getHeight());
        	Graphics g = myImage.getGraphics();
        	this.print(g); 
			//resize 
			BufferedImage thumbImage =  ImageUtil.resizeImage(myImage, fwidth/2, fheight/3);
		 	return thumbImage;
	}
	
	public void populateModelFromUI( ){
		 Platform.runLater(new Runnable() {
		      @Override public void run() {
		    	  if( question == null)
		    		  return;
		    	   String content =  importHtmlContent();
		    	    question.setContent(content);
		    	  //  question.setBackAllowed(canBack);
		      }
		 });  
	}
	public Dimension getPreferredSize() {
		return new Dimension(fwidth, fheight);
	}
	public Dimension getMinimumSize() {
		return new Dimension(fwidth, fheight);
	}
	class Browser extends Region {
		 
	    final WebView browser = new WebView();
	    final WebEngine webEngine = browser.getEngine();
	     
	    public Browser() {
	        //apply the styles
	        getStyleClass().add("browser");
	        // load the web page
	      //  webEngine.load("http://www.oracle.com/products/index.html");
	        //add the web view to the scene
	        getChildren().add(browser);
	 
	    }
	    
	    public void loadContent(String content){
	    	System.out.println( content );
	    	webEngine.loadContent(content);
	   // 	 JSObject jsobj = (JSObject) webEngine.executeScript("window");
		  //    jsobj.setMember("r9gifstyle", new Bridge());
	    }
	    public void updateImageListInBrowser() {
	    	StringBuilder sb = new StringBuilder();
	    	List<File> files = CourseCreatingManager.sharedInstance.getAllImageFiles( );
			sb.append("   var r9imagelist = [];");
	 		for(File f : files)
	            	sb.append(" r9imagelist.push('" + f.getName() + "');");
	 		webEngine.executeScript(sb.toString()); 
	    }
	    
	    private Node createSpacer() {
	        Region spacer = new Region();
	        HBox.setHgrow(spacer, Priority.ALWAYS);
	        return spacer;
	    }
	 
	    @Override protected void layoutChildren() {
	        double w = getWidth();
	        double h = getHeight();
	        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
	    }
	 
	    @Override protected double computePrefWidth(double height) {
	        return fwidth ;
	    }
	 
	    @Override protected double computePrefHeight(double width) {
	        return fheight;
	    }
	} 
 
	class Bridge {
	    public void invokeGifAction(String actionid) {
	         System.out.println( actionid );
	    }
	}
 
}

