package com.hospital.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String numberPhone;


}
