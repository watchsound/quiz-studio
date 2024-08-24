package r9.quiz.cards;

import java.io.Serializable;

public class Category implements Serializable{
	 
		private static final long serialVersionUID = 1L;
    private long categoryId;
    private String categoryName;
    private String categoryCode;
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
    
    
}
