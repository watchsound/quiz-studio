package r9.quiz.cards;

import java.io.Serializable;

import com.google.gson.annotations.R9Ref;

import r9.quiz.surveyjs.Quiz;
import r9.quiz.util.Utils;

public class CardHtmlPage extends CardPage implements HtmlContent , Serializable{
	 
		private static final long serialVersionUID = 1L; 

 
	
	public CardHtmlPage(){
		super();
		setType( QuestionType.html_page );
	}
	
 
 
	 

	public String replaceTemplate(String template){
		if( template == null || template.length() == 0)  return template;
		template = super.replaceTemplate(template);
 
		return template;
	}




	@Override
	public String[] getOptions() {
	 	return new String[0];
	}




	@Override
	public void setOptions(String[] options) { 
	}




	@Override
	public String getAnswers() {
	 	return "";
	}




	@Override
	public void setAnswers(String answer) {
		 
	}
	
}
