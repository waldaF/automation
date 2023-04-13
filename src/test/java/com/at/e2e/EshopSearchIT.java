package com.at.e2e;

import com.at.GenericIT;
import com.at.client.rest.Client;
import com.at.client.rest.vo.ResponseVo;
import com.at.client.selenium.SeleniumClient;
import com.at.client.selenium.SeleniumWrapper;
import com.at.client.selenium.dto.ProductDto;
import com.at.client.selenium.dto.response.ProductListResponse;
import com.at.client.selenium.po.EshopHomePo;
import com.at.data.E2EDataProvider;
import com.at.exception.SeleniumTestFailedException;
import com.at.provider.ExtentReporterProvider;
import com.at.provider.KeyProvider;
import com.at.utils.DeserializationUtils;
import com.at.utils.SerializationUtils;
import com.at.validator.ProductValidator;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.annotations.Test;

public class EshopSearchIT extends GenericIT {

	@Test(
			dataProvider = "searchProducts",
			dataProviderClass = E2EDataProvider.class,
			description = "Search specific products in eshop ui and that validate results with eshop core using rest api call" +
					"link to TMC <a href='#'>TMC-XYZ</a>"
	)
	public void search(final String productName) {
		ExtentTest extentTest = ExtentReporterProvider.getTest();
		extentTest.assignCategory("System Integration");
		extentTest.info(MarkupHelper.createLabel("Call eshop ui via selenium", ExtentColor.BLUE));
		var url = KeyProvider.loadProperty("com.at.ui.eshop.url");
		var localhost = Boolean.parseBoolean(KeyProvider.loadProperty("com.at.ui.eshop.localhost"));
		var seleniumClient = SeleniumClient.createClient(localhost);
		var productDto = new ProductDto(productName);
		final EshopHomePo home = new EshopHomePo(seleniumClient, productDto, url);

		var data = SeleniumWrapper.ui(extentTest, home).orElseThrow(() -> new SeleniumTestFailedException("Product not found"));
		var jsonData = SerializationUtils.serialize(data);
		extentTest.info("Products from eshop ui obtained");

		final ProductListResponse productsUi = DeserializationUtils.deserialize(jsonData, ProductListResponse.class);
		extentTest.info(MarkupHelper.createLabel("Call eshop core", ExtentColor.BLUE));

		final ResponseVo responseVo = new Client().httpDummyGet(extentTest);
		final ProductListResponse productsBackend = DeserializationUtils.deserialize(
				responseVo.getResponseJson(), ProductListResponse.class);
		extentTest.info(MarkupHelper.createLabel("duration: " + responseVo.getDuration().toMillis(), ExtentColor.BLUE));

		ProductValidator.validate(extentTest, productsUi, productsBackend);
	}

	@Test
	public void searchNotExistingProducts() {

	}


}
