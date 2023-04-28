package com.at.client.selenium.vo;

import com.at.client.selenium.dto.ByAttribute;
import org.openqa.selenium.By;

public record FrameVo(By value, int iterations, int sleep) implements ByAttribute {
    @Override
    public By get() {
        return value;
    }

    public FrameVo(By value) {
        this(value, 5, 500);
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
