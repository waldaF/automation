package com.at.provider;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class ExtentReporterProvider {
	private static final String DIR = "ExtentReportResults.html";
	private static final Path PATH = Paths.get(KeyProvider.loadProperty("user.dir"));
	private static final ExtentReports extent = init();
	private static final ThreadLocal<ExtentTest> threadLocal = new ThreadLocal<>();
	private static final ExtentReports init() {

		final ExtentSparkReporter htmlReporter = new ExtentSparkReporter(PATH.resolve(DIR).toString());
		final ExtentSparkReporterConfig configuration = htmlReporter.config();
		configuration.setTheme(Theme.DARK);
		configuration.setDocumentTitle("Localhost E2E result ");
		configuration.setEncoding(StandardCharsets.UTF_8.name());
		configuration.setReportName("Localhost");
		final ExtentReports extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setAnalysisStrategy(AnalysisStrategy.TEST);
		return extent;
	}

	public static ExtentTest createTest(final String name, final String description, final String category) {
		final ExtentTest extentTest = extent.createTest(name, description)
				.assignCategory(category);
		threadLocal.set(extentTest);
		return extentTest;
	}

	public static ExtentTest getTest() {
		final ExtentTest extentTest = threadLocal.get();
		if (extentTest == null) {
			throw new IllegalArgumentException("Empty reporter");
		}
		return extentTest;
	}

	public static void flush() {
		extent.flush();
	}
}
