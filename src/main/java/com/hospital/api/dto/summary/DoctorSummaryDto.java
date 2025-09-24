package com.hospital.api.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSummaryDto {
    private Long id;
    private String fullName;
    private String specialty;
}
