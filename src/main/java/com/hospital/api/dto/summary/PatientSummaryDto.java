package com.hospital.api.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientSummaryDto {
    private Long id;
    private String firstName;
    private String lastName;
}
