package r9.quiz.ui.comps;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import r9.quiz.Constants;
 

public class BgVideoSetting implements Serializable{
 
	private static final long serialVersionUID = 1L;

	public static final double POS_GAP_IN_SEC = 1.5;  //seconds
	private String videoURLLink;
	
	private String videoName;
	private long duration;
 	
	private String videoURLLink2;
	
	private List<BgVideoSettingItem> items = new ArrayList<BgVideoSettingItem>();
	
	
	public BgVideoSetting(){}
	public BgVideoSetting(BgVideoSetting copy){
		videoURLLink = copy.videoURLLink;
		videoURLLink2 = copy.videoURLLink2;
		videoName = copy.videoName;
		duration = copy.duration;
		 for( BgVideoSettingItem item : copy.items){
			 items.add(new BgVideoSettingItem(item));
		 }
	}
	
	public List<BgVideoSettingItem> getItems() {
		return items;
	}

	public void setItems(List<BgVideoSettingItem> items) {
		this.items = items;
	}
	
	public BgVideoSettingItem getItem(double posInVideo){
		for(BgVideoSettingItem i : items){
			if( Math.abs( i.getPosInVideo() - posInVideo) <= POS_GAP_IN_SEC )
				return i;
		}
		return null;
	}
	

	public boolean isSameSource(BgVideoSetting setting){
		return ( videoName != null && videoName.equals(setting.videoName)) ||
				( videoURLLink != null && videoURLLink.equals(setting.videoURLLink)) ;
	}
	
	public String getVideoURLLink() {
		return videoURLLink;
	}
	public void setVideoURLLink(String videoURLLink) {
		this.videoURLLink = videoURLLink;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		File f  = new File(videoName); 
		this.videoName = f.getName();
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public boolean isValid(){
		return videoName != null && videoName.length() >0;
	}
 
	public String getVideoURLLink2() {
		return videoURLLink2;
	}
	public void setVideoURLLink2(String videoURLLink2) {
		this.videoURLLink2 = videoURLLink2;
	}

	public BgVideoSettingItem getPreroll() {
		for( BgVideoSettingItem i : items){
			if( i.getType() == BgVideoSettingItem.Preroll){
				return i;
			}
		}
		return null;
	}
	public BgVideoSettingItem getPostroll() {
		for( BgVideoSettingItem i : items){
			if( i.getType() == BgVideoSettingItem.Postroll){
				return i;
			}
		}
		return null;
	}
	public BgVideoSettingItem getOne(String timeline) {
		for( BgVideoSettingItem i : items){
			if( i.getTimeline().equals(timeline)){
				return i;
			}
		}
		return null;
	}

	public void setPreroll(BgVideoSettingItem item) {
		for( BgVideoSettingItem i : items){
			if( i != item && i.getType() == BgVideoSettingItem.Preroll){
				  i.setType(BgVideoSettingItem.Midroll);
			}
		} 
	}
	public void setPostroll(BgVideoSettingItem item) {
		for( BgVideoSettingItem i : items){
			if( i != item && i.getType() == BgVideoSettingItem.Postroll){
				  i.setType(BgVideoSettingItem.Midroll);
			}
		} 
	}

 
	public String getConfigurationForVideojs( ){
		StringBuilder sb = new StringBuilder("{");
		sb.append("'studio': r9, ");
		sb.append("'width': "+Constants.CARD_WIDTH+" , ");
		sb.append("'height':  "+Constants.CARD_HEIGHT+" , ");
		int count = 0;
		BgVideoSettingItem  pitem =  getPreroll();
		if( pitem == null ){
			sb.append("'playPreroll': '', ");
		} else {
			sb.append("'playPreroll': '"+ pitem.getTimeline() +"', ");
			count++;
		}
		pitem =  getPostroll();
		if( pitem == null ){
			sb.append("'playPostroll': '', ");
		} else {
			sb.append("'playPostroll': '"+ pitem.getTimeline() +"', ");
			count++;
		}
		count = items.size() -count;
		if( count == 0 ){
			sb.append("'r9TimeTable': []");
		} else {
			sb.append("'r9TimeTable': [  ");
			StringBuilder sbb = new StringBuilder();
			for( BgVideoSettingItem i : items){
				if( i.getType() == BgVideoSettingItem.Midroll){
					sbb.append(", {'timeline': '"+ i.getTimeline() + "', 'posInVideo' : "+ i.getPosInVideo() +"}");
				}
			}
			sb.append(sbb.substring(1) + " ] ");
		}  
		sb.append("}");
		return sb.toString();
	}
	
}
