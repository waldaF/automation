package com.at.client.rest;

import com.at.client.rest.vo.ResponseVo;
import com.at.client.selenium.dto.ProductDto;
import com.at.client.selenium.dto.response.ProductListResponse;
import com.at.utils.DeserializationUtils;
import com.at.utils.SerializationUtils;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class Client {

	private static final Random RANDOM = new Random();

	// support duplicity using List
	public static final ProductListResponse DUMMY = new ProductListResponse(
			List.of(new ProductDto("Printed Dress"),
					new ProductDto("Printed Dress"),
					new ProductDto("Printed Dress"),
					new ProductDto("Printed Summer Dress"),
					new ProductDto("Printed Summer Dress"),
					new ProductDto("Printed Chiffon Dress"),
					new ProductDto("Printed Chiffon Dress")
			)
	);


	private String url;

	public ResponseVo httpDummyGet() {
		return ResponseVo.builder()
				.requestJson("")
				.duration(Duration.ofMillis(RANDOM.nextInt(600)))
				.responseJson(SerializationUtils.serialize(DUMMY))
				.build();
	}

}
