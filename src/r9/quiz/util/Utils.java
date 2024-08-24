package r9.quiz.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import r9.quiz.component.ImageFileChooser;
import r9.quiz.component.MutableFilter;
 
public class Utils {
	public static String delHtmlTag(String str){ 
		String newstr = ""; 
		newstr = str.replaceAll("<[.[^>]]*>","");
		newstr = newstr.replaceAll("&nbsp;", ""); 
		return newstr;
    }
	public static String conpressHtml(String str){ 
		str = str.replaceAll("<p .*?>", "UVWUVW");  
		str = str.replaceAll("<br\\s*/?>", "UVWUVW");  
		String newstr = ""; 
		newstr = str.replaceAll("<[.[^>]]*>",""); 
		newstr = newstr.replaceAll("UVWUVW","<br/>"); 
		newstr = newstr.replaceAll("&nbsp;", " "); 
		return newstr;
    }
	
	public static String[] asciiChartSet_c2en =  new String[] {
			//    " ", "&nbsp;",
			//    "!", "&excl;",
		          "\"", "&quot;",
		          "\n", "&nl;",
			//    "#", "&num;",
			//    "$", "&dollar;",
			//    "%", "&percnt;",
			//    "&", "&amp;",
			    "'", "&apos;",
			//    "(", "&lpar;",
			//    ")", "&rpar;",
			//    "*", "&ast;",
			//    "+", "&plus;",
			//    ",", "&comma;",
			//    "-", "&hyphen;",
			//    ".", "&period;",
			    "/", "&sol;",
			//    ",", "&colon;",
			//    ";", "&semi;",
			    "<", "&lt;",
			//    "=", "&equals;",
			    ">", "&gt;",
			//    "?", "&quest;",
			//    "@", "&commat;",
			    "\\[", "&lsqb;",
			    "\\\\", "&bsol;",
			    "]", "&rsqb;",
			//    "^", "&circ;",
			//    "_", "&lowbar;",
			//    "`", "&grave;",
			    "\\{", "&lcub;",
	 		//    "|", "&verbar;",
	 		    "}", "&rcub;",
	 		 //   "~", "&tilde;" 
		};
	public static Map<String, String> getAscii2entity(){
		Map<String,String> map = new HashMap<>();
		for(int i = 0; i < asciiChartSet_c2en.length; i+=2 ) {
			map.put(asciiChartSet_c2en[i], asciiChartSet_c2en[i+1]);
		}
		return map;
	} 
	public static String normalize_ascii2entity(String input) {
		for(Entry<String, String> b : getAscii2entity().entrySet() ) {
			input = input.replaceAll(b.getKey(), b.getValue());
		}
		return input;
	}
	public static String  handleNull(String value){
		return value == null ? "" : value;
	}
    public static String fromIntsToString(int[] values){
    	if( values == null || values.length == 0)
    		return "";
    	
    	StringBuilder sb = new StringBuilder();
    	for(int v : values){
    		sb.append(" " + v);
    	}
    	return sb.substring(1);
    }
    
    public static String toString(String[] values){
    	if( values == null || values.length == 0)
    		return "";
    	
    	StringBuilder sb = new StringBuilder();
    	for(String v : values){
    		sb.append(" " + v);
    	}
    	return sb.substring(1);
    }
    public static String fromCheckBox(JCheckBox[] checkboxlist) {
    	String answer = "";
    	char v = 'A';
    	for(int i = 0  ; i < checkboxlist.length; i++) {
    		if( checkboxlist[i].isSelected())
    			answer += v +  " ";
    		v = (char) (v +  1);
    	}
    	return answer.trim();
    }
    
    public static String[] fromString(String values, boolean decompose){
    	if( values == null || values.trim().length() == 0)
    		return new String[0];
    	
    	values = values.replaceAll("/\\s+/g", " ");
    	 
    	String[] result = values.split(" ");
    	if( result.length == 1 && result[0].length() >1 && decompose ){
    		String  key = result[0];
    		String[] nresult = new String[key.length()];
    		for(int i = 0; i < key.length(); i++){
    			nresult[i] = key.charAt(i) + "";
    		}
    		return nresult;
    	}
    	return result;
    }
    
    public static void copyFileToFile(File srcfile, File outfile) throws IOException {
    	if( !outfile.exists())
    		outfile.createNewFile();
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
    public static void copyStringToFile(String content, File outfile) throws IOException {
		try {
			 
			if (outfile.exists()) {
				outfile.delete();
			}
			outfile.createNewFile();

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outfile), "UTF8"));

			out.write(content);
			out.close(); 
		} catch (Exception ex) {
			ex.printStackTrace();
		}  
	}

    public static File getFileFromChooser(JComponent frame, String startDir, int dialogType, String[] exts, String desc)
	{
		JFileChooser jfileDialog = new JFileChooser(startDir);
		jfileDialog.setDialogType(dialogType);
		jfileDialog.setFileFilter(new r9.quiz.component.MutableFilter(exts, desc));
		int optionSelected = JFileChooser.CANCEL_OPTION;
		if(dialogType == JFileChooser.OPEN_DIALOG)
		{
			optionSelected = jfileDialog.showOpenDialog(frame);
		}
		else if(dialogType == JFileChooser.SAVE_DIALOG)
		{
			optionSelected = jfileDialog.showSaveDialog(frame);
		}
		else // default to an OPEN_DIALOG
		{
			optionSelected = jfileDialog.showOpenDialog(frame);
		}
		if(optionSelected == JFileChooser.APPROVE_OPTION)
		{
			return jfileDialog.getSelectedFile();
		}
		return (File)null;
	}
    
    public static File browseForImage(String startDir, int dialogType, String[] exts,
			String desc) {
		ImageFileChooser jImageDialog = new ImageFileChooser(startDir);
		jImageDialog.setDialogType(JFileChooser.CUSTOM_DIALOG);
		jImageDialog.setFileFilter(new MutableFilter(exts, desc));
		jImageDialog.setDialogTitle( "Image Dialog " );
		int optionSelected = JFileChooser.CANCEL_OPTION;
		optionSelected = jImageDialog.showDialog(null, "Insert" );
		if (optionSelected == JFileChooser.APPROVE_OPTION) {
			return jImageDialog.getSelectedFile();
		}
		return (File) null;
	}
    
    public static int parseInt(String value, int defaultValue){
    	try{
    		return Integer.parseInt(value);
    	}catch(Exception ex){
    		return defaultValue;
    	}
    }
    
    public static  void copyFolder(File src, File dest) throws IOException{
        if(src.isDirectory()){
            if(!dest.exists()){
                dest.mkdir();
            }

            String files[] = src.list();

            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);

                copyFolder(srcFile,destFile);
            }

        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest); 

            byte[] buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }
    public static void setClipboardString(String text) {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
    }

    /**
     * 从剪贴板中获取文本（粘贴）
     */
    public static String getClipboardString() {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);

        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    return text;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    } 
    public static String trimTrailing(String str)
    {
      if( str == null || str.length() == 0)  return str;
      int len = str.length();
      for( ; len > 0; len--)
      {
        if( ! Character.isWhitespace( str.charAt( len - 1)))
           break;
      }
      return str.substring( 0, len);
    }  
}
