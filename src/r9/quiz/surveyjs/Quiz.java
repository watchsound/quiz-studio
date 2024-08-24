package r9.quiz.surveyjs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.R9RefCollectionValue;

import r9.quiz.util.ImageUtil;

/**
 * https://surveyjs.io/Examples/Library/?id=survey-quiz&platform=jQuery&theme=default
 * 
 * survey.startTimer();
 * survey.stopTimer();
 * 
 * Use question.correctAnswer to the set correct answer to questions.
 *  After that you may use {correctedAnswers} and {inCorrectedAnswers} variables, 
 * for example in completedHtml.
 * @author hanningni
 *
 */
public class Quiz implements Serializable{
	 
    private static final long serialVersionUID = 1L;
		
    public static enum ClearInvisibleValuesType {none, onComplete, onHidden}
    public static enum ShowProgressBarType {bottom, top}
    public static enum ShowTimerPanelType {bottom, top}
    public static enum ShowQuestionNumbersType {on, off}
    public static enum ShowTimerPanelModeType  { page, survey, all}
	private String title="";
	
	
    private ShowProgressBarType showProgressBar = ShowProgressBarType.bottom;
    
    //Show timer panel on top or bottom. If showTimerPanel value is different from "none",
    //the startTimer() function is called on render.
    private ShowTimerPanelType showTimerPanel =  ShowTimerPanelType.top;
    //and/or pages in seconds
    private int maxTimeToFinishPage = 25;
    // also can be overwritten at page level : survey.pages[0].maxTimeToFinish = 20;
    
    //You may set the maximum time to complete the survey (in seconds)
    private int maxTimeToFinish = 25;
     
    private String startSurveyText = "开始测试";
    private String completedHtml = "";
    //You may use the first page as the start page.
    private boolean firstPageIsStarted;
   //You may hide the previous button from the navigation.
    private boolean showPrevButton = true;
    
    //Use showTimerPanelMode property or/and onTimerPanelInfoText event to control the timer panel text.
    private ShowTimerPanelModeType showTimerPanelMode =  ShowTimerPanelModeType.page;
    
    private ShowQuestionNumbersType showQuestionNumbers = ShowQuestionNumbersType.off;
    
    private ClearInvisibleValuesType clearInvisibleValues = ClearInvisibleValuesType.none; //onComplete|onHidden
    
    @R9RefCollectionValue
    private List<Question> questions = new ArrayList<Question>();
    @R9RefCollectionValue
    private List<Page> pages = new ArrayList<Page>();
    private String uuid = ImageUtil.createCompactUID();
    
    public boolean isEmpty() {
    	return pages.isEmpty();
    }
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	 
	public ShowProgressBarType getShowProgressBar() {
		return showProgressBar;
	}
	public void setShowProgressBar(ShowProgressBarType showProgressBar) {
		this.showProgressBar = showProgressBar;
	}
	public ShowTimerPanelType getShowTimerPanel() {
		return showTimerPanel;
	}
	public void setShowTimerPanel(ShowTimerPanelType showTimerPanel) {
		this.showTimerPanel = showTimerPanel;
	}
	public ShowQuestionNumbersType getShowQuestionNumbers() {
		return showQuestionNumbers;
	}
	public void setShowQuestionNumbers(ShowQuestionNumbersType showQuestionNumbers) {
		this.showQuestionNumbers = showQuestionNumbers;
	}
	public ClearInvisibleValuesType getClearInvisibleValues() {
		return clearInvisibleValues;
	}
	public void setClearInvisibleValues(
			ClearInvisibleValuesType clearInvisibleValues) {
		this.clearInvisibleValues = clearInvisibleValues;
	}
	public int getMaxTimeToFinishPage() {
		return maxTimeToFinishPage;
	}
	public void setMaxTimeToFinishPage(int maxTimeToFinishPage) {
		this.maxTimeToFinishPage = maxTimeToFinishPage;
	}
	public int getMaxTimeToFinish() {
		return maxTimeToFinish;
	}
	public void setMaxTimeToFinish(int maxTimeToFinish) {
		this.maxTimeToFinish = maxTimeToFinish;
	}
	public boolean isFirstPageIsStarted() {
		return firstPageIsStarted;
	}
	public void setFirstPageIsStarted(boolean firstPageIsStarted) {
		this.firstPageIsStarted = firstPageIsStarted;
	}
	public String getStartSurveyText() {
		return startSurveyText;
	}
	public void setStartSurveyText(String startSurveyText) {
		this.startSurveyText = startSurveyText;
	}
	public String getCompletedHtml() {
		return completedHtml;
	}
	public void setCompletedHtml(String completedHtml) {
		this.completedHtml = completedHtml;
	}
	
	
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	public void rebuildPages(){
		pages.clear();
		Page p = new Page();
		pages.add(p);
		for(int i = 0; i < questions.size(); i++){
			Question qs = questions.get(i);
			if(qs.isMergeWithPreviousOne() || i == 0){
				p.getQuestions().add(qs);
				p.setMaxTimeToFinish(p.getMaxTimeToFinish() + qs.getDuration());
				continue;
			}
			p = new Page();
			pages.add(p);
			p.getQuestions().add(qs);
			p.setMaxTimeToFinish(p.getMaxTimeToFinish() + qs.getDuration());
		}
	}
	
	public List<Page> getPages() {
		return pages;
	}
	public void setPages(List<Page> pages) {
		this.pages = pages;
	}
	public boolean isShowPrevButton() {
		return showPrevButton;
	}
	public void setShowPrevButton(boolean showPrevButton) {
		this.showPrevButton = showPrevButton;
	}
	public ShowTimerPanelModeType getShowTimerPanelMode() {
		return showTimerPanelMode;
	}
	public void setShowTimerPanelMode(ShowTimerPanelModeType showTimerPanelMode) {
		this.showTimerPanelMode = showTimerPanelMode;
	}
    public int getPagePos(Question question) {
    	for(int i =0; i < pages.size() ; i++){ 
    		Page page = pages.get(i);
    		if( page.getQuestions().contains(question))
    			return i;
    	}
    	return -1;
    }
    public Question getByName(String name) {
    	for(int i =0; i < pages.size()  ; i++){ 
    		Page page = pages.get(i);
    		for(Question q : page.getQuestions()) {
    			if( name.equals(q.getName()) )
    		        return q;
    		}
    	}
    	return null;
    }
    public List<String> getPageNames(Question question){
    	int pos = getPagePos(question);
    	if( pos < 0 ) return new ArrayList<String>();
    	return getPageNames(pos, question.getName());
    }
	public List<String> getPageNames(int curPagePos, String name){
		List<String> names = new ArrayList<String>();
		for(int i =0; i < pages.size() && i < curPagePos; i++){ 
    		Page page = pages.get(i);
    		for(Question q : page.getQuestions())
    			if( names.indexOf(q.getName())< 0)
    		        names.add(q.getName());
    	}
		Page curPage = pages.get(curPagePos);
		for(Question q : curPage.getQuestions()) {
			if( q.getName().equals(name))
				break;
			if( names.indexOf(q.getName())< 0)
		        names.add(q.getName());
		}
		return names;
	}
	public boolean isUniquePageNames( ){
		List<String> names = new ArrayList<String>();
		for(int i =0; i < pages.size(); i++){ 
    		Page page = pages.get(i);
    		for(Question q : page.getQuestions()) {
    			if( names.contains( q.getName() ))
    				return false;
    		     names.add(q.getName());
    		}
    	} 
		return true;
	}
	public boolean isUniquePageNames( Question question , String newName){ 
		for(int i =0; i < pages.size(); i++){ 
    		Page page = pages.get(i);
    		for(Question q : page.getQuestions()) {
    			if( q == question ) continue;
    			if( newName.equals( q.getName() ))
    				return false; 
    		}
    	} 
		return true;
	}
    public String toJson(){ 
    	StringBuilder sb = new StringBuilder();
    	sb.append("{");
    	sb.append("\ntitle: \"" + this.getTitle() + "\", ");
    	sb.append("\nshowProgressBar: \"" + this.getShowProgressBar() + "\", ");
    	sb.append("\nshowTimerPanel: \"" + this.getShowTimerPanel() + "\", ");
    	sb.append("\nshowTimerPanelMode: \"" + this.getShowTimerPanelMode() + "\", ");
    	if( getMaxTimeToFinish() > 0)
    	    sb.append("\nmaxTimeToFinish:  " + this.getMaxTimeToFinish() + " , ");
    	if( getMaxTimeToFinishPage() > 0)
    	    sb.append("\nmaxTimeToFinishPage:  " + this.getMaxTimeToFinishPage() + " , ");
    	sb.append("\nfirstPageIsStarted: " + this.firstPageIsStarted + ", ");
    	sb.append("\nshowPrevButton: " + this.showPrevButton + ", ");
    	sb.append("\nstartSurveyText: \"" + this.getStartSurveyText() + "\", ");
    	sb.append("pages: [");
    	for(int i =0; i < pages.size(); i++){
    		if( i != 0 ) sb.append(", ");
    		Page page = pages.get(i);
    		sb.append( page.toJson() );
    	}
    	
    	sb.append("], ");
    	sb.append("\ncompletedHtml: \"" + this.getCompletedHtml() + "\"");
    	sb.append("}");
    	System.out.println( sb.toString() );
    	return sb.toString();
    }
    
    public String getValidatorCode2() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("function r9validate_" + uuid + "(s, options) {");
    	for(int i =0; i < pages.size(); i++){ 
    		Page page = pages.get(i);
    		for(Question q : page.getQuestions() ) {
    			sb.append("\n" + q.getValidatorCode2()  );
    		} 
    	} 
    	sb.append(" }");
    	return sb.toString();
    }
    
    public String getValidatorCode() {
    	StringBuilder sb = new StringBuilder();
    	for(int i =0; i < pages.size(); i++){ 
    		Page page = pages.get(i);
    		for(Question q : page.getQuestions() ) {
    			sb.append("\n" + q.getValidatorCode()  );
    		} 
    	} 
    	return sb.toString();
    }
    
    public String generateHtmlJSCode(String containerID){
    	rebuildPages();
    	StringBuilder sb = new StringBuilder();
    	sb.append("var _sameobj = function(one,two) { var oob = typeof one == 'object', tob = typeof two == 'object'; "
    			+ " if( oob && tob ){  for(var k in one) { if(one[k] != two[k]) return false; } return true; } "
    			+ " return one == two;  };");
    	sb.append("var r9_survey_json = " + toJson()  + ";");
    	sb.append("var r9_survey = new Survey.Model(r9_survey_json);  r9_survey.locale = 'r9';");
    	sb.append("$(\"#" + containerID + "\").Survey({ ");
    	sb.append("    model: r9_survey,    ");
    	
    	sb.append("   onCurrentPageChanged  : mathjax_create, ");
    	sb.append("  onValidateQuestion: r9validate_" + uuid + ",");
    	sb.append("    onComplete:function(){  nextPage();  }   ");
    	sb.append("});   ");
    	
//    	sb.append("survey.onComplete.add(function (sender, options) { ");
//    	sb.append("  nextPage();"); 
//    	sb.append(" });"); 
    	return sb.toString();
    }
}
