package com.at.client.rest.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.Duration;

@Builder
@Getter
public class ResponseVo {

	private String url;
	private String requestJson;
	private Duration duration;
	private String responseJson;

}
