package com.at.utils;

import com.at.exception.DeserializationUtilsException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.experimental.UtilityClass;

import java.util.function.BiFunction;

@UtilityClass
public class DeserializationUtils {
	private static final Gson GSON = new GsonBuilder().create();

	public static <T> T deserialize(final Object json, Class<T> tClass) {
		return commonFunctionWrapper(json, tClass, GSON::fromJson);
	}

	private static <T> T commonFunctionWrapper(final Object json,
											   final Class<T> clazz,
											   final BiFunction<String, Class<T>, T> biFunction) {

		try {
			return biFunction.apply(json.toString(), clazz);
		} catch (JsonSyntaxException jse) {
			final String errMsg = "Error during deserialize " + json + "\nTO " + clazz.getName() + ".class\n";
			throw new DeserializationUtilsException(errMsg, jse);
		}
	}
}
