package r9.quiz.ui.comps;
 

import java.awt.Dimension;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities; 

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
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
import r9.quiz.Constants;
import r9.quiz.R9SystemSetting;
import r9.quiz.util.Utils;

/** 
  */
public class MathJaxImportDialog extends JDialog {
 
	private static final long serialVersionUID = 1L;

	public static interface MathJaxCallback {
		void onResult(String result, String imageStr);
		String getContent();
		boolean needImageStr();
	}
	private JOptionPane jOptionPane;

	final JFXPanel fxPanel;

	private Browser browser;

	private final MathJaxCallback workspace; 
	public MathJaxImportDialog(Frame parent, final MathJaxCallback workspace ) {
		super(parent, "MathJax Workspace", true);
		this.workspace = workspace;  
		fxPanel = new JFXPanel();
		 ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
		 
		final Object[] buttonLabels = {rb.getString("Close"), rb.getString("Import"), rb.getString("Copy") };
		Object[] panelContents = { fxPanel };
		jOptionPane = new JOptionPane(panelContents, JOptionPane.PLAIN_MESSAGE,
				JOptionPane.DEFAULT_OPTION, null, buttonLabels, buttonLabels[0]);

		setContentPane(jOptionPane);
//		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//
//		addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent we) {
//				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
//			}
//		});

		jOptionPane
				.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
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
								setVisible(false);
							}
							if (value.equals(buttonLabels[1])) {
								jOptionPane
										.setValue(JOptionPane.UNINITIALIZED_VALUE);

//								String[] values = { "1", "1.2", "1.4", "1.5",
//										"1.8", "2", "2.2", "2.5", "2.8", "3",
//										"3.2", "3.5", "3.8", "4", "4.5", "5",
//										"6", "7", "8", "9", "10" };
//								String answer = null;
//								answer = (String) JOptionPane
//										.showInputDialog(
//												null,
//												"缩放比例",
//												"Math",
//												JOptionPane.PLAIN_MESSAGE,
//												null, values, "3");
//
//								if (answer == null)
//									answer = "3";
//
//								final double scale = Double.parseDouble(answer);
							 
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										importMathJaxResultToWorkspace( ); 
									} 
								});
							} else if (value.equals(buttonLabels[2])) {
								 copyToCopyPasteBoard();
								 setVisible(false); 
							} else {
								jOptionPane
										.setValue(JOptionPane.UNINITIALIZED_VALUE);
							}
						}
					}
				});

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX();

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						setPreferredSize(new Dimension(920, 750));
						pack();
					}
				});
			}
		});

	}

	private void copyToCopyPasteBoard() {
		String svgString = (String) browser.webEngine
				.executeScript("exportMathJaxStringToWorkspace( )");
		Utils.setClipboardString(svgString);
	}
	private void importMathJaxResultToWorkspace(  ) { 
		String svgString = (String) browser.webEngine
				.executeScript("exportMathJaxStringToWorkspace( )");
//		String svgString = (String) browser.webEngine
//				.executeScript("exportToWorkspace(1)");
		  System.out.println( svgString);
		// String viewbox =
		// (String)browser.webEngine.executeScript("getViewBox()");
		  if( !workspace.needImageStr() ) {
		      workspace.onResult(svgString, null);
		      setVisible(false);
		  } else { 
			  browser.webEngine .executeScript("generateImageString( )");
				 final Timer timer = new Timer(); 
				 timer.schedule(new TimerTask() {           
					 public void run() {
						 Platform.runLater(new Runnable() {
								@Override
								public void run() {
									  timer.cancel();
					                  String c = (String) browser.webEngine .executeScript("getImageString( )");
					                 // SwingUtilities.invokeLater(new Runnable() { 
									//	public void run() {
											 workspace.onResult(svgString, c);
											 setVisible(false);
									//	}}); 
								} 
							}); 
		             } 
				 }, 1000); 
		  }
	}
	 

	private void loadContent() {
		File dir = new File(R9SystemSetting.sharedInstance.getThirdPartyDir(), "mathjax");
		File mathJaxEntry = new File(dir, "MathFormulaWithJax.html");

		try {
			final ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
			browser.webEngine.getLoadWorker().stateProperty().addListener(
			            new ChangeListener<State>() {
			              @Override public void changed(ObservableValue ov, State oldState, State newState) {

			                  if (newState == Worker.State.SUCCEEDED) {
			                	  String inputLatex =  Utils.normalize_ascii2entity( workspace.getContent());
			                	  if( inputLatex == null || inputLatex.trim().length() == 0 ){
			                		  inputLatex = "\\\\(             \\\\)";
			                	  }
			                	  if( inputLatex != null && inputLatex.length() > 0 ){
			                		 String suggestion = rb.getString("JaxSuggestion");
			          				 String script = "(function() { var block = document.getElementById('math_input'); block.value=entity2html('" +
		                  						inputLatex  + "');   document.getElementById('jax_suggestions').innerHTML='"+ suggestion +"'    ;"
		                  						+ "    })(); ";
			          				 System.out.println(script);
			                         browser.webEngine .executeScript(script); 
			          			}
			                }
			                  
			                }
			            });
			browser.webEngine.load(mathJaxEntry.toURI().toURL().toString());
		 	
			
		} catch (Exception e) {
			// u.p(e);
		}
	}

	protected void initFX() {

		// final StackPane layout = new StackPane();
		browser = new Browser();
		loadContent();

		// layout.getChildren().addAll( browser );
		Scene scene = new Scene(browser, Constants.CARD_WIDTH,
				Constants.CARD_HEIGHT+200, javafx.scene.paint.Color.web("#FFFFFF"));

		fxPanel.setScene(scene);

	}

	class Browser extends Region {

		final WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();

		public Browser() {
			// apply the styles
			getStyleClass().add("browser");
			// load the web page
			// webEngine.load("http://www.oracle.com/products/index.html");
			// add the web view to the scene
			getChildren().add(browser);

		}

		public void loadContent(String content) {
			System.out.println(content);
			webEngine.loadContent(content);
			// JSObject jsobj = (JSObject) webEngine.executeScript("window");
			// jsobj.setMember("r9gifstyle", new Bridge());
		}

		private Node createSpacer() {
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			return spacer;
		}

		@Override
		protected void layoutChildren() {
			double w = getWidth();
			double h = getHeight();
			layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
		}

		@Override
		protected double computePrefWidth(double height) {
			return Constants.CARD_WIDTH;
		}

		@Override
		protected double computePrefHeight(double width) {
			return Constants.CARD_HEIGHT+200;
		}
	}

	class Bridge {
		public void invokeGifAction(String actionid) {
			System.out.println(actionid);
		}
	}
}
