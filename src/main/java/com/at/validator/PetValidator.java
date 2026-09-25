package com.at.validator;

import com.at.client.rest.dto.ApiResponseDto;
import com.at.client.rest.dto.PetDto;
import com.at.client.rest.dto.Status;
import com.at.client.rest.vo.ResponseVo;
import com.at.utils.DeserializationUtils;
import com.at.utils.StringUtils;
import com.aventstack.extentreports.ExtentTest;
import lombok.experimental.UtilityClass;
import org.testng.asserts.SoftAssert;

import java.util.List;

@UtilityClass
public class PetValidator {

	// JUST DUMMY VALIDATION FOR STATUS
	public static void validate(final ExtentTest extentTest,
                                final List<PetDto> actual,
                                final Status expectedStatus) {
		final boolean allMatch = actual.stream().allMatch(pet -> expectedStatus.equals(pet.status()));

		if (!allMatch) {
			extentTest.fail("Some pets does not have expected status " + expectedStatus.value);
		} else {
			extentTest.pass("Validation PASSED");
		}
	}

	public static void validate(final PetDto actual, final PetDto expected) {
		final SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(actual.id(), expected.id(), "id");
		softAssert.assertEquals(actual.name(), expected.name(), "name");
		softAssert.assertEquals(actual.status(), expected.status(), "status");
		softAssert.assertAll();
	}

	public static void validateNotFound(final ExtentTest extentTest, final ResponseVo responseVo) {
		final SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(responseVo.getStatusCode(), 404, "status code");
		final ApiResponseDto error = DeserializationUtils.deserialize(responseVo.getResponseJson(), ApiResponseDto.class);
		softAssert.assertTrue(StringUtils.isNotEmpty(error.message()), "error message present");
		softAssert.assertAll();
		extentTest.pass("Validation PASSED");
	}
}