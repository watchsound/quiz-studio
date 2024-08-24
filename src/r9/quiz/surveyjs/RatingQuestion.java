package r9.quiz.surveyjs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingQuestion extends Question{
 
	private static final long serialVersionUID = 1L;

	private String minRateDescription = "Not Satisfied";
    private String maxRateDescription = "Completely satisfied";
     
     
	public RatingQuestion(){
		setType( QuestionType.rating ); 
	}

	 public   List<String> getValues(){
		 List<String> values = new ArrayList<String>();
		 
		 return values;
	 }
	 
		public   String getCorrectAnswer() {
			return "";
		}

		public   void setCorrectAnswer(String correctAnswer) {
			 
		}
		
	public String getMinRateDescription() {
		return minRateDescription;
	}


	public void setMinRateDescription(String minRateDescription) {
		this.minRateDescription = minRateDescription;
	}


	public String getMaxRateDescription() {
		return maxRateDescription;
	}


	public void setMaxRateDescription(String maxRateDescription) {
		this.maxRateDescription = maxRateDescription;
	}
  
	public String toJson(){
		StringBuilder sb = new StringBuilder("{");
		sb.append(super.toJson());
		if( this.maxRateDescription != null && this.maxRateDescription.length() > 0 )
			sb.append("maxRateDescription: \"" + this.maxRateDescription + "\", ");
		if( this.minRateDescription != null && this.minRateDescription.length() > 0 )
			sb.append("minRateDescription: \"" + this.minRateDescription + "\", ");
		sb.append("}" );
		return sb.toString();
	}
	
	public String toStringDetail(){
		return super.toStringDetail() + "\n" 
				+ "min:" + minRateDescription + "\n"
				+ "max" + maxRateDescription;
	}
}
