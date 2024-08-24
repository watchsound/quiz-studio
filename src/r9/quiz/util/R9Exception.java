package r9.quiz.util;

public class R9Exception extends Exception{
	 
	private static final long serialVersionUID = 1L;
	 
    public R9Exception() {
        super();
    }
 
    public R9Exception(String message) {
        super(message);
    }
 
    public R9Exception(String message, Throwable cause) {
        super(message, cause);
    }
 
    public R9Exception(Throwable cause) {
        super(cause);
    } 
}