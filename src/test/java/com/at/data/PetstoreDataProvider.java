package com.at.data;

import com.at.client.rest.dto.Status;
import lombok.experimental.UtilityClass;
import org.testng.annotations.DataProvider;

import java.util.Random;

@UtilityClass
public class PetstoreDataProvider {

	private static final Random RANDOM = new Random();
	private static final long PET_ID_LOWER_BOUND = 1_000_000L;
	private static final long PET_ID_UPPER_BOUND = 9_999_999L;

	@DataProvider(parallel = true)
	public static Object[][] petStatuses() {
		return new Object[][]{
				{Status.AVAILABLE},
				{Status.PENDING},
				{Status.SOLD},
		};
	}

	@DataProvider(parallel = true)
	public static Object[][] newPets() {
		return new Object[][]{
				{randomPetId(), "Rex", Status.AVAILABLE},
				{randomPetId(), "Buddy", Status.PENDING},
				{randomPetId(), "Milo", Status.SOLD},
		};
	}

	@DataProvider(parallel = true)
	public static Object[][] petsToDelete() {
		return new Object[][]{
				{randomPetId(), "Ghost", Status.AVAILABLE},
				{randomPetId(), "Shadow", Status.SOLD},
		};
	}

	@DataProvider(parallel = true)
	public static Object[][] nonExistentPetIds() {
		return new Object[][]{
				{Long.MAX_VALUE},
				{Long.MAX_VALUE - 1},
		};
	}

	private static long randomPetId() {
		return RANDOM.nextLong(PET_ID_LOWER_BOUND, PET_ID_UPPER_BOUND);
	}
}