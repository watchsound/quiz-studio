package r9.quiz.ui.comps;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import r9.quiz.util.ImageUtil;

 

public class ResourceManager {
 
	public static BufferedImage getToolBufferedImageIcon(String iconName){
		URL url = ResourceManager.class.getResource( iconName);
		try {
			return  ImageIO.read( url ); 
		} catch (Exception e) {
			 
		}
	  	return null;
	}
	
	public static ImageIcon getToolIcon(String iconName){
		return getToolIcon(iconName, 16);
	}
	public static ImageIcon getToolIcon(String iconName, int size){
		URL url = ResourceManager.class.getResource( iconName);
		try {
			BufferedImage simage = ImageIO.read( url );
			if( size <=0 ) size = 16;
			return new ImageIcon(ImageUtil.resizeImage(simage, size, size)); 
		} catch (Exception e) {
			 
		}
	  	return null;
	}
	public static BufferedImage getToolIconImage(String iconName){
		URL url = ResourceManager.class.getResource( iconName);
		try {
			return ImageIO.read( url ); 
		} catch (Exception e) {
			 
		}
	  	return null;
	}
	
	public static ImageIcon getToolIcon(String iconName, int w, int h){
		URL url = ResourceManager.class.getResource( iconName);
		try {
			BufferedImage simage = ImageIO.read( url );
			return new ImageIcon(ImageUtil.resizeImage(simage, w, h, false, false)); 
		} catch (Exception e) {
			 
		}
	  	return null;
	}
	public static BufferedImage getToolIconImage(String iconName, int w, int h){
		URL url = ResourceManager.class.getResource( iconName);
		try {
			BufferedImage simage = ImageIO.read( url );
			return ImageUtil.resizeImage(simage, w, h, false, false); 
		} catch (Exception e) {
			 
		}
	  	return null;
	}
	
}
