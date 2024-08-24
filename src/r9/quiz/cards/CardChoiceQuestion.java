package r9.quiz.cards;

import java.io.Serializable;

import r9.quiz.util.Utils;

  
public class CardChoiceQuestion  extends CardPage implements  IQuestion, HtmlContent , Serializable{
	 
		private static final long serialVersionUID = 1L; 
   
    private String[] options;
    private String answers;
   
    public CardChoiceQuestion(){ 
    	this("",null, "");
    }
    
    public CardChoiceQuestion(String questionBody, String[] options, String answers){
    	super(questionBody);
    	this.options = options == null? new String[0] : options; 
    	this.answers = answers;
    	setType( QuestionType.choice);
    }
     

	public void addOption(String option){
		String[] newoptions = new String[options.length +1];
		System.arraycopy(options, 0, newoptions, 0, options.length);
		options = newoptions;
		newoptions[newoptions.length-1] = option;
		
	}
	  
	 
	public String[] getOptions() {
		return options;
	}
	 
	public void setOptions(String[] options) {
		this.options = options;
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
		if( this.options != null && this.options.length > 0){
			for(int i = 0; i < options.length; i++){
				 template = template.replace("${options" + i + "}", Utils.handleNull(options[i]));
			}
		}   
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
}
