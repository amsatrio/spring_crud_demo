package io.github.amsatrio.spring_crud_demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class RefreshTokenRequest {
    @NotBlank
    @NonNull
    @JsonProperty("main_token")
    private String mainToken;

    @NotBlank
    @NonNull
    @JsonProperty("refresh_token")
    private String refreshToken;
}
