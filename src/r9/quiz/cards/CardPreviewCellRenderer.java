package r9.quiz.cards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import r9.quiz.*; 
import r9.quiz.util.ImageUtil;


public class CardPreviewCellRenderer implements ListCellRenderer {
	public static final int CARD_PREVIEW_WIDTH = 170;
	public static final int CARD_PREVIEW_HEIGHT = 255;

	  public Component getListCellRendererComponent(final JList list,
	      final Object value, final int index, final boolean isSelected,
	      final boolean cellHasFocus) {
	      return new  CellPreviewPanel(list, value,index, isSelected, cellHasFocus);
	  }
	  
	  private static class CellPreviewPanel extends JPanel {
			private static final long serialVersionUID = -4624762713662343786L;
            private JList list;
            private CardPage value;
            private int index;
            private boolean isSelected;
            private boolean cellHasFocus;
            private Image coverImage;
            private Image backgroundImage;
            
			public CellPreviewPanel(final JList list,
				      final Object value, final int index, final boolean isSelected,
				      final boolean cellHasFocus) {
				super(new BorderLayout());
				setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder(2, 2, 2, 2),
						 BorderFactory.createLineBorder(Color.darkGray, 1)));
				this.list = list;
				this.value = (CardPage) value;
				this.index = index;
				this.isSelected = isSelected;
				this.cellHasFocus = cellHasFocus;
				this.coverImage = CourseCreatingManager.sharedInstance.getCoverImage();
				this.backgroundImage =  CourseCreatingManager.sharedInstance.getBackgroundImage();
			}

			 public void paintComponent(Graphics g) {
			        super.paintComponent(g);
			        g.setColor(isSelected ? list.getSelectionBackground() : list
			                .getBackground());
			        g.fillRect(0, 0, getWidth(), getHeight());
			       
			        if ( coverImage != null && index == 0 ){
			        	int width = this.getWidth();
			        	int height = this.getHeight();
			        	int imagewidth = coverImage.getWidth(null);
			        	int imageheight = coverImage.getHeight(null);
			            g.drawImage(coverImage, (width- imagewidth)/2, (height - imageheight)/2, null);
			            return;
			        }
			        Exam cardset = CourseCreatingManager.sharedInstance.getExam();
			        
			        CardPreviewListModel model = (CardPreviewListModel) list.getModel();
			       
//			        BufferedImage image = null;
//			        if ( value.isImagePreview() ){ 
//			         	image = mapview.createBufferedImage2(nodeview,  CARD_PREVIEW_WIDTH, CARD_PREVIEW_WIDTH);
//			         	if ( image != null ){
//				        	int width = this.getWidth();
//				        	int height = this.getHeight();
//				        	int imagewidth = image.getWidth();
//				        	int imageheight = image.getHeight();
//				        	 g.drawImage(image, (width- imagewidth)/2, (height - imageheight)/2, null);
//				        	 
//				            String uuid = value.getImageFileName();
//				            if ( uuid == null ){
//				            	uuid = ImageUtil.createCompactUID();
//				            	value.setImageFileName(uuid);
//				            }
//				            	image = mapview.createBufferedImage2(nodeview,  Constants.CARD_WIDTH, Constants.CARD_HEIGHT);
//				                CourseCreatingManager.sharedInstance.saveTempImage(uuid, image);
//				             
//			         	}
//			        } else if ( nodeview != null ) {
//			        	String nodeStr = node.toString();
//			        	 image = nodeview.getImagePreviewForText();
//		        	     if ( image != null ){
//		        		     BufferedImage image2 = ImageUtil.resizeImage(image, CARD_PREVIEW_WIDTH, CARD_PREVIEW_HEIGHT, false, true);
//		        		     g.drawImage(image2, 0,0, null);
//		        		         
//		        		     String uuid = value.getImageForCreatingPreivew();
//		        		     CourseCreatingManager.sharedInstance.saveTempImage(uuid, image2);
//		         	     } else {
//		         	    	 String uuid = value.getImageForCreatingPreivew();
//		         	    	 Image img = CourseCreatingManager.sharedInstance.getTempImage(uuid);
//		         	    	 if ( img != null ){
//		         	    		g.drawImage(img, 0,0, null);
//		         	    	 }
//		         	     }
//		        	     
//			        	if(  value.getContent() == null || value.getContent().length() == 0 ){
//			        		g.setColor(Color.black);
//			        		
//			        	 	g.drawString( nodeStr, CARD_PREVIEW_WIDTH /2, CARD_PREVIEW_HEIGHT/2);
//			        	}  
//			        } 
			        
			      }

			      public Dimension getPreferredSize() { 
			        return new Dimension(CARD_PREVIEW_WIDTH+4, CARD_PREVIEW_HEIGHT+4);
			      }
		}
}
