package com.at.data;

import com.at.client.selenium.dto.ProductDto;
import com.at.client.selenium.dto.response.ProductListResponse;
import lombok.experimental.UtilityClass;
import org.testng.annotations.DataProvider;

import java.util.List;

@UtilityClass
public class RestApiDataProvider {

	public static final ProductListResponse FADED = new ProductListResponse(
			List.of(new ProductDto("Faded Short Sleeve T-shirts"))
	);
	public static final ProductListResponse DRESS = new ProductListResponse(
			List.of(new ProductDto("Printed Dress"),
					new ProductDto("Printed Summer Dress"),
					new ProductDto("Printed Chiffon Dress")
			)
	);

	@DataProvider(parallel = true)
	public static Object[][] searchProducts() {
		return new Object[][]{
				{new SearchBy.Name("FaDeD ").value, FADED},
				{new SearchBy.Name("FADED ").value, FADED},
				{new SearchBy.Name("FaD eD").value, FADED},
				{new SearchBy.Name("dress").value, DRESS},
		};
	}

	private static class SearchBy {
		private record Name(String value) {
		}
	}
}
