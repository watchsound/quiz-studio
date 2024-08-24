package r9.quiz.surveyjs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.R9RefCollectionValue;

import r9.quiz.surveyjs.ImagePickerQuestion.ImageLink;
import r9.quiz.util.Utils;

public class MultipleTextQuestion extends Question{
 
	private static final long serialVersionUID = 1L;

	  @R9RefCollectionValue
     private List<TextItem> items; 
    
     private int colCount;
     
	public MultipleTextQuestion(){
		setType( QuestionType.multipletext );
		items = new ArrayList<TextItem>(); 
	}
	@Override
	 public   List<String> getValues(){
		 List<String> values = new ArrayList<String>();
		 for(int i = 0; i < items.size(); i++){
			 values.add(items.get(i).name); 
		 } 
		 return values;
	 }
	 @Override
	 public   String getCorrectAnswer() {
			return  toJson(this.items);
     }
	 @Override
		public   void setCorrectAnswer(String correctAnswer) { 
		}
		
	public List<TextItem> getItems() {
		return items;
	}


	public void setItems(List<TextItem> items) {
		this.items = items;
	}

	@Override
	public boolean answerNeedsQuote() {
		return false;
	}


	public int getColCount() {
		return colCount;
	}


	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	public String toJson(){
		StringBuilder sb = new StringBuilder("{");
		sb.append(super.toJson());
		if( this.colCount > 0  )
			sb.append("colCount:  " + this.colCount+ " , ");
		
		sb.append("items: [ " );
		for(int i = 0; i < items.size(); i++){
			if( i != 0) sb.append(", ");
			sb.append("{" );
			sb.append( "name:  \"" + items.get(i).name + "\", ");
			//sb.append( "title:  \"" + items.get(i).title + "\", ");
			sb.append("title:  entity2html('" + Utils.normalize_ascii2entity( items.get(i).title ) + "') ");
			sb.append("}" );
		}
		
		sb.append("] " );
		sb.append("}" );
		return sb.toString();
	}
  
	public String toStringDetail(){
		return super.toStringDetail() + "\n" 
				+ "items:" + items.toString()  ;
	}
	public static class TextItem implements Serializable{
		 
		private static final long serialVersionUID = 1L;
		public String name;
		public String title;
		public boolean correct;
		public TextItem(String name, String title){
		    this(name, title,false);
		}
		public TextItem(String name, String title, boolean correct){
			this.name = name;
			this.title = title;
			this.correct = correct;
		}
		public String toString(){
			return name;
		}
	}
	 public static String getCorrects(List<TextItem> sc) {
		   StringBuilder sb = new StringBuilder();
		   for(TextItem s : sc) {
			   if( s.correct )
				   sb.append(" " + s.name);
		   }
		   return sb.toString().length() > 0 ? sb.substring(1) : "";
	   }
	 private   String toJson(List<TextItem> sc) {
		   if( sc.isEmpty() ) return "{}";
		   StringBuilder sb = new StringBuilder("");
		   for(TextItem s : sc) { 
				  sb.append(  ", \"" + s.name + "\": \"" + s.name + "\"");
		   }
		   return  "{" +  sb.substring(1)  + "}";
	   }
}
