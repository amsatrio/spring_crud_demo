package io.github.amsatrio.spring_crud_demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class LoginRequest {
    @NotBlank
    @NonNull
    private String username;
    @NotBlank
    @NonNull
    private String password;
}
