package r9.quiz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncodingUtil {
	 private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };
	 public static String encryptDES(String encryptString, String encryptKey) throws Exception {
              IvParameterSpec zeroIv = new IvParameterSpec(iv);
             SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");

	           Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

	           cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

	           byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

              return Base64Coding.encode64(encryptedData);

	}
	 
	 public static void encryptFile(File file, File destFile, String encryptKey) {  
	        InputStream is = null;  
	        OutputStream out = null;  
	        CipherInputStream cis = null;  
	        try {  
	        	   IvParameterSpec zeroIv = new IvParameterSpec(iv);
	               SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");

	  	           Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

	  	           cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
	  	           
	         //   Cipher cipher = Cipher.getInstance("DES");  
	          //  cipher.init(Cipher.ENCRYPT_MODE, this.key);  
	            is = new FileInputStream(file);  
	            out = new FileOutputStream(destFile);  
	            cis = new CipherInputStream(is, cipher);  
	            byte[] buffer = new byte[1024];  
	            int r;  
	            while ((r = cis.read(buffer)) > 0) {  
	                out.write(buffer, 0, r);  
	            }  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	        } finally {  
	            try {  
	                cis.close();  
	                is.close();  
	                out.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }   
	  
}
