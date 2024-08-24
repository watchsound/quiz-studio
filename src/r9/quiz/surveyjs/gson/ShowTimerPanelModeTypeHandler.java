package r9.quiz.surveyjs.gson;


import java.lang.reflect.Type;

import r9.quiz.surveyjs.Question.QuestionType;
import r9.quiz.surveyjs.Quiz.ShowTimerPanelModeType;

import com.google.gson.JsonDeserializationContext;  
import com.google.gson.JsonDeserializer;  
import com.google.gson.JsonElement;  
import com.google.gson.JsonParseException;  
import com.google.gson.JsonPrimitive;  
import com.google.gson.JsonSerializationContext;  
import com.google.gson.JsonSerializer;  
 
public class ShowTimerPanelModeTypeHandler implements JsonSerializer<ShowTimerPanelModeType>,  
       JsonDeserializer<ShowTimerPanelModeType> {  
 
   // 对象转为Json时调用,实现JsonSerializer<PackageState>接口  
   @Override  
   public JsonElement serialize(ShowTimerPanelModeType state, Type arg1,  
           JsonSerializationContext arg2) {  
       return new JsonPrimitive(state.ordinal());  
   }  
 
   // json转为对象时调用,实现JsonDeserializer<PackageState>接口  
   @Override  
   public ShowTimerPanelModeType deserialize(JsonElement json, Type typeOfT,  
           JsonDeserializationContext context) throws JsonParseException {  
       if (json.getAsInt() < ShowTimerPanelModeType.values().length)  
           return ShowTimerPanelModeType.values()[json.getAsInt()];  
       return null;  
   }  
 
}  