package r9.quiz;
 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import r9.quiz.cards.HtmlTemplate;
import r9.quiz.cards.HtmlTemplateList;
import r9.quiz.script.ScripManager;
import r9.quiz.util.ImageUtil;
import r9.quiz.util.OSUtil;
import r9.quiz.util.Utils;
 
public class R9SystemSetting {
	//public static final String R9Studio = "R9Studio_2";
	public static final String R9TestStudio = "R9QuizStudio";
 	public static final Locale LOCALE =   Locale.SIMPLIFIED_CHINESE; //new Locale("zh","CN");
 //	public static final Locale LOCALE = new Locale("en", "US");
	   
    public static final String KIND =           "kind";
    public static final String FONT =           "font";
    public static final String PATTERN =        "PATTERN";
    public static final String PALETTE =        "PALETTE";
    public static final String FILEPATH =       "filepath";
    public static final String NAME =           "name";
    public static final String STATIC_LIST =    "STATIC_LIST";
    public static final String SYMBOLSET =      "SYMBOLSET";


    public static final String HTML_TEMPLATES =      "html_templates";
 
    
    
    public static final R9SystemSetting sharedInstance = new R9SystemSetting();
    
    private File resourceDir;
   
    private final File imageDir;
    
    private final File thirdPartyDir;
    
    private static R9SystemSetting _db;
    private File symbolsDir;
    
    private final File tempDir;
    
    private final File createdFilesDir;
    
    private final File htmlTempDir;
    
    Properties props;
	
	 
	public Properties getProperties(){
		return props;
	}
    private R9SystemSetting() {
    
        resourceDir = new File(OSUtil.getBaseStorageDir(System.getProperty("r9.test.basedirname",R9SystemSetting.R9TestStudio)));
       if ( !resourceDir.exists() )
          resourceDir.mkdir();
      
      	props =  loadProperties();  
       
       htmlTempDir = new File(resourceDir, HTML_TEMPLATES);
       if ( !htmlTempDir.exists() )
    	   htmlTempDir.mkdir();
       
        symbolsDir = new File(resourceDir,"symbols");
        if ( !symbolsDir.exists() )
          symbolsDir.mkdir();
        
        imageDir = new File(resourceDir,"image");
        if ( !imageDir.exists() )
          imageDir.mkdir();
        
        tempDir = new File(resourceDir,"temp");
        if ( !tempDir.exists() )
           tempDir.mkdir();

        createdFilesDir  = new File(resourceDir,"export");
        if ( !createdFilesDir.exists() )
           createdFilesDir.mkdir();
        
        thirdPartyDir = new File( resourceDir, "third_party");
        if ( !thirdPartyDir.exists() ){
        	thirdPartyDir.mkdir();
        	File currentLoc =  new File("./third_party");
        	if( currentLoc.exists() ){
        		try {
					Utils.copyFolder(currentLoc, thirdPartyDir); 
				} catch (IOException e) {
				 	e.printStackTrace();
				 	 try {
		        		 File f = new File(thirdPartyDir,"bootstrap.min.js");
		        		 if(! f.exists())
		                   Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("bootstrap.min.js"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =   new File(thirdPartyDir,"bootstrap.min.css");
		        		 if(! f.exists())
		                   Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("bootstrap.min.css"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =   new File(thirdPartyDir,"jquery-1.11.3.min.js");
		        		 if(! f.exists())
		                   Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("jquery-1.11.3.min.js"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =   new File(thirdPartyDir,"summernote.min.js");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("summernote.min.js"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =  new File(thirdPartyDir,"summernote.css");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("summernote.css"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =  new File(thirdPartyDir,"r9quiz.css");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("r9quiz.css"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =  new File(thirdPartyDir,"survey.jquery.min.js");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("survey.jquery.min.js"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =  new File(thirdPartyDir,"survey.min.css");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("survey.min.css"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 
		        	 try {
		        		 File f =  new File(thirdPartyDir,"VideojsR9.js");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("VideojsR9.js"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =  new File(thirdPartyDir,"videojs.min.js");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("videojs.min.js"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =  new File(thirdPartyDir,"videojs.min.css");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("videojs.min.css"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =  new File(thirdPartyDir,"videojs-contrib-r9.min.js");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("videojs-contrib-r9.min.js"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
		        	 try {
		        		 File f =  new File(thirdPartyDir,"videojs-contrib-r9.css");
		        		 if(! f.exists())
		                    Utils.copyStreamToFile(ScripManager.class.getResourceAsStream("videojs-contrib-r9.css"),f);
		             } catch (Exception ex) {
		                 ex.printStackTrace();
		             }
				}
        		
        		try {
					File templateSrc = new File(currentLoc, "html_templates");
					File templateTarget = new File(resourceDir, "html_templates");
					Utils.copyFolder(templateSrc, templateTarget);
				} catch (IOException e) {
				   e.printStackTrace();
				}
        		
        	}
        }
        	
      
    }
     
    public File getImageDir(){
    	return imageDir;
    }
    
    

    public File getThirdPartyDir() {
		return thirdPartyDir;
	}

	 
 

    public File getResourceDir() {
		return resourceDir;
	}

	public File getSymbolsDir() {
		return symbolsDir;
	}

	public File getCreatedFilesDir() {
		return createdFilesDir;
	}
	
	public String getLastProjectName() {
		return props.getProperty("getLastProjectName");
	}
	public void setLastProjectName(String projectName) {
	   props.setProperty("getLastProjectName", projectName);
	   save();
	}

	public String[] getCssFilesInArray(){
		 List<String> result = getCssFiles();
		 String[] sresult = new String[result.size()];
		 for(int i = 0;i < result.size(); i++)
			 sresult[i] = result.get(i);
		 return sresult;
	}
	public List<String> getCssFiles(){
		 return getFiles(false);
	}
	public void setCssFiles(List<String> jsfiles){
		StringBuilder sb = new StringBuilder();
		for(String f : jsfiles) {
			sb.append("|" + f);
		}
		props.setProperty("cssfilelist", jsfiles.isEmpty()? "" : sb.substring(1));   
		save();
	}  
	public void setJsFiles(List<String> jsfiles){
		StringBuilder sb = new StringBuilder();
		for(String f : jsfiles) {
			sb.append("|" + f);
		}
		props.setProperty("jsfilelist", jsfiles.isEmpty()? "" : sb.substring(1));   
		save();
	}
	
	public List<String> reorderJsFile(Collection<String> jsfiles){
		List<String> files = new ArrayList<String>();
		List<String> ordered = getJsFiles();
		for(String o : ordered) {
			if( jsfiles.contains(o) ) {
				files.add(o);
			}
		}
		return files;
	}
	
	public List<String> getJsFiles(){
		return getFiles(true);
	}
	private List<String> getFiles(boolean isJsFile){
		List<String> filesInDisk = getFilesFromFileSystem(isJsFile);
		List<String> jsfiles = new ArrayList<String>();
		String value = isJsFile ? props.getProperty("jsfilelist") : props.getProperty("cssfilelist");
	 	if( value == null ) { 
	//		if( isJsFile ) setJsFiles(filesInDisk);
	//		else  setCssFiles(filesInDisk);
			return filesInDisk;
	 	}
		for(String v :  value.split("\\|")) {
			if( !jsfiles.contains(v))
			    jsfiles.add(v);
			filesInDisk.remove(v);
		}
		for( String t : filesInDisk) {
			if( !jsfiles.contains(t))
		       jsfiles.add(t);
		}
		return jsfiles;
	}
		
	public String[] getJsFilesInArray(){
		 List<String> result = getJsFiles();
		 String[] sresult = new String[result.size()];
		 for(int i = 0;i < result.size(); i++)
			 sresult[i] = result.get(i);
		 return sresult;
	}
	private List<String> getFilesFromFileSystem(boolean isJsFile){
		 List<String> result = new ArrayList<String>();
		 String jquery = "";
		 for(String f : this.thirdPartyDir.list()){
			 if(isJsFile && f.endsWith(".js")) {
				 if( f.startsWith("jquery")) {
					 jquery = f;
					 continue;
				 }
			    result.add(f);
			 }
			 if(!isJsFile && f.endsWith(".css"))
				  result.add(f);
		 }
		 if( jquery.length() > 0)
			 result.add(0, jquery);
		 return result;
	}
	public File getTempDir() {
		return tempDir;
	}
	public String getTemplateById(String tid){
		HtmlTemplateList list = getHtmlTemplateMetaData();
		HtmlTemplate t = list.getTemplate(tid);
		if( t == null) return "";
		File f = new File(this.htmlTempDir, t.getFileName());
		if( !f.exists() ) return "";
		return this.fileToString(f);
	}
	
	public File getHtmlTemplateDir(){
		return this.htmlTempDir;
	}
	public HtmlTemplateList getHtmlTemplateMetaData(){
		File f = new File( this.htmlTempDir, "metadata.json");
		if( f.exists() ){
			try{
				String content =  R9SystemSetting.fileToString(f);
				Gson gson = new GsonBuilder().excludeFieldsWithModifiers( java.lang.reflect.Modifier.TRANSIENT | java.lang.reflect.Modifier.STATIC).create(); 
				return gson.fromJson(content, HtmlTemplateList.class);
			}catch(Exception e){ } 
		}  
		return new HtmlTemplateList();
	}
	public void saveHtmlTemplateMetaData(HtmlTemplateList data){
		if( data == null) return;
		data.cleanup();
		File f = new File( this.htmlTempDir, "metadata.json");
		if( f.exists() )
			f.delete();
		{
			try{
				Gson gson = new GsonBuilder().excludeFieldsWithModifiers( java.lang.reflect.Modifier.TRANSIENT | java.lang.reflect.Modifier.STATIC).create();
				String content = gson.toJson(data);
				stringToFile(content, f);
			}catch(Exception e){ } 
		}   
	}
	   public static String fileToString(File file)  {
	        try{
	         	 return fileToString( new FileInputStream(file) );
	        }catch(Exception ex){
	        	
	        } 
	        return "";
	    }
	    
	    
	    
	    public static String fileToString(InputStream in) throws IOException {
	        Reader reader = new InputStreamReader(in, "UTF-8");
	        StringWriter writer = new StringWriter();
	        char[] buf = new char[1024];
	        while(true) {
	            int n = reader.read(buf);
	            if(n == -1) {
	                break;
	            }
	            writer.write(buf,0,n);
	        }
	        return writer.toString();
	    }
	    
	    
	    public static void stringToFile(String text, File file) throws IOException {
	        FileWriter writer = new FileWriter(file);
	        StringReader reader = new StringReader(text);
	        char[] buf = new char[1000];
	        while(true) {
	            int n = reader.read(buf,0,1000);
	            if(n == -1) {
	                break;
	            }
	            writer.write(buf,0,n);
	        }
	        writer.flush();
	        writer.close();
	    }
	    

	public static void copyFileToFile(File srcfile, File outfile) throws IOException {
        FileInputStream in = new FileInputStream(srcfile);
        FileOutputStream out = new FileOutputStream(outfile);
        byte[] buf = new byte[1024];
        while(true) {
            int n = in.read(buf);
            if(n == -1) {
                break;
            }
            out.write(buf,0,n);
        }
        in.close();
        out.close();
    }

	public static void copyStreamToFile(InputStream in, File outfile) throws IOException {
        FileOutputStream out = new FileOutputStream(outfile);
        byte[] buf = new byte[1024];
        while(true) {
            int n = in.read(buf);
            if(n == -1) {
                break;
            }
            out.write(buf,0,n);
        }
        in.close();
        out.close();
    }

    public static R9SystemSetting getInstance() {
        if(_db == null) {
            _db = new R9SystemSetting();
        }
        return _db;
    } 

    
    
    public  Properties loadProperties( ){
		 return loadProperties( resourceDir, "r9class.properties"); 
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
				//u.p(e);
			} finally{
				if ( is != null)
					try {
						is.close();
					} catch (IOException e) {
						//u.p(e);
					}
			}
		 }
		 return props;
	}
	 
  
	 
	public void save(){
		save( props );
	}
	
	 
   public void save(Properties props){
		save(props, resourceDir , "r9class.properties" );
   }
   
   public static void save(Properties props, File dir, String name){
		 File pfile = new File( dir, name);
		 if (! pfile.exists() )
			try {
				pfile.createNewFile();
			} catch (IOException e) {
				//u.p(e);
			}
		 OutputStream is = null;
		 try {
			 is = new FileOutputStream( pfile );
			 props.store(is, "");
		} catch (Exception e) {
			//u.p(e);
		} finally{
			if ( is != null)
				try {
					is.close();
				} catch (IOException e) {
				//	u.p(e);
				}
		}
	}
   
	public static BufferedImage getToolBufferedImageIcon(String iconName){
		URL url = R9SystemSetting.class.getResource( iconName);
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
		URL url = R9SystemSetting.class.getClassLoader().getResource("r9/quiz/" + iconName);
		try {
			BufferedImage simage = ImageIO.read( url );
			if( size <=0 ) size = 16;
			return new ImageIcon(ImageUtil.resizeImage(simage, size, size)); 
		} catch (Exception e) {
			 
		}
	  	return null;
	}
}
