package com.at.data;

import lombok.experimental.UtilityClass;
import org.testng.annotations.DataProvider;

@UtilityClass
public class UIDataProvider {

	@DataProvider(parallel = true)
	public static Object[][] searchProducts() {
		return new Object[][]{
				{new SearchBy.Name("FaDeD ").value, ExpectedCount.ONE},
				{new SearchBy.Name("FaD eD").value, ExpectedCount.ONE},
				{new SearchBy.Name("FADED").value, ExpectedCount.ONE},
				{new SearchBy.Name("faded").value, ExpectedCount.ONE},
				{new SearchBy.Name("F aDeD").value, ExpectedCount.ONE},
				{new SearchBy.Name("dress").value, ExpectedCount.THREE},
		};
	}

	private static class ExpectedCount {
		private static final int ONE = 1;
		private static final int THREE = 3;
	}

	private static class SearchBy {
		private record Name(String value) {
		}
	}
}
