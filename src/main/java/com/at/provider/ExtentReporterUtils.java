package com.at.provider;

import com.at.utils.StringUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class ExtentReporterUtils {

	public static void logRequest(final ExtentTest extentTest, final String requestJson) {
		if (StringUtils.isNotEmpty(requestJson)) {
			extentTest.info(MarkupHelper.createLabel("Body:", ExtentColor.BLUE));
			extentTest.info(MarkupHelper.createCodeBlock(requestJson, CodeLanguage.JSON));
		}
	}

	public static void logResponse(final ExtentTest extentTest, final String responseJson) {
		extentTest.info(MarkupHelper.createLabel("Response:", ExtentColor.BLUE));
		extentTest.info(MarkupHelper.createCodeBlock(responseJson, CodeLanguage.JSON));
	}

	public static void logDuration(final ExtentTest extentTest, final Duration duration) {
		extentTest.info(MarkupHelper.createLabel("duration: " + duration.toMillis(), ExtentColor.BLUE));
	}
}
