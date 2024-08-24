package r9.quiz.cards;

import java.util.ArrayList;
import java.util.List;

public class CardLayoutFactory {
    public static final CardLayoutFactory instance = new CardLayoutFactory();
    private List<CardLayoutI> registeredLayout;
    
    private CardLayoutFactory(){
    	registeredLayout = new ArrayList<CardLayoutI>();
    	
    }
    
    public CardLayoutI getCardLayout(String name){
    	for(CardLayoutI layout : registeredLayout){
    		if ( layout.getName().equalsIgnoreCase(name))
    			return layout;
    	}
    	return null;
    }
}
