package r9.quiz.surveyjs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.R9RefCollectionValue;

import r9.quiz.surveyjs.Question.ChoicesOrderType;

public class ImagePickerQuestion extends Question{
 
	private static final long serialVersionUID = 1L;

	  @R9RefCollectionValue
     private List<ImageLink> choices;
     private ChoicesOrderType    choicesOrder =  ChoicesOrderType.none;
     private int colCount;
     private boolean showLabel = false;
     
     
	public ImagePickerQuestion(){
		setType(QuestionType.imagepicker);
		choices = new ArrayList<ImageLink>();
	}

	 public   List<String> getValues(){
		 List<String> values = new ArrayList<String>();
		 for(ImageLink l : choices)
		     values.add(l.value);
		 return values;
	 }
		public   String getCorrectAnswer() {
			return  getCorrects(this.choices);
		}

		public   void setCorrectAnswer(String correctAnswer) { 
		}
		
	public int getColCount() {
		return colCount;
	}


	public void setColCount(int colCount) {
		this.colCount = colCount;
	}


	public List<ImageLink> getChoices() {
		return choices;
	}

	public void setChoices(List<ImageLink> choices) {
		this.choices = choices;
	}


	public ChoicesOrderType getChoicesOrder() {
		return choicesOrder;
	}


	public void setChoicesOrder(ChoicesOrderType choicesOrder) {
		this.choicesOrder = choicesOrder;
	}

 
 
	public boolean isShowLabel() {
		return showLabel;
	}


	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	public String toJson(){
		StringBuilder sb = new StringBuilder("{");
		sb.append(super.toJson());
		if( this.choicesOrder != null  )
			sb.append("choicesOrder: \"" + this.choicesOrder.name() + "\", ");
		if( this.showLabel    )
			sb.append("showLabel: true, ");
		sb.append("choices: [ " );
		for(int i = 0; i < choices.size(); i++){
			if( i != 0) sb.append(", ");
			sb.append("{" );
			sb.append( "value:  \"" + choices.get(i).value + "\", ");
			sb.append( "imageLink:  \"" + choices.get(i).imageLink + "\", ");
			sb.append("}" );
		}
		
		sb.append("]" );
		sb.append("}" );
		
		return sb.toString();
	}
	public String toStringDetail(){
		return super.toStringDetail() + "\n" 
				+ "Choices:" + choices.toString();
	}
	

	public static class ImageLink implements Serializable{
		 
		private static final long serialVersionUID = 1L;
		public String value;
		public String imageLink;
		public boolean correct;
		public ImageLink(String value, String link){
			this(value, link, false);
		}
		public ImageLink(String value, String link, boolean correct){
			this.value = value;
			this.imageLink = link;
			this.correct = correct;
		}
		public String toString(){
			return value;
		}
	}
	 public static String getCorrects(List<ImageLink> sc) {
		   StringBuilder sb = new StringBuilder();
		   for(ImageLink s : sc) {
			   if( s.correct )
				   sb.append(" " + s.value);
		   }
		   return sb.toString().length() > 0 ? sb.substring(1) : "";
	   }
	
}
