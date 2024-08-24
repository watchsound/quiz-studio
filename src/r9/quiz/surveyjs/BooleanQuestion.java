package r9.quiz.surveyjs;

import java.util.ArrayList;
import java.util.List;

public class BooleanQuestion extends Question{
 
	private static final long serialVersionUID = 1L;

 
	private String label;
     
	private boolean correct;
     
	public BooleanQuestion(){
		setType(QuestionType.bool); 
	}


	public boolean isCorrect() {
		return correct;
	}


	public void setCorrect(boolean correct) {
		this.correct = correct;
	}


	public String getLabel() {
		return label;
	}
	public   String getCorrectAnswer() {
		return correct ? "true" : "false";
	}
	public String getValidatorCode2() {
		if( this.getWrongFollow() == null || this.getWrongFollow().length() == 0)
			return "";
		String condition = "";
		if( this.correct ) {
			condition = "   if (options.value != true ) { ";
		} else {
			condition = "   if ( typeof options.value != 'undefined' && options.value ) { ";
		}
		return "  if (options.name == '" + name + "') { " + 
				  condition + 
				"        options.error = \"" +  this.answerNeedsQuote()  + "\"; " + 
				"    } " + 
				" }" ;
	}
	
	public boolean answerNeedsQuote() {
		return false;
	}
	public   void setCorrectAnswer(String correctAnswer) {
		this.correct = Boolean.parseBoolean(correctAnswer);
	}
	
	
	 public   List<String> getValues(){
		 List<String> values = new ArrayList<String>();
		 values.add("true");
		 values.add("false");
		 return values;
	 }

	public void setLabel(String label) {
		this.label = label;
	}

	public String toJson(){
		StringBuilder sb = new StringBuilder("{");
		sb.append(super.toJson());
		if( this.label != null && this.label.length() > 0 )
			sb.append("label: \"" + this.label + "\"  ");
		sb.append("}" );
		return sb.toString();
	}
	
	public String toStringDetail(){
		return super.toStringDetail() + "\n" 
				+ label;
	}
}
