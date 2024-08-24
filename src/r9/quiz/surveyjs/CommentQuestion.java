package r9.quiz.surveyjs;

import java.util.ArrayList;
import java.util.List;

public class CommentQuestion extends Question{
 
	private static final long serialVersionUID = 1L;
 
	private String rows;
	
	public CommentQuestion(){
		setType(QuestionType.comment); 
	}
	
	 public   List<String> getValues(){
		 List<String> values = new ArrayList<String>();
		  return values;
	 }

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}
	public   String getCorrectAnswer() {
		return "";
	}

	public   void setCorrectAnswer(String correctAnswer) { 
	}
  
	public String toJson(){
		StringBuilder sb = new StringBuilder("{");
		sb.append(super.toJson());
		
		sb.append("}" );
		return sb.toString();
	}
	
	public String toStringDetail(){
		return super.toStringDetail() + "\n" 
				+ "Rows:" + rows;
	}
}
