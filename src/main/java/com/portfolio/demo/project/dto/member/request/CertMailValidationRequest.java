package com.portfolio.demo.project.dto.member.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CertMailValidationRequest {
    @NotNull
    private Long memNo;
    @NotEmpty
    private String certKey;
}
