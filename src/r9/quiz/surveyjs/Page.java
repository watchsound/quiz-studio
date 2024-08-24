package r9.quiz.surveyjs;

import java.io.Serializable;
import java.util.*;

import com.google.gson.annotations.R9RefCollectionValue;

public class Page  implements Serializable{
	 
		private static final long serialVersionUID = 1L;
		  @R9RefCollectionValue
		private List<Question> questions = new ArrayList<Question>();
		  private int maxTimeToFinish = 25;
		  
		public List<Question> getQuestions() {
			return questions;
		}

		public void setQuestions(List<Question> questions) {
			this.questions = questions;
		}

		public int getMaxTimeToFinish() {
			return maxTimeToFinish;
		}

		public void setMaxTimeToFinish(int maxTimeToFinish) {
			this.maxTimeToFinish = maxTimeToFinish;
		}
		
		public String toJson(){
			StringBuilder sb = new StringBuilder();
			if( getMaxTimeToFinish() > 0 )
				sb.append("{ maxTimeToFinish :  " + getMaxTimeToFinish() + ", ");
			if(questions.size()>0 && questions.get(0).getName() != null)
				sb.append(" name :  \"" +  questions.get(0).getName()    + "\",");
	    	sb.append(" questions : [");
	    	for(int i = 0; i < questions.size(); i++){
	    		if( i != 0 ) sb.append(", ");
	    		sb.append( questions.get(i).toJson() );
	    	}
	    	
	    	sb.append(" ] } ");
			return sb.toString();
		}

}
