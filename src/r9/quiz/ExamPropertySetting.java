package r9.quiz;
 
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;

public class ExamPropertySetting implements java.io.Serializable{
 
	public static final int WIDTH = 850;
	public static final int HEIGHT = 580;
	
	public static class ImageConfig{
     	String originalName;
     	String savedName; 
    }
	
	private static final long serialVersionUID = -5797972503828431945L;
	
	//public static ExamPropertySetting sharedInstance = new ExamPropertySetting();
	
	 
	Properties props;
	
	private String courseName;
	public  ExamPropertySetting(String courseName){
		this.courseName = courseName;
		props =  loadProperties();  
	}
	
	public Properties getProperties(){
		return props;
	}
	 
	 
	public List<ImageConfig> getImagesInfo(){
		  
		List<ImageConfig> tools = new ArrayList<ImageConfig>();
		String value = props.getProperty("ImageConfig");
		List<String> enabled  ;
	 	if ( value == null ){
			enabled = new ArrayList<String>( ); 
		} else {
			enabled =  Arrays.asList( value.split(",") );
		}
		for(int i = 0; i < enabled.size(); i += 2){
			 
			ImageConfig c = new ImageConfig();
			c.originalName = enabled.get(i);
			c.savedName = enabled.get(i+1);
		 	tools.add( c );
		}
		return tools;
	}
	public void saveImagesInfo(List<ImageConfig> tools){
		  
	    StringBuilder sb = new StringBuilder();
	    for( ImageConfig tc : tools){
	        sb.append("," + tc.originalName + "," + tc.savedName ); 
	    }
	    String value = sb.length() > 1 ?  sb.substring(1) : null;
	    props.setProperty("ImageConfig", value); 
	    save();
	}
	
 
	 
	public boolean showControlButton(){
		String value = props.getProperty("SHOW_CONTROL_BUTTON");
		return value == null ? true : false;
	}
	public void showControlButton(boolean show){
		if ( !show)
		    props.setProperty("SHOW_CONTROL_BUTTON", "1");
		else 
			props.remove("SHOW_CONTROL_BUTTON");
		 
	}
	
	public boolean showMuteButton(){
		String value = props.getProperty("showMuteButton");
		return value == null ? true : false;
	}
	public void showMuteButton(boolean show){
		if ( !show)
		    props.setProperty("showMuteButton", "1");
		else 
			props.remove("showMuteButton");
		 
	}
	
	
 
 
	
	public boolean sharedJS(){
		String value = props.getProperty("SHARED_JS");
		return value == null ? false : true;
	}
	
	public void sharedJS(boolean show){
		if ( show)
		    props.setProperty("SHARED_JS", "1");
		else 
			props.remove("SHARED_JS");
		save();
	}
	
	 
	
	public boolean useCardSet(){
		String value = props.getProperty("CARDSET");
		return value == null ? false : true;
	}
	
	public void useCardSet(boolean show){
		if ( show)
		    props.setProperty("CARDSET", "1");
		else 
			props.remove("CARDSET");
		save();
	}
	 
	 
 
	
	
	public void hotphase_btn_width(int width){
		props.setProperty("hotphase_btn_width",  width + "");
		save();
	}
	
	public int hotphase_btn_width(){
		try{
			String w = props.getProperty("hotphase_btn_width");
			if ( w != null ){
				return Integer.parseInt(w);
			} 
		}catch(Exception ex){
	 	}
		return 20;
	}
	
	 
	public void hotphase_btn_border_wdith(int width){
		props.setProperty("hotphase_btn_border_wdith",  width + "");
		save();
	}
	
	public int hotphase_btn_border_wdith(){
		try{
			String w = props.getProperty("hotphase_btn_border_wdith");
			if ( w != null ){
				return Integer.parseInt(w);
			} 
		}catch(Exception ex){
	 	}
		return 1;
	}
	
	public void hotphase_btn_border_color(int rgb){
		props.setProperty("hotphase_btn_border_color",  rgb + "" );
		save();
	}
	
	public int hotphase_btn_border_color(){
		try{
			String w = props.getProperty("hotphase_btn_border_color");
			if ( w != null ){
				return Integer.parseInt(w);
			} 
		}catch(Exception ex){
	 	}
		return Color.GRAY.getRGB();
	}
	
	public void hotphase_btn_border_color_alpha(int alpha){
		props.setProperty("hotphase_btn_border_color_alpha",  alpha + "" );
		save();
	}
	
	public int hotphase_btn_border_color_alpha(){
		try{
			String w = props.getProperty("hotphase_btn_border_color_alpha");
			if ( w != null ){
				return Integer.parseInt(w);
			} 
		}catch(Exception ex){
	 	}
		return 60;
	}
	
	//
	public void hotphase_btn_color(int  rgb){
		props.setProperty("hotphase_btn_color",   rgb + "" );
		save();
	}
	
	public int hotphase_btn_color(){
		try{
			String w = props.getProperty("hotphase_btn_color");
			if ( w != null ){
				return Integer.parseInt(w);
			} 
		}catch(Exception ex){
	 	}
		return Color.gray.getRGB();
	}
	public void hotphase_btn_s_color(int  rgb){
		props.setProperty("hotphase_btn_s_color",   rgb + "" );
		save();
	}
	 
	
	public void save(){
		save( props );
	}
	
	 
    public void save(Properties props){
    	String home = CourseCreatingManager.sharedInstance.getCourseHome(courseName);
    	
		save(props, new File(home) , "exam.properties" );
    }
    
    public static void save(Properties props, File dir, String name){
		 File pfile = new File( dir, name);
		 if (! pfile.exists() )
			try {
				pfile.createNewFile();
			} catch (IOException e) {
			 	e.printStackTrace();
			}
		 OutputStream is = null;
		 try {
			 is = new FileOutputStream( pfile );
			 props.store(is, "");
		} catch (Exception e) {
			 e.printStackTrace();
		} finally{
			if ( is != null)
				try {
					is.close();
				} catch (IOException e) {
				 	e.printStackTrace();
				}
		}
	}
    
    
    public List<String> getRecentFile(){
    	String info = props.getProperty("RecentFiles");
    	List<String> files;
    	if ( info == null ){
    		files = new ArrayList<String>();
    	} else {
    		 Gson gson = new Gson();
    		 files = gson.fromJson(info, ArrayList.class);
    	}
    	return files;
    }
    
    public void updateRecentFile(String filename){
    	List<String> files = getRecentFile();
    	if(files.contains(filename)){
    		files.remove(filename);
    	}
    	if( files.size() > 10 ){ //10 is the max capability
    		files.remove(files.size()-1);
    	}
    	files.add(0, filename);
    	Gson gson = new Gson(); 
   	   props.setProperty("RecentFiles",  gson.toJson(files)); 
 	   save();
    }
    
    
	
    public  Properties loadProperties( ){
         String home = CourseCreatingManager.sharedInstance.getCourseHome(courseName);
    	 return loadProperties( new File(home)  , "exam.properties"); 
	}
    
	public static Properties loadProperties(File dir, String name){
		 Properties props = new Properties();
		 
		 File pfile = new File( dir , name);
		 if ( pfile.exists() ){
			 InputStream is = null;
			 try {
				 is = new FileInputStream( pfile );
				 props.load(is);
			} catch (Exception e) {
				 e.printStackTrace();
			} finally{
				if ( is != null)
					try {
						is.close();
					} catch (IOException e) {
					 	e.printStackTrace();
					}
			}
		 }
		 return props;
	}
	 
   

}
