package r9.quiz.cards;
 
public interface PageNaviagtionI {
    public String getParentUUID();
 //   public List<Card> getChildren();
    public int getOrder();
    
    public boolean isBackAllowed();
}
