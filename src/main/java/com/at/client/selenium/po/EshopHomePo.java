package com.at.client.selenium.po;

import com.at.client.selenium.SeleniumClient;
import com.at.client.selenium.dto.PageObject;
import com.at.client.selenium.dto.ProductDto;
import com.at.client.selenium.dto.response.ProductListResponse;
import com.at.client.selenium.vo.ClickVo;
import com.at.client.selenium.vo.ElementVo;
import com.at.client.selenium.vo.WaitVo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EshopHomePo implements PageObject {
	private final ProductDto productDto;
	private final SeleniumClient seleniumClient;
	private final String mainUrl;
	private static final By SEARCH_INPUT = By.id("search_query_top");
	private static final By PRODUCT_LIST = By.className("product_list");
	private static final By PRODUCT_CONTAINER = By.className("product-container");
	private static final By PRODUCT_NAME_TAG = By.tagName("h5");

	public EshopHomePo(final SeleniumClient seleniumClient,
					   final ProductDto productDto,
					   final String mainUrl) {
		this.productDto = productDto;
		this.seleniumClient = seleniumClient;
		this.mainUrl = mainUrl;
	}

	@Override
	public Optional<Object> ui() {
		loadMainPage();
		searchProduct();
		return Optional.of(collectProducts());
	}

	private void loadMainPage() {
		seleniumClient.loadMainPage(mainUrl, new WaitVo(SEARCH_INPUT));
		seleniumClient.deleteCookies();
		seleniumClient.takeScreenshot("Load main page");
	}

	private void searchProduct() {
		seleniumClient.clickAndFillIn(new ClickVo(SEARCH_INPUT), productDto.productName());
		seleniumClient.clickEnter(new ElementVo(SEARCH_INPUT));
		seleniumClient.takeScreenshot("Search product");
	}

	private ProductListResponse collectProducts() {
		final WebElement listProducts = seleniumClient.findBy(new ElementVo(PRODUCT_LIST));
		seleniumClient.scrollDown(200);
		seleniumClient.takeScreenshot("Collect products after search");
		final List<WebElement> productContainers = listProducts.findElements(PRODUCT_CONTAINER);
		var products = productContainers.stream()
				.map(this::mapToProduct)
				.collect(Collectors.toList());
		return new ProductListResponse(products);
	}

	private ProductDto mapToProduct(WebElement webElement) {
		var name = webElement.findElement(PRODUCT_NAME_TAG).getText();
		return new ProductDto(name);
	}

	@Override
	public SeleniumClient client() {
		return seleniumClient;
	}
}
