package com.at.rest;

import com.at.GenericIT;
import com.at.service.rest.PetstoreService;
import com.at.client.rest.dto.PetDto;
import com.at.client.rest.dto.Status;
import com.at.client.rest.vo.ResponseVo;
import com.at.data.PetstoreDataProvider;
import com.at.provider.Environment;
import com.at.provider.ExtentReporterProvider;
import com.at.utils.StringUtils;
import com.at.validator.PetValidator;
import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;

public class PetstoreIT extends GenericIT {

	@Test(
			dataProvider = "petStatuses",
			dataProviderClass = PetstoreDataProvider.class,
			groups = Environment.GROUP_ALL,
			description = "Simple GET against public Petstore API for each status dataset"
	)
	public void getPets(final Status status) {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		extentTest.info("Search by status " + status.value);
		final List<PetDto> pets = new PetstoreService(extentTest).getList(status);
		PetValidator.validate(extentTest, pets, status);
	}

	@Test(
			dataProvider = "newPets",
			dataProviderClass = PetstoreDataProvider.class,
			groups = {Environment.GROUP_DEV, Environment.GROUP_STG},
			description = "Simple POST against public Petstore API, verified by GET"
	)
	public void createPet(final Long id, final String name, final Status status) {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		final PetDto newPet = new PetDto(id, name, status);
		final PetstoreService petstoreService = new PetstoreService(extentTest);

		final PetDto createdPet = petstoreService.create(newPet);
		final PetDto fetchedPet = petstoreService.getById(createdPet.id());

		if (Objects.nonNull(fetchedPet) && StringUtils.isNotEmpty(fetchedPet.name())) {
			PetValidator.validate(fetchedPet, newPet);
		} else {
			extentTest.fail("Fetched pet has no name, response: " + fetchedPet);
		}
	}

	@Test(
			dataProvider = "petsToDelete",
			dataProviderClass = PetstoreDataProvider.class,
			groups = {Environment.GROUP_DEV, Environment.GROUP_STG},
			description = "Create a pet then delete it via DELETE, verify it no longer exists"
	)
	public void deletePet(final Long id, final String name, final Status status) {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		final PetstoreService petstoreService = new PetstoreService(extentTest);

		final PetDto createdPet = petstoreService.create(new PetDto(id, name, status));
		petstoreService.delete(createdPet.id());
		final ResponseVo afterDelete = petstoreService.getByIdRaw(createdPet.id());

		PetValidator.validateNotFound(extentTest, afterDelete);
	}

	@Test(
			dataProvider = "nonExistentPetIds",
			dataProviderClass = PetstoreDataProvider.class,
			groups = Environment.GROUP_ALL,
			description = "GET with a pet id that does not exist returns 404"
	)
	public void getPetNotFound(final Long nonExistentId) {
		final ExtentTest extentTest = ExtentReporterProvider.getTest();
		final ResponseVo responseVo = new PetstoreService(extentTest).getByIdRaw(nonExistentId);
		PetValidator.validateNotFound(extentTest, responseVo);
	}
}