package com.at.client.selenium.vo;

import com.at.client.selenium.dto.ByAttribute;
import org.openqa.selenium.By;

public record WaitVo(By value, int iterations, int sleep) implements ByAttribute {
	@Override
	public By get() {
		return value;
	}

	public WaitVo(By value) {
		this(value, 5, 5000);
	}

	@Override
	public Integer getIterations() {
		return iterations;
	}

	@Override
	public Integer getSleepTime() {
		return sleep;
	}
}
