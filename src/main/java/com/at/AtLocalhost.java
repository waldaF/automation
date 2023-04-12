package com.at;

import lombok.SneakyThrows;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * To be able execute separate test directly via IDEA runn button
 */
public class AtLocalhost implements ISuiteListener {

	@SneakyThrows
	@Override
	public void onStart(ISuite suite) {
		final Properties properties = System.getProperties();
		final Path root = Paths.get(properties.get("user.dir").toString()).getParent();
		final File file = new File(root + "/automation/src/test/resources/local.properties");
		properties.load(new FileInputStream(file));
	}

}
