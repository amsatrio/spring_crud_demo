package io.github.amsatrio.spring_crud_demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class RefreshTokenRequest {
    @NotBlank
    @NonNull
    private String mainToken;

    @NotBlank
    @NonNull
    private String refreshToken;
}
