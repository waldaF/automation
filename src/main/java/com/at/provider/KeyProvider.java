package com.at.provider;

import com.at.utils.StringUtils;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@UtilityClass
public class KeyProvider {
	public static final ConcurrentMap<String, String> propertiesMap = new ConcurrentHashMap<>();

	public static String loadProperty(final String key) {
		return findProperty(key).orElseThrow(() -> new IllegalArgumentException("Not found value for key " + key));
	}

	private static Optional<String> findProperty(final String key) {
		final String propValue = System.getProperty(key);
		if (StringUtils.isEmpty(propValue)) {
			return Optional.empty();
		}
		propertiesMap.putIfAbsent(key, propValue);
		return Optional.of(propValue);
	}
}
