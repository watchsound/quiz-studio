package r9.quiz.cards;
 
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.annotations.R9Ref;

import r9.quiz.R9SystemSetting;
import r9.quiz.util.ImageUtil;
import r9.quiz.util.Utils;
 

public abstract class CardPage implements PageNaviagtionI, IQuestion,  HasIdName,   HtmlContent,Serializable{
	  
	private static final long serialVersionUID = -3502710925726833718L;

	private int order;
     
    private long cardId;
    
    @R9Ref
    private CardCssLayout cardLayout;
    
    private String parentUUID; 
    
    private String uuid;
       
    private String name =""; 
    private String questionBody;
    
    private boolean backAllowed;
    private boolean forwardAllowed;
    private boolean timeSharedWithPrevious;
      
    private int duration;
    private boolean mergeToPreviousPage;
    
    private String voiceFileName;
    
    
    private String[] imageList = new String[0];  
    
    private String htmlTemplateId;
    private double timeInVideo;
  
    //transient?
    private String imageForCreatingPreivew;
    
    private transient BufferedImage previewImage; 
    
    private String taglist;
    
    private String correctFollow;
    private String wrongFollow;
    
    private int    level;
    private int    score = 1;
    
    @R9Ref
    private QuestionType type;
    
    public CardPage(){
    	 this("");
    }
    public CardPage(String body) {
    	this.questionBody = body;
    	uuid = ImageUtil.createUID(8);
    	type = QuestionType.html_page; 
    }

	public QuestionType getType() {
		return type;
	}


	public void setType(QuestionType type) {
		this.type = type;
	}


	public String getQuestionBody() {
		return questionBody;
	}
	public void setQuestionBody(String questionBody) {
		this.questionBody = questionBody;
	}
	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public String getHtmlTemplateId() {
		return htmlTemplateId;
	}


	public void setHtmlTemplateId(String htmlTemplateId) {
		this.htmlTemplateId = htmlTemplateId;
	}
	
	  public double getPosValue() {
		  return getTimeInVideo();
	  }
	  public void setPosValue(double v) {
		  setTimeInVideo(v);
	  }

	public double getTimeInVideo() {
		return timeInVideo;
	}


	public void setTimeInVideo(double timeInVideo) {
		DecimalFormat  df  =  new  DecimalFormat("######.00");
		String t = df.format(timeInVideo);
		this.timeInVideo = Double.parseDouble(t);
	}


	public long getCardId() {
		return cardId;
	}


	public void setCardId(long cardId) {
		this.cardId = cardId;
	}


	public CardCssLayout getCardLayout() {
		return cardLayout;
	}


	public void setCardLayout(CardCssLayout cardLayout) {
		this.cardLayout = cardLayout;
	}


	public String getParentUUID() {
		return parentUUID;
	}


	public void setParentUUID(String parentUUID) {
		this.parentUUID = parentUUID;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	} 
 

	public boolean isBackAllowed() {
		return backAllowed;
	}


	public void setBackAllowed(boolean backAllowed) {
		this.backAllowed = backAllowed;
	}

	

	public boolean isForwardAllowed() {
		return forwardAllowed;
	}


	public void setForwardAllowed(boolean forwardAllowed) {
		this.forwardAllowed = forwardAllowed;
	}


	public BufferedImage getPreviewImage() {
		return previewImage;
	}


	public void setPreviewImage(BufferedImage previewImage) {
		this.previewImage = previewImage;
	}


	public boolean isTimeSharedWithPrevious() {
		return timeSharedWithPrevious;
	}


	public void setTimeSharedWithPrevious(boolean timeSharedWithPrevious) {
		this.timeSharedWithPrevious = timeSharedWithPrevious;
	}


	public String getImageForCreatingPreivew() {
		if ( imageForCreatingPreivew == null )
			imageForCreatingPreivew = ImageUtil.createCompactUID();
		return imageForCreatingPreivew;
	}

	public void setImageForCreatingPreivew(String imageForCreatingPreivew) {
		this.imageForCreatingPreivew = imageForCreatingPreivew;
	}


	public String getVoiceFileName() {
		return voiceFileName;
	}


	public void setVoiceFileName(String voiceFileName) {
		this.voiceFileName = voiceFileName;
	}

	 
	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public boolean isMergeToPreviousPage() {
		return mergeToPreviousPage;
	}


	public void setMergeToPreviousPage(boolean mergeToPreviousPage) {
		this.mergeToPreviousPage = mergeToPreviousPage;
	}

	
	
	public String getCorrectFollow() {
		return correctFollow;
	}
	public void setCorrectFollow(String correctFollow) {
		this.correctFollow = correctFollow;
	}
	public String getWrongFollow() {
		return wrongFollow;
	}
	public void setWrongFollow(String wrongFollow) {
		this.wrongFollow = wrongFollow;
	}
	public String getTaglist() {
		return taglist;
	}
	public void setTaglist(String taglist) {
		this.taglist = taglist;
	}
	 
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean hasImages(){
		if( imageList == null ) return false;
		for(int i = 0; i < imageList.length; i++) {
			if( imageList[i] != null && imageList[i].length() > 0)
				return true;
		}
		return false; 
	}
	public String[] getImageList() {
		if( imageList == null) imageList = new String[0];
		return imageList;
	}

	public String getImage(int order){
		if( imageList == null ) return null;
		return order < imageList.length ? imageList[order] : null;
	}
	public void setImage(int order, String image){
		getImageList();
		if( order < imageList.length) {
			imageList[order] = image;
			return;
		}
		String[]  newimages = new String[ order + 1];
		if( imageList.length > 0)
		    System.arraycopy(imageList, 0, newimages, 0, imageList.length);
		for(int i = imageList.length; i < order; i++)
			newimages[i] = null;
		newimages[order] = image;
		this.imageList = newimages;
	}
	public void deleteImage(int order ){
		getImageList();
		if( order < imageList.length)
	    	this.imageList[order] = null;
	}
  
		public String getErrorMessage(Exam exam){
			return "";
		}
		
		
		public String replaceTemplate(String template){
			getImageList();
			 ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
			if( template == null || template.length() == 0)  return template;
			//if( name != null && name.length() >0)
			 template = template.replace("${Submit}", rb.getString("Submit"));
			 template = template.replace("${NextPage}", rb.getString("NextPage"));
			 
			    template = template.replace("${name}", Utils.handleNull(name));
			    template = template.replace("${questionBody}", Utils.handleNull(questionBody));
			    template = template.replace("${msgError}", Utils.handleNull(this.getWrongFollow()));
			    template = template.replace("${msgInfo}", Utils.handleNull(this.getCorrectFollow()));
			    
	 		    for(int i = 0; i < imageList.length; i++) {
			    	if( imageList[i] != null && imageList[i].length() > 0) {
			    		 template = template.replace("${image" + i + "}", Utils.handleNull(imageList[i]));
			    	}
			    } 
			return template;
		}
		
		@Override
		public String getContent() {
			return  getQuestionBody();
		}
	 
		@Override
		public void setContent(String content) {
			 setQuestionBody(content);
		}
	   	
	public String toString() {
		String desc =  this.getType().name + ":" + this.order + ":";
	 	if( name != null) {
			if( name.length() > 15 )
				return desc +  name.substring(0,15);
			return desc + name;
		}
	 	return desc;
	}
}
