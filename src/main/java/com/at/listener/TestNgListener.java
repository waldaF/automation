package com.at.listener;

import com.at.provider.ExtentReporterProvider;
import com.at.utils.StringUtils;
import com.aventstack.extentreports.ExtentTest;
import org.testng.IInvokedMethodListener;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TestNgListener implements ITestListener, ISuiteListener, IReporter, IInvokedMethodListener {

	private static final Map<String, ExtentTest> mutex = new ConcurrentHashMap<>();

	@Override
	public void generateReport(List<XmlSuite> list, List<ISuite> list1, String s) {
		ExtentReporterProvider.flush();
	}

	/**
	 * TODO SOLVE DATA PROVIDER FOR SAME METHOD == tst child
	 *
	 * @param result
	 */
	@Override
	public void onTestStart(ITestResult result) {
		final String className = result.getTestClass().getRealClass().getSimpleName();
		final ITestNGMethod method = result.getMethod();
		final String description = method.getDescription();
		final String methodName = method.getMethodName();
		final String key = String.format("%s.%s", className, methodName);
		final String classNameDescription = className + "<br>";
		final String wholeDescription = StringUtils.isEmpty(description)
				? classNameDescription
				: classNameDescription + description;
		final ExtentTest extentTestFather = ExtentReporterProvider.createTest(methodName, wholeDescription, result.getTestContext().getSuite().getName());
		if (Objects.nonNull(mutex.get(key))) {
			throw new IllegalArgumentException("Same method with dataset not supported yet - child test!");
		} else {
			mutex.put(key, extentTestFather);
		}
	}
}
