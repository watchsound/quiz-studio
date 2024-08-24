package r9.quiz.surveyjs;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import r9.quiz.R9SystemSetting;
import r9.quiz.util.Utils;

public abstract class Question implements Serializable{
 
	private static final long serialVersionUID = 1L;
	public static enum QuestionType { 
		text(0, "填空题", "text"), 
		multipletext(0, "多填空题", "multipletext"), 
		choice(1,"多选择题", "checkbox"),
		radiogroup(1,"单选择题", "radiogroup"),
		bool(1,"对错题", "boolean"),
		rating(1,"评分题", "rating"),
		comment(1,"文字反馈", "comment"),
		dropdown(1,"下拉菜单", "dropdown"),
		imagepicker(1,"图片选择题", "imagepicker"),
		matrix(1,"表格选择题", "matrix"), 
		html_choice(2,"富文本选择题", "html", "htmlchoice"),
		html_page(3,"富文本页面", "html") ;  
	    
	    private QuestionType(int value, String name, String surveyid){     
	       this.value = value;     
	       this.name = name;     
	       this.surveyid = surveyid;
	       this.langid = surveyid;
	    } 
	    private QuestionType(int value, String name, String surveyid, String langid){     
		       this.value = value;     
		       this.name = name;     
		       this.surveyid = surveyid;
		       this.langid = langid;
		    } 
	    public static QuestionType fromName(String name){
	    	if( text.name.equals(name) )
	    		return text;
	    	if( choice.name.equals(name) )
	    		return choice;
	    	if( html_choice.name.equals(name) )
	    		return html_choice;
	    	return html_page;
	    }
	   
	    public String toString(){
	    	return name;
	    }
	    public int getValue() {
			return value;
		}
	    
	    public String getDescription() {
	    	ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
	    	return rb.getString(langid);
	    }
	
		public String getName() {
			return name;
		} 
		public String getSurveyJsID(){
			return surveyid;
		}
	
		private int value;  //自定义数据域，private为了封装。     
	    private String name;     
	    String surveyid;
	    String langid;
	}    

	public static enum ChoicesOrderType { none, random }
	protected QuestionType type = QuestionType.html_page ;
	protected String name="" ;
     private String title;
     
    
     private String wrongFollow="";
     
     private boolean isRequired=true;
     private String visibleIf;
     
     private boolean mergeWithPreviousOne;
     private int duration;
     private boolean isBackAllowed;
    
    public abstract List<String> getValues();
	
	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public abstract String getCorrectAnswer()  ;
	public boolean answerNeedsQuote() {
		return true;
	}

	public abstract void setCorrectAnswer(String correctAnswer)  ;

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getVisibleIf() {
		return visibleIf;
	}

	public void setVisibleIf(String visibleIf) {
		this.visibleIf = visibleIf;
	}
	
	
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isMergeWithPreviousOne() {
		return mergeWithPreviousOne;
	}

	public void setMergeWithPreviousOne(boolean mergeWithPreviousOne) {
		this.mergeWithPreviousOne = mergeWithPreviousOne;
	}
	
	

	public String getWrongFollow() {
		return wrongFollow;
	}

	public void setWrongFollow(String wrongFollow) {
		this.wrongFollow = wrongFollow;
	}

	public boolean isBackAllowed() {
		return isBackAllowed;
	}

	public void setBackAllowed(boolean isBackAllowed) {
		this.isBackAllowed = isBackAllowed;
	}

	public String toStringDetail(){
		return "TYPE:" + this.type.name + "\n NAME:" + this.name;
	}
	
	public String getValidatorName() {
		return "my_"+ name;
	}
	
	public String getValidatorCode2() {
		if( this.wrongFollow == null || this.wrongFollow.length() == 0)
			return "";
		return "  if (options.name == '" + name + "') { " + 
				( this.answerNeedsQuote() ? "   if ( !equalsObject( options.value, '" + this.getCorrectAnswer() + "' ) ) { " : "   if ( !equalsObject(options.value, " + this.getCorrectAnswer() + ") ) { ") + 
				"        options.error = \"" + this.wrongFollow  + "\"; " + 
				"    } " + 
				" }" ;
	}
	
	public String getValidatorCode() {
		if( this.wrongFollow == null || this.wrongFollow.length() == 0)
			return "";
		return "function " + getValidatorName() + "(params) {\n" + 
				"    var value = params[0].trim();\n" + 
				( this.answerNeedsQuote() ?   "    return equalsObject(value, \"" +  this.getCorrectAnswer() + "\");\n" :"    return equalsObject(value, " +  this.getCorrectAnswer() + ");\n"     ) + 
				"}" +
				";Survey.FunctionFactory.Instance.register(\""+ this.getValidatorName() +"\", " +this.getValidatorName()+ ");";
	}
	
	public String getValidatorInvokerCode() {
		if( this.wrongFollow == null || this.wrongFollow.length() == 0)
			return "";
		return "  \"validators\": [ " + 
				" { " + 
				"    \"type\": \"expression\", " + 
				"    \"text\": \"" +  this.wrongFollow  + "\", " + 
				"    \"expression\": \"" + getValidatorName()  + "({" + name +"})\" " + 
				"   } " + 
				"] ,";
	}
	
	public String toJson(){
		StringBuilder sb = new StringBuilder();
		if( this.type != null   )
			sb.append("type: \"" + this.type.surveyid + "\", ");
		if( this.name != null && this.name.length() > 0 )
			sb.append("name: \"" + this.name + "\", ");
		if( this.title != null && this.title.length() > 0 )
	       	sb.append("title: entity2html('" + Utils.normalize_ascii2entity(this.title) + "'), ");
		
		if( this.isRequired )
			sb.append("isRequired: true, ");
		else
			sb.append("isRequired: false, ");
		if( this.visibleIf != null && this.visibleIf.length() > 0 )
			sb.append("visibleIf:  entity2html('" + this.visibleIf + "'), ");
	 
		// sb.append( getValidatorInvokerCode() );
		
		return sb.toString();
	}
	 
	
	
}
