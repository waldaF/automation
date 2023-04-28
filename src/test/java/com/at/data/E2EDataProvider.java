package com.at.data;

import org.testng.annotations.DataProvider;

public class E2EDataProvider {

	@DataProvider(parallel = true)
	public static Object[][] searchProducts() {
		return new Object[][]{
				{new SearchBy.Name("FaDeD ").value},
				{new SearchBy.Name("FADED ").value},
				{new SearchBy.Name("FaD eD").value,},
				{new SearchBy.Name("dress").value},
		};
	}

	private static class SearchBy {
		private record Name(String value) {
		}
	}
}
