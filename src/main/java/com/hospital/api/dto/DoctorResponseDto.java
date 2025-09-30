package com.hospital.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for doctor information response")
public class DoctorResponseDto {

    @Schema(description = "Doctor's unique identifier")
    private Long id;

    @Schema(description = "Doctor's first name")
    private String firstName;

    @Schema(description = "Doctor's last name")
    private String lastName;

    @Schema(description = "Doctor's email address")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String email;

    @Schema(description = "Doctor's medical specialty")
    private String specialty;

}
