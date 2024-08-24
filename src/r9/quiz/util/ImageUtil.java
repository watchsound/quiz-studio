package r9.quiz.util;
 

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.imageio.ImageIO;

import r9.quiz.Constants; 

public class ImageUtil {
	
	public static void main(String[] argv){
		System.out.println( ImageUtil.createUUID());
	}
	
	public static BufferedImage resizeImage(Image image, int width, int height) {
		return resizeImage(image, width, height, false, false);
	}
	public static BufferedImage resizeImage(Image image, int width, int height, boolean keepRatio, boolean rotateIfNeeded) {
        if ( keepRatio ){
			double thumbRatio = (double) width / (double) height;
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
			double imageRatio = (double) imageWidth / (double) imageHeight;
			if (thumbRatio < imageRatio * 0.75 ) {
				height = (int) (width / imageRatio);
			} else if ( imageRatio < thumbRatio * 0.75 ) {
				width = (int) (height * imageRatio);
			}
        }
		BufferedImage thumbImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, width, height, null);
		
		return rotateIfNeeded ?   rotate90ToLeftIfNeeded( thumbImage ) : thumbImage;
	}
	
 
	public static BufferedImage resizeImage(Image image, double scale, boolean rotateIfNeeded) {
       
			int width = (int)(image.getWidth(null) * scale);
			int height = (int)( image.getHeight(null) * scale);
	 
		BufferedImage thumbImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, width, height, null);
		
		return rotateIfNeeded ? rotate90ToLeftIfNeeded ( thumbImage ) : thumbImage;
	}
 
	
	
	
	public static BufferedImage copyAndAdjustImage(String filename, String outFilename, boolean isBackground)
			throws InterruptedException, FileNotFoundException, IOException {
		// load image from filename
		Image image = Toolkit.getDefaultToolkit().getImage(filename);
		 
		BufferedImage thumbImage = null;
		if ( isBackground ){
			thumbImage =   (BufferedImage) resizeImage(image, Constants.CARD_WIDTH	, Constants.CARD_HEIGHT, false, true);
		} else {
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
			if ( imageWidth > Constants.CARD_WIDTH || imageHeight > Constants.CARD_HEIGHT ){
				thumbImage =   (BufferedImage) resizeImage(image,
						 imageWidth > Constants.CARD_WIDTH ? Constants.CARD_WIDTH : imageWidth	,
						 imageHeight > Constants.CARD_HEIGHT ?	 Constants.CARD_HEIGHT : imageHeight, true, false);
			}
			else {
				thumbImage =   (BufferedImage) resizeImage(image, imageWidth, imageHeight, false, false);
			}
				
		} 
		// save thumbnail image to outFilename
		saveImageToFile(thumbImage, outFilename, 100);
		return thumbImage;
	}
	
	
	public static void createThumbnail(String filename, int thumbWidth,
			int thumbHeight, int quality, String outFilename, boolean forceFit)
			throws InterruptedException, FileNotFoundException, IOException {
		// load image from filename
		Image image = Toolkit.getDefaultToolkit().getImage(filename);
		 
		BufferedImage thumbImage =   (BufferedImage) resizeImage(image, thumbWidth, thumbHeight, forceFit, true);
		 
		// save thumbnail image to outFilename
		saveImageToFile(thumbImage, outFilename, quality);
	}
	
	
	public static void createThumbnail(String filename, int thumbWidth,
			int thumbHeight, int quality, String outFilename)
			throws InterruptedException, FileNotFoundException, IOException {
		// load image from filename
		Image image = Toolkit.getDefaultToolkit().getImage(filename);
		 
		BufferedImage thumbImage =   (BufferedImage) resizeImage(image, thumbWidth, thumbHeight);
		 
		// save thumbnail image to outFilename
		saveImageToFile(thumbImage, outFilename, quality);
	}
	
	public static String saveImageToFile(BufferedImage image, String outFilename,  int quality) throws IOException
	{
		File f =new File(  outFilename);
	    if(!f.exists()){
	    	try{
		  f.createNewFile();
	    	}catch(Exception ex){
	    		System.err.println( f );
	    	   ex.printStackTrace();
	    	}
	    }
	    String type = outFilename.substring(outFilename.indexOf(".") + 1);
	    Set format = getFormats();
	    if ( !format.contains(type) )
	    	type = "jpg";
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(  outFilename));
		ImageIO.write(image, type, out);
		out.close();
		return f.getAbsolutePath();
	}
	
	public static String saveImageToFile(Image image, String outFilename,  int quality, boolean rotateIfNeeded) throws IOException
	{
		BufferedImage bimage = resizeImage(image, 1.0, rotateIfNeeded);
		return saveImageToFile(bimage, outFilename, quality);
	}
	
	 public static Set getFormats() {
	        String[] formats = ImageIO.getWriterFormatNames();
	        Set<String> formatSet = new TreeSet<String>();
	        for (String s : formats) {
	            formatSet.add(s.toLowerCase());
	        }
	        return formatSet;
	    }
	
	public static boolean deleteImageFile(String fileName){
		  boolean success = Boolean.FALSE;  
	        File f = new File(fileName);  
	        if (f.exists()) {  
	           f.delete();  
	           success = Boolean.TRUE;  
	        }   
	        return success;  
	}
	public static String createUID(){
		return createUID(40);
	}
	public static int createUniqueIntID(){
		return createUID(10).hashCode();
	}
	public static String createRandomString(int length)
	{
		String uuid = UUID.randomUUID().toString();
		//create a random string with 40 characters.
		char[]  uid = new char[length];
		Random random = new Random();
		uid[0] = 'a';
		for (int i =1; i < length; i++ )
		{
			uid[i] = uuid.charAt(random.nextInt(uuid.length()));
			if ( uid[i] == '-' )
				uid[i] = '_';
		}
		return new String(uid);
	}
	public static String createCompactUID(){
		return createUID(10);
	}
	public static String createUUID(){
		String uuid = createUID(36);
		return mapTo36(uuid);
	}
	public static String mapTo36(String uuid){
		int length = uuid.length();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< 36; i ++){
			if( i == 8 || i == 13 || i == 18 || i == 23 ){
				sb.append("-");
			} else {
				if ( i < length ){
					if( Character.isLetterOrDigit(uuid.charAt(i))){
						sb.append( uuid.charAt(i));
					}else {
						sb.append("a");
					}
					
				} else {
					sb.append("a");
				}
			}
		}
		return sb.toString(); 
	}
	public static String createUID(int length)
	{
		String uuid = UUID.randomUUID().toString();
		//create a random string with 40 characters.
		char[]  uid = new char[length];
		Random random = new Random();
		uid[0] = 'a';
		for (int i =1; i < length; i++ )
		{
			uid[i] = uuid.charAt(random.nextInt(uuid.length()));
			if ( uid[i] == '-' )
				uid[i] = '_';
		}
		return new String(uid);
	}
	
	public static BufferedImage  rotate90ToLeftIfNeeded( BufferedImage inputImage ){
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		if ( width - 50 > height   ){
			return rotate90ToLeft( inputImage );
		} else {
			return inputImage;
		}
	}
	
	public static BufferedImage rotate90ToLeft( BufferedImage inputImage ){
		return rotate(inputImage, 90);
//		//The most of code is same as before
//			int width = inputImage.getWidth();
//			int height = inputImage.getHeight();
//			BufferedImage returnImage = new BufferedImage( height, width , inputImage.getType()  );
//		//We have to change the width and height because when you rotate the image by 90 degree, the
//		//width is height and height is width <img src='http://forum.codecall.net/public/style_emoticons/<#EMO_DIR#>/smile.png' class='bbc_emoticon' alt=':)' />
//
//			for( int x = 0; x < width; x++ ) {
//				for( int y = 0; y < height; y++ ) {
//					returnImage.setRGB(y, width - x - 1, inputImage.getRGB( x, y  )  );
//		//Again check the Picture for better understanding
//				}
//				}
//			return returnImage;

		}
	
	public static BufferedImage rotate(BufferedImage img, double angle)
	{
	    double sin = Math.abs(Math.sin(Math.toRadians(angle))),
	           cos = Math.abs(Math.cos(Math.toRadians(angle)));

	    int w = img.getWidth(null), h = img.getHeight(null);

	    int neww = (int) Math.floor(w*cos + h*sin),
	        newh = (int) Math.floor(h*cos + w*sin);

	    BufferedImage bimg = new  BufferedImage( neww, newh, img.getType());
	    Graphics2D g = bimg.createGraphics();

	    g.translate((neww-w)/2, (newh-h)/2);
	    g.rotate(Math.toRadians(angle), w/2, h/2);
	    g.drawRenderedImage( img , null);
	    g.dispose();

	    return  bimg ;
	}
	
	public static class ImageWrapper {
		public String uuid;
		public String name;
		public Image image;
	}
}
