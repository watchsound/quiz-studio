package r9.quiz;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder; 
 
import r9.quiz.cards.CardHtmlPage;
import r9.quiz.cards.CardPage;
import r9.quiz.cards.Exam;  
import r9.quiz.cards.PageAdapter;
import r9.quiz.surveyjs.Question.ChoicesOrderType;
import r9.quiz.surveyjs.Question.QuestionType;
import r9.quiz.surveyjs.Quiz.ClearInvisibleValuesType;
import r9.quiz.surveyjs.Quiz.ShowProgressBarType;
import r9.quiz.surveyjs.Quiz.ShowQuestionNumbersType;
import r9.quiz.surveyjs.Quiz.ShowTimerPanelModeType;
import r9.quiz.surveyjs.Quiz.ShowTimerPanelType;
import r9.quiz.surveyjs.gson.*;
import r9.quiz.util.EncodingUtil;
import r9.quiz.util.ImageUtil;
import r9.quiz.util.ImageUtil.ImageWrapper;

public class CourseCreatingManager {
	static final int BUFFER = 2048;
	public static final CourseCreatingManager sharedInstance = new CourseCreatingManager();

	private Exam exam;

	private Image coverImage;
	private Image backgroundImage;
	
	
	private CardPage currentPage;

	private CourseCreatingManager() {

		exam = new Exam();
	}

	
	public CardPage getPreviousPage(){
		if( currentPage == null)
			return null;
		int index = exam.getPages().indexOf(currentPage);
		if( index > 0) 
			return exam.getPages().get(index -1);
		else
			return null;
	}
	
	public CardPage getCurrentPage() {
		return currentPage;
	}



	public void setCurrentPage(CardPage currentPage) {
		this.currentPage = currentPage;
	}



	public void save() { 
        		String filename = getCourseFolder() + File.separator + "cardset.json";
        		//mapping cardset
        		for(CardPage card : exam.getCardList())
        		{
        			 
        		}
        		
        		save(filename, exam);
        		filename = getCourseFolder() + File.separator + "cardset.obj";
        		saveObject(filename, exam);
        		 R9JsonIOHandler handler = new R9JsonIOHandler();
       	 	    handler.save( exam, new File(getCourseFolder() + File.separator + "exam.r9json"));
       
        		new HtmlCodeRender( ).preview(exam, true);
        		R9SystemSetting.sharedInstance.setLastProjectName(exam.getName());
	}

	public void save(String filename, Exam exam ) {

		//normalize data model 
		for( CardPage card : exam.getCards()){
	 
		}  
		saveJsonMetaDataObject(filename, exam);
		

		zipR9ProjectFile();
		coverImage = null;
		backgroundImage = null;
	 
	}
	public void saveObject(String filename, Exam obj){
		ObjectOutputStream oo = null;
		try{
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			oo = new ObjectOutputStream(new FileOutputStream(file));
			 oo.writeObject(obj);
		}catch(Exception ex){
			
		}finally{
			if( oo != null ){
				try {
					oo.close();
				} catch (IOException e) { 
				}
			}
		}
		
	}
	public  Gson getGson(){
		return new GsonBuilder().registerTypeAdapter(CardPage.class, new PageAdapter())
				.registerTypeAdapter(ChoicesOrderType.class, new ChoicesOrderTypeHandler())
				.registerTypeAdapter(ClearInvisibleValuesType.class, new ClearInvisibleValuesTypeHandler())
				.registerTypeAdapter(QuestionType.class, new QuestionTypeHandler())
				.registerTypeAdapter(ShowProgressBarType.class, new ShowProgressBarTypeHandler())
				.registerTypeAdapter(ShowQuestionNumbersType.class, new ShowQuestionNumbersTypeHandler())
				.registerTypeAdapter(ShowTimerPanelModeType.class, new ShowTimerPanelModeTypeHandler())
				.registerTypeAdapter(ShowTimerPanelType.class, new ShowTimerPanelTypeHandler())
				.excludeFieldsWithModifiers( java.lang.reflect.Modifier.TRANSIENT | java.lang.reflect.Modifier.STATIC).create();
	}
	
	public void saveJsonMetaDataObject(String filename, Object obj) {
 

		Gson gson = getGson();

		  
		String jsonString = gson .toJson(  obj );
		System.out.println(jsonString);
		try {
			System.out.println(filename);
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF8"));

			out.write(jsonString);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
	public File saveHtmlTempForTesting(String content){
		try {
			System.out.println( content );
			String filename = ImageUtil.createUID(8) + ".html";
			File file = new File( this.getCourseTempFolder() + File.separator + filename);
			if (!file.exists()) {
				file.createNewFile();
			}

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF8"));

			out.write(content);
			out.close();
			
			return file;
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return null;
	}

	public boolean load(String projectName) {
		if (projectName.endsWith(".mm"))
			projectName = projectName
					.substring(0, projectName.lastIndexOf("."));
		exam = null;
		createCourseFolder(projectName);
		String projectHomePath = getCourseFolder(projectName);
		try {
			R9JsonIOHandler handler = new R9JsonIOHandler();
			exam = (Exam) handler.read(new File(projectHomePath + File.separator  + "exam.r9json") ); 

		} catch ( Exception e) {
		 	System.err.println(" exam.r9json open failed;;;;;; " +e.getMessage());
		 	try {
				String filename =projectHomePath + File.separator  + "cardset.obj" ; 
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
				exam = (Exam) ois.readObject(); 
				 ois.close(); 		 		
		 	}catch(Exception ex) {
		 		System.err.println("  cardset.obj open failed;;;;;; " + ex.getMessage());
		 		return false;
		 	} 
		}
		
//		try {
//			String jsonstr = readFile(projectHomePath + File.separator
//					 + "cardset.json", Charset.forName("UTF8"));
//			exam = getGson().fromJson(jsonstr, Exam.class);
//
//		} catch ( Exception e) {
//			// TODO Auto-generated catch block
//			System.err.println(e.getMessage());
//			try {
//				String jsonstr = readFile(projectHomePath + File.separator
//						+ "cardset.json", Charset.forName("UTF8"));
//				exam = getGson().fromJson(jsonstr, Exam.class);
//
//			} catch ( Exception ex) {
//			 	return false;
//			}
//		}
		exam.setName(projectName);
        //clear temp folder
		String tempFolder = this.getCourseTempFolder();
		File file = new File(tempFolder);
		if( file.exists() )
			file.delete();
		file.mkdir();
		return true;
		//r9.quiz.curFreeMindAppInstance.hookCardSetPreviewPane();
	}
	
	 
	
	 
	
	public List<SavedCardMetaInfo> getAllSavedCardMetaInfo(){
		List<SavedCardMetaInfo>  cardMetaInfoList = new ArrayList<SavedCardMetaInfo>();
		File  savedRepository = new File( getHomeSystemFolder() + File.separator + "saved_cards" );
		if (! savedRepository.exists() ){
			return cardMetaInfoList;
		}
		for(File file : savedRepository.listFiles()){
			if ( file.isFile() ){
				String name = file.getName();
				if ( name.endsWith(".jpg" ) ){
					SavedCardMetaInfo info = new SavedCardMetaInfo();
					info.imageFile = file.getAbsolutePath();
					info.uuid = name.replace(".jpg", "");
					cardMetaInfoList.add(info);
				}
			}
		}
		
		return cardMetaInfoList;
	}
	
	public void deleteSavedCardFromSystem(SavedCardMetaInfo cardInfo){
		File  savedRepository = new File( getHomeSystemFolder() + File.separator + "saved_cards" );  
		File outFilename =  new File( savedRepository.getAbsolutePath() +  File.separator + cardInfo.uuid + ".jpg" );
		if ( outFilename.exists() )
			outFilename.delete();
		File savedCardFolder = new File( savedRepository.getAbsolutePath()  + File.separator + cardInfo.uuid );
		if ( savedCardFolder.exists() )
			savedCardFolder.delete();
		
	}
	
	public void importCardForCurrentCard(SavedCardMetaInfo cardInfo ){
		File  savedRepository = new File( getHomeSystemFolder() + File.separator + "saved_cards" );    	
		CardPage card = this.getCurrentPage();
		File savedCardFolder = new File( savedRepository.getAbsolutePath()  + File.separator + cardInfo.uuid );
		CardPage  importedCard = null;
		try {
			String jsonstr = readFile(savedCardFolder.getAbsolutePath() +  File.separator + card.getUuid() + ".json",
					Charset.forName("UTF8"));
			importedCard = getGson().fromJson(jsonstr, CardPage.class);

		} catch (IOException e) {
		    e.printStackTrace();
		}
		if ( importedCard == null)
			return;
		
		String projectHomePath = getCourseFolder();
		
//		 String imageFileName = importedCard.getImage();
//		 if ( imageFileName!= null && imageFileName.length() > 0 ){
//			 card.setImage(imageFileName);
//			 if ( imageFileName.indexOf(".") < 0  )
//				 imageFileName = imageFileName + ".jpg";
//			 this.copyFile( new File(savedCardFolder.getAbsolutePath() + File.separator + imageFileName),
//			                new File(projectHomePath + File.separator + "image" + File.separator + imageFileName )  
//					);
//		 }
//		 
//		 String voiceFileName =  importedCard.getVoiceFileName();
//		 if ( voiceFileName!= null && voiceFileName.length() > 0 ){
//			 card.setVoiceFileName(voiceFileName);
//			 this.copyFile( new File(savedCardFolder.getAbsolutePath() + File.separator + voiceFileName),
//					 new File(projectHomePath + File.separator + voiceFileName ) 
//					);
//		 }
//		 String videoFileName = importedCard.getVideoFileName();
//		 if ( videoFileName!= null && videoFileName.length() > 0 ){
//			 card.setVideoFileName(videoFileName);
//			 this.copyFile( new File(savedCardFolder.getAbsolutePath() + File.separator + videoFileName),
//					 new File(projectHomePath + File.separator + videoFileName )  
//					);
//		 }
//		 String jsonHtmlFile = importedCard.getHtmlFileName();
//		 if ( jsonHtmlFile!= null && jsonHtmlFile.length() > 0 ){
//			 card.setHtmlFileName(jsonHtmlFile);
//			 if ( jsonHtmlFile.indexOf(".") < 0  )
//				  jsonHtmlFile = jsonHtmlFile + ".json";
//			 this.copyFile( new File(savedCardFolder.getAbsolutePath() + File.separator + jsonHtmlFile),
//					 new File(projectHomePath + File.separator + jsonHtmlFile ) 
//					);
//		 }
		
		 
	 
//		 card.setContent( importedCard.getContent());
//		 card.setSlide1( importedCard.getSlide1());
//		 card.setSlide2( importedCard.getSlide2());
//		 card.setLevel( importedCard.getLevel());
//		 card.setName(importedCard.getName());
//		 card.setTaglist(importedCard.getTaglist());
//		 card.setUserNote(importedCard.getUserNote());
//		 card.setUuid(importedCard.getUuid());
//		 card.setCardType(importedCard.getCardType());
//		 String content = importedCard.getContent();
//		 if ( content == null || content.length() == 0 )
//			 content = card.getSlide1();
	 
	}
	
	public void exportCurrentCard(  ){
		File  savedRepository = new File( getHomeSystemFolder() + File.separator + "saved_cards" );
		if (! savedRepository.exists() ){
			try {
				savedRepository.createNewFile();
			} catch (IOException e) {
				 	e.printStackTrace();
			}
		}
		CardPage card = this.getCurrentPage();
		//save cached image 
//	    BufferedImage image = null;//ekitCore.getImagePreview(Constants.CARD_WIDTH/3, Constants.CARD_HEIGHT/3);
//	  
//		String outFilename =  savedRepository.getAbsolutePath() +  File.separator + card.getUuid() + ".jpg";
//		try {
//			ImageUtil.saveImageToFile(image, outFilename, 100);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		File savedCardFolder = new File( savedRepository.getAbsolutePath()  + File.separator + card.getUuid() );
		if (! savedCardFolder.exists() ){
			try {
				savedCardFolder.createNewFile();
			} catch (IOException e) {
				 	e.printStackTrace();
			}
		} 
		
		saveJsonMetaDataObject(  savedCardFolder.getAbsolutePath() +  File.separator + card.getUuid() + ".json", card);
		 
		
		String projectHomePath = getCourseFolder();
		
//		 String imageFileName = card.getImage();
//		 if ( imageFileName!= null && imageFileName.length() > 0 ){
//			 if ( imageFileName.indexOf(".") < 0  )
//				 imageFileName = imageFileName + ".jpg";
//			 this.copyFile(new File(projectHomePath + File.separator + "image" + File.separator + imageFileName ), 
//					 new File(savedCardFolder.getAbsolutePath() + File.separator + imageFileName));
//		 }
//		 
//		 String voiceFileName =  card.getVoiceFileName();
//		 if ( voiceFileName!= null && voiceFileName.length() > 0 ){
//			 this.copyFile(new File(projectHomePath + File.separator + voiceFileName ), 
//					 new File(savedCardFolder.getAbsolutePath() + File.separator + voiceFileName));
//		 }
//		 String videoFileName = card.getVideoFileName();
//		 if ( videoFileName!= null && videoFileName.length() > 0 ){
//			 this.copyFile(new File(projectHomePath + File.separator + videoFileName ), 
//					 new File(savedCardFolder.getAbsolutePath() + File.separator + videoFileName));
//		 }
//		 String jsonHtmlFile = card.getHtmlFileName();
//		 if ( jsonHtmlFile!= null && jsonHtmlFile.length() > 0 ){
//			 if ( jsonHtmlFile.indexOf(".") < 0  )
//				  jsonHtmlFile = jsonHtmlFile + ".json";
//			 this.copyFile(new File(projectHomePath + File.separator + jsonHtmlFile ), 
//					 new File(savedCardFolder.getAbsolutePath() + File.separator + jsonHtmlFile));
//		 }
//		
//		 String gifHtmlFile =  card.getGifstyleFileName();
//		 if ( gifHtmlFile!= null && gifHtmlFile.length() > 0 ){
//			 if ( gifHtmlFile.indexOf(".") < 0  )
//				 gifHtmlFile = gifHtmlFile + ".json";
//			 this.copyFile(new File(projectHomePath + File.separator + gifHtmlFile ), 
//					 new File(savedCardFolder.getAbsolutePath() + File.separator + gifHtmlFile));
//	
//		 }
		 
		
	}
	
//	public Set<String> loadSystemTagList() {
//		String projectHomePath = this.getHomeSystemFolder();
//	    Set<String> tagList = new HashSet<String>();
//		try {
//			String tagstr = readFile(projectHomePath + File.separator
//					 + "tag_repository.text", Charset.forName("UTF8")); 
//			String[] tags = tagstr.split(",");
//			Arrays.
//
//		} catch (IOException e) {
//		   e.printStackTrace();
//		}
//		
//	}
	
	public Image getSystemImage(String imageUuid)
	{
		if( imageUuid.indexOf(".") < 0)
			imageUuid = imageUuid + ".jpg";
		return getImage(this.getHomeSystemImageFolder(), imageUuid);
 	}
 
	public void zipR9ProjectFile() {
		String projectHomePath = getCourseFolder();
		try {
		   //copy temp image to image
//			for(Page card  : exam.getCardList()){
//				if( card.isImagePreview() && card.getImageForCreatingPreivew() != null){
//					try{
//					this.copyFile(new File( this.getCourseTempFolder() + "/" + card.getImageForCreatingPreivew() + ".jpg"),
//							      new File( this.getCourseImageFolder() + "/" + card.getImageForCreatingPreivew() + ".jpg"));
//					}catch(Exception e){
//						e .printStackTrace();
//					}
//				}
//			}
			
			  File directory = new File(projectHomePath);
	             File zipfile = new File (projectHomePath
	 					+ File.separator +  exam.getName() + ".r9");
	             String skipFile =    ".r9unencoding";
	             String skipFile2 =    ".r9";
				 zip(  directory,   zipfile,   skipFile, skipFile2); 
				  
				 
//             File directory = new File(projectHomePath);
//             File zipfile = new File (projectHomePath
// 					+ File.separator +  exam.getName() + ".r9unencoding");
//             String skipFile =    ".r9unencoding";
//             String skipFile2 =    ".r9";
//			 zip(  directory,   zipfile,   skipFile, skipFile2); 
//			 //encrpty 
//			 File encodingFile = new File (projectHomePath
//	 					+ File.separator +  exam.getName() + ".r9");
//			 EncodingUtil.encryptFile(zipfile, encodingFile, "leling12345678");
			 
			 
			 //remove preview image
			//copy temp image to image
//				for(Page card  : exam.getCardList()){
//					if( card.isImagePreview() && card.getImageForCreatingPreivew() != null){
//						try{
//						 File f =  new File( this.getCourseImageFolder() + "/" + card.getImageForCreatingPreivew() + ".jpg");
//						 f.delete();
//						}catch(Exception e){
//							e.printStackTrace();
//						}
//					}
//				}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

//	private void addFileToZip(ZipOutputStream out, File file) {
//		byte data[] = new byte[BUFFER];
//		try {
//			FileInputStream fi = new FileInputStream(file);
//			BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
//			ZipEntry entry = new ZipEntry(file.getName());
//			out.putNextEntry(entry);
//			int count;
//			while ((count = origin.read(data, 0, BUFFER)) != -1) {
//				out.write(data, 0, count);
//			}
//			origin.close();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	public void markDirty(){
		 
	}

	 
	static String readFile(String path, Charset encoding) throws IOException {
		//only for jdk1.7 and above
		//byte[] encoded = Files.readAllBytes(Paths.get(path));
		//return encoding.decode(ByteBuffer.wrap(encoded)).toString();
		InputStream is = new FileInputStream(  path );
	 	String str = IOUtils.toString(is, "UTF-8");
	 	is.close();
	 	return str;
	}

	public void rename(String newProjectName) {
		if (newProjectName.endsWith(".mm"))
			newProjectName = newProjectName.substring(0,
					newProjectName.lastIndexOf("."));
		String currentProjectName = exam.getName();
		exam.setName(newProjectName);
		String projectHomePath = getCourseFolder(newProjectName);
		File file = new File(currentProjectName);
		file.renameTo(new File(projectHomePath));
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Image getCoverImage() {
		if (coverImage == null && this.exam.getCoverImage() != null) {
			coverImage = (Image) this.getImage(this.exam.getCoverImage());
		}
		return coverImage;
	}

	public Image getBackgroundImage() {
		if (backgroundImage == null && this.exam.getBackgrdImage() != null) {
			backgroundImage = (Image) this.getImage(this.exam
					.getBackgrdImage());
		}
		return backgroundImage;
	}
	
	public String getCardSetBackground(){
		return this.exam.getBackgrdImage();
	}

	 
	public String getCurrentCourseName() {
		// String projectName =((MindMapController)
		// r9.quiz.curFreeMindAppInstance.getController()
		// .getModeController()).getMap().getName();
		String projectName = exam.getName();
		if (projectName == null || projectName.length() == 0) {
			projectName = "FIXME"; //FIXME
			exam.setName(projectName);
		}
		return projectName;
	}

	public String getCourseFolder() {
		return getCourseFolder(getCurrentCourseName());
	}

	public String getCourseImageFolder() {
		return getCourseImageFolder(getCurrentCourseName());
	}

	public String getCourseCssFolder() {
		return getCourseCssFolder(getCurrentCourseName());
	}

	public String getCourseJsFolder() {
		return getCourseJsFolder(getCurrentCourseName());
	}

	public String getCourseTempFolder() {
		return getCourseTempFolder(getCurrentCourseName());
	}
	
	public String getSystemHome(){
		File home =  R9SystemSetting.sharedInstance.getCreatedFilesDir();
		 
		return tryToCreateDirIfNotExists( home.getAbsolutePath() ); 
	}
 
	public static String tryToCreateDirIfNotExists(String path){
		File homeFile = new File( path );
		if (! homeFile.exists() ){
			homeFile.mkdirs();
		}
		return path;
	}

	public String getCourseHome(String courseName){
		return getSystemHome() + File.separator + courseName;
	}
	
	public String getCourseFolder(String courseName) {
		return tryToCreateDirIfNotExists( getCourseHome(courseName) );
	}

	public String getCourseImageFolder(String courseName) {
		//FIXME - for YKW,  we dont use sub-folder
		//return this.getCourseFolder();
		return  tryToCreateDirIfNotExists (getSystemHome() + File.separator + courseName
				+ File.separator + "image" );
	}

	public String getCourseCssFolder(String courseName) {
		return tryToCreateDirIfNotExists( getSystemHome() + File.separator + courseName
				+ File.separator + "css" );
	}

	public String getCourseJsFolder(String courseName) {
		return tryToCreateDirIfNotExists( getSystemHome() + File.separator + courseName
				+ File.separator + "script" );
	}

	public String getCourseTempFolder(String courseName) {
		return tryToCreateDirIfNotExists( getSystemHome() + File.separator + courseName
				+ File.separator + "tmp" );
	}

	public String getCourseScratchFolder(String courseName) {
		return tryToCreateDirIfNotExists( getSystemHome() + File.separator + courseName
				+ File.separator + "scratch" );
	}

	public String getCourseHtmlFolder(String courseName) {
		return tryToCreateDirIfNotExists( getSystemHome() + File.separator + courseName
				+ File.separator + "html" );
	}

	public String getHomeSystemFolder( ) {
		return  tryToCreateDirIfNotExists( getSystemHome() + File.separator + ".r9system" );
	}
	public String getHomeSystemImageFolder( ) {
		return tryToCreateDirIfNotExists ( getHomeSystemFolder( ) + File.separator + "images" );
	}
	
	public void createCourseFolder(String courseName) {
		if (courseName.endsWith(".mm"))
			courseName = courseName.substring(0, courseName.lastIndexOf("."));
        if( exam != null)
		    exam.setName(courseName);
		String projectHome = getCourseFolder(courseName);
		File file = new File(projectHome);
		if (!file.exists() || file.isFile()) {
			file.mkdir();
		}
//		String projectImage = getCourseImageFolder(courseName);
//		file = new File(projectImage);
//		if (!file.exists() || file.isFile()) {
//			file.mkdir();
//		}
//		String projectCss = getCourseCssFolder(courseName);
//		file = new File(projectCss);
//		if (!file.exists() || file.isFile()) {
//			file.mkdir();
//		}
//		String projectJs = getCourseJsFolder(courseName);
//		file = new File(projectJs);
//		 
//		String tempDir = getCourseTempFolder(courseName);
//		file = new File(tempDir);
//		if (!file.exists() || file.isFile()) {
//			file.mkdir();
//		}
//		String scratchDir = getCourseScratchFolder(courseName);
//		file = new File(scratchDir);
//		if (!file.exists() || file.isFile()) {
//			file.mkdir();
//		}
//		String htmlDir = getCourseHtmlFolder(courseName);
//		file = new File(htmlDir);
//		if (!file.exists() || file.isFile()) {
//			file.mkdir();
//		}
		//check the system folder
		String systemFolder = getHomeSystemFolder();
		file = new File(systemFolder);
		if (!file.exists() || file.isFile()) {
			file.mkdir();
		}
		//check the system folder
		String systemImageFolder = this.getHomeSystemImageFolder();
		file = new File(systemImageFolder);
		if (!file.exists() || file.isFile()) {
			file.mkdir();
		}
	}

	public Image getTempImage(String imageUUID) {
		String imageFolder = getCourseTempFolder(this.getCurrentCourseName());
		String filename = imageFolder + File.separator + imageUUID + ".jpg";
		return Toolkit.getDefaultToolkit().getImage(filename);
	}

	public void saveTempImage(String imageUUID, BufferedImage image) {
		String imageFolder = getCourseTempFolder(this.getCurrentCourseName());
		String imageType = ".jpg";
		try {
			ImageUtil.saveImageToFile(image, imageFolder + File.separator
					+ imageUUID + imageType, 100);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String saveImage(String imageUUID, BufferedImage image) {
		String imageFolder = getCourseFolder(getCurrentCourseName()); //getCourseImageFolder(this.getCurrentCourseName());
		String imageType = ".jpg";
		try {
			String filename = ImageUtil.saveImageToFile(image, imageFolder
					+ File.separator + imageUUID + imageType, 100);
			return filename;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	

	public String saveVoiceFile(File file, boolean updateCard) {
		String type = file.getName().substring(file.getName().lastIndexOf(".") +1);
		String fileName = saveFile(this.getCurrentCourseName(), file, type);
		if (fileName != null && updateCard) {
			CardPage card = getCurrentPage();
		    card.setVoiceFileName(fileName);
			markDirty();
		}
		return fileName;
	}

	public void deleteVoice() {
		CardPage card = getCurrentPage();
		if( card == null)
			return;
		String fileName = card.getVoiceFileName();
		if (fileName == null)
			return;
		String targetFile = getCourseFolder(getCurrentCourseName()) + File.separator + fileName;
		
		File file = new File(targetFile);
		file.delete();
		card.setVoiceFileName(null);
		 markDirty();
	}

	 

 
	 

	public void copyFolder(File source, File to) {
		for (File f : source.listFiles()) {
			File newfile = new File(to.getAbsolutePath() + File.separator
					+ f.getName());
			this.copyFile(f, newfile);
		}
	}

  
  

	public Image getImage(String imageName) {
		if( imageName.indexOf(".") < 0)
			imageName = imageName + ".jpg";
		Image image = getImage(getCurrentCourseName(), imageName);
		if (image == null)
			image = getImage(this.getCourseTempFolder(), imageName);
	 
		return image;
	}
	public Image getImageIncludeSystemOnes(String imageName) {
	    Image image = getImage(  imageName);
		if (image == null){
			image = getImage(this.getCourseTempFolder(), imageName);
			String filename = getHomeSystemImageFolder() + File.separator + imageName;
			image = Toolkit.getDefaultToolkit().getImage(filename);
		}
		return image;
	}

	public void deleteImage(String imageName) {
		if( imageName == null) return;
		String imageFolder = getCourseFolder(getCurrentCourseName()); //getCourseImageFolder(this.getCurrentCourseName());
		String imageType = ".jpg";
		String filename = imageFolder + File.separator + imageName;
		if (imageName.indexOf(".") < 0)
			filename += imageType;

		File file = new File(filename);
		if (file.exists()) {
			file.delete();
		} 
	}
	public void deleteTopLevelImage(String imageName) {
		if( imageName == null) return;
		String imageFolder = getCourseFolder(this.getCurrentCourseName());
		String imageType = ".jpg";
		String filename = imageFolder + File.separator + imageName;
		if (imageName.indexOf(".") < 0)
			filename += imageType;

		File file = new File(filename);
		if (file.exists()) {
			file.delete();
		} 
	}
	
	public void deleteVoice(String voiceFileName) {
		if ( voiceFileName == null || voiceFileName.length() == 0 )
			return;
		String courseFolder = this.getCourseFolder();
		String voiceType = ".mp3";
		String filename = courseFolder + File.separator + voiceFileName;
		if (voiceFileName.indexOf(".") < 0)
			filename += voiceType;

		File file = new File(filename);
		if (file.exists()) {
			file.delete();
		} else {
			if (voiceFileName.indexOf(".") < 0){
				filename = filename.replace(".mp3", "wav");
			    file = new File(filename);
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}
	
	public void copyImageToSystemFolder(String imageName)
	{
		String imageFolder = getCourseFolder(getCurrentCourseName()); //getCourseImageFolder();
		if ( imageName.indexOf(".") < 0 ){
			imageName = imageName + ".jpg";
		}
		String originalImageFile = imageFolder + File.separator + imageName;
		
		String destImageFile = this.getHomeSystemImageFolder() + File.separator + imageName;
		
		File destFile =  new File(destImageFile);
		if ( destFile.exists() )
			return;
		
		this.copyFile(new File(originalImageFile), destFile);
	}
	
	public void copyImageFromSystemFolder(String imageName)
	{
		String imageFolder = getCourseFolder(getCurrentCourseName()); //getCourseImageFolder();
		if ( imageName.indexOf(".") < 0 ){
			imageName = imageName + ".jpg";
		}
		String destImageFile = imageFolder + File.separator + imageName;
		
		String originalImageFile = this.getHomeSystemImageFolder() + File.separator + imageName;
		
		File destFile =  new File(destImageFile);
		if ( destFile.exists() )
			return;
		
		this.copyFile(new File(originalImageFile), destFile);
	}
	

	private Image getImage(String projectName, String imageName) {
		String imageFolder = getCourseFolder(getCurrentCourseName()); //getCourseImageFolder(projectName);
		if ( imageName.indexOf(".") < 0 ){
			imageName = imageName + ".jpg";
		}
		String filename = imageFolder + File.separator + imageName;
		return Toolkit.getDefaultToolkit().getImage(filename);
	}

	public String saveCoverOrBackgrdImage(String filename, boolean isCover) {
		String imageFolder = getCourseFolder(getCurrentCourseName()); //getCourseImageFolder(getCurrentCourseName());
		String imageType = filename.substring(filename.lastIndexOf("."));
		String imageUUID = ImageUtil.createCompactUID();
		String outFilename = imageFolder + File.separator + imageUUID
				+ imageType;
		BufferedImage image = null;
		try {
			image = ImageUtil.copyAndAdjustImage(filename, outFilename, true);

			if (isCover) {
				String iconFilename = imageFolder + File.separator + "Default"
						+ imageType;
				ImageUtil.createThumbnail(outFilename, 44, 44, 100,
						iconFilename);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (image != null) {
			if (isCover) {
				exam.setCoverImage(imageUUID + imageType);
				this.coverImage = image;
			} else {
				exam.setBackgrdImage(imageUUID + imageType);
				this.backgroundImage = image;
			}
			markDirty();
			return imageUUID + imageType;
		} else {
			return null;
		}
	}
	
	public void importImageFileForCard(File file, String imageType , int order, boolean inImageFolder) {
		String imageFolder = inImageFolder?  getCourseImageFolder(getCurrentCourseName()) : getCourseFolder();
	//	String imageUUID = ImageUtil.createUID();
		String outFilename = imageFolder + File.separator + file.getName();
		 
		this.copyFile(file , new File( outFilename));
		CardPage card = getCurrentPage();
		card.setImage(order, file.getName());
		 
	//	card.setOriginalImage(imageUUID + imageType);
  
		markDirty(); 
	}
	public File importImageFile(File file,   boolean inImageFolder) {
		String imageFolder = inImageFolder?  getCourseImageFolder(getCurrentCourseName()) : getCourseFolder();
	//	String imageUUID = ImageUtil.createUID();
		String outFilename = imageFolder + File.separator + file.getName();
		File outFile = new File( outFilename);
		this.copyFile(file , outFile);
		return outFile;
	}
	public void importHtmlImageFileForCard(File file, String imageType , int order, boolean inImageFolder) {
		String imageFolder = inImageFolder?  getCourseImageFolder(getCurrentCourseName()) : getCourseFolder();
	//	String imageUUID = ImageUtil.createUID();
		String outFilename = imageFolder + File.separator + file.getName();
		 
		this.copyFile(file , new File( outFilename));
		CardPage acard = getCurrentPage();
		if ( !(acard instanceof CardHtmlPage) ){
			return;
		}
		 
  
		markDirty(); 
	}
	
	public void deleteBackgroundForCurrentCard(){
//		Page card = getCurrentCard();
//	 	card.setOriginalImage("");
//        card.setImage("");
//        card.setImageFileName("");
//		markDirty(); 
	}
	

	public ImageWrapper importImageForCard(BufferedImage image,
			String imageType, boolean isBackground) {
		String imageFolder =getCourseFolder(getCurrentCourseName()); //getCourseImageFolder(getCurrentCourseName());
		String imageUUID = ImageUtil.createCompactUID();
		String outFilename = imageFolder + File.separator + imageUUID
				+ imageType;
		try {
			ImageUtil.saveImageToFile(image, outFilename, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		CardPage card = getCurrentPage();
	//	card.setOriginalImage(imageUUID + imageType);

		image = ImageUtil.resizeImage(image, Constants.CARD_WIDTH,
				Constants.CARD_HEIGHT, true, isBackground);
		imageUUID = ImageUtil.createUID();
		outFilename = imageFolder + File.separator + imageUUID + imageType;
		try {
			ImageUtil.saveImageToFile(image, outFilename, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ( isBackground ) {
//	    	card.setImageFileName(imageUUID + imageType);
		}

		markDirty();

		ImageWrapper wrapper = new ImageWrapper();
		wrapper.name = outFilename;
		wrapper.image = image;
		return wrapper;
	}

	public ImageWrapper importImageForCard(String filename, boolean isBackground, boolean forCurCard) {
		String imageFolder = getCourseFolder(getCurrentCourseName()); //getCourseImageFolder(getCurrentCourseName());
		String imageType = filename.substring(filename.lastIndexOf("."));
		String imageUUID = ImageUtil.createCompactUID();
		String outFilename = imageFolder + File.separator + imageUUID
				+ imageType;
		Image image = null;
		try {
			image = ImageUtil.copyAndAdjustImage(filename, outFilename,
					isBackground);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (image != null && forCurCard && isBackground) {
			CardPage card = getCurrentPage();
	//		card.setImageFileName(imageUUID + imageType);
			markDirty();
		}

		ImageWrapper wrapper = new ImageWrapper();
		wrapper.name = outFilename;
		wrapper.image = image;
		wrapper.uuid = imageUUID;
		// TODO : how to change to relative
		return wrapper;
	}
	public List<File> getAllImageFiles( ){
		return this.getAllImageFiles(this.getCurrentCourseName());
	}
	public List<File> getAllImageFiles(String courseName){
		List<File> files = new ArrayList<File>();
		for(File f : new File(getCourseFolder(courseName)).listFiles(new FileFilter(){ 
			@Override
			public boolean accept(File pathname) {
				String fname = pathname.getName();
				 return fname.endsWith(".jpg") || fname.endsWith(".png") ;
			}})){
			files.add(f);
		}
		return files;
	}

 
	private String saveFile(String courseName, File file, String extension) {
		String uuid = ImageUtil.createCompactUID();
		String targetFile = getCourseFolder(courseName) + File.separator + uuid
				+ "." + extension;
		File target = new File(targetFile);
		boolean success = copyFile(file, target);
		return success ? uuid + "." + extension : null;
	}

	public void writeStringToFile(String content, String outFileName) {
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outFileName), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			try {
				if (out != null)
					out.write(content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public boolean copyFile(File f1, File f2) {
		try {

			InputStream in = new FileInputStream(f1);

			// For Append the file.
			// OutputStream out = new FileOutputStream(f2,true);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		} catch (Exception ex) {
			System.out
					.println("copyFile :" + ex.getMessage() + " in the specified directory.");
		 
			return false;
		}
		return true;
	}

	public static void zip(File directory, File zipfile, String skipFile, String skipFile2) throws IOException {
		URI base = directory.toURI();
		Deque<File> queue = new LinkedList<File>();
		queue.push(directory);
		if ( zipfile.exists() )
			zipfile.delete();
		zipfile.createNewFile();
		OutputStream out = new FileOutputStream(zipfile);
		Closeable res = out;
		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			res = zout;
			while (!queue.isEmpty()) {
				directory = queue.pop();
				for (File kid : directory.listFiles()) {
					String name = base.relativize(kid.toURI()).getPath();
					if (kid.isDirectory()) {
						if ( kid.getName().endsWith("tmp"))
							continue;
						queue.push(kid);
						name = name.endsWith("/") ? name : name + "/";
						zout.putNextEntry(new ZipEntry(name));
					} else {
						if ( name.endsWith(skipFile) || name.endsWith(skipFile2) || name.endsWith("cardset.r9"))
							continue;
						zout.putNextEntry(new ZipEntry(name));
						copy(kid, zout);
						zout.closeEntry();
					}
				}
			}
		} finally {
			res.close();
		}
	}

	public static void unzip(File zipfile, File directory) throws IOException {
		ZipFile zfile = new ZipFile(zipfile);
		Enumeration<? extends ZipEntry> entries = zfile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File file = new File(directory, entry.getName());
			if (entry.isDirectory()) {
				file.mkdirs();
			} else {
				file.getParentFile().mkdirs();
				InputStream in = zfile.getInputStream(entry);
				try {
					copy(in, file);
				} finally {
					in.close();
				}
			}
		}
	}

	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = in.read(buffer);
			if (readCount < 0) {
				break;
			}
			out.write(buffer, 0, readCount);
		}
	}

	public static void copy(File file, OutputStream out) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			copy(in, out);
		} finally {
			in.close();
		}
	}

	public static void copy(InputStream in, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {
			copy(in, out);
		} finally {
			out.close();
		}
	}
	
	public static void copy(File filein, File fileout) throws IOException {
		InputStream in = new FileInputStream(filein);
		FileOutputStream out = new FileOutputStream(fileout);
		try {
			copy(in, out);
		} finally {
			in.close();
		}
	}
	 
	public  static class SavedCardMetaInfo  {
		public String  uuid;
		public String  imageFile;
		public BufferedImage image;
	}
}
