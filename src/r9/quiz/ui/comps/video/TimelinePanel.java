package r9.quiz.ui.comps.video;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import r9.quiz.R9SystemSetting;
import r9.quiz.cards.HasIdName;
import r9.quiz.ui.comps.BgVideoSetting;
import r9.quiz.ui.comps.BgVideoSettingItem;
import r9.quiz.ui.comps.ResourceManager;
 
public class TimelinePanel extends JPanel{
	public static final int   PX_PER_SECOND = 100;
	public static final int   BOX_TAIL_WIDTH =  0;
	
	public static interface TimelinePanelCallback {
		void onTimelineChange(long timespan);
		//void onTimelineMark(long markpos);
		void onTimeBoxSelected(BgVideoSettingItem box);
		void onTimeBoxMoving(BgVideoSettingItem box);
		void onTimeBoxDeleted(BgVideoSettingItem box);
		//void onTimeBoxRangeChange(long startPos);
	}
	public static interface AudioDurationCallback {
		void onDurationInMiniSec(long duration);
	}
	public static final Color BOX_COLOR = new Color(220,254,142);
	public static final Color LINE_COLOR = new Color(254,142,154);
	public static final Color HIGHLIGHT_COLOR = Color.BLUE;

	public static final int ROW_HEIGHT = 25;
	public static final int DELETE_COL_WIDTH = 16;
	public static final int   PLAY_BTN_WIDTH = 45;
	
	private     double time2space = 100;    // 1 second to 100 px?
	
	BgVideoSetting group;
	private static List<Object> timeBoxList = new ArrayList<Object>();
	TimelinePanelCallback callback;
	
	TimeBox selectedTimeBox;
	List<TimeBox> highlightedBoxes = new ArrayList<TimeBox>();
	//VectorDocContext context;
	//int drawingMode = 0;
	double  fixedTotalTime; 
	
 	 
	private BufferedImage deleteIcon = ResourceManager.getToolIconImage("_button_cancel.png",16,16);
 
	private int curAudioPosition;
 
	public void setFixedTotalTimeInSec(double fixedTotalTimeInSec ){
		this.fixedTotalTime = fixedTotalTimeInSec; 
		setupTimebox();
	}
//	public void adjustFixedTotalTimeInSec(double newFixedTotalTimeInSec ){
//		 if( newFixedTotalTimeInSec <= 1 ) return;
//	 	 double scale  = newFixedTotalTimeInSec / fixedTotalTime;
//		
//		for(BgVideoSettingItem item : group.getItems()){ 
//				item.setPosInVideo( item.getPosInVideo() * scale ); 
//		} 
//		 
//		this.fixedTotalTime = newFixedTotalTimeInSec; 
//		setupTimebox();
//	}
   
	public void updateAudioPosition(long duration){ 
		if( fixedTotalTime == 0 ) return;
		this.curAudioPosition = (int)((duration / 1000.0 / fixedTotalTime) * getAdjustWidth());
		repaint(DELETE_COL_WIDTH + curAudioPosition -50, 0, 100, this.getHeight());
		currentVideoProgress = duration;
	}
	
	public int getAdjustWidth(){
		if( getWidth() == 0)
			return 0;
		return getWidth() - this.DELETE_COL_WIDTH - PLAY_BTN_WIDTH;
	}
	public double getTime2space(){
	   return  fixedTotalTime == 0 ? time2space :  ( getAdjustWidth() / fixedTotalTime ); 
	} 
	
	  
	final List<HasIdName> timelineNames;
	private long currentVideoProgress;
	private ResourceBundle rb;
	public TimelinePanel(final BgVideoSetting group,  
			final TimelinePanelCallback callback,  
			final int time2space , final List<HasIdName> timelineNames ){ 
		this.group = group;
		this.callback = callback; 
		this.time2space = time2space; 
		this.timelineNames = timelineNames;
		rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
	 	//setupTimebox(); 
		MouseAdapter adapter =  new MouseAdapter()
	    {
			Point start;
		 	 
			 public void mouseClicked(MouseEvent e) {
				 if( e.getX()< DELETE_COL_WIDTH ){
					final int rowIndex =   e.getY() / ROW_HEIGHT ;
					 if( rowIndex < timeBoxList.size() ){
						 final Object obj = timeBoxList.get(rowIndex);
						 if( obj instanceof TimeBox && ((TimeBox)obj).item != null){
							 JPopupMenu menu = new JPopupMenu();
							 JMenuItem item = new JMenuItem( rb.getString("Delete"));
							 item.addActionListener(new ActionListener(){ 
								@Override
								public void actionPerformed(ActionEvent e) { 
									 group.getItems().remove(((TimeBox)obj).item );
									 setupTimebox();
									 ((TimeBox)obj).item.getTimeline().setPosValue(-1);
									 callback.onTimeBoxDeleted( ((TimeBox)obj).item );
								}}); 
							 menu.add( item );
							 menu.show(TimelinePanel.this, e.getX(), e.getY());
							 return;
						 }
						
					 }
				 }
				 selectedTimeBox = null;
				 for(Object obj : timeBoxList){
					 if(!( obj instanceof TimeBox) )
						 continue;
					 TimeBox t = (TimeBox)obj;
					 if( t.contains(e.getX(), e.getY())){
						 selectedTimeBox = t; 
						 break;
					 }
				 }
				 if( selectedTimeBox == null   ){
					 callback.onTimeBoxSelected(null);
					 JPopupMenu menu = new JPopupMenu();
					 for(final HasIdName t : timelineNames){
						 JMenuItem item = new JMenuItem(  rb.getString("Add") + "： " + t );
						 item.addActionListener(new ActionListener(){ 
							@Override
							public void actionPerformed(ActionEvent e) { 
								double st = fixedTotalTime == 0 ? 5  : fixedTotalTime/2; 
								group.getItems().add(new BgVideoSettingItem(st, t));
								setupTimebox(); 
							}}); 
						 menu.add( item );
					 }
					 menu.show(TimelinePanel.this, e.getX(), e.getY());
				 } else if( selectedTimeBox.item != null)
				     callback.onTimeBoxSelected(selectedTimeBox.item);
				 repaint();
				 
			 }
             public void mousePressed(MouseEvent e) {
            	 start = e.getPoint(); 
             }
 
			    public void mouseReleased(MouseEvent e) {
			    	start = null;
			    }
 
			    public void mouseEntered(MouseEvent e) {}
 
			    public void mouseExited(MouseEvent e) {}

			    
			    public void mouseDragged(MouseEvent e){
			    	if( selectedTimeBox == null)
			    		return;
			    	Point p = e.getPoint(); 
			    	if( start != null && selectedTimeBox != null   ){
			    		double xoffset = p.getX() - start.getX();
			    		if( xoffset == 0 ){
			    			start = p;
			    			return;
			    		}  
	    				selectedTimeBox.size.x += xoffset;
	    				if( selectedTimeBox.size.x <= DELETE_COL_WIDTH ){
			    			BgVideoSettingItem b = group.getPreroll( );
			    			if( b != null && b != selectedTimeBox.item ){
			    				 JPopupMenu menu = new JPopupMenu();
								 JMenuItem item = new JMenuItem( rb.getString("onlyoneheader") + "！"); 
								 menu.add( item );
								 menu.show(TimelinePanel.this, e.getX(), e.getY());
								 
								 selectedTimeBox.size.x = 50;
								 selectedTimeBox.setStartTime( (selectedTimeBox.size.x - DELETE_COL_WIDTH) / getTime2space()   );
								 return;
			    			} else {
			    				selectedTimeBox.size.x = DELETE_COL_WIDTH; 
//				    			selectedTimeBox.item.setType(BgVideoSettingItem.Preroll);
//				    			group.setPreroll(selectedTimeBox.item);
			    			}
			    		} 
	    				if( selectedTimeBox.size.x  >=  getWidth() - PLAY_BTN_WIDTH ){
			    			BgVideoSettingItem b = group.getPostroll( );
			    			if( b != null && b != selectedTimeBox.item ){
			    				 JPopupMenu menu = new JPopupMenu();
								 JMenuItem item = new JMenuItem( rb.getString("onlyonetailer") + "！"); 
								 menu.add( item );
								 menu.show(TimelinePanel.this, e.getX(), e.getY()); 
								 selectedTimeBox.size.x -= 100;
								 selectedTimeBox.setStartTime( (selectedTimeBox.size.x - DELETE_COL_WIDTH) / getTime2space()   );
								 return;
			    			} else { 
			    				selectedTimeBox.size.x = getWidth() - PLAY_BTN_WIDTH; 
//				    			selectedTimeBox.item.setType(BgVideoSettingItem.Postroll);
//				    			group.setPostroll(selectedTimeBox.item);
			    			}
			    		}  
			    		  
			    		selectedTimeBox.setStartTime(  (selectedTimeBox.size.x - DELETE_COL_WIDTH) / getTime2space()  );
			    		 
			    		
			    		callback.onTimeBoxMoving(selectedTimeBox.item);
			    		//setupTimebox(false);
			    		 repaint();
			    	}  
			    	start = p;
			    } 
			    public void mouseMoved(MouseEvent e){}
	    };
	    
	    this.addMouseListener(adapter);
	    this.addMouseMotionListener(adapter);
	}
	
	public void select(BgVideoSettingItem item){
		 this.selectedTimeBox =  fromItem(item, timeBoxList);
		 this.highlightedBoxes.clear();
		 repaint();
	}
	 
	  
	public void setupTimebox(){
		setupTimebox(true);
	}
	public void setupTimebox(boolean clearHighlight){
		timeBoxList.clear();
		if( clearHighlight )
			this.highlightedBoxes.clear();
		int row = 0;
		double time2space = this.getTime2space();
		 
		for(BgVideoSettingItem item : group.getItems()){
			TimeBox tb = new TimeBox(item, row++, time2space); 
			if( this.selectedTimeBox != null && this.selectedTimeBox.item == item )
				this.selectedTimeBox = tb;
			timeBoxList.add(tb);
		} 
		
		double w =  getWidth();//getAdjustWidth(); 
		double h = ROW_HEIGHT * (timeBoxList.size() + 2);
		if( h < 160 ) h = 160;
		this.setPreferredSize(new Dimension((int)w,(int)h));
		this.setSize(new Dimension((int)w,(int)h));
		 
		repaint(); 
		callback.onTimelineChange(row);
	}
	
	 
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor( Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
        
		 if(  this.curAudioPosition > 0){
			 g.setColor(Color.RED);
			 g.drawLine( DELETE_COL_WIDTH + curAudioPosition, 0, DELETE_COL_WIDTH + curAudioPosition, getHeight());
			 if( currentVideoProgress > 0 ){
				 int minutes = (int) currentVideoProgress / 60000;
					int seconds = (int) (currentVideoProgress % 60000) / 1000;
					g.drawString( minutes + ":" + seconds,  DELETE_COL_WIDTH + curAudioPosition, 10);
			 }
		 } 
		 if( selectedTimeBox != null && selectedTimeBox.size != null){
			 g.setColor(Color.BLUE);
			 g.drawLine( selectedTimeBox.size.x , 0,  selectedTimeBox.size.x, getHeight());
	     }
		 
		 
		
         //draw the null / empty square
	    g.setColor(Color.BLACK);
	   
	    for(Object tb : timeBoxList){
	    	if( tb instanceof TimeBox)
	    	   ((TimeBox)tb).paintComponent(g); 
	    } 
	    Rectangle r = g.getClipBounds();  
		int w =  (int)(  r.getWidth() ); 
	    for(Object obj : timeBoxList){
	    	if(!( obj instanceof TimeBox) ) {
	    		continue;
	    	}
	    	TimeBox tb = (TimeBox)obj;
	    	//double startX = tb.size.x + tb.size.width;
	    	double startY = tb.size.y + tb.size.height/2;
	    	if( tb == selectedTimeBox )
	    		g.setColor(Color.BLUE);
	    	else
	        	g.setColor(BOX_COLOR);
	    	g.drawLine(0, (int)startY, w, (int)startY);
	    	
	    	g.setColor(BOX_COLOR);
	    	g.drawImage(deleteIcon, 0, (int)startY-8, null); 
	    	 
	    } 
	    
	}
	 
	 
	private static TimeBox  fromItem(BgVideoSettingItem item, List<Object> timeBoxList){
         for(Object obj : timeBoxList){
        	 if(!( obj instanceof TimeBox) )
        		 continue;
        	 TimeBox tb = (TimeBox)obj;
	    	if( tb.item == item )
	    		return tb;
	    }
         return null;
	}
 
	 
	
	 
	private   class TimeBox {
		
		BgVideoSettingItem item;
		int rowIndex;
		Rectangle size; 
	 
		public TimeBox(BgVideoSettingItem item, int rowIndex, double time2space){
			this.item = item;
			this.rowIndex  = rowIndex;
			 
			calculateSize(time2space);
		}
		 
		public void calculateSize(double time2space){
			double xoffset = DELETE_COL_WIDTH +  time2space *   item.getPosInVideo()   ;
			double yoffset = ROW_HEIGHT * rowIndex;
			int width = 150;
			size = new Rectangle((int)xoffset, (int)yoffset, (int)width, (int)(ROW_HEIGHT * 0.8 ) ); 
		}
		public void setStartTime(double time){
			 if( time < 0 )  time = 0;
			 item.setPosInVideo( time );  System.out.println(time); 
			 if( time == 0 ){
				 item.setType( BgVideoSettingItem.Preroll );
				 group.setPreroll(item);
			 } else if( time >=  fixedTotalTime ){
				 item.setType( BgVideoSettingItem.Postroll );
				 group.setPostroll(item);
			 } else {
				 item.setType( BgVideoSettingItem.Midroll );
			 }
		}
	 
		void paintComponent(Graphics g){ 
			 
			g.setColor(BOX_COLOR);
			g.fillRect(size.x, size.y, size.width, size.height);
		 	 
			long ttt = (long)(item.getPosInVideo() * 1000);
			int minutes = (int) ttt / 60000;
			int seconds = (int) (ttt % 60000) / 1000;
			
			String text =  item.getTimeline() + " : [" + minutes + ":" + seconds + "]";
			g.setColor(Color.GRAY);
			g.drawString(text, size.x+5 , size.y+15);
				   
         	 
			if( highlightedBoxes.contains( this ) ){
			 	g.setColor( HIGHLIGHT_COLOR); 
				g.drawRect(size.x, size.y, size.width, size.height); 
			}
			if( this == selectedTimeBox){
				g.setColor( LINE_COLOR); 
				g.drawRect(size.x, size.y, size.width, size.height);
			}
		}
		 
		
		boolean contains(int x, int y){
			return new Rectangle( (int)(size.getX() ), (int)size.getY(),  (int)size.getWidth()+ BOX_TAIL_WIDTH, (int)size.getHeight()).contains(x, y); 
		}   
	} 
	
}
