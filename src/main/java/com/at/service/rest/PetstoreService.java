package com.at.service.rest;

import com.at.client.rest.PetstoreClient;
import com.at.client.rest.dto.ApiResponseDto;
import com.at.client.rest.dto.PetDto;
import com.at.client.rest.dto.Status;
import com.at.client.rest.vo.ResponseVo;
import com.at.exception.RestClientException;
import com.at.provider.ExtentReporterUtils;
import com.at.utils.DeserializationUtils;
import com.at.utils.StringUtils;
import com.aventstack.extentreports.ExtentTest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PetstoreService {

    private final PetstoreClient petstoreClient = PetstoreClient.getInstance();
    private final ExtentTest extentTest;

    public List<PetDto> getList(final Status status) {
        final ResponseVo responseVo = petstoreClient.getPets(extentTest, status);
        logResponse(responseVo);
        final PetDto[] pets = DeserializationUtils.deserialize(responseVo.getResponseJson(), PetDto[].class);
        return List.of(pets);
    }

    public PetDto create(final PetDto pet) {
        final ResponseVo responseVo = petstoreClient.createPet(extentTest, pet);
        logResponse(responseVo);
        return DeserializationUtils.deserialize(responseVo.getResponseJson(), PetDto.class);
    }

    public PetDto getById(final Long petId) {
        final ResponseVo responseVo = petstoreClient.getPet(extentTest, petId);
        logResponse(responseVo);
        return DeserializationUtils.deserialize(responseVo.getResponseJson(), PetDto.class);
    }

    public ResponseVo getByIdRaw(final Long petId) {
        final ResponseVo responseVo = petstoreClient.getPet(extentTest, petId);
        logResponse(responseVo);
        return responseVo;
    }

    public ApiResponseDto delete(final Long petId) {
        final ResponseVo responseVo = petstoreClient.deletePet(extentTest, petId);
        logResponse(responseVo);
        return DeserializationUtils.deserialize(responseVo.getResponseJson(), ApiResponseDto.class);
    }

    private void logResponse(final ResponseVo responseVo) {
        if (StringUtils.isEmpty(responseVo.getResponseJson())) {
            throw new RestClientException("Empty response body from " + responseVo.getUrl());
        }
        ExtentReporterUtils.logDuration(extentTest, responseVo.getDuration());
        ExtentReporterUtils.logResponse(extentTest, responseVo.getResponseJson());
    }

}