package com.at.ui;


import com.at.client.selenium.SeleniumClient;
import com.at.client.selenium.SeleniumWrapper;
import com.at.client.selenium.dto.ProductDto;
import com.at.client.selenium.po.EshopHomePo;
import com.at.listener.TestNgListener;
import com.at.provider.ExtentReporterProvider;
import com.at.provider.KeyProvider;
import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Test NG naming  want to have IT even thought it is E2E (real DB etc no Mock, H2 ...)
 */
@Listeners({TestNgListener.class})
public class EshopIT {

	@Test
	public void search() {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		var url = KeyProvider.loadProperty("com.at.ui.eshop.url");
		extentTest.info("Starting UI test for url " + url);
		var localhost = Boolean.getBoolean(KeyProvider.loadProperty("com.at.ui.eshop.localhost"));
		var seleniumClient = SeleniumClient.createClient(true);
		var productDto = new ProductDto("faDeD");
		final EshopHomePo home = new EshopHomePo(seleniumClient, productDto, url);
		SeleniumWrapper.ui(extentTest, home);
	}

}
