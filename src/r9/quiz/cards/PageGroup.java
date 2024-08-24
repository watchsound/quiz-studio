package r9.quiz.cards;

import java.io.Serializable;
import java.util.*;

public class PageGroup  implements Serializable{
     
	private static final long serialVersionUID = 1L;
	public static final int TIME_GAP = 3; //3 second
	private List<CardPage> pages = new ArrayList<CardPage>();
    private double timeInVideo;
    private double timeInVideoEnd;
    
    public PageGroup(CardPage page){
    	pages.add(page);
    	timeInVideo = page.getTimeInVideo();
    	timeInVideoEnd = page.getTimeInVideo();
    }
    
    public String getIdsInJs(){
    	if( pages.isEmpty() ) return "[]";
    	StringBuilder sb  = new StringBuilder();
    	 
    	for(CardPage p : pages){
    		sb.append(",'" + p.getUuid() + "' ");
    	}
    	return "[" + sb.substring(1) + "]";
    }
    
    
    public String getId(){
    	if(pages.isEmpty()) return "";
    	return pages.get(0).getUuid();
    }
    
    public void addPage(CardPage page){
    	if( timeInVideo > page.getTimeInVideo() ){
    		timeInVideo = page.getTimeInVideo();
    	}
    	if( timeInVideoEnd < page.getTimeInVideo() ){
    		timeInVideoEnd = page.getTimeInVideo();
    	}
    	if( !pages.contains(page))
    	   pages.add(page);
    	
    	Collections.sort(pages, new Comparator<CardPage>(){ 
			@Override
			public int compare(CardPage o1, CardPage o2) {
				 if( o1.getTimeInVideo() == o2.getTimeInVideo()) return 0;
				 return o1.getTimeInVideo() < o2.getTimeInVideo() ? -1 : 1;
			}});
    }
    
    public boolean shouldAdd(CardPage page){
    	if( Math.abs( page.getTimeInVideo() - timeInVideo ) < TIME_GAP )
    		return true;
    	if( Math.abs( page.getTimeInVideo() - timeInVideoEnd ) < TIME_GAP )
    		return true;
    	return false;
    }
    
    
    public void clear(){
    	pages.clear();
    	timeInVideo = 0;
    	timeInVideoEnd = 0;
    }
    
    
    
    public  List<CardPage> getPages() {
		return pages ;
	}
 
	public void setPages(List<CardPage> pages) {
		this.pages = pages;
	}



	public double getTimeInVideoEnd() {
		return timeInVideoEnd;
	}





	public void setTimeInVideoEnd(double timeInVideoEnd) {
		this.timeInVideoEnd = timeInVideoEnd;
	}





	public double getTimeInVideo() {
		return timeInVideo;
	}



	public void setTimeInVideo(double timeInVideo) {
		this.timeInVideo = timeInVideo;
	}

 
}
