package com.at.client.rest.dto;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;
import java.util.Arrays;

@RequiredArgsConstructor
@JsonAdapter(Status.Deserializer.class)
public enum Status {
	@SerializedName("available")
	AVAILABLE("available"),
	@SerializedName("pending")
	PENDING("pending"),
	@SerializedName("sold")
	SOLD("sold"),
	UNKNOWN("unknown");

	public final String value;

	static class Deserializer implements JsonDeserializer<Status> {
		@Override
		public Status deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) {
			return Arrays.stream(Status.values())
					.filter(status -> status.value.equals(json.getAsString()))
					.findFirst()
					.orElse(Status.UNKNOWN);
		}
	}
}
