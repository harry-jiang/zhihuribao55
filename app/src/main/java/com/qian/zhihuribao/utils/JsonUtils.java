package com.qian.zhihuribao.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

/**
 * gson工具类
 *
 * @author G0210181
 */
public class JsonUtils {
    private static final String TAG = "JSONHelper";
    private static final GsonBuilder gsonb;

    static {
        gsonb = new GsonBuilder();
        gsonb.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context)
                    throws JsonParseException {
                String date = json.getAsJsonPrimitive().getAsString();
                String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
                Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
                Matcher matcher = pattern.matcher(date);
                String result = matcher.replaceAll("$2");
                try {
                    return new Date(new Long(result));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static <T> T fromJson(String JSONString, Type typeOfT) {
        final Gson gson = gsonb.create();
        try {
            return gson.fromJson(JSONString, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String JSONString, Class<T> classOfT) {
        final Gson gson = gsonb.create();
        T entity = null;
        try {
            entity = gson.fromJson(JSONString, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entity;
    }

    public static <T> T fromJson(byte[] bytes, Class<T> classOfT) {
        final Gson gson = gsonb.create();
        T entity = null;
        try {
            entity = gson.fromJson(new String(bytes, "UTF-8"), classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entity;
    }

    /**
     * 转换成json数据
     */
    public static String toJson(Object src) {
        final Gson gson = gsonb.create();
        String result = gson.toJson(src);
        return result;
    }
}
