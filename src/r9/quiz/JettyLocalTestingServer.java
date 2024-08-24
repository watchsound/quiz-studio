package r9.quiz;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.*;

import r9.quiz.util.ImageUtil;
 

public class JettyLocalTestingServer {

	public static interface Callback {
		void info(boolean needRestart);
	}
	
	 public static final int PORT = 8081;
	    public static final String HOST = "http://127.0.0.1";
		
		 
		public static JettyLocalTestingServer getInstance(){
			if( instance == null){
				 instance = new JettyLocalTestingServer();
			}
			return instance;
		}
		
		private static  JettyLocalTestingServer instance; 
		
		private String project = "";
		Server server = null;
		private JettyLocalTestingServer(){
			
		}
		public static URI getURI( String project, String filename ){
			try { 
				
				return new URI(HOST + ":" + PORT + "/export/" +  URLEncoder.encode(project, "utf-8")  + "/" +   filename +   "?asynctoken=" + ImageUtil.createUID()  );
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		public void runServer(String project, Callback callback){
			if( server != null){
				if( this.project.equals( project )){
					callback.info(false);
					return;
				}
				try {
					server.stop();
				} catch (Exception e) {
					 e.printStackTrace();
				}
				server.destroy();
				server = null; 
			}
			this.project = project;
			
			server = new Server(PORT);  
			ResourceHandler resourceHandler = new ResourceHandler();
		//	resourceHandler.setResourceBase(isTempDir? Settings.TEMP_DIR.getAbsolutePath() : Settings.TEMP_RESTORE_DIR.getAbsolutePath());
			resourceHandler.setResourceBase( R9SystemSetting.sharedInstance.getResourceDir().getAbsolutePath() );
					//CourseCreatingManager.sharedInstance.getCourseFolder()  );
		 	resourceHandler.setDirectoriesListed(true);
			server.setHandler(resourceHandler);
			
			callback.info(true);
			try {
				server.start();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					server.stop();
				} catch (Exception e1) { 
				}
				try {
					server.destroy();
				} catch (Exception e1) { 
				} 
				server = null;   //falsed
			}
			
		}
}
