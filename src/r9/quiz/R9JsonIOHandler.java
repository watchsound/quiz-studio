package r9.quiz; 

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter; 
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import r9.quiz.cards.Exam;
import r9.quiz.util.Utils; 

public class R9JsonIOHandler {

	public   void save(Exam exam ,   File file) { 
		Gson gson = Gson.createR9Json(true).registerTypeAdapter(Color.class, new ColorTypeAdapter()) 
				.registerTypeAdapter(Rectangle.class, new RectangleTypeAdapter())
				.registerTypeAdapter(Point .class, new PointTypeAdapter()) 
				.registerTypeAdapter(Point2D .class, new Point2DTypeAdapter()) 
				.registerTypeAdapter(Point2D.Double .class, new Point2DDTypeAdapter()) 
				.registerTypeAdapter(Point2D.Float .class, new Point2DFTypeAdapter())  ;
			//	.create();
		String json = gson.toR9Json(exam, Exam.class);
		try {
			R9SystemSetting.stringToFile(json, file);
		} catch (IOException e) {
			 	e.printStackTrace(); 
		}
	}
	
	public   Exam read( File file) {
		Gson gson = Gson.createR9Json(true).registerTypeAdapter(Color.class, new ColorTypeAdapter()) 
				.registerTypeAdapter(Rectangle.class, new RectangleTypeAdapter())
				.registerTypeAdapter(Point .class, new PointTypeAdapter()) 
				.registerTypeAdapter(Point2D .class, new Point2DTypeAdapter()) 
				.registerTypeAdapter(Point2D.Double .class, new Point2DDTypeAdapter()) 
				.registerTypeAdapter(Point2D.Float .class, new Point2DFTypeAdapter())  ;
				//.create();
		String json = R9SystemSetting.fileToString(file);
		//StringUtils.skipGenerateNodeID = true;
		try {
			Exam   doc = gson.fromR9Json(json,  Exam.class);
		    return doc;
		}finally {
			//StringUtils.skipGenerateNodeID = false;
		}
	
	}
	
	
	public final class ColorTypeAdapter extends TypeAdapter<Color> {  
	  @Override public synchronized Color read(JsonReader in) throws IOException {
	    if (in.peek() == JsonToken.NULL) {
	      in.nextNull();
	      return null;
	    }
	    try {
	      int v = Integer.parseInt(in.nextString());
	      return new Color(v);
	    } catch ( Exception e) {
	      throw new JsonSyntaxException(e);
	    }
	  } 
	  @Override public synchronized void write(JsonWriter out, Color value) throws IOException {
	    out.value(value == null ? null :value.getRGB() + "");
	  }
	}
 
	public final class RectangleTypeAdapter extends TypeAdapter<Rectangle> {  
		  @Override public synchronized Rectangle read(JsonReader in) throws IOException {
		    if (in.peek() == JsonToken.NULL) {
		      in.nextNull();
		      return null;
		    }
		    try {
		     String[] v =in.nextString().split("#") ;
		      return new Rectangle((int)Double.parseDouble(v[0]), 
		    		  (int)Double.parseDouble(v[1]),
		    		  (int)Double.parseDouble(v[2]),
		    		  (int)Double.parseDouble(v[3]));
		    } catch ( Exception e) {
		      throw new JsonSyntaxException(e);
		    }
		  } 
		  @Override public synchronized void write(JsonWriter out, Rectangle value) throws IOException {
		    out.value(value == null ? null : value.x +"#" + value.y + "#" + value.width +"#" + value.height );
		  }
		}
	public final class PointTypeAdapter extends TypeAdapter<Point> {  
		  @Override public synchronized Point read(JsonReader in) throws IOException {
		    if (in.peek() == JsonToken.NULL) {
		      in.nextNull();
		      return null;
		    }
		    try {
		     String[] v =in.nextString().split("#") ;
		      return new Point((int)Double.parseDouble(v[0]),  (int)Double.parseDouble(v[1]) );
		    } catch ( Exception e) {
		      throw new JsonSyntaxException(e);
		    }
		  } 
		  @Override public synchronized void write(JsonWriter out, Point value) throws IOException {
		    out.value(value == null ? null : value.x +"#" + value.y  );
		  }
		}
	public final class Point2DTypeAdapter extends TypeAdapter<Point2D> {  
		  @Override public synchronized Point2D read(JsonReader in) throws IOException {
		    if (in.peek() == JsonToken.NULL) {
		      in.nextNull();
		      return null;
		    }
		    try {
		     String[] v =in.nextString().split("#") ;
		      return new Point2D.Double((int)Double.parseDouble(v[0]),  (int)Double.parseDouble(v[1]) );
		    } catch ( Exception e) {
		      throw new JsonSyntaxException(e);
		    }
		  } 
		  @Override public synchronized void write(JsonWriter out, Point2D value) throws IOException {
		    out.value(value == null ? null : value.getX()+"#" + value.getY() );
		  }
		}
	public final class Point2DDTypeAdapter extends TypeAdapter<Point2D.Double> {  
		  @Override public synchronized Point2D.Double read(JsonReader in) throws IOException {
		    if (in.peek() == JsonToken.NULL) {
		      in.nextNull();
		      return null;
		    }
		    try {
		     String[] v =in.nextString().split("#") ;
		      return new Point2D.Double((int)Double.parseDouble(v[0]),  (int)Double.parseDouble(v[1]) );
		    } catch ( Exception e) {
		      throw new JsonSyntaxException(e);
		    }
		  } 
		  @Override public synchronized void write(JsonWriter out, Point2D.Double value) throws IOException {
		    out.value(value == null ? null : value.getX()+"#" + value.getY() );
		  }
		}
	  public final class Point2DFTypeAdapter extends TypeAdapter<Point2D.Float> {  
		  @Override public synchronized Point2D.Float read(JsonReader in) throws IOException {
		    if (in.peek() == JsonToken.NULL) {
		      in.nextNull();
		      return null;
		    }
		    try {
		     String[] v =in.nextString().split("#") ;
		      return new Point2D.Float((int)Float.parseFloat(v[0]),  (int)Float.parseFloat(v[1]) );
		    } catch ( Exception e) {
		      throw new JsonSyntaxException(e);
		    }
		  } 
		  @Override public synchronized void write(JsonWriter out, Point2D.Float value) throws IOException {
		    out.value(value == null ? null : value.getX()+"#" + value.getY() );
		  }
		}
 
}
