package r9.quiz.ui.comps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
 

public class MathJaxImportTextField extends JTextArea{
  
	private static final long serialVersionUID = 1L;
	
	private BufferedImage bgimage;
	private boolean showImage = false;
	public MathJaxImportTextField( ) {
		this(0,0);
	}
	public MathJaxImportTextField(int row, int col) {
		super(row, col); 
		super.setBorder(BorderFactory.createLineBorder(Color.green)); 
		this.setPreferredSize(new Dimension(600,32));
		this.setMinimumSize(new Dimension(600,32));
		Font f = this.getFont();
		if( f != null )
			this.setFont(f.deriveFont(24));
		this.addMouseListener(new MouseAdapter() {
			 public void mousePressed(MouseEvent e) {
				 if( !e.isPopupTrigger())
					 return;
				 showPopup( );
			 }
			 public void mouseReleased(MouseEvent e) {
				 if( !e.isPopupTrigger())
					 return;
				 showPopup( );
			 }
			
		}); 
	}
	 public void showPopup( ) {
		 MathJaxImportDialog.MathJaxCallback callback = new MathJaxImportDialog.MathJaxCallback() {
		 		@Override
				public void onResult(String result, String imageStr) { 
		 			InputStream buffin = null;
		 			 try {
		 				 setText(result);
		 				 //remove data:image/png;base64,
		 				 imageStr = imageStr.substring(imageStr.indexOf(",",1)+1, imageStr.length());
		 				 //can not use Base64.getDecoder(), it return a basic decoder, which 
		 				 //can not handle newline ...
			 			 byte[] cs = Base64.getMimeDecoder().decode(imageStr);  
			 			  buffin = new ByteArrayInputStream(cs);
						 bgimage =  ImageIO.read(buffin);
						 repaint();
					} catch (Exception e) {
						e.printStackTrace();
					}
		 			 finally {
		 				 if( buffin != null ) try {  buffin.close();     }catch(Exception ex) {}
		 			 } 
				}
				
				@Override
				public String getContent() {
					 return  getText();
				}
				@Override
				public boolean needImageStr() {
					return true;
				}
			};
			MathJaxImportDialog dialog = new MathJaxImportDialog(null, callback);
			dialog.setVisible(true);   
	 }
	public BufferedImage getBgimage() {
		return bgimage;
	}
	public void setBgimage(BufferedImage bgimage) {
		this.bgimage = bgimage;
	}
	public void setShowImage(boolean show) {
		this.showImage = show;
	}
	public boolean isShowImage() {
		return this.showImage;
	}
	public void toggle() {
	     this.showImage = ! this.showImage;
	     if( this.showImage && this.bgimage == null ) {
	    	 showPopup( );
	     } else {
	    	 repaint();
	     }
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int w = this.getWidth();
		int h = this.getHeight(); 
		if( this.bgimage != null && this.showImage) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, w, h);
			int bw = this.bgimage.getWidth();
			int bh = this.bgimage.getHeight();
			g.drawImage(this.bgimage, 0, 0, bw, Math.min( h, bh), 0,0, bw, bh,null);
		}
	}
}
