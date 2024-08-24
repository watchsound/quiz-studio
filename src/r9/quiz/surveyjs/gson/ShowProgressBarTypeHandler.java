package r9.quiz.surveyjs.gson;


import java.lang.reflect.Type;

import r9.quiz.surveyjs.Question.ChoicesOrderType;
import r9.quiz.surveyjs.Question.QuestionType;
import r9.quiz.surveyjs.Quiz.ShowProgressBarType;

import com.google.gson.JsonDeserializationContext;  
import com.google.gson.JsonDeserializer;  
import com.google.gson.JsonElement;  
import com.google.gson.JsonParseException;  
import com.google.gson.JsonPrimitive;  
import com.google.gson.JsonSerializationContext;  
import com.google.gson.JsonSerializer;  
 
public class ShowProgressBarTypeHandler implements JsonSerializer<ShowProgressBarType>,  
       JsonDeserializer<ShowProgressBarType> {  
 
   // 对象转为Json时调用,实现JsonSerializer<PackageState>接口  
   @Override  
   public JsonElement serialize(ShowProgressBarType state, Type arg1,  
           JsonSerializationContext arg2) {  
       return new JsonPrimitive(state.ordinal());  
   }  
 
   // json转为对象时调用,实现JsonDeserializer<PackageState>接口  
   @Override  
   public ShowProgressBarType deserialize(JsonElement json, Type typeOfT,  
           JsonDeserializationContext context) throws JsonParseException {  
       if (json.getAsInt() < ShowProgressBarType.values().length)  
           return ShowProgressBarType.values()[json.getAsInt()];  
       return null;  
   }  
 
}  