package r9.quiz.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

import com.google.gson.annotations.R9RefCollectionValue;

import r9.quiz.R9SystemSetting;
import r9.quiz.util.ImageUtil;
 
public class Exam implements Serializable{
 
	private static final long serialVersionUID = 4422753933022953008L;
	private List<String> tags;
	private String tagList;
	
	@R9RefCollectionValue
	private List<CardPage> pages;
	private String title;
	private String description;
 	private String name;
	//private long id;
    private long examId;
    private String uuid;
    
 
	private String coverImage;
	private String backgrdImage;
 
	private String level;
	private String subject;
	
	private int duration; //second;
	 
    private String videoName = "";
    private String videoLink = "";
    
    private boolean useMathJax = true;
    
    private String libDirectory = "";
	
	private transient WeakHashMap<String, CardPage> nodeToCardMap = new WeakHashMap<String, CardPage>();

	public Exam() {
		uuid = ImageUtil.createUUID();
		pages = new ArrayList<CardPage>();
		examId = ImageUtil.createUniqueIntID();
	}
     
	
	
	public String getVideoLink() {
		return videoLink;
	}



	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getVideoSrc() {
		if( this.videoLink != null && this.videoLink.length() > 0)
			return this.videoLink;
		return videoName;
	}


	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public boolean isSurveyJsOnly() {
		if( pages.size() != 1)	
			return false;
		CardPage p = pages.get(0);
		if( p instanceof CardSurveyJsJumpPage && ((CardSurveyJsJumpPage)p).getSurveyQuiz()!=null){
			return true;
		}
		return false;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<String> getAllCssFiles(){
		HtmlTemplateList meta = R9SystemSetting.sharedInstance.getHtmlTemplateMetaData();
		 Set<String> result = new HashSet<String>();
		 for(CardPage p : pages){
			 HtmlTemplate hid = meta.getTemplate(p.getHtmlTemplateId());
			 if( hid == null ) continue;
			 for(String css : hid.getCssFiles())
				 result.add(css);
		 }
		 return result;
	}
	public Set<String> getAllJsFiles(){
		HtmlTemplateList meta = R9SystemSetting.sharedInstance.getHtmlTemplateMetaData();
		 Set<String> result = new HashSet<String>();
		 for(CardPage p : pages){
			 HtmlTemplate hid = meta.getTemplate(p.getHtmlTemplateId());
			 if( hid == null ) continue;
			 for(String js : hid.getJsFiles())
				 result.add(js);
		 }
		 return result;
	}

	
	public void normalize(){
		CardPage prevP = null;
		for(CardPage p : pages){
			if( prevP == null){
				prevP = p;
				continue;
			}
			if( p.getTimeInVideo() < 0 )
				continue;
			else if( p.getTimeInVideo() < 0.001 )
				p.setTimeInVideo(prevP.getTimeInVideo());
			prevP = p; 
		}
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public String getBackgrdImage() {
		return backgrdImage;
	}

	public void setBackgrdImage(String backgrdImage) {
		this.backgrdImage = backgrdImage;
	}
	
	public void addPage(CardPage page){
		this.pages.add(page);
	}
	public void deletePage(CardPage page){
		this.pages.remove(page);
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

 
	public String getTagList() {
		return tagList;
	}

	public void setTagList(String tagList) {
		this.tagList = tagList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
	
	public boolean isUseMathJax() {
		return useMathJax;
	}

	public void setUseMathJax(boolean useMathJax) {
		this.useMathJax = useMathJax;
	}

	public List<CardPage> getCards() {
		return pages;
	}

	public void setCards(List<CardPage> pages) {
		this.pages = pages;
	}

//	public long getId() {
//		return id;
//	}
//
//	public void setId(long id) {
//		this.id = id;
//	}

	public String getLibDirectory() {
		return libDirectory == null ? "" : libDirectory;
	}



	public void setLibDirectory(String libDirectory) {
		this.libDirectory = libDirectory;
	}



	public long getExamId() {
		return examId;
	}

	 public boolean hasSurveyJs(){
		 for(CardPage p : pages){
			 if( p instanceof CardSurveyJsJumpPage){
				 CardSurveyJsJumpPage hp = (CardSurveyJsJumpPage)p;
				 if( hp.getSurveyQuiz() != null)
					 return true;
			 }
		 }
		 return false;
	 }
 

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean hasSurveyJsCode() {
		for(CardPage p : pages) {
			if( p instanceof CardSurveyJsJumpPage && ((CardSurveyJsJumpPage)p).getSurveyQuiz()!=null){
				return true;
			}
		}
		return false;
	}
	public List<CardPage> getPages() {
		return pages;
	}

	public void setPages(List<CardPage> pages) {
		this.pages = pages;
	}

	public void setExamId(long examId) {
		this.examId = examId;
	}

	public WeakHashMap<String, CardPage> getNodeToCardMap() {
		return nodeToCardMap;
	}

	public void setNodeToCardMap(WeakHashMap<String, CardPage> nodeToCardMap) {
		this.nodeToCardMap = nodeToCardMap;
	}
	
	

	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void addTag(String tag) {
		if (tags == null)
			tags = new ArrayList<String>();
		if(! tags.contains(tag))
		   tags.add(tag);
	}

	public List<CardPage> getCardList() {
		if (pages == null) {
			pages = new ArrayList<CardPage>();
		}
		return pages;
	}

	 
	public CardPage getCardAt(int index) {
		if (pages == null || pages.size() <= index)
			return null;
		return pages.get(index);
	}

	public CardPage getCardByUUID(String uuid ) {
		for (CardPage card : pages) {
			if (card.getUuid().equals(uuid) ) {
				return card;
			}
		}
		return null;
	}
	 
	public List<PageGroup> getPageGroup(){ 
		List<PageGroup> result = new ArrayList<PageGroup>();
		if( pages.isEmpty()) return result;
		
		List<CardPage> pagesCopy = new ArrayList<CardPage>( );
		for(CardPage p : pages) {
			if( p.getPosValue() >=0 )
				pagesCopy.add(p);
		}
		Collections.sort(pagesCopy, new Comparator<CardPage>(){ 
			@Override
			public int compare(CardPage o1, CardPage o2) {
				 if( o1.getTimeInVideo() == o2.getTimeInVideo()) return 0;
				 return o1.getTimeInVideo() < o2.getTimeInVideo() ? -1 : 1;
			}});
		
		CardPage p1 = pagesCopy.get(0);
		PageGroup group = new PageGroup(p1);
		result.add(group);
		 
		for(int i =1; i < pagesCopy.size(); i++){
			 p1 = pagesCopy.get(i);
			if( group.shouldAdd(p1)){
				group.addPage(p1);
			} else {
				group = new PageGroup(p1);
				result.add(group);  
			}
		}
		
	    return result;
	}
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void updateNodeIds(List<String> oldIds, List<String> newIds){
		for( int i = 0; i < oldIds.size(); i ++){
			String oldId = oldIds.get(i);
			String newId = newIds.get(i);
			if ( nodeToCardMap.containsKey(oldId) ){
				CardPage card = nodeToCardMap.remove(oldId);
				nodeToCardMap.put(newId, card);
			}
		}
	}
	public CardPage getPrevious(CardPage page){
		int index = pages.indexOf(page);
		if( index == 0 )
			return null;
		return pages.get(index-1);
	}
	public CardPage getNext(CardPage page){
		int index = pages.indexOf(page);
		if( index == pages.size() -1 )
			return null;
		return pages.get(index+1);
	}
	private CardPage getFirstMergedCandidate(CardPage page){
		if (! page.isMergeToPreviousPage() ){
			return page;
		}
		CardPage previous = getPrevious(page);
		if( previous == null)
			return page;
		return getFirstMergedCandidate(previous);
	}
	
	public int getClusterSize(CardPage page){
		CardPage previous = getFirstMergedCandidate(page);
		int count = 1;
		CardPage next = null;
		while( (next = getNext(previous)) != null ){
			if ( next.isMergeToPreviousPage() )
				count++;
			else
				break;
			previous = next;
		}
		return count; 
	}
}
