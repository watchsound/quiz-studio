package r9.quiz.cards;

import java.io.Serializable;

import r9.quiz.util.Utils;

  
public class CardYesNoQuestion  extends CardPage implements  IQuestion , Serializable{
	 
		private static final long serialVersionUID = 1L; 
   
   
    private boolean correct;
     
    public CardYesNoQuestion(){ 
    	this("", true);
    }
    
    public CardYesNoQuestion(String questionBody,   boolean answers){
    	super(questionBody);
        this.correct = answers;
    	setType( QuestionType.yesno);
    }
     
    
	  
	 
	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
		 
	}

	public String[] getOptions() {
		return new String[0];
	}
	 
	public void setOptions(String[] options) { 
	}
	 
	 
	public String replaceTemplate(String template){
		if( template == null || template.length() == 0)  return template;
		template = super.replaceTemplate(template);
	  	template = template.replace("${answer}", Utils.handleNull(getAnswers()));
		   
		return template;
	}

	@Override
	public String getAnswers() {
		 return this.correct ? "yes" : "no";
	}

	@Override
	public void setAnswers(String answer) {
		 this.correct = Boolean.parseBoolean(answer);
	}
}
