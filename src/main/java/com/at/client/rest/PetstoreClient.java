package com.at.client.rest;

import com.at.client.rest.dto.PetDto;
import com.at.client.rest.dto.Status;
import com.at.client.rest.vo.ResponseVo;
import com.at.exception.RestClientException;
import com.at.provider.ExtentReporterUtils;
import com.at.provider.KeyProvider;
import com.at.utils.SerializationUtils;
import com.at.utils.StringUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

public final class PetstoreClient {

    private static final PetstoreClient INSTANCE = new PetstoreClient();
    private static final String NO_BODY = "";

    private final HttpClient httpClient;
    private final String baseUrl;

    private PetstoreClient() {
        final long connectTimeoutMs = Long.parseLong(KeyProvider.loadProperty("com.at.rest.petstore.connect-timeout-ms"));
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .build();
        this.baseUrl = KeyProvider.loadProperty("com.at.rest.petstore.url");
    }

    public static PetstoreClient getInstance() {
        return INSTANCE;
    }

    public ResponseVo getPets(final ExtentTest extentTest, final Status status) {
        final HttpRequest request = httpRequest(extentTest, "/pet/findByStatus?status=" + status.value, "GET")
                .GET()
                .build();
        return send(request, NO_BODY);
    }

    public ResponseVo getPet(final ExtentTest extentTest, final Long petId) {
        final HttpRequest request = httpRequest(extentTest, "/pet/" + petId, "GET")
                .GET()
                .build();
        return send(request, NO_BODY);
    }

    public ResponseVo createPet(final ExtentTest extentTest, final PetDto pet) {
        final String serialized = SerializationUtils.serialize(pet);
        final String requestJson = StringUtils.isNotEmpty(serialized) ? serialized : NO_BODY;
        final HttpRequest.Builder builder = httpRequest(extentTest, "/pet", "POST");
        ExtentReporterUtils.logRequest(extentTest, requestJson);
        final HttpRequest request = builder
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();
        return send(request, requestJson);
    }

    public ResponseVo deletePet(final ExtentTest extentTest, final Long petId) {
        final HttpRequest request = httpRequest(extentTest, "/pet/" + petId, "DELETE")
                .DELETE()
                .build();
        return send(request, NO_BODY);
    }

    private HttpRequest.Builder httpRequest(final ExtentTest extentTest, final String path, final String method) {
        final String url = baseUrl + path;
        extentTest.info(MarkupHelper.createLabel(method + " " + url, ExtentColor.BLUE));
        return HttpRequest.newBuilder().uri(URI.create(url));
    }

    private ResponseVo send(final HttpRequest request, final String requestJson) {
        final String url = request.uri().toString();
        final Instant start = Instant.now();
        try {
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return ResponseVo.builder()
                    .url(url)
                    .requestJson(requestJson)
                    .duration(Duration.between(start, Instant.now()))
                    .responseJson(response.body())
                    .statusCode(response.statusCode())
                    .build();
        } catch (IOException | InterruptedException e) {
            throw new RestClientException("Failed to call " + url, e);
        }
    }
}