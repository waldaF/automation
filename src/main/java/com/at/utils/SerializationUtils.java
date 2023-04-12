package com.at.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class SerializationUtils {
	private static final Gson GSON = new GsonBuilder().create();

	public static String serialize(final Object object) {
		return Objects.isNull(object) ? null : GSON.toJson(object);
	}
}
