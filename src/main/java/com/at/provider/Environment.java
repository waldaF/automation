package com.at.provider;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Environment {
	ALL("all"),
	DEV("dev"),
	STG("stg"),
	PROD("prod");

	public static final String GROUP_ALL = "all";
	public static final String GROUP_DEV = "dev";
	public static final String GROUP_STG = "stg";

	public final String group;

	public static Environment current() {
		final String value = KeyProvider.loadProperty("com.at.environment");
		return Arrays.stream(values())
				.filter(environment -> environment.group.equalsIgnoreCase(value))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unknown com.at.environment value: " + value));
	}
}