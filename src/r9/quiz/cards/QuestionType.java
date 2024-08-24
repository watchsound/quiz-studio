package r9.quiz.cards;

import java.util.ResourceBundle;

import r9.quiz.R9SystemSetting;

public enum QuestionType { yesno(0, "YesNoProblem" ),  fill_in(1, "FillInProblem" ), choice(2,"ChoiceProblem" ),
	image_choice(3,"ImageChoiceProblem" ),
	html_page(4,"RichTextProblem" ) ;  
    
    private QuestionType(int value, String name ){     
       this.value = value;     
       this.name = name;        
    } 
    public static QuestionType fromName(String name){
    	if( fill_in.name.equals(name) )
    		return fill_in;
    	if( choice.name.equals(name) )
    		return choice;
    	if( image_choice.name.equals(name) )
    		return image_choice;
    	return html_page;
    }

    public String toString(){
    	return getName();
    }
    public int getValue() {
		return value;
	}

	public String getName() {
		ResourceBundle rb = ResourceBundle.getBundle("r9.quiz.message",R9SystemSetting.LOCALE);
    	return rb.getString(name);
	} 
 

	private int value;  //自定义数据域，private为了封装。     
    String name;        
    
}