package com.at.client.selenium.dto.response;

import com.at.client.selenium.dto.ProductDto;

import java.util.List;

public record ProductListResponse(List<ProductDto> products) {
}
