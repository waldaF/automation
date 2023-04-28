package com.at.client.selenium.vo;

import com.at.client.selenium.dto.ByAttribute;
import org.openqa.selenium.By;

public record ClickVo(By value) implements ByAttribute {

	@Override
	public By get() {
		return value;
	}
}
