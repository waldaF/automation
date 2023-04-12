package com.at.client.selenium.po;

import com.at.client.selenium.SeleniumClient;
import com.at.client.selenium.dto.PageObject;
import com.at.client.selenium.dto.ProductDto;
import com.at.client.selenium.vo.ClickVo;
import com.at.client.selenium.vo.WaitVo;
import org.openqa.selenium.By;

import java.util.Optional;

public class EshopHomePo implements PageObject {
	private final ProductDto productDto;
	private final SeleniumClient seleniumClient;
	private final String mainUrl;
	private static final By SEARCH_INPUT = By.id("search_query_top");

	public EshopHomePo(final SeleniumClient seleniumClient,
					   final ProductDto productDto,
					   final String mainUrl) {
		this.productDto = productDto;
		this.seleniumClient = seleniumClient;
		this.mainUrl = mainUrl;
	}

	@Override
	public Optional<Object> ui() {
		this.loadMainPage();
		this.searchProduct();
		return Optional.of("ok");
	}

	private void loadMainPage() {
		this.seleniumClient.loadMainPage(this.mainUrl, new WaitVo(SEARCH_INPUT));
		this.seleniumClient.deleteCookies();
		this.seleniumClient.takeScreenshot("Load main page");
	}

	private void searchProduct() {
		this.seleniumClient.clickAndFillIn(new ClickVo(SEARCH_INPUT), productDto.productName());
		this.seleniumClient.takeScreenshot("Search product");
	}


	@Override
	public SeleniumClient client() {
		return this.seleniumClient;
	}
}
