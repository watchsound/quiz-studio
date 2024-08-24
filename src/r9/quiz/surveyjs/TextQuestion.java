package r9.quiz.surveyjs;

import java.util.ArrayList;
import java.util.List;

public class TextQuestion extends Question{
 
	private static final long serialVersionUID = 1L;

	public static enum  InputType {
		color("color"),
		date("date"),
		datetime("datetime"),
		datetime_local("datatime-local"),
		email("email"),
		month("month"),
		number("number"),
		password("password"),
		range("range"),
		tel("tel"),
		text("text"),
		time("time"),
		url("url"),
		week("week");
		String name;
		private InputType(String name){
			this.name = name;
		}
		public String getName(){
			return name;
		}
		public String toString(){
			return getName();
		}
	}
	 
	private InputType inputType = InputType.text;
	private String correct;
    private String placeHolder;
	public TextQuestion(){
		setType( QuestionType.text); 
	}
	public InputType getInputType() {
		return inputType;
	}
	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}
	 
	public String getCorrectAnswer() {
		return correct;
	}
	public void setCorrectAnswer(String correct) {
		this.correct = correct;
	}
	public String getPlaceHolder() {
		return placeHolder;
	}
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	 public   List<String> getValues(){
		 List<String> values = new ArrayList<String>();
		 
		 return values;
	 }
 
	public String toJson(){
		StringBuilder sb = new StringBuilder("{");
		sb.append(super.toJson());
		if( this.placeHolder != null && this.placeHolder.length() > 0 )
			sb.append("placeHolder: \"" + this.placeHolder + "\", ");
		if( this.inputType != null   )
			sb.append("inputType: \"" + this.inputType.getName() + "\", ");
		
		sb.append("}" );
		return sb.toString();
	}
	
	public String toStringDetail(){
		return super.toStringDetail() + "\n" 
				+ "placeHolder:" + placeHolder + "\n"
				+ "inputType" + inputType.getName();
	}
	
}
