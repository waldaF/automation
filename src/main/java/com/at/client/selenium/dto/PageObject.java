package com.at.client.selenium.dto;

import com.at.client.selenium.SeleniumClient;

import java.util.Optional;

public interface PageObject {
	Optional<Object> ui();

	SeleniumClient client();
}
