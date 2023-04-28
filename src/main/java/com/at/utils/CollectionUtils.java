package com.at.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class CollectionUtils {
	public static <T> boolean isEmpty(Collection<T> col) {
		return col == null || col.isEmpty();
	}
}