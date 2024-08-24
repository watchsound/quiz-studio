package r9.quiz.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import r9.quiz.util.ImageUtil;

public class HtmlTemplate  implements Serializable{
 
	private static final long serialVersionUID = 1L;

	private String name;
	private String fileName;
	private QuestionType problemType;
	private String content;
	  private List<String> cssFiles = new ArrayList<String>();
	    private List<String> jsFiles = new ArrayList<String>();
	public HtmlTemplate(){
		this.fileName = ImageUtil.createRandomString(10);
		this.name = "";
		this.content = "";
		this.problemType = QuestionType.choice;
	}
	public HtmlTemplate(String name,   QuestionType problemType, String content){
		this.name = name;
		this.fileName = ImageUtil.createRandomString(10);
		this.problemType = problemType;
		this.content = content;
	}
	public boolean valid(){
		return name != null &&name.length() > 0 ;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public QuestionType getProblemType() {
		return problemType;
	}
	public void setProblemType(QuestionType problemType) {
		this.problemType = problemType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
    
		public List<String> getCssFiles() {
			if( cssFiles == null)
				cssFiles = new ArrayList<String>();
			return cssFiles;
		}

		public void setCssFiles(Object[] values){
			 List<String> files = getCssFiles();
			 files.clear();
			 if( values == null) return;
			 for(Object v : values){
				 if(v instanceof String)
					 files.add((String)v);
			 }
		}

		public void setCssFiles(List<String> cssFiles) {
			this.cssFiles = cssFiles;
		}

		public String[] getJsFilesInArray() {
			 List<String> result = getJsFiles();
			 String[] sresult = new String[result.size()];
			 for(int i = 0;i < result.size(); i++)
				 sresult[i] = result.get(i);
			 return sresult;
		}
		
		public String[] getCssFilesInArray() {
			 List<String> result = getCssFiles();
			 String[] sresult = new String[result.size()];
			 for(int i = 0;i < result.size(); i++)
				 sresult[i] = result.get(i);
			 return sresult;
		}

		public List<String> getJsFiles() {
			if( jsFiles == null)
				jsFiles = new ArrayList<String>();
			return jsFiles;
		}
		public void setJsFiles(Object[] values){
			 List<String> files = getJsFiles();
			 files.clear();
			 if( values == null) return;
			 for(Object v : values){
				 if(v instanceof String)
					 files.add((String)v);
			 }
		}

		public void setJsFiles(List<String> jsFiles) {
			this.jsFiles = jsFiles;
		}

	public String toString(){
		return name   + ":" + problemType;
	}
	
	public boolean equals(Object t) {
		if( t == null ) return false;
		return toString().equals(t.toString());
	}
	public int hashCode() {
		return toString().hashCode();
	}
}
