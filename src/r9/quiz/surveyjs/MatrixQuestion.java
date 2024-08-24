package r9.quiz.surveyjs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.annotations.R9Ref;
import com.google.gson.annotations.R9RefCollectionValue;

import r9.quiz.util.Utils;

public class MatrixQuestion extends Question{
 
	private static final long serialVersionUID = 1L;

	  @R9RefCollectionValue
     private List<MatrixField> columns;
	  @R9RefCollectionValue
     private List<MatrixField> rows; 
	  @R9RefCollectionValue
     private List<Cell> corrects;
     private  ChoicesOrderType    choicesOrder =  ChoicesOrderType.none;
     private int colCount;
     private boolean showLabel = false;
     private boolean isAllRowRequired = false;
     
	public MatrixQuestion(){
		setType(QuestionType.matrix);
		columns = new ArrayList<MatrixField>();
		rows = new ArrayList<MatrixField>(); 
		corrects = new ArrayList<Cell>();
	}
	 public   List<String> getValues(){
		 List<String> values = new ArrayList<String>();
		 for(int i = 0; i < rows.size(); i++){
			 values.add(rows.get(i).value); 
		 } 
		 return values;
	 }
	 
	 
 
	public List<Cell> getCorrects() {
		return corrects;
	}
	public void setCorrects(List<Cell> corrects) {
		this.corrects = corrects;
	}
	public int getColCount() {
		return colCount;
	}


	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	public   String getCorrectAnswer() {
		return getCorrects(this.corrects);
	}

	public   void setCorrectAnswer(String correctAnswer) {
		 
	}
 
 

	public List<MatrixField> getColumns() {
		return columns;
	}


	public void setColumns(List<MatrixField> columns) {
		this.columns = columns;
	}


	public List<MatrixField> getRows() {
		return rows;
	}


	public void setRows(List<MatrixField> rows) {
		this.rows = rows;
	}


	public ChoicesOrderType getChoicesOrder() {
		return choicesOrder;
	}


	public void setChoicesOrder(ChoicesOrderType choicesOrder) {
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
		if( this.showLabel    )
			sb.append("showLabel: true, ");
		
		sb.append("columns: [ " );
		for(int i = 0; i < columns.size(); i++){
			if( i != 0) sb.append(", ");
			sb.append("{" );
			sb.append( "value:  \"" + columns.get(i).value + "\", ");
			sb.append("text:  entity2html('" + Utils.normalize_ascii2entity( columns.get(i).text ) + "') ");
			//sb.append( "text:  \"" + columns.get(i).text + "\" ");
			sb.append("}" );
		}
		
		sb.append("], " );
		
		sb.append("rows: [ " );
		for(int i = 0; i < rows.size(); i++){
			if( i != 0) sb.append(", ");
			sb.append("{" );
			sb.append( "value:  \"" + rows.get(i).value + "\", ");
			sb.append("text:  entity2html('" + Utils.normalize_ascii2entity( rows.get(i).text ) + "') ");
			//sb.append( "text:  \"" + rows.get(i).text + "\" ");
			sb.append("}" );
		}
		
		sb.append("] " );
		sb.append("}" );
		return sb.toString();
	}
	
	public String toStringDetail(){
		return super.toStringDetail() + "\n" 
				+ "columns:" + columns.toString() + "\n"
				+ "rows:" + rows.toString() ;
	}

	public static class MatrixField implements Serializable{
		 
		private static final long serialVersionUID = 1L;
		public String value;
		public String text;
		public int order;
		public MatrixField(String value, String text){
			this.value = value;
			this.text = text;
		}
		public String toString(){
			return text;
		}
	}
	
	public void cleanup() {
		for(int i = corrects.size()-1; i>=0; i--) {
			Cell cell = corrects.get(i);
			if( !this.columns.contains(cell.col) || !this.rows.contains(cell.row))
				corrects.remove(i);
		}
	}
	public Cell get(MatrixField row, MatrixField col) {
		for(Cell cell : corrects) {
			if( cell.row == row && cell.col == col )
				return cell;
		}
		return null;
	}
	public static class Cell implements Serializable{ 
		private static final long serialVersionUID = 1L;
		@R9Ref
		public MatrixField col;
		@R9Ref
		public MatrixField row;
		public Cell(MatrixField row, MatrixField col  ){
			this.col = col;
			this.row = row;
		}
		public String toString(){
			return col.value + ":" + row.value;
		}
	}
	
	public String getValidatorCode2() {
		if( this.getWrongFollow() == null || this.getWrongFollow().length() == 0)
			return "";
		return "  if (options.name == '" + name + "') { " + 
				 "   if ( !equalsObject(options.value, " + this.getCorrectAnswer() + " ) ){ "  + 
				"        options.error = \"" + this.getWrongFollow()  + "\"; " + 
				"    } " + 
				" }" ;
	}
	
	public String getValidatorCode() {
		if( this.getWrongFollow() == null || this.getWrongFollow().length() == 0)
			return "";
		return "function " + getValidatorName() + "(params) {\n" + 
				"    var value = params[0] ;\n" + 
				"    return equalsObject(value, " +  this.getCorrectAnswer() + ");\n" + 
				"}" +
				";Survey.FunctionFactory.Instance.register(\""+ this.getValidatorName() +"\", " +this.getValidatorName()+ ");";
	}
	
	
	public   String getCorrects(List<Cell> sc) {
		for(int i = 0; i < this.rows.size(); i++) {
			this.rows.get(i).order = i;
		}
		Collections.sort(sc, new Comparator<Cell>() { 
			@Override
			public int compare(Cell o1, Cell o2) {
				if( o1.row.order == o2.row.order)
					return 0;
				return o1.row.order < o2.row.order ? -1 : 1;
			}});
		
	   StringBuilder sb = new StringBuilder();
	   for(Cell s : sc) { 
		   sb.append(",\"" + s.row.value + "\":" + "\"" + s.col.value + "\"");
	   }
	   return sb.toString().length() > 0 ?  "{" + sb.substring(1) + "}" : "";
    }
	
}
