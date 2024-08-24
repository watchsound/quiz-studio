package r9.quiz;

   
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
  
 



import r9.quiz.cards.Exam;
import r9.quiz.cards.CardHtmlPage;
import r9.quiz.cards.CardPage;
import r9.quiz.cards.CardSurveyJsJumpPage;
import r9.quiz.cards.PageGroup;
import r9.quiz.surveyjs.Quiz;

public class HtmlCodeRender {

	public static void openWebpage(URI uri, String project) { 
		 
	 	new Thread(){
			public void run(){
				JettyLocalTestingServer.Callback callback = new JettyLocalTestingServer.Callback() {
					@Override
					public void info(boolean needRestart) {
						 if(needRestart){
							 try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								
							}
						 }
						 new Thread(){
							 public void run(){
								 Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
											: null;
									if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
										 try {
											desktop.browse(JettyLocalTestingServer.getURI(
													project,	   "demo.html" ));
										} catch (IOException e) {
											 
										}
									}
							 }
						 }.start();
					}
				};
				
				JettyLocalTestingServer.getInstance().runServer(project, callback); 
			}
		}.start();
		
		
		
		 
	}

	public static void openWebpage(URL url, String project) {
		try {
			openWebpage(url.toURI(), project);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
 
	public void  preview(Exam exam, boolean saveOnly){
		String htmlCode = renderHtmlCode( exam, false);
		File out = new File( CourseCreatingManager.sharedInstance.getCourseFolder(), "demo.html");
		if( out.exists() ) out.delete();
		try {
			R9SystemSetting.stringToFile(htmlCode, out);
			if(!saveOnly)
			    openWebpage( out.toURI(), CourseCreatingManager.sharedInstance.getCurrentCourseName());
		} catch (IOException e) {
			 e.printStackTrace();
		}
	}
	
	public String renderHtmlCode(Exam exam, boolean export){
		if( exam.getVideoSrc() != null && exam.getVideoSrc().length() > 0) {
			return this.renderHtmlCodeVideoBased(exam, export);
		} else {
			return this.renderHtmlCodeNoVideo(exam, export);
		}
	}
	public String renderHtmlCodeVideoBased(Exam exam, boolean export){
		exam.normalize();
		List<PageGroup> pgs = exam.getPageGroup();
		
		String libDir = export ?  exam.getLibDirectory() : "../../third_party/";
		StringBuilder sb = new StringBuilder();
		sb.append(" <!DOCTYPE HTML> ");
		sb.append(" <html>");
		sb.append(" <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width\"> ");
		sb.append("\n <link href=\"" + libDir+ "videojs.min.css\" rel=\"stylesheet\">");
		sb.append("\n <link href=\"" + libDir+ "videojs-contrib-r9.css\" rel=\"stylesheet\">");
		sb.append("\n <link href=\"" + libDir+ "r9quiz.css\" rel=\"stylesheet\">");
		boolean useBootstrap  = false;
		for(String css : exam.getAllCssFiles() ){
			sb.append("\n <link href=\"" + libDir + css + "\" rel=\"stylesheet\">");
			if( css.indexOf("bootstrap") >= 0)
				useBootstrap = true;
		}
		if( exam.hasSurveyJs() ){ 
			sb.append("\n <link href=\"" + libDir+ "survey.min.css\" rel=\"stylesheet\">"); 
		 	sb.append("\n <script src=\"" + libDir+ "jquery-1.11.3.min.js\"></script>");
	 		sb.append("\n <script src=\"" + libDir+ "survey.jquery.min.js\"></script>");
	 		sb.append("\n <script src=\"" + libDir+ "r9quiz.js\"></script>");
		}
		sb.append("\n    <script src=\"" + libDir+ "videojs.min.js\"></script>");
		sb.append(" \n   <script src=\"" + libDir+ "pagebus.js\"></script>");
		sb.append(" \n   <script src=\"" + libDir+ "videojs-contrib-r9.min.js\"></script>");
	
		//js file order matters!, we setup order for all js files in the system.
		List<String> orderedJs = R9SystemSetting.sharedInstance.reorderJsFile(exam.getAllJsFiles() );
		for(String js : orderedJs){
			if( exam.hasSurveyJs() && js.equals("jquery-1.11.3.min.js"))
				continue;
			sb.append("\n <script src=\"" + libDir + js + "\"></script>");
		}
		if( exam.isUseMathJax() ) {
		 	sb.append("\n <script src=\"https://polyfill.io/v3/polyfill.min.js?features=es6\"></script>");
		    sb.append("\n <script>  MathJax = {\n" + 
	     		"    tex: {inlineMath: [['$', '$'], ['\\\\(', '\\\\)']]}\n" + 
		    		"  }; </script> ");
	        sb.append("\n <script id=\"MathJax-script\"  src=\"https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-chtml.js\"></script>");
			 
	        sb.append("\n <script> function mathjax_create(){MathJax.typeset(); } </script>");
		} else {
			  sb.append("\n <script> function mathjax_create(){  } </script>");
		}
		
		sb.append(" </head>");
		sb.append(" <body>");
		sb.append(" <video  id=\"" + Constants.PLAYER_CONTAINER_ID +"\"  class=\"video-js\" ");//style=\"display: block; margin: auto\"" );
		sb.append("  controls  playsinline=\"true\" webkit-playsinline=\"true\" ");
		sb.append(" preload=\"auto\" ");
 		sb.append("		    height=\"" + Constants.VIDEO_WIDTH  + "px\" ");
 		sb.append("		    width=\"" + Constants.VIDEO_HEIGHT + "px\"");  
		sb.append("> "); 
		sb.append("      <source src=\"" +  exam.getVideoSrc()  +"\"></source> ");
		sb.append("  </video>"); 
		
		if(   exam.hasSurveyJsCode() ) {
			sb.append("<script>");
			sb.append( "var asciiChartSet_en2c = { " + 
			"   '&quot;' : '\"',   '&apos;': '\\'', '&sol;': '/', '&lt;': '<', '&gt;':'>', '&lsqb;': '[',  '&bsol;': '\\\\', '&rsqb;': ']', '&lcub;': '{',  '&rcub;':'}' ,  '&nl;': '\\n'   " + 
		 	"} " +  
			" \n;function entity2html(input){\n" + 
			"   var output = input.replace(/(&.+?;)/g, function (matched) {\n" + 
			"    var rs = asciiChartSet_en2c[matched];\n" + 
			"    return rs == undefined ? matched : rs;\n" + 
			"  });\n" + 
			"  console.log(input);  \n" + 	
			"  console.log(output);  \n" + 	
			"  return output; \n" + 	
			"}" );
			sb.append("</script>");
		}
		
		
		
		sb.append("\n  <div id=\"ykw_wka_backlog\" class=\"quiz-main\">"); 
		for(CardPage p : exam.getPages() ){ 
			if( p instanceof CardSurveyJsJumpPage && ((CardSurveyJsJumpPage)p).getSurveyQuiz()!=null){
				sb.append(" \n  <div id=\"" + p.getUuid() +"\" class=\"quiz-surveyjs-data\" style=\"");
	 			sb.append("		    height: " + Constants.VIDEO_WIDTH + "px;");
	    		sb.append("		    width: " + Constants.VIDEO_HEIGHT + "px;");
				sb.append("		    background-color: white; color:black; display:none;  \"  >");  
				
				Quiz survey = ((CardSurveyJsJumpPage)p).getSurveyQuiz();
				sb.append("<script>"); 
				if(R9SystemSetting.LOCALE.getLanguage().equals("zh"))
				    sb.append(" Survey.surveyLocalization.locales[\"r9\"]  = " +
						getChineseLocaleStringForSurveyJS() + ";  ");
				if( useBootstrap )
					sb.append(" Survey.StylesManager.applyTheme(\"bootstrap\"); ");
				sb.append( survey.getValidatorCode2()   );  
				sb.append( survey.generateHtmlJSCode(p.getUuid() )  ); 
				sb.append("</script>");
				sb.append("\n  </div>");
			} else{
				sb.append(" \n  <div id=\"" + p.getUuid() +"\" class=\"quiz-r9-data\" style=\"");
	 			sb.append("		    height: " + Constants.VIDEO_WIDTH + "px;");
	 		sb.append("		    width: " + Constants.VIDEO_HEIGHT + "px;");
				sb.append("		    background-color: white; color:black; display:none;  \"  >");  
				
				sb.append("\n   " + p.replaceTemplate(R9SystemSetting.sharedInstance.getTemplateById(p.getHtmlTemplateId()) ));
				sb.append("\n  </div>");
			}
			
		}
		sb.append("\n  </div>");
	//	sb.append("   <script src=\"../../third_party/VideojsR9.js\"></script>");
		sb.append(" \n   <script src=\"" + libDir+ "VideojsR9.js\"></script>");
		sb.append("  <script>"); 
		sb.append("\n  var bgvideotimeline, idsinpagegroup=[],    curpageid=null, bgvideojs = videojs('" + Constants.PLAYER_CONTAINER_ID +"'); "); 
  	     // initalize example ad plugin for this player
		sb.append("\n   bgvideojs.r9playerlayer(" +  getConfigurationForVideojs(exam,pgs)  +");  ");  

		sb.append("\n  function validateR9(){"); 
		  sb.append("\n   var inputs = ''; var curpage = document.getElementById( curpageid );"); 
		  sb.append("\n   var fields = curpage.getElementsByTagName('input');      ");
		  sb.append("\n   for(var i = 0; i < fields.length;i++ ){");
		  sb.append("\n      if( fields[i].type == 'text'){ inputs += fields[i].value + ' '; }");
		  sb.append("\n      else if( fields[i].checked ) { if(! fields[i].name ){ inputs += 'yes ';   } else { inputs += fields[i].name + ' ';}  } ");
	 	  sb.append("\n   }");
		  sb.append("\n   inputs = inputs.trim(); ");
		for( PageGroup doc : pgs){  
			  for( CardPage p : doc.getPages()) { 
				   sb.append("\n if( curpageid === '" + p.getUuid() + "' ){" );
				   sb.append("    var correct = inputs == '" + p.getAnswers() + "';"); 
				   if( p.getCorrectFollow() != null && p.getCorrectFollow().length() > 0) {
					   sb.append("\n  if( correct ){ ");
					   sb.append("\n     curpage.getElementsByClassName('msg-panel-error')[0].className='msg-panel-error hidden';   ");
					   sb.append("\n    curpage.getElementsByClassName('msg-panel-info')[0].className='msg-panel-info';   ");
					   sb.append("\n  }");
				   }
					  
				   if( p.getWrongFollow() != null && p.getWrongFollow().length() > 0) {
					   sb.append("\n  if( !correct ){ ");
					   sb.append("\n     curpage.getElementsByClassName('msg-panel-info')[0].className='msg-panel-info hidden';   ");
					   sb.append("\n     curpage.getElementsByClassName('msg-panel-error')[0].className='msg-panel-error';   ");
					   sb.append("\n  }"); 
				   }
					   
				   sb.append("\n  }");
			  } 
	    }
		sb.append("\n  }");
		
		sb.append("\n  function nextPage(){"); 
		sb.append("\n     var overlay = document.getElementById('" +Constants.PLAYER_OVERLAY_ID + "')" );
		sb.append("\n     var lastchild = overlay.lastChild; "); 
		sb.append("\n     if( lastchild ) { "); 
		sb.append("\n         var f = document.createDocumentFragment(); f.appendChild(lastchild); document.getElementById('ykw_wka_backlog').appendChild(f); "); 
		sb.append("\n        lastchild.style.display='none'; "); 
		sb.append("\n     } "); 
		sb.append("\n     if( idsinpagegroup.length == 0 ) {  overlay.style.display='none'; bgvideojs.trigger('r9ended'); return;     } "); 
		sb.append("\n     curpageid = idsinpagegroup.shift();"); 
		sb.append("\n     var fragment = document.createDocumentFragment(); var curpage = document.getElementById(curpageid); "); 
		sb.append("\n     fragment.appendChild(curpage); "); 
		sb.append("\n     document.getElementById('" +Constants.PLAYER_OVERLAY_ID + "').appendChild(fragment); curpage.style.display='block';"); 
		sb.append("\n     if( typeof mathjax_create != 'undefined' ){  mathjax_create();   }"); 
		sb.append("\n  } "); 
		sb.append("\n ; var onTimelineChanges =  function(subject, edo, ssd){");
		sb.append("\n if ( subject == 'r9.core.animation.timeline'){");
		sb.append("\n if( edo.frombgvideo ){  bgvideotimeline = edo.timeline;  var overlay = document.getElementById('" +   Constants.PLAYER_OVERLAY_ID  +"'); overlay.style.display='block'; }");
          
       for( PageGroup doc : pgs){ 
    	   sb.append("\n if ( edo.timeline == '" + doc.getId() + "'){");
    	   sb.append("\n   overlay.innerHTML =\"\"; ");
    	   sb.append("\n   idsinpagegroup = " + doc.getIdsInJs() + ";"); 
    	   sb.append("\n   nextPage();");
    	   sb.append("\n  ");
    	   sb.append("\n }");
       }
       sb.append("\n }");
       sb.append("\n 	}");
       sb.append("\n  window.PageBus.subscribe('r9.core.animation.timeline', this,  onTimelineChanges, null); ");

		
  	    sb.append("  </script>");  
  	    sb.append("  </body></html>"); 
		return sb.toString();
	}
	
	public String renderHtmlCodeNoVideo(Exam exam, boolean export){
		exam.normalize();
		List<PageGroup> pgs = exam.getPageGroup();
		String libDir = export ?  exam.getLibDirectory() : "../../third_party/";
		StringBuilder sb = new StringBuilder();
		sb.append(" <!DOCTYPE HTML> ");
		sb.append(" <html>");
		sb.append(" <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width\"> "); 
		sb.append("\n <link href=\"" + libDir+ "r9quiz.css\" rel=\"stylesheet\">");
		boolean useBootstrap  = false;
		for(String css : exam.getAllCssFiles() ){
			sb.append("\n <link href=\"" + libDir + css + "\" rel=\"stylesheet\">");
			if( css.indexOf("bootstrap") >= 0)
				useBootstrap = true;
		}
		if( exam.hasSurveyJs() ){ 
			sb.append("\n <link href=\"" + libDir+ "survey.min.css\" rel=\"stylesheet\">"); 
			sb.append("\n <script src=\"" + libDir+ "jquery-1.11.3.min.js\"></script>");
			sb.append("\n <script src=\"" + libDir+ "survey.jquery.min.js\"></script>");
			sb.append("\n <script src=\"" + libDir+ "r9quiz.js\"></script>");
		} 
		sb.append("   \n <script src=\"" + libDir+ "pagebus.js\"></script>");
		 
	
		//js file order matters!, we setup order for all js files in the system.
		List<String> orderedJs = R9SystemSetting.sharedInstance.reorderJsFile(exam.getAllJsFiles() );
		for(String js : orderedJs){
			if( exam.hasSurveyJs() && js.equals("jquery-1.11.3.min.js"))
				continue;
			sb.append("\n <script src=\"" + libDir + js + "\"></script>");
		}
		
		if( exam.isUseMathJax() ) {
		 	sb.append("\n <script src=\"https://polyfill.io/v3/polyfill.min.js?features=es6\"></script>");
		    sb.append("\n <script>  MathJax = {\n" + 
	     		"    tex: {inlineMath: [['$', '$'], ['\\\\(', '\\\\)']]}\n" + 
		    		"  }; </script> ");
	        sb.append("\n <script id=\"MathJax-script\"  src=\"https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-chtml.js\"></script>");
			 
	        sb.append("\n <script> function mathjax_create(){MathJax.typeset(); } </script>");
		    
//		    sb.append("\n <script>  function convertToMathJax(outnode) {  "  );
//		    sb.append("  if(outnode.children.length >  0 ) return;  var input = outnode.innerHTML.trim();  if( input.length == 0 ) return;  ");
//		    sb.append("\n MathJax.texReset();  var options = MathJax.getMetricsFor(outnode, false);    ");
//		    sb.append(" \n MathJax.tex2chtmlPromise(input, options).then(function (node) { ");
//		    sb.append("\n     outnode.innerHTML = '';  outnode.appendChild(node); ");
//		    sb.append("\n     MathJax.startup.document.clear();  MathJax.startup.document.updateDocument();");
//		    sb.append("  }).catch(function (err) { }).then(function () { });");
//		    sb.append(" ");
//		    sb.append(" }"); 
//			sb.append(" function mathjax_create(){   ");
//			//sb.append("\n var mathspans = document.getElementsByTagName('span'); ");
//			 sb.append("\n var mathspans = document.getElementById('" +Constants.PLAYER_OVERLAY_ID + "').getElementsByTagName('span'); ");
//			sb.append("\n for(var i = 0; i < mathspans.length; i++){");
//			sb.append("\n   if( mathspans[i].dataset.bind &&  mathspans[i].dataset.bind == 'text: koRenderedHtml'){ ");
//		 	sb.append("\n      convertToMathJax(mathspans[i]);");
//			sb.append("   }"); 
//			sb.append(" }");
//			sb.append("}"); 
//		    sb.append("</script>");
		}else {
			sb.append("\n <script> function mathjax_create(){  } </script>");
		}
		
		sb.append(" </head>");
		sb.append(" <body>");
		sb.append("  <div  id='" +Constants.PLAYER_OVERLAY_ID + "' class=\"quiz-data\" style=\"");
	//	sb.append("		    height: " + Constants.VIDEO_WIDTH + "px;");
	//	sb.append("		    width: " + Constants.VIDEO_HEIGHT + "px;");
		sb.append("  \"></div>"); 
	
		if(   exam.hasSurveyJsCode() ) {
			sb.append("<script>");
			sb.append( "var asciiChartSet_en2c = { " + 
			"   '&quot;' : '\"',   '&apos;': '\\'', '&sol;': '/', '&lt;': '<', '&gt;':'>', '&lsqb;': '[',  '&bsol;': '\\\\', '&rsqb;': ']', '&lcub;': '{',  '&rcub;':'}' ,  '&nl;': '\\n'   " + 
		 	"} " +  
			"\n;function entity2html(input){\n" + 
			"   var output = input.replace(/(&.+?;)/g, function (matched) {\n" + 
			"    var rs = asciiChartSet_en2c[matched];\n" + 
			"    return rs == undefined ? matched : rs;\n" + 
			"  });\n" + 
			"  console.log(input);  \n" + 	
			"  console.log(output);  \n" + 	
			"  return output; \n" + 	
			"}" );
			sb.append("</script>");
		}
		
		
		
		sb.append("\n  <div id=\"ykw_wka_backlog\" class=\"quiz-main\">"); 
		for(CardPage p : exam.getPages() ){
			sb.append(" \n  <div id=\"" + p.getUuid() +"\" class=\"quiz-r9-data\" style=\"");
		//	sb.append("		    height: " + Constants.VIDEO_WIDTH + "px;");
		//	sb.append("		    width: " + Constants.VIDEO_HEIGHT + "px;");
			sb.append("		    background-color: white; color:black; display:none;  \"  >");  
			if( p instanceof CardSurveyJsJumpPage && ((CardSurveyJsJumpPage)p).getSurveyQuiz()!=null){
				Quiz survey = ((CardSurveyJsJumpPage)p).getSurveyQuiz();
				sb.append("<script>"); 
				if(R9SystemSetting.LOCALE.getLanguage().equals("zh"))
				  sb.append(" Survey.surveyLocalization.locales[\"r9\"]  = " +
						 getChineseLocaleStringForSurveyJS() + ";  ");
				if( useBootstrap )
					sb.append(" Survey.StylesManager.applyTheme(\"bootstrap\"); ");
				sb.append( survey.getValidatorCode2()   );  
				sb.append( survey.generateHtmlJSCode(p.getUuid() )  );  
			
			
				
				sb.append("</script>");
			} else{
				sb.append("\n   " + p.replaceTemplate(R9SystemSetting.sharedInstance.getTemplateById(p.getHtmlTemplateId()) ));
		 	}
			sb.append("\n  </div>");
		}
		sb.append("\n  </div>");
		 
		sb.append("  <script>"); 
		sb.append("\n  var  idsinpagegroup=[] ; var curpageid=null;"); 
		for( PageGroup doc : pgs){  
	    	   sb.append("\n  var idsinjs = " + doc.getIdsInJs() + ";"); 
	    	   sb.append("\n  idsinpagegroup = idsinpagegroup.concat( idsinjs );"); 
	    }
		
		sb.append("\n  function validateR9(){"); 
 		  sb.append("\n   var inputs = ''; var curpage = document.getElementById( curpageid );"); 
		  sb.append("\n   var fields = curpage.getElementsByTagName('input');      ");
		  sb.append("\n   for(var i = 0; i < fields.length;i++ ){");
		  sb.append("\n      if( fields[i].type == 'text'){ inputs += fields[i].value + ' '; }");
		  sb.append("\n      else if( fields[i].checked ) { if(! fields[i].name ){ inputs += 'yes ';   } else { inputs += fields[i].name + ' ';}  } ");
		  sb.append("\n   }");
		  sb.append("\n   inputs = inputs.trim(); ");
		for( PageGroup doc : pgs){  
			  for( CardPage p : doc.getPages()) { 
				   sb.append("\n if( curpageid === '" + p.getUuid() + "' ){" );
				   sb.append("    var correct = inputs == '" + p.getAnswers() + "';"); 
				   if( p.getCorrectFollow() != null && p.getCorrectFollow().length() > 0) {
					   sb.append("\n  if( correct ){ ");
					   sb.append("\n     curpage.getElementsByClassName('msg-panel-error')[0].className='msg-panel-error hidden';   ");
					   sb.append("\n    curpage.getElementsByClassName('msg-panel-info')[0].className='msg-panel-info';   ");
					   sb.append("\n  }");
				   }
					  
				   if( p.getWrongFollow() != null && p.getWrongFollow().length() > 0) {
					   sb.append("\n  if( !correct ){ ");
					   sb.append("\n     curpage.getElementsByClassName('msg-panel-info')[0].className='msg-panel-info hidden';   ");
					   sb.append("\n     curpage.getElementsByClassName('msg-panel-error')[0].className='msg-panel-error';   ");
					   sb.append("\n  }"); 
				   }
					   
				   sb.append("\n  }");
			  } 
	    }
		sb.append("\n  }");
		 
		sb.append("\n  function nextPage(){"); 
		sb.append("\n     var overlay = document.getElementById('" +Constants.PLAYER_OVERLAY_ID + "')" );
		sb.append("\n     var lastchild = overlay.lastChild; "); 
		sb.append("\n     if( lastchild ) { "); 
		sb.append("\n         var f = document.createDocumentFragment(); f.appendChild(lastchild); document.getElementById('ykw_wka_backlog').appendChild(f); "); 
		sb.append("\n        lastchild.style.display='none'; "); 
		sb.append("\n     } "); 
		sb.append("\n     if( idsinpagegroup.length == 0 ) {    return;     } "); 
		sb.append("\n       curpageid = idsinpagegroup.shift();"); 
		sb.append("\n     var fragment = document.createDocumentFragment(); var curpage = document.getElementById(curpageid); "); 
		sb.append("\n     fragment.appendChild(curpage); "); 
		sb.append("\n     document.getElementById('" +Constants.PLAYER_OVERLAY_ID + "').appendChild(fragment); curpage.style.display='block';"); 
		sb.append("\n    if( typeof mathjax_create != 'undefined' ){  mathjax_create();   }"); 
		sb.append("\n  } "); 
		 
		sb.append("\n  nextPage(); "); 
  	    sb.append("  </script>");  
  	    sb.append("  </body></html>"); 
		return sb.toString();
	}
	
	public String getConfigurationForVideojs(Exam exam, List<PageGroup> pgs){
	   StringBuilder sb = new StringBuilder();
		sb .append("{");
		sb.append("'r9containerid': '"+Constants.PLAYER_OVERLAY_ID+"', ");
		sb.append("'width': "+Constants.VIDEO_WIDTH+" , ");
		sb.append("'height':  "+Constants.VIDEO_HEIGHT+" , ");
		 
		 sb.append("'playPreroll': '', ");
		 sb.append("'playPostroll': '', ");
		 
		 
		int count = pgs.size()  ;
		if( count == 0 ){
			sb.append("'r9TimeTable': []");
		} else {
			sb.append("'r9TimeTable': [  ");
			StringBuilder sbb = new StringBuilder();
			for( PageGroup i : pgs){
				sbb.append(", {'timeline': '"+ i.getId() + "', 'posInVideo' : "+ i.getTimeInVideo() +"}");
			}
			sb.append(sbb.substring(1) + " ] ");
		}  
		sb.append("}");
		return sb.toString();
	}
	
	private String getChineseLocaleStringForSurveyJS() {
		return " {\n" + 
				"  pagePrevText: \"上一页\",\n" + 
				"  pageNextText: \"下一页\",\n" + 
				"  completeText: \"提交问卷\",\n" + 
				"  otherItemText: \"填写其他答案\",\n" + 
				"  progressText: \"第 {0} 页, 共 {1} 页\",\n" + 
				"  emptySurvey: \"问卷中沒有问題或页面\",\n" + 
				"  completingSurvey: \"感谢您的参与!\",\n" + 
				"  loadingSurvey: \"问卷载入中...\",\n" + 
				"  optionsCaption: \"请选择...\",\n" + 
				"  requiredError: \"请填写此问題\",\n" + 
				"  requiredInAllRowsError: \"请填写所个有行中问題\",\n" + 
				"  numericError: \"答案必須是數字\",\n" + 
				"  textMinLength: \"答案长度至少 {0} 个字元\",\n" + 
				"  textMaxLength: \"答案长度不能超過 {0} 個字元\",\n" + 
				"  textMinMaxLength: \"答案长度必须在 {0} - {1} 个元之間\",\n" + 
				"  minRowCountError: \"最少需要填充 {0} 行答案\",\n" + 
				"  minSelectError: \"最少需要选择 {0} 項答案\",\n" + 
				"  maxSelectError: \"最多只能选择 {0} 項答案\",\n" + 
				"  numericMinMax: \"答案 '{0}' 必須大于等于 {1} 且小于等于2}\",\n" + 
				"  numericMin: \"答案 '{0}' 必須大于等于 {1}\",\n" + 
				"  numericMax: \"答案 '{0}' 必須小于等于 {1}\",\n" + 
				"  invalidEmail: \"请输入有效的 Email 地址\",\n" + 
				"  urlRequestError: \"载入选项時发生錯誤 '{0}': {1}\",\n" + 
				"  urlGetChoicesError: \"未能載入有效的选项或請求參數路徑有誤\",\n" + 
				"  exceedMaxSize: \"文件大小不能超過 {0}\",\n" + 
				"  otherRequiredError: \"請完成其他問題\",\n" + 
				"  uploadingFile: \"文件上传中... 請耐心等待几秒後重試\",\n" + 
				"  addRow: \"添加答案\",\n" + 
				"  removeRow: \"刪除答案\",\n" + 
				"  choices_Item: \"选项\",\n" + 
				"  matrix_column: \"列\",\n" + 
				"  matrix_row: \"行\",\n" + 
				
				"saveAgainButton: \"重试\",\n" + 
				"  timerMin: \"分\",\n" + 
				"  timerSec: \"秒\",\n" + 
				"  timerSpentAll: \"您已经在本题上使用了 {0}, 总共使用了 {1} .\",\n" + 
				"  timerSpentPage: \"您已经在本题上使用了 {0} .\",\n" + 
				"  timerSpentSurvey: \"您总共使用了 {0}.\",\n" + 
				"  timerLimitAll:\n" + 
				"    \"您已经在本题上使用了{0} / {1},  总共使用了 {2} / {3}.\",\n" + 
				"  timerLimitPage: \"您已经在本题上使用了 {0} / {1} .\",\n" + 
				"  timerLimitSurvey: \"您总共使用了 {0} / {1} .\"," +
				
				"  savingData: \"正在將結果保存到服务器...\",\n" + 
				"  savingDataError: \"在保存結果过程中发生了错误，結果未能保存\",\n" + 
				"  savingDataSuccess: \"結果保存成功!\",\n" + 
				"  saveAgainButton: \"请重试\"\n" + 
				"}";
	}
	
	public static List<String> getLibs(Exam exam){ 
		List<String> libs = new ArrayList<String>(); 
		exam.normalize();
		boolean videbased = exam.getVideoSrc() != null && exam.getVideoSrc().length() > 0;
		if( videbased ) {
		  libs.add( "videojs.min.css" );
		  libs.add( "videojs-contrib-r9.css" );
		}
		libs.add( "r9quiz.css");
		boolean useBootstrap  = false;
		for(String css : exam.getAllCssFiles() ){
			libs.add(  css  );
			if( css.indexOf("bootstrap") >= 0)
				useBootstrap = true;
		}
		if( exam.hasSurveyJs() ){ 
			libs.add( "survey.min.css"); 
			libs.add(  "jquery-1.11.3.min.js");
			libs.add(  "survey.jquery.min.js");
			libs.add(  "r9quiz.js");
		}
		if( videbased ) {
		   libs.add(  "videojs.min.js");
		   libs.add(  "pagebus.js");
		   libs.add(  "VideojsR9.js");
		   libs.add(  "videojs-contrib-r9.min.js");
		}
	
		//js file order matters!, we setup order for all js files in the system.
		List<String> orderedJs = R9SystemSetting.sharedInstance.reorderJsFile(exam.getAllJsFiles() );
		for(String js : orderedJs){
			if( exam.hasSurveyJs() && js.equals("jquery-1.11.3.min.js"))
				continue;
			  libs.add( js );
		}
        return libs;
	}
}
