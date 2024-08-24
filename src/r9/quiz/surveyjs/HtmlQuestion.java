package r9.quiz.surveyjs;

import java.util.ArrayList;
import java.util.List;

import r9.quiz.cards.HtmlContent;
import r9.quiz.util.Utils;

public class HtmlQuestion extends Question implements HtmlContent{
 
	private static final long serialVersionUID = 1L;

	private String html;
	public HtmlQuestion(){
		setType( QuestionType.html_page );
	}
	
	 public   List<String> getValues(){
		 List<String> values = new ArrayList<String>();
	 	 return values;
	 }

	 
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public   String getCorrectAnswer() {
		return "";
	}

	public   void setCorrectAnswer(String correctAnswer) { 
	}
	public String toJson(){
		StringBuilder sb = new StringBuilder("{"); 
		if( this.type != null   )
			sb.append("type: \"" + this.type.surveyid + "\", ");
		if( this.name != null && this.name.length() > 0 )
			sb.append("name: \"" + this.name + "\", ");
		if( this.html != null && this.html.length() > 0 )
			sb.append("html: entity2html('" + Utils.normalize_ascii2entity(this.html) + "') ");
		sb.append("}" );
		return sb.toString();
	}
	
	
	public String toStringDetail(){
		return super.toStringDetail() + "\n" 
				+ "html: \":" +html;
	}
	@Override
	public String getContent() {
		 	return html;
	}
	@Override
	public void setContent(String content) {
		this.html = content;
	}
}
