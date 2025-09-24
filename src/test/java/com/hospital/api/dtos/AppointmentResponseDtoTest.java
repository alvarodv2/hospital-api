package com.hospital.api.dtos;

import com.hospital.api.dto.AppointmentResponseDto;
import com.hospital.api.dto.summary.DoctorSummaryDto;
import com.hospital.api.dto.summary.PatientSummaryDto;
import com.hospital.api.dto.summary.RoomSummaryDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AppointmentResponseDtoTest {

    @Test
    void shouldCreateAppointmentResponseDtoCorrectly() {
        AppointmentResponseDto dto = new AppointmentResponseDto(
                1L,
                LocalDateTime.now(),
                "Checkup notes",
                new PatientSummaryDto(2L, "John", "Doe"),
                new DoctorSummaryDto(3L, "Dr. Smith", "Cardiology"),
                new RoomSummaryDto(4L, "101")
        );

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNotes()).isEqualTo("Checkup notes");
        assertThat(dto.getPatient().getFirstName()).isEqualTo("John");
        assertThat(dto.getPatient().getLastName()).isEqualTo("Doe");
        assertThat(dto.getDoctor().getSpecialty()).isEqualTo("Cardiology");
        assertThat(dto.getRoom().getRoomNumber()).isEqualTo("101");
    }
}
