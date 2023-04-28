package com.at.client.selenium.dto;

import org.openqa.selenium.By;

public interface ByAttribute {
	By get();

	default Integer getIterations() {
		return null;
	}

	default Integer getSleepTime() {
		return null;
	}
}
