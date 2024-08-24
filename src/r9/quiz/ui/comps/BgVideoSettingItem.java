package r9.quiz.ui.comps;


import java.io.Serializable;

import r9.quiz.cards.HasIdName;

public class BgVideoSettingItem implements Serializable{
 
	private static final long serialVersionUID = 1L;
 
	public static final int Preroll = 1;
	public static final int Midroll = 0;
	public static final int Postroll = 2;

	private double posInVideo ;
	private HasIdName timeline;
	
	//1: preroll  0: mid-roll  2: post-roll
	private int type;
	
	public BgVideoSettingItem(  HasIdName t){
		this.posInVideo  = t.getPosValue();
		this.timeline = t;
	}
	/**
	 * we DID NOT USE  posInVidieo  from HasIdName directly,  as we do which to provide flexibility:
	 * a HasIdName can be used one or multiple times for different BgVideoSettingItem。  
	 * or,  it can be used at several time-spots   on the timeline.
	 * @param posInVideo
	 * @param t
	 */
	public BgVideoSettingItem(double posInVideo,  HasIdName t){
		this.posInVideo  = posInVideo;
		this.timeline = t;
	}
	public BgVideoSettingItem(BgVideoSettingItem copy){
		this.posInVideo  =  copy.posInVideo;
		this.timeline = copy.timeline;
		this.type = copy.type;
	}
	
	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}



	public double getPosInVideo() {
		return posInVideo;
	}



	public void setPosInVideo(double posInVideo) {
		this.posInVideo = posInVideo;
	}



	public HasIdName getTimeline() {
		return timeline;
	}



	public void setTimeline(HasIdName timeline) {
		this.timeline = timeline;
	}



	public boolean isSameSource(BgVideoSettingItem setting){
		return ( timeline != null && timeline.equals(setting.timeline))  && this.posInVideo == setting.posInVideo ;
	}
	
	
	public boolean isValid(){
		return timeline != null;
	}

	
	
}
