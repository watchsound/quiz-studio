package r9.quiz.ui.comps.video; 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import r9.quiz.CourseCreatingManager;
import r9.quiz.ui.comps.BgVideoSetting;
import r9.quiz.ui.comps.ResourceManager;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
 





 

public class VideoMediaView extends JPanel {
	BufferedImage rightarrow =  ResourceManager.getToolBufferedImageIcon("arrow_right1.png");
	 
	public static interface DurationCallback {
		void onDurationInMiniSec(long duration);
		void onDurationProgress(long duration);
	}
	public static final double Duration_In_Millis = 10000;
	double markerPos = 0;
  
	private static final int SpectrumBarHeight  = 0;
	String videoName;
	//private MediaPlayer mediaPlayer;
	//private double durationInMillis =Duration_In_Millis;
	JViewport container;
	final TimelinePanel timelinePanel;
	DurationCallback callback;
	CachedEventQueue cachedEventQueue = new CachedEventQueue();
	CachedRepaintQueue cachedRepaintQueue = new CachedRepaintQueue();
	JPanel medieContainer;
	private String videoLink;
	public VideoMediaView(final String videoName, final String videoLink, final JPanel medieContainer,
			final TimelinePanel timelinePanel,  final JViewport container, final DurationCallback callback) {
		this.videoName = videoName;
		this.videoLink = videoLink;
		this.container = container;
	    this.timelinePanel = timelinePanel;
	    this.medieContainer = medieContainer;
	    this.callback =callback;
		
		setBackground(Color.white);
		setOpaque(true);
		 
		setLayout(new BorderLayout());
		medieContainer.setLayout(new BorderLayout());
		 
		final JFXPanel fxpanel =  new JFXPanel();
		 add(fxpanel, BorderLayout.CENTER);
		 
		 final JFXPanel fxpanel2 =  new JFXPanel();
		 medieContainer.add(fxpanel2, BorderLayout.CENTER);
		 
		container.addComponentListener(new ComponentListener(){

			@Override
			public void componentResized(ComponentEvent e) {
				Platform.runLater(new Runnable() {
		  		      @Override public void run() {
		  		    	try {
		  		    		setWidth( container.getWidth() );
						} catch ( Exception e) {
							 	e.printStackTrace();
						} 
		  		      }
				 });
				 
			}

			@Override
			public void componentMoved(ComponentEvent e) { }

			@Override
			public void componentShown(ComponentEvent e) { }

			@Override
			public void componentHidden(ComponentEvent e) { }});
		
		
		try{
			 
			 Platform.runLater(new Runnable() {
	  		      @Override public void run() {
	  		    	try {
						initFX( fxpanel, fxpanel2 );
					} catch (MalformedURLException e) {
						 	e.printStackTrace();
					} 
	  		      }
			 });
			
    		//mediaPlayer.play();
			}catch(Exception ex){ 
				//u.p(ex);
			}
		
	}
	
	  MediaPlayer mp;
	   MediaView mediaView;
	     final boolean repeat = false;
	     boolean stopRequested = false;
	    boolean atEndOfMedia = false;
	      Duration duration;
	      Slider timeSlider;
	  //   Label playTime;
	  //    Slider volumeSlider;
	      HBox mediaBar;
	      
	protected void initFX(JFXPanel fxpanel, JFXPanel fxpanel2) throws MalformedURLException{
		
	    if( videoLink == null || videoLink.length() == 0) {
		 	File f = new File(CourseCreatingManager.sharedInstance.getCourseFolder(), videoName ); 
			Media hit = new Media(f.toURI().toURL().toString()); 
			mp = new MediaPlayer(hit); 
	    } else {
		 	URL url = new URL(videoLink);
			Media hit = new Media(url.toString()); 
			mp = new MediaPlayer(hit); 
	    }

  
		Group root = new Group();  
        Scene scene = new Scene(root, container.getWidth(), container.getHeight());  
        fxpanel.setScene(scene);  
        
        Group root2 = new Group();  
        Scene scene2 = new Scene(root2, container.getWidth(), container.getHeight());  
        fxpanel2.setScene(scene2); 
	      
        mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
        BorderPane.setAlignment(mediaBar, Pos.CENTER);

        final Button playButton = new Button(">");

        playButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Status status = mp.getStatus();

                if (status == Status.UNKNOWN || status == Status.HALTED) {
                    // don't do anything in these states
                    return;
                }

                if (status == Status.PAUSED
                        || status == Status.READY
                        || status == Status.STOPPED) {
                    // rewind the movie if we're sitting at the end
                    if (atEndOfMedia) {
                        mp.seek(mp.getStartTime());
                        atEndOfMedia = false;
                    }
                    mp.play();
                } else {
                    mp.pause();
                }
            }
        });
        mp.currentTimeProperty().addListener (new ChangeListener<Duration>() { 
			@Override
			public void changed(ObservableValue arg0, Duration arg1, Duration arg2) {
				 System.out.println("..currentTimeProperty().." + mp.getCurrentTime() );
	             updateValues(arg2);
			}
        });

        mp.setOnPlaying(new Runnable() {
            public void run() {
                if (stopRequested) {
                    mp.pause();
                    stopRequested = false;
                } else {
                    playButton.setText("||");
                }
            }
        });

        mp.setOnPaused(new Runnable() {
            public void run() {
                System.out.println("onPaused");
                playButton.setText(">");
            }
        });

        mp.setOnReady(new Runnable() {
            public void run() {
                duration = mp.getMedia().getDuration(); 
                updateValues(Duration.ZERO);
                SwingUtilities.invokeLater(new Runnable(){ 
					@Override
					public void run() {
						if( callback != null ){
							callback.onDurationInMiniSec((long)duration.toMillis()); 
						} 
					 	timelinePanel.setFixedTotalTimeInSec(duration.toMillis()/1000); 
					}});
            }
        });

        mp.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
        mp.setOnEndOfMedia(new Runnable() {
            public void run() {
                if (!repeat) {
                    playButton.setText(">");
                    stopRequested = true;
                    atEndOfMedia = true;
                }
            }
        });
   //     mp.setAudioSpectrumInterval(AudioSpectrumInfo.AudioSpectrumInterval_InSec);
    
        // Add spacer
//        Label spacer = new Label("   ");
//        mediaBar.getChildren().add(spacer);

        // Add Time label
//        Label timeLabel = new Label("Time: ");
//        mediaBar.getChildren().add(timeLabel);

        // Add time slider
        timeSlider = new Slider();
        timeSlider.setMin(0);
        timeSlider.setMax(1000);
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(container.getWidth() - 50); 
        timeSlider.setPrefWidth(container.getWidth() -50);
        timeSlider.setMaxWidth(container.getWidth() -50);
        timeSlider.valueProperty(). addListener(new ChangeListener<Number>() {
        	  @Override public void changed(ObservableValue  observable,
        			  Number oldValue, Number newValue) {
                if (timeSlider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                  //  mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                    //we try to avoid update player too frequently.
                	//basically stop updating player during user drag.
                	Duration s = mp.getStartTime();
                	Duration e = mp.getStopTime();
                	Duration seek =   e.subtract(s).multiply(newValue.floatValue() / 1000.0);
                    cachedEventQueue.addQueue( seek );
                    System.out.println("...");
                }else {
                	 System.out.println(">>>>>");
                }
            } 
        });
        mediaBar.getChildren().add(timeSlider);
        mediaBar.getChildren().add(playButton);
        
//        // Add Play label
//        playTime = new Label();
//        playTime.setPrefWidth(130);
//        playTime.setMinWidth(50);
//        mediaBar.getChildren().add(playTime);

//        // Add the volume label
//        Label volumeLabel = new Label("Vol: ");
//        mediaBar.getChildren().add(volumeLabel);

//        // Add Volume slider
//        volumeSlider = new Slider();
//        volumeSlider.setPrefWidth(70);
//        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
//        volumeSlider.setMinWidth(30);
//        volumeSlider.valueProperty().addListener(new InvalidationListener() {
//            public void invalidated(Observable ov) {
//                if (volumeSlider.isValueChanging()) {
//                    mp.setVolume(volumeSlider.getValue() / 100.0);
//                }
//            }
//        });
//        mediaBar.getChildren().add(volumeSlider);
  
        final  MediaView  mediaview = new MediaView(mp);
        root2.getChildren().add(mediaview);  
        root.getChildren().add(mediaBar);  
	      
        
        mediaview.setFitHeight(scene2.getHeight()); 
        mediaview.setFitWidth(scene2.getWidth());
         
        scene2.heightProperty().addListener(new ChangeListener<Number>() {
          @Override public void changed(ObservableValue observable,
          Number oldValue, Number newValue) {
            mediaview.setFitHeight((Double)newValue);
          } 
        });
         
        scene2.widthProperty().addListener(new ChangeListener<Number>() {
          @Override public void changed(ObservableValue observable,
          Number oldValue, Number newValue) {
            mediaview.setFitWidth((Double)newValue);
          }
        });
	}
 
 
	
	public void setWidth(int w){
		 timeSlider.setPrefWidth(w -50);
		  timeSlider.setMinWidth(w - 50);  
	        timeSlider.setMaxWidth(w -50);
	}
	
	public void setMarkerPos(double percentage){
		markerPos = percentage;
		repaint();
		 
	}
	
	public void stop() {
		try {
			mp.pause();
		}catch(Exception ex) {}
		
	}
	
	
	public Dimension getPreferredSize(){
		return new Dimension(container.getWidth(), 25 + SpectrumBarHeight);
	} 


	
	protected void updateValues(final Duration newDuration) {
        if (  timeSlider != null  ) {
            Platform.runLater(new Runnable() {
                public void run() {
 //                  final Duration currentTime = mp.getCurrentTime();
//                    playTime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !timeSlider.isValueChanging()) {
                    	timeSlider.setValue(newDuration.toMillis() / duration.toMillis() 
                                * 1000.0);
                    }
                    SwingUtilities.invokeLater(new Runnable(){ 
						@Override
						public void run() {
							if( callback != null ){
								callback.onDurationProgress((long)newDuration.toMillis());
								timelinePanel.updateAudioPosition((long)newDuration.toMillis());
							} 
						}});
                   
//                    if (!volumeSlider.isValueChanging()) {
//                        volumeSlider.setValue((int) Math.round(mp.getVolume()
//                                * 100));
//                    }
                }
            });
        }
    }
	
	
	
	class CachedEventQueue { 
		Timer worker = new Timer();
		public void addQueue(final Duration d){    
			synchronized( worker ){ 
				if( worker != null ){
					worker.cancel(); 
					worker = new Timer();
					worker.schedule(new TimerTask(){ 
						@Override
						public void run() { System.out.println("..." + d.toString());
						   if (mp.getStatus() == MediaPlayer.Status.STOPPED) {
							  mp.pause();
						   } 
						   mp.seek(d);
						}}, 1000);
				}
			}
		}
	}
	class CachedRepaintQueue { 
		Timer worker = new Timer();
		public void addQueue( ){
			synchronized( worker ){ 
				if( worker != null ){
					worker.cancel(); 
					worker = new Timer();
					worker.schedule(new TimerTask(){ 
						@Override
						public void run() {
							//repaintSpectrumPane();
						}}, 1000);
				}
			}
		}
	}
	
	
 
}

