package com.at.client.selenium.vo;

import com.at.client.selenium.dto.ByAttribute;
import org.openqa.selenium.By;

public record ElementVo(By value, Integer iterations, Integer sleep) implements ByAttribute {
    @Override
    public By get() {
        return value;
    }
    public ElementVo(By value) {
        this(value, 1, 100);
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
