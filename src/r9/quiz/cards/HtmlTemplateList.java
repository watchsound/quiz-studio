package r9.quiz.cards;

import java.io.Serializable;
import java.util.*;

public class HtmlTemplateList implements Serializable{
 
	private static final long serialVersionUID = 1L;

	private List<HtmlTemplate> templates = new ArrayList<HtmlTemplate>();

	public List<HtmlTemplate> getTemplates() {
		return templates;
	}

	public void cleanup(){
		for(int i = templates.size()-1; i>=0; i--){
			HtmlTemplate t = templates.get(i);
			if( !t.valid() )
				templates.remove(i);
		}
	}
	public HtmlTemplate getTemplate(String id){
		for(HtmlTemplate t : templates){
			if( t.getFileName().equals(id))
				return t;
		}
		return null;
	}
	
	public void setTemplates(List<HtmlTemplate> templates) {
		this.templates = templates;
	}
	
	public Vector<HtmlTemplate> filter(QuestionType type){
		Vector<HtmlTemplate> result = new Vector<HtmlTemplate>();
		for(HtmlTemplate t : templates){
			if( t!=null && t.getProblemType() == type  )
				result.add(t);
		}
		return result;
	}
	
	
}
