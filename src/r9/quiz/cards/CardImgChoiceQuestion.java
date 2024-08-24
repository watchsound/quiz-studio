package r9.quiz.cards;

import java.io.Serializable;

import r9.quiz.util.Utils;

  
public class CardImgChoiceQuestion  extends CardPage implements  IQuestion, HtmlContent , Serializable{
	 
		private static final long serialVersionUID = 1L; 
    
    private String answers;
   
    public CardImgChoiceQuestion(){ 
    	this("",  "");
    }
    
    public CardImgChoiceQuestion(String questionBody,  String answers){
    	super(questionBody); 
    	this.answers = answers;
    	setType( QuestionType.image_choice);
    }
     
 

	public String getContent() {
		return this.getQuestionBody();
	}

	public void setContent(String content) {
		this.setQuestionBody(content);
	}
  
    
	public String replaceTemplate(String template){
		if( template == null || template.length() == 0)  return template;
		template = super.replaceTemplate(template); 
	 
	    template = template.replace("${anwser}",  Utils.handleNull(answers ) );
		return template;
	}

	@Override
	public String getAnswers() {
	 	return answers;
	}

	@Override
	public void setAnswers(String answer) {
		 this.answers = answer;
	}

	@Override
	public String[] getOptions() {
	 	return new String[0];
	}

	@Override
	public void setOptions(String[] options) {
		 
	}
}
