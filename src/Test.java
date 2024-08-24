import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;
import java.util.Base64.Encoder;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
			 String value = "02F7FF5C-926B-4A97-A903-F7026BBDE3C614890687172e65e22133a14eafbdc5fe0790d2e6e0";
		  //System.out.println( EncoderByMd5(value) );
			 System.out.println( getMD5(value) );
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	public static String getMD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};        
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest md5=MessageDigest.getInstance("MD5");
        Encoder base64en = Base64.getEncoder();
       // byte[] newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
          ;
          byte[] result = md5.digest(str.getBytes("utf-8"));
          for(int i = 0; i < result.length; i ++)
        	  System.out.print( i  );
          return "";
	 }
}
