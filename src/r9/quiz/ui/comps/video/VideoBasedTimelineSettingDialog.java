package r9.quiz.ui.comps.video;
 
 
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener; //property change stuff
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import r9.quiz.Constants;
import r9.quiz.CourseCreatingManager;
import r9.quiz.QuizStyleDialog;
import r9.quiz.cards.HasIdName;
import r9.quiz.ui.comps.BgVideoSetting;
import r9.quiz.ui.comps.BgVideoSettingItem;
 
 

public class VideoBasedTimelineSettingDialog extends QuizStyleDialog
                   implements    PropertyChangeListener {
 
     
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JOptionPane optionPane;
 
    private String btnString1 =  "保存";
    private String btnString2 =  "关闭窗口"; 
    
    private  BgVideoSetting group;
    
	private JScrollPane bottomTimelineScrollPane;

	 
	private TimelinePanel timelinePane;

	private VideoMediaView mediaView;

	private JPanel medieContainer;
  
    /** Creates the reusable dialog. */
    public VideoBasedTimelineSettingDialog(  ) {
        super( );
        List<HasIdName> timelines= new ArrayList<HasIdName>();
        timelines.addAll( CourseCreatingManager.sharedInstance.getExam().getCardList() );
    	 
    	
       this. group = new BgVideoSetting(  );  
       for( HasIdName item : timelines) { 
    	   if( item.getPosValue() < 0)
    		   continue;
    	   BgVideoSettingItem ii = new BgVideoSettingItem(item);
    	   this.group.getItems().add(ii);
       }
      
     
       
       TimelinePanel.TimelinePanelCallback CALLBACK = new TimelinePanel.TimelinePanelCallback() { 
		@Override
		public void onTimelineChange(long timespan) { 
		} 
		@Override
		public void onTimeBoxSelected(BgVideoSettingItem box) { 
		} 
		@Override
		public void onTimeBoxMoving(BgVideoSettingItem box) { 
		} 
		@Override
		public void onTimeBoxDeleted(BgVideoSettingItem box) { 
		}
	};
	

	
	timelinePane = new TimelinePanel(group,  CALLBACK,  TimelinePanel.PX_PER_SECOND, timelines);
	timelinePane.setPreferredSize(new Dimension( Constants.CARD_WIDTH  , 160));

	bottomTimelineScrollPane = new JScrollPane(timelinePane);

	bottomTimelineScrollPane
			.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	bottomTimelineScrollPane.getHorizontalScrollBar()
			.addAdjustmentListener(new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if (medieContainer != null)
						medieContainer.repaint(); 
				}
			});
	
	bottomTimelineScrollPane.getViewport().addChangeListener(
			new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (medieContainer != null)
						medieContainer.repaint();
					 
				}
			}); 
	
	//bottomTimelineScrollPane.setMinimumSize(new Dimension((int)getWidth(),160));
	
	
	      medieContainer = new JPanel();
	    VideoMediaView.DurationCallback dcallback = new VideoMediaView.DurationCallback() { 
			@Override
			public void onDurationProgress(long duration) { 
			}
			
			@Override
			public void onDurationInMiniSec(long duration) { 
			}
		};
	    mediaView = new VideoMediaView(CourseCreatingManager.sharedInstance.getExam().getVideoName(),
	    		CourseCreatingManager.sharedInstance.getExam().getVideoLink(),
	    		medieContainer, timelinePane,
			bottomTimelineScrollPane.getViewport(), dcallback); 
	    bottomTimelineScrollPane.setColumnHeaderView(mediaView);
	    
	    JPanel contentPane = new JPanel();
	       contentPane.setLayout(new BorderLayout());
	       contentPane.add(medieContainer, BorderLayout.CENTER);
	       
	       contentPane.add(bottomTimelineScrollPane, BorderLayout.SOUTH); 
	       
	       
        Object[] options = {btnString1,   btnString2};
 
        //Create the JOptionPane.
        optionPane = new JOptionPane(contentPane,
                                    JOptionPane.PLAIN_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);
 
        //Make this dialog display it.
        setContentPane(optionPane);
 
        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                    optionPane.setValue(new Integer(
                                        JOptionPane.CLOSED_OPTION));
            }
        });
 
        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
             
            }
        });
 
      
        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
    }
 
    /** This method reacts to state changes in the option pane. */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
 
        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();
 
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }
 
            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);
 
            if (btnString1.equals(value)) {
            	   mediaView.stop();
            	   for(BgVideoSettingItem item : group.getItems()) {
            		   item.getTimeline().setPosValue(item.getPosInVideo());
            	   }
                   setVisible(false);
            } else if (btnString2.equals(value)) {
            	 mediaView.stop();
                 setVisible(false);
          } else {   
            	 
            	  setVisible(false);
            }
        }
    }
  
   
  
}