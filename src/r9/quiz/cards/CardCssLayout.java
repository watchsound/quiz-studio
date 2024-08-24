package r9.quiz.cards;

import java.io.Serializable;

public class CardCssLayout implements CardLayoutI , Serializable{
	 
		private static final long serialVersionUID = 1L; 
    private String name;
    private String definition;
    
    
	public void setName(String name) {
		this.name = name;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@Override
	public String getName() {
		 return name;
	}

	@Override
	public String getDefinition() {
	 	return definition;
	}

}
