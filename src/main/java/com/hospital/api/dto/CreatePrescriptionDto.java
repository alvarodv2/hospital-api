package com.hospital.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePrescriptionDto {

    private String medication;
    private String instructions;
    private Long appointmentId;

}