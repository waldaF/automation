package com.at.listener;

import com.at.mutex.MutexMap;
import com.at.provider.Environment;
import com.at.provider.ExtentReporterProvider;
import com.at.utils.StringUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TestNgListener implements ITestListener, ISuiteListener, IReporter, IInvokedMethodListener, IMethodInterceptor {

	private static final MutexMap<String, ExtentTest> mutex = new MutexMap<>();
	private static final Set<String> ENVIRONMENT_GROUPS = Arrays.stream(Environment.values())
			.map(environment -> environment.group)
			.collect(Collectors.toSet());

	/**
	 * A test not tagged with any Environment.GROUP_* always runs. A tagged test only runs
	 * when the active environment matches one of its groups, or it is tagged GROUP_ALL.
	 */
	@Override
	public List<IMethodInstance> intercept(final List<IMethodInstance> methods, final ITestContext context) {
		final Environment environment = Environment.current();
		return methods.stream()
				.filter(instance -> matchesEnvironment(instance, environment))
				.collect(Collectors.toList());
	}

	private boolean matchesEnvironment(final IMethodInstance instance, final Environment environment) {
		final List<String> groups = Arrays.asList(instance.getMethod().getGroups());
		final boolean isEnvironmentScoped = groups.stream().anyMatch(ENVIRONMENT_GROUPS::contains);
		if (!isEnvironmentScoped) {
			return true;
		}
		return groups.contains(Environment.GROUP_ALL) || groups.contains(environment.group);
	}

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
			final String parameters = Arrays.stream(result.getParameters())
					.map(String::valueOf)
					.collect(Collectors.joining(", ", "[", "]"));
			ExtentReporterProvider.createNodeTest(parentTest, methodName + " " + parameters);
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
		final Throwable throwable = result.getThrowable();
		if (Objects.isNull(throwable)) {
			ExtentReporterProvider.getTest().fail("Test failed");
		} else {
			ExtentReporterProvider.getTest().fail(throwable);
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentReporterProvider.getTest().skip("Test skipped");
	}
}
