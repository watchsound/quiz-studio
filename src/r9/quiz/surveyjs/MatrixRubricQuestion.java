package r9.quiz.surveyjs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.R9RefCollectionValue;
import com.google.gson.annotations.R9RefMapValue;

public class MatrixRubricQuestion extends Question{
 
	private static final long serialVersionUID = 1L;

	
     private List<String> columns;
     @R9RefCollectionValue
     private List<MatrixField> rows;
     @R9RefMapValue
     private Map<String,Map<String,List<String>>> cells;
     private String   choicesOrder =  "none";//"random";
     private int colCount;
     private boolean showLabel = false;
     private boolean isAllRowRequired = false;
     
	public MatrixRubricQuestion(){
		setType(QuestionType.matrix);
		columns = new ArrayList<String>();
		rows = new ArrayList<MatrixField>();
		cells = new HashMap<String,Map<String,List<String>>>();
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
	public int getColCount() {
		return colCount;
	}


	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

 
 


	public List<String> getColumns() {
		return columns;
	}


	public void setColumns(List<String> columns) {
		this.columns = columns;
	}


	public Map<String, Map<String, List<String>>> getCells() {
		return cells;
	}


	public void setCells(Map<String, Map<String, List<String>>> cells) {
		this.cells = cells;
	}


	public List<MatrixField> getRows() {
		return rows;
	}


	public void setRows(List<MatrixField> rows) {
		this.rows = rows;
	}


	public String getChoicesOrder() {
		return choicesOrder;
	}


	public void setChoicesOrder(String choicesOrder) {
		this.choicesOrder = choicesOrder;
	}

 
 
	public boolean isAllRowRequired() {
		return isAllRowRequired;
	}


	public void setAllRowRequired(boolean isAllRowRequired) {
		this.isAllRowRequired = isAllRowRequired;
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
		if( true ) throw new RuntimeException("not implemented yet");
		sb.append("}" );
		return sb.toString();
	}

	public static class MatrixField implements Serializable{
		 
		private static final long serialVersionUID = 1L;
		public String value;
		public String text;
		public MatrixField(String value, String text){
			this.value = value;
			this.text = text;
		}
	}
	
}
