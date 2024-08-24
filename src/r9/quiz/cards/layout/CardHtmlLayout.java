package r9.quiz.cards.layout;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CardHtmlLayout {
	    private List<CardHtmlDivBox> boxList = new ArrayList<CardHtmlDivBox>();

	    private CardHtmlDivBox defaultBox;
	    
	    private transient BufferedImage previewImage; 
	    
	    
		public BufferedImage getPreviewImage() {
			return previewImage;
		}

		public void setPreviewImage(BufferedImage previewImage) {
			this.previewImage = previewImage;
		}

		public CardHtmlDivBox getDefaultBox() {
			return defaultBox;
		}

		public void setDefaultBox(CardHtmlDivBox defaultBox) {
			this.defaultBox = defaultBox;
		}

		public List<CardHtmlDivBox> getBoxList() {
			return boxList;
		}

		public void setBoxList(List<CardHtmlDivBox> boxList) {
			this.boxList = boxList;
		}

}
