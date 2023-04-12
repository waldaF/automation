package com.at;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class StringUtilsTest {

	@ParameterizedTest
	@MethodSource("isEmptyDataset")
	void isEmpty_dataInserted_success(final String input, final boolean expected) {
		var actualValue = StringUtils.isEmpty(input);
		Assertions.assertEquals(expected, actualValue);
	}

	@ParameterizedTest
	@MethodSource("isEmptyDataset")
	void isNotEmpty_dataInserted_success(final String input, final boolean expected) {
		var actualValue = StringUtils.isNotEmpty(input);
		var expectedReversed = !expected;
		Assertions.assertEquals(expectedReversed, actualValue);
	}

	private static Stream<Arguments> isEmptyDataset() {
		return Stream.of(
				Arguments.of("", Expected.EMPTY),
				Arguments.of(" ", Expected.NOT_EMPTY),
				Arguments.of(" Te ste r ", Expected.NOT_EMPTY),
				Arguments.of(null, Expected.EMPTY)
		);
	}

	static class Expected {
		private static final boolean EMPTY = true;
		private static final boolean NOT_EMPTY = false;
	}
}