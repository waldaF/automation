package com.at.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static boolean isNotEmpty(final CharSequence cs) {
		return !isEmpty(cs);
	}
}
