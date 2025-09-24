package com.hospital.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentDto {

    private LocalDateTime appointmentDate;
    private String notes;
    private Long doctorId;
    private Long patientId;
    private Long roomId;


}
