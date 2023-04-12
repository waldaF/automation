package com.at.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

class CollectionUtilsTest {

	@ParameterizedTest
	@MethodSource("emptyCollectionDataset")
	void isEmpty_dataInserted_success(final List<String> input, final boolean expectedResult) {
		var actualResult = CollectionUtils.isEmpty(input);
		Assertions.assertEquals(expectedResult, actualResult);
	}

	private static Stream<Arguments> emptyCollectionDataset() {
		return Stream.of(
				Arguments.of(List.of("a"), Expected.NOT_EMPTY),
				Arguments.of(null, Expected.EMPTY),
				Arguments.of(Collections.emptyList(), Expected.EMPTY)
		);
	}

	static class Expected {
		private static final boolean EMPTY = true;
		private static final boolean NOT_EMPTY = false;
	}
}