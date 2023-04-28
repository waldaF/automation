package com.at.client.selenium;

import com.at.client.selenium.dto.PageObject;
import com.at.client.selenium.vo.SeleniumMessageVo;
import com.at.utils.CollectionUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@UtilityClass
public class SeleniumWrapper {

	/**
	 * Actually ui method will all the times close client no matter what
	 * (Could be solved with another Method List<PageObject> in case of need
	 */
	public static Optional<Object> ui(final ExtentTest extentTest, final PageObject pageObject) {
		return innerUi(extentTest, pageObject);
	}

	private Optional<Object> innerUi(final ExtentTest extentTest,
									 final PageObject pageObject) {
		var uiClient = pageObject.client();
		try {
			var data = pageObject.ui();
			printAllSuccessScreensDuringUi(extentTest, pageObject);
			return data;
		} catch (Exception ex) {
			printAllSuccessScreensDuringUi(extentTest, pageObject);
			if (CollectionUtils.isEmpty(uiClient.errorScreensUi)) {
				extentTest.fail(
						String.format("Error during ui test %s", ex.getMessage()),
						MediaEntityBuilder.createScreenCaptureFromBase64String(uiClient.captureScreen()).build());
			} else {
				printAllErrorScreensDuringUi(extentTest, pageObject);
				uiClient.quit();
			}
		} finally {
			uiClient.quit();
		}
		return Optional.empty();
	}

	private void printAllSuccessScreensDuringUi(final ExtentTest extentTest,
												final PageObject pageObject) {
		innerPrint(() -> pageObject.client().screensUi, extentTest::info);
	}

	private void printAllErrorScreensDuringUi(final ExtentTest extentTest,
											  final PageObject pageObject) {
		innerPrint(() -> pageObject.client().errorScreensUi, extentTest::fail);
	}


	private void innerPrint(final Supplier<List<SeleniumMessageVo>> supplier,
							final BiFunction<String, Media, ExtentTest> function) {
		for (SeleniumMessageVo messageVo : supplier.get()) {
			function.apply(
					messageVo.message(),
					MediaEntityBuilder.createScreenCaptureFromBase64String(messageVo.screen()).build());
		}
	}
}
