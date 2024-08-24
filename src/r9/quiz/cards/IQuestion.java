package r9.quiz.cards;

import java.awt.image.BufferedImage;

public interface IQuestion {

	public void setType(QuestionType type);
	public QuestionType getType();
	
	public   void setQuestionBody(String questionBody);

	public   String getQuestionBody();
 
	public String[] getOptions();
	 
	public void setOptions(String[] options) ;
	
	public   String  getAnswers();

	public   void setAnswers(String  answer);

	
	public String getCorrectFollow();
	public void setCorrectFollow(String correctFollow);
	public String getWrongFollow();
	public void setWrongFollow(String wrongFollow) ;
	
	
	public   String getTaglist();

	public   void setTaglist(String taglist);

	public   int getLevel();

	public   void setLevel(int level);

	public int getScore() ;

	public void setScore(int score); 
	
	public BufferedImage getPreviewImage(); 
	public void setPreviewImage(BufferedImage previewImage);
	
}