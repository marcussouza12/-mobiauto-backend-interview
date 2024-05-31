package com.mobiauto.service;

import com.mobiauto.data.ApiResponseDTO;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;

@Singleton
@Serdeable
@ConfigurationProperties("authorization")
public class AuthorizationService {

    public boolean isAuthorized(String apiKey) {
        String xApiKey = "1234567890";
        return !xApiKey.equals(apiKey);
    }

    public HttpResponse<ApiResponseDTO> getHttpResponse(String apiKey){
        if (isAuthorized(apiKey)) {
            return HttpResponse.status(HttpStatus.FORBIDDEN).body(new ApiResponseDTO("Invalid API key", null));
        }
        else {
            return HttpResponse.ok(new ApiResponseDTO("Success", null));
        }
    }

}
