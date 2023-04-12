package com.at.rest;

import com.at.client.rest.Client;
import com.at.client.rest.vo.ResponseVo;
import com.at.client.selenium.dto.response.ProductListResponse;
import com.at.data.RestApiDataProvider;
import com.at.listener.TestNgListener;
import com.at.provider.ExtentReporterProvider;
import com.at.provider.KeyProvider;
import com.at.utils.DeserializationUtils;
import com.at.utils.SerializationUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestNgListener.class})
public class EshopCoreIT {
	@Test(
			dataProvider = "searchProducts",
			dataProviderClass = RestApiDataProvider.class,
			description = "Separate Rest API test for search products compare results against dataset"
	)
	public void searchEshopCore(final String searchBy, final ProductListResponse expectedResponse) {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		extentTest.assignCategory("REST");
		extentTest.info("Search by " + searchBy);

		var url = KeyProvider.loadProperty("com.at.ui.eshop-core.url");
		extentTest.info("GET  " + url);

		final ResponseVo responseVo = new Client().httpDummyGet();
		final ProductListResponse serverResponse = DeserializationUtils.deserialize(
				responseVo.getResponseJson(), ProductListResponse.class);
		extentTest.info(MarkupHelper.createLabel("duration: " + responseVo.getDuration().toMillis(), ExtentColor.BLUE));
		validate(extentTest, serverResponse, expectedResponse);
	}
	@Test
	public void searchNotFound(){
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		extentTest.assignCategory("REST");
	}
	@Test(description = "Call rest api search with too long string,  with CSRF etc...")
	public void searchBadRequest(){
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		extentTest.assignCategory("REST");
	}

	// JUST DUMMY VALIDATION FOR SIZE
	private void validate(final ExtentTest extentTest,
						  final ProductListResponse actual,
						  final ProductListResponse expected) {

		var actualSize = actual.products().size();
		var expectedSize = expected.products().size();

		if (actualSize != expectedSize) {
			var message = String.format("Product count does not correspond, some duplicity returned!?<br>" +
					" Expected %s products but found %s products!", expectedSize, actualSize);

			extentTest.fail(message);
			extentTest.fail(MarkupHelper.createLabel("Server response", ExtentColor.BLUE));
			extentTest.fail(MarkupHelper.createCodeBlock(SerializationUtils.serialize(actual), CodeLanguage.JSON));
			extentTest.fail(MarkupHelper.createLabel("Expected response", ExtentColor.BLUE));
			extentTest.fail(MarkupHelper.createCodeBlock(SerializationUtils.serialize(expected), CodeLanguage.JSON));
		}
	}
}