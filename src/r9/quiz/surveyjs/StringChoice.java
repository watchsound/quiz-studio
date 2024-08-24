package r9.quiz.surveyjs;

import java.io.Serializable;
import java.util.List;

import r9.quiz.util.Utils;

public class StringChoice implements Serializable{
   
	private static final long serialVersionUID = -528547825496375777L;
	
   public String value;
   public boolean correct;
   public StringChoice(String value, boolean correct) {
	   this.value = value;
	   this.correct = correct;
   }
   public String toString() {
	   return value;
   }
   
   public static String getCorrects(List<StringChoice> sc) {
	   StringBuilder sb = new StringBuilder();
	   for(StringChoice s : sc) {
		   if( s.correct )
			   sb.append(" " + s.value);
	   }
	   return sb.toString().length() > 0 ? sb.substring(1) : "";
   }
   public static String getCorrectsInJsArray(List<StringChoice> sc) {
	   StringBuilder sb = new StringBuilder();
	   for(StringChoice s : sc) {
		   if( s.correct )
			   sb.append(",\"" + s.value + "\"");
	   }
	   return sb.toString().length() > 0 ? "[" + sb.substring(1) + "]" : "[]";
   }
   public static String getCorrectsInJsArrayEncoded(List<StringChoice> sc) {
	   StringBuilder sb = new StringBuilder();
	   for(StringChoice s : sc) {
		   if( s.correct )
			   sb.append(", entity2html(\"" +  Utils.normalize_ascii2entity(s.value )+ "\")");
	   }
	   return sb.toString().length() > 0 ? "[" + sb.substring(1) + "]" : "[]";
   }
}
