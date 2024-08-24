package r9.quiz.cards;

import java.io.Serializable;

import com.google.gson.annotations.R9Ref;

import r9.quiz.surveyjs.Quiz;
import r9.quiz.util.Utils;

public class CardSurveyJsJumpPage extends CardPage implements HtmlContent , Serializable{
	 
		private static final long serialVersionUID = 1L; 

    @R9Ref
	private Quiz surveyQuiz;
	
	public CardSurveyJsJumpPage(){
		super();
		setType( QuestionType.html_page );
	}
	
 
 
	
	public Quiz getSurveyQuiz() {
		return surveyQuiz;
	}

	public void setSurveyQuiz(Quiz surveyQuiz) {
		this.surveyQuiz = surveyQuiz;
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
