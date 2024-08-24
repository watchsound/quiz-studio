package r9.quiz.cards;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import r9.quiz.util.Utils;

public class CardFillInBlankQuestion  extends CardPage implements  IQuestion , Serializable{
	 
		private static final long serialVersionUID = 1L; 
  
   
    private String answers;
     
    
    private transient BufferedImage previewImage; 
    
    public CardFillInBlankQuestion(){ 
     	this.answers ="";
     	setType( QuestionType.fill_in);
    }
    
    public CardFillInBlankQuestion(String questionBody,  String  answers){
    	super( questionBody );
     	this.answers = answers;
     	setType( QuestionType.fill_in);
    }
    
	public BufferedImage getPreviewImage() {
		return previewImage;
	}

	public void setPreviewImage(BufferedImage previewImage) {
		this.previewImage = previewImage;
	}

	  
	   
	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}
	
	@Override
	public String[] getOptions() {
	 	return new String[0];
	}

	@Override
	public void setOptions(String[] options) {
		 
	}

//	public boolean isCorrect(String  answers){
//		if( this.answers.length != answers.length )
//			return false;
//		for(String a : this.answers){
//			boolean found = false;
//			for(String aa : answers){
//				if ( a.equals(aa) ){
//					found = true;
//					break;
//				}
//			}
//			if( !found )
//			   return false;
//		}
//		return true;
//	}
    
	public String getErrorMessage(Exam exam){
		int size = exam.getClusterSize(this);
		if (size > 1){
			return "只有简单选择题\n可以合并到一页";
		}
		return "";
	}
	
	public String replaceTemplate(String template){
		if( template == null || template.length() == 0)  return template;
		template = super.replaceTemplate(template);
		//if( questionBody != null && questionBody.length() >0)
 
	    template = template.replace("${anwser}",  Utils.handleNull(answers ) );
	  
		return template;
	}


}
