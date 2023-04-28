package com.at.ui;


import com.at.GenericIT;
import com.at.client.selenium.SeleniumClient;
import com.at.client.selenium.SeleniumWrapper;
import com.at.client.selenium.dto.ProductDto;
import com.at.client.selenium.dto.response.ProductListResponse;
import com.at.client.selenium.po.EshopHomePo;
import com.at.data.UIDataProvider;
import com.at.exception.SeleniumTestFailedException;
import com.at.provider.ExtentReporterProvider;
import com.at.provider.KeyProvider;
import com.at.utils.DeserializationUtils;
import com.at.utils.SerializationUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.annotations.Test;

/**
 * Test NG naming  want to have IT even thought it is E2E (real DB etc no Mock, H2 ...)
 */
public class EshopIT extends GenericIT {

	@Test(
			dataProvider = "searchProducts",
			dataProviderClass = UIDataProvider.class,
			description = "Separate UI test for search products compare results against dataset"
	)
	public void searchUI(final String searchBy, final int expectedCount) {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		extentTest.info("Search by " + searchBy);
		var url = KeyProvider.loadProperty("com.at.ui.eshop.url");
		var localhost = Boolean.parseBoolean(KeyProvider.loadProperty("com.at.ui.eshop.localhost"));
		var seleniumClient = SeleniumClient.createClient(localhost);
		var productDto = new ProductDto(searchBy);
		final EshopHomePo home = new EshopHomePo(seleniumClient, productDto, url);

		var data = SeleniumWrapper.ui(extentTest, home).orElseThrow(() -> new SeleniumTestFailedException("Product not found"));
		var jsonData = SerializationUtils.serialize(data);
		extentTest.info(MarkupHelper.createLabel("Product from server", ExtentColor.BLUE));
		extentTest.info(MarkupHelper.createCodeBlock(jsonData, CodeLanguage.JSON));

		final ProductListResponse actualProducts = DeserializationUtils.deserialize(jsonData, ProductListResponse.class);
		validate(extentTest, actualProducts.products().size(), expectedCount);
	}

	@Test(
			dataProvider = "searchProducts",
			dataProviderClass = UIDataProvider.class,
			description = "Separate UI test which check that search does not find product."
	)
	public void searchNotFound(final String searchBy, final int expectedCount) {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		extentTest.info("Product " + searchBy + "not found");
	}

	@Test
	public void signIn() {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
	}

	@Test
	public void purchaseNoSignIn() {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
	}

	@Test
	public void purchaseWithExistingAccount() {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
	}

	private void validate(final ExtentTest extentTest, final int actualSize, final int expectedSize) {
		if (actualSize == expectedSize) {
			extentTest.info("Product count corresponds");
		}
		if (actualSize != expectedSize) {
			var errMessage = String.format("Validation failed expected %s but found %s products",
					expectedSize,
					actualSize);
			extentTest.fail(errMessage);
		}
	}
}
