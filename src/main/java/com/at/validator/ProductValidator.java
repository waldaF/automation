package com.at.validator;

import com.at.client.selenium.dto.response.ProductListResponse;
import com.at.utils.SerializationUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductValidator {

	// JUST DUMMY VALIDATION FOR SIZE
	public static void validate(final ExtentTest extentTest,
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
