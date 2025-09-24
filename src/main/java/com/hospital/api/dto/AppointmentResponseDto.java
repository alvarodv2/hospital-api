package com.hospital.api.dto;

import com.hospital.api.dto.summary.DoctorSummaryDto;
import com.hospital.api.dto.summary.PatientSummaryDto;
import com.hospital.api.dto.summary.RoomSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDto {

    private Long id;
    private LocalDateTime appointmentDate;
    private String notes;

    private PatientSummaryDto patient;
    private DoctorSummaryDto doctor;
    private RoomSummaryDto room;


}
