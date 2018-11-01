package com.huayin.common.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class JsonUtils
{
	private static Gson gson = new GsonBuilder().create();

	/**
	 * <pre>
	 *  将对象转换成json格式 
	 * </pre>
	 * @param object
	 * @return
	 */
	public static String toJson(Object object)
	{
		return gson.toJson(object);
	}
	/**
	 * <pre>
	 *  将对象转换成json格式 
	 * </pre>
	 * @param object
	 * @return
	 */
	public static <T> T jsonToObject(String jsonStr, Class<T> cl)
	{
		return gson.fromJson(jsonStr, cl);
	}

	/**
	 * 
	 * <pre>
	 * 带格式化日期转换OBJECT-->JSON
	 * </pre>
	 * @param ts
	 * @param dateformat
	 * @return
	 */
	public static String objectToJsonDateSerializer(Object ts, final String dateformat)
	{
		String jsonStr = null;
		gson = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>()
		{
			public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context)
			{
				SimpleDateFormat format = new SimpleDateFormat(dateformat);
				return new JsonPrimitive(format.format(src));
			}
		}).setDateFormat(dateformat).create();
		if (gson != null)
		{
			jsonStr = gson.toJson(ts);
		}
		return jsonStr;
	}
	/**
	 * 
	 * <pre>
	 * 带格式化日期转换JSON-->OBJECT
	 * </pre>
	 * @param jsonStr
	 * @param cl
	 * @param pattern
	 * @return
	 */
    @SuppressWarnings("unchecked")  
    public static <T> T jsonToBeanDateSerializer(String jsonStr, Class<T> cl,  
            final String pattern) {  
        Object obj = null;  
        gson = new GsonBuilder()  
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {  
                    public Date deserialize(JsonElement json, Type typeOfT,  
                            JsonDeserializationContext context)  
                            throws JsonParseException {  
                        SimpleDateFormat format = new SimpleDateFormat(pattern);  
                        String dateStr = json.getAsString();  
                        try {  
                            return format.parse(dateStr);  
                        } catch (ParseException e) {  
                            e.printStackTrace();  
                        }  
                        return null;  
                    }  
                }).setDateFormat(pattern).create();  
        if (gson != null) {  
            obj = gson.fromJson(jsonStr, cl);  
        }  
        return (T) obj;  
    }  
	/**
	 * 将json格式转换成list对象
	 * @param jsonStr
	 * @return
	 */
	public static List<?> jsonToList(String jsonStr)
	{
		return gson.fromJson(jsonStr, new TypeToken<List<?>>()
		{
		}.getType());
	}

	/**
	 * 将json格式转换成Set对象
	 * @param jsonStr
	 * @return
	 */
	public static Set<?> jsonToSet(String jsonStr)
	{
		return gson.fromJson(jsonStr, new TypeToken<Set<?>>()
		{
		}.getType());
	}
	/**
	 * 将json格式转换成Map对象
	 * @param jsonStr
	 * @return
	 */
	public static Map<?,?> jsonToMap(String jsonStr)
	{
		return gson.fromJson(jsonStr, new TypeToken<Map<?,?>>()
		{
		}.getType());
	}
	/**
	 * 将json格式转换成list对象，并准确指定类型
	 * @param jsonStr
	 * @param type
	 * @return
	 */
	public static List<?> jsonToList(String jsonStr, Type type)
	{
		return gson.fromJson(jsonStr, type);
	}
	
	public static String formatJSON(String jsonStr)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(new JsonParser().parse(jsonStr));
	}
}
