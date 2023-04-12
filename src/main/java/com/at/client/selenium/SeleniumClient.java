package com.at.client.selenium;

import com.at.client.selenium.dto.ByAttribute;
import com.at.exception.SeleniumUriException;
import com.at.client.selenium.vo.ClickVo;
import com.at.client.selenium.vo.ElementVo;
import com.at.client.selenium.vo.FrameVo;
import com.at.client.selenium.vo.SeleniumMessageVo;
import com.at.client.selenium.vo.WaitVo;
import com.at.provider.ThreadProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeleniumClient {
	private static final String INCOGNITO_MODE = "--incognito";
	private static final String ALLOW_ORIGINS = "--remote-allow-origins=*";
	private static final String REMOTE_URL = "SOME-REMOTE-SERVER/wd/hub";
	private final ChromeOptions chromeOptions;
	private final WebDriver driver;
	private final boolean localhost;
	public final List<SeleniumMessageVo> errorScreensUi = new ArrayList<>();
	public final List<SeleniumMessageVo> screensUi = new ArrayList<>();

	public static SeleniumClient createClient(final boolean localhost) {
		var selenium = new SeleniumClient(localhost);
		selenium.deleteCookies();
		selenium.maximizeSize();
		return selenium;
	}

	private SeleniumClient(final boolean localhost) {
		this.chromeOptions = initOptions(INCOGNITO_MODE, ALLOW_ORIGINS);
		this.localhost = localhost;
		this.driver = driverInit();
	}

	public void clickEnter(final ByAttribute field) {
		var element = findBy(field);
		element.sendKeys(Keys.ENTER);
	}

	public void maximizeSize() {
		driver.manage().window().maximize();
	}

	public void deleteCookies() {
		this.driver.manage().deleteAllCookies();
	}

	public void addCookie(Cookie cookie) {
		this.driver.manage().addCookie(cookie);
	}

	public void scrollDown(int value) {
		scroll(new ScrollUp(0), new ScrollDown(value));
	}

	public void scrollUp(int value) {
		scroll(new ScrollUp(value), new ScrollDown(0));
	}

	public void fillIn(final ByAttribute field, final String value) {
		var element = findBy(field);
		element.sendKeys(value);
	}

	public List<WebElement> findElementsBy(final ByAttribute byAttribute) {
		retryIsEnabled(byAttribute);
		return driver.findElements(byAttribute.get());
	}

	public void clickAndFillIn(final ByAttribute field, final String value) {
		click(field);
		fillIn(field, value);
	}

	public void clickWithPageRedirect(final ClickVo field, WaitVo wait) {
		click(field);
		retryIsEnabled(wait);
	}

	public void switchToFrameFillInElement(final FrameVo frameVo,
										   final ElementVo elementVo,
										   final String value) {
		retryIsEnabled(frameVo);
		driver.switchTo().frame(driver.findElement(frameVo.value()));
		fillIn(elementVo, value);
		driver.switchTo().defaultContent();
	}

	public void retryIsEnabled(final ByAttribute attribute) {
		if (isNull(attribute.getIterations()) || isNull(attribute.getSleepTime())) {
			return;
		}
		int retries = 0;
		while (retries < attribute.getIterations()) {
			retries++;
			if (driver.findElements(attribute.get()).isEmpty()) {
				ThreadProvider.sleepMs(attribute.getSleepTime());
			} else if (!driver.findElement(attribute.get()).isEnabled()) {
				ThreadProvider.sleepMs(attribute.getSleepTime());
			} else {
				return;
			}
		}
		var errorMessage = String.format("Unable to locate element %s with retry %s times and sleep %s [ms]",
				attribute.get().toString(),
				attribute.getIterations(),
				attribute.getSleepTime());
		errorScreensUi.add(new SeleniumMessageVo(captureScreen(), errorMessage, LocalDateTime.now()));
		throw new RuntimeException("Problem to find element");
	}

	private boolean isNull(Integer value) {
		return Objects.isNull(value);
	}

	public void get(final String url) {
		this.driver.get(url);
	}

	public void loadMainPage(final String url, final ByAttribute waitTillElementEnabled) {
		get(url);
		waitTillJsLoaded();
		retryIsEnabled(waitTillElementEnabled);
	}

	private void waitTillJsLoaded() {
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(
				webDriver -> ((JavascriptExecutor) webDriver)
						.executeScript("return document.readyState").equals("complete"));
	}

	public WebElement findBy(ByAttribute by) {
		retryIsEnabled(by);
		return driver.findElement(by.get());
	}

	public void quit() {
		this.driver.quit();
	}

	public void takeScreenshot(final String message) {
		screensUi.add(new SeleniumMessageVo(captureScreen(), message, LocalDateTime.now()));
	}

	public String captureScreen() {
		TakesScreenshot newScreen = (TakesScreenshot) driver;
		String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
		return "data:image/jpg;base64, " + scnShot;
	}

	private void click(final ByAttribute field) {
		var element = findBy(field);
		element.click();
	}

	private WebDriver driverInit() {
		System.setProperty("webdriver.http.factory", "jdk-http-client");
		if (!localhost) {
			return new RemoteWebDriver(asURL(), chromeOptions);
		}
		return WebDriverManager.chromedriver().capabilities(chromeOptions).create();
	}

	private ChromeOptions initOptions(final String... data) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments(data);
		return options;
	}

	private URL asURL() {
		try {
			return new URL(REMOTE_URL);
		} catch (MalformedURLException e) {
			throw new SeleniumUriException("Unable to get URL from" + REMOTE_URL, e);
		}
	}

	private void scroll(final ScrollUp up, final ScrollDown down) {
		var jsCommand = "window.scrollBy(%UP%,%DOWN%)"
				.replaceAll("%UP%", String.valueOf(up.value))
				.replaceAll("%DOWN%", String.valueOf(down.value));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(jsCommand, "");
	}

	private record ScrollUp(int value) {
	}

	private record ScrollDown(int value) {
	}

}

