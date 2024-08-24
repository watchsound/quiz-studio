package r9.quiz.surveyjs;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.R9RefCollectionValue;

import r9.quiz.surveyjs.Question.ChoicesOrderType;
import r9.quiz.util.Utils;

public class RadioGroupQuestion extends Question{
 
	private static final long serialVersionUID = 1L;

	  @R9RefCollectionValue
     private List<StringChoice> choices;
     private ChoicesOrderType    choicesOrder =  ChoicesOrderType.none;
     private int colCount;
     
	public RadioGroupQuestion(){
		setType( QuestionType.radiogroup );
		choices = new ArrayList<StringChoice>();
	}
	 public   List<String> getValues(){
		 List<String> values = new ArrayList<String>();
			for(StringChoice c : this.choices)
				values.add(c.value);
		 return values;
	 }
		public   String getCorrectAnswer() {
			return StringChoice.getCorrects(choices);
		}

		public   void setCorrectAnswer(String correctAnswer) {
			 
		}
	public int getColCount() {
		return colCount;
	}


	public void setColCount(int colCount) {
		this.colCount = colCount;
	}


	public List<StringChoice> getChoices() {
		return choices;
	}

	public void setChoices(List<StringChoice> choices) {
		this.choices = choices;
	}


	public ChoicesOrderType getChoicesOrder() {
		return choicesOrder;
	}


	public void setChoicesOrder(ChoicesOrderType choicesOrder) {
		this.choicesOrder = choicesOrder;
	}
 
	public String toJson(){
		StringBuilder sb = new StringBuilder("{");
		sb.append(super.toJson());
		if( this.choicesOrder != null  )
			sb.append("choicesOrder: \"" + this.choicesOrder.name() + "\", "); 
		if( this.colCount > 0  )
			sb.append("colCount:  " + this.colCount+ " , ");
		sb.append("choices: [ " );
		for(int i = 0; i < choices.size(); i++){
			if( i != 0) sb.append(", ");
			sb.append(" entity2html('" + Utils.normalize_ascii2entity( choices.get(i).value ) + "') ");
			//sb.append( "\"" + choices.get(i).value + "\" ");
		}
		
		sb.append("]" );
		sb.append("}" );
		return sb.toString();
	}
	
	public String toStringDetail(){
		return super.toStringDetail() + "\n" 
				+ "choices:" + choices.toString()  ;
	}
}
