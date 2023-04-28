package com.at.listener;

import com.at.mutex.MutexMap;
import com.at.provider.ExtentReporterProvider;
import com.at.utils.StringUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.util.List;

public class TestNgListener implements ITestListener, ISuiteListener, IReporter, IInvokedMethodListener {

	private static final MutexMap<String, ExtentTest> mutex = new MutexMap<>();

	@Override
	public void generateReport(List<XmlSuite> list, List<ISuite> list1, String s) {
		ExtentReporterProvider.flush();
	}

	/**
	 * For data provider same testMethod is added as child
	 */
	@Override
	public void onTestStart(ITestResult result) {
		final String className = result.getTestClass().getRealClass().getSimpleName();
		final ITestNGMethod method = result.getMethod();
		final String description = method.getDescription();
		final String methodName = method.getMethodName();
		final String key = String.format("%s.%s", className, methodName);

		if (mutex.containsKey(key)) {
			final ExtentTest parentTest = mutex.get(key);
			ExtentReporterProvider.createNodeTest(parentTest);
		} else {
			final String classNameDescription = className + "<br>";
			final String wholeDescription = StringUtils.isEmpty(description)
					? classNameDescription
					: classNameDescription + description;
			final ExtentTest extentTestFather = ExtentReporterProvider.createTest(methodName, wholeDescription, result.getTestContext().getSuite().getName());
			mutex.put(key, extentTestFather);
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		Status status = ExtentReporterProvider.getTest().getStatus();
		if (status != null && status != Status.PASS) {
			testResult.setStatus(2);
		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentReporterProvider.getTest().pass("Test complete");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		ExtentReporterProvider.getTest().fail("Test failed");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentReporterProvider.getTest().skip("Test skipped");
	}
}
