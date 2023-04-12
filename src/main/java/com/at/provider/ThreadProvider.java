package com.at.provider;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class ThreadProvider {
	public static void sleepMs(final long sleepDuration) {
		try {
			TimeUnit.MILLISECONDS.sleep(sleepDuration);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
