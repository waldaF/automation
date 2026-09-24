package com.at.rest;

import com.at.GenericIT;
import com.at.client.rest.Client;
import com.at.client.rest.vo.ResponseVo;
import com.at.client.selenium.dto.response.ProductListResponse;
import com.at.data.RestApiDataProvider;
import com.at.provider.ExtentReporterProvider;
import com.at.provider.ExtentReporterUtils;
import com.at.utils.DeserializationUtils;
import com.at.validator.ProductValidator;
import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.Test;

public class EshopCoreIT extends GenericIT {
	@Test(
			dataProvider = "searchProducts",
			dataProviderClass = RestApiDataProvider.class,
			description = "Separate Rest API test for search products compare results against dataset"
	)
	public void searchEshopCore(final String searchBy, final ProductListResponse expectedResponse) {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		extentTest.info("Search by " + searchBy);
		final ResponseVo responseVo = new Client().httpDummyGet(extentTest);
		final ProductListResponse serverResponse = DeserializationUtils.deserialize(
				responseVo.getResponseJson(), ProductListResponse.class);
		ExtentReporterUtils.logDuration(extentTest, responseVo.getDuration());
		ProductValidator.validate(extentTest, serverResponse, expectedResponse);
	}

	@Test
	public void searchNotFound() {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
	}

	@Test(description = "Call rest api search with too long string,  with CSRF etc...")
	public void searchBadRequest() {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
	}
}