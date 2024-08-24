package r9.quiz.cards;
//package freemind.cards;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.WeakHashMap;
//
//import freemind.cards.animation.CardAnimation;
//import freemind.modes.MindMapNode;
//
//public class CardSetBack {
//	private List<String> tags;
//    private Card root;  
//    private CardCssLayout cardLayout;
//    private String name;
//    
//    private String coverImage;
//    private String backgrdImage;
//    
//    private List<SingleChoiceQuestion> questions;
//    
//    private WeakHashMap<MindMapNode, Card> nodeToCardMap = 
//    		new WeakHashMap<MindMapNode, Card>();
//    
//    public CardSetBack(){
//    	
//    }
//    
//    public Card getCardByNode(MindMapNode node){
//    	return nodeToCardMap.get(node);
//    }
//    
//    
//    public List<SingleChoiceQuestion> getQuestions() {
//    	if ( questions == null ){
//		    questions = new ArrayList<SingleChoiceQuestion>();
//		}
//		return questions;
//	}
//
//	public void setQuestions(List<SingleChoiceQuestion> questions) {
//		this.questions = questions;
//	}
//	
//	public void addQuestion(SingleChoiceQuestion question){
//		if ( questions == null ){
//		    questions = new ArrayList<SingleChoiceQuestion>();
//		}
//		questions.add( question );
//	}
//
//	public CardSetBack(Card root){
//    	this.root = root;
//    } 
//    
//	public String getCoverImage() {
//		return coverImage;
//	}
//
//	public void setCoverImage(String coverImage) {
//		this.coverImage = coverImage;
//	}
//
//	public String getBackgrdImage() {
//		return backgrdImage;
//	}
//
//	public void setBackgrdImage(String backgrdImage) {
//		this.backgrdImage = backgrdImage;
//	}
//
//	public List<String> getTags() {
//		return tags;
//	}
//	public void setTags(List<String> tags) {
//		this.tags = tags;
//	}
//	public Card getRoot() {
//		return root;
//	}
//	public void setRoot(Card root) {
//		this.root = root;
//	}
//	public CardCssLayout getCardLayout() {
//		return cardLayout;
//	}
//	public void setCardLayout(CardCssLayout cardLayout) {
//		this.cardLayout = cardLayout;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public void addTag(String tag){
//		if ( tags == null )
//			tags = new ArrayList<String>();
//		tags.add(tag);
//	}
//	
//	public List<Card> getCardList(){
//		return root == null ? new ArrayList<Card>() : root.getAllChildren();
//	}
//	
//	public Card getCardBy(MindMapNode node){
//		if ( root == null ){
//		    setRoot(Card.fromNode(node));
//	 	}
//		return root.getCardBy(node);
//	}
//	
//}
