package com.feinno.appengine.configuration;

import java.lang.reflect.Type;
import java.util.List;

import com.feinno.util.KeyValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class FAEAppAnnotationDecoder
{
	private static Gson gson;

	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(AppBeanBaseClassInfo.class, new JsonDeserializer<AppBeanBaseClassInfo>() {
			@SuppressWarnings("unchecked")
			@Override
			public AppBeanBaseClassInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
			{
				AppBeanBaseClassInfo bean = new AppBeanBaseClassInfo();
				JsonObject object = json.getAsJsonObject();
				bean.setType(object.get("type").getAsString());
				JsonElement jsonSources = object.get("genericParams");
				Type listType = new TypeToken<List<KeyValuePair<String, String>>>() {
				}.getType();
				bean.setGenericParams((List<KeyValuePair<String, String>>) context.deserialize(jsonSources, listType));
				return bean;
			}
		});
		builder.registerTypeAdapter(AppBeanAnnotation.class, new JsonDeserializer<AppBeanAnnotation>() {
			@SuppressWarnings("unchecked")
			@Override
			public AppBeanAnnotation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
			{
				AppBeanAnnotation bean = new AppBeanAnnotation();
				JsonObject object = json.getAsJsonObject();
				bean.setType(object.get("type").getAsString());
				JsonElement jsonSources = object.get("fields");
				if (jsonSources != null) {
					Type listType = new TypeToken<List<KeyValuePair<String, String>>>() {
					}.getType();
					bean.setFields((List<KeyValuePair<String, String>>) context.deserialize(jsonSources, listType));
					return bean;
				}
				jsonSources = object.get("childAnnotations");
				if (jsonSources != null) {
					Type arrayType = new TypeToken<AppBeanAnnotation[]>() {
					}.getType();
					bean.setChildAnnotations((AppBeanAnnotation[]) context.deserialize(jsonSources, arrayType));
					return bean;
				}
				return bean;
			}
		});

		builder.registerTypeAdapter(AppBeanAnnotations.class, new JsonDeserializer<AppBeanAnnotations>() {

			@SuppressWarnings("unchecked")
			@Override
			public AppBeanAnnotations deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
			{
				AppBeanAnnotations bean = new AppBeanAnnotations();
				JsonObject object = json.getAsJsonObject();
				JsonElement jsonSources = object.get("classInfo");
				bean.setClassInfo((AppBeanClassInfo) context.deserialize(jsonSources, AppBeanClassInfo.class));
				jsonSources = object.get("annotations");
				Type listType = new TypeToken<List<AppBeanAnnotation>>() {
				}.getType();
				bean.setAnnotations((List<AppBeanAnnotation>) context.deserialize(jsonSources, listType));
				return bean;
			}
		});
		gson = builder.create();
	}

	public static AppBeanAnnotations decode(String json)
	{
		return gson.fromJson(json, AppBeanAnnotations.class);
	}
}
