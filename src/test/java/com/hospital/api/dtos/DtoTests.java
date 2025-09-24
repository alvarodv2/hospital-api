package com.hospital.api.dtos;

import com.hospital.api.dto.CreateAppointmentDto;
import com.hospital.api.dto.CreatePatientDto;
import com.hospital.api.dto.UpdatePatientDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DtoTests {

    @Test
    void createPatientDto_shouldStoreDataCorrectly() {
        CreatePatientDto dto = new CreatePatientDto("John", "Doe", "john@example.com", "123456789");

        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
        assertThat(dto.getNumberPhone()).isEqualTo("123456789");
    }

    @Test
    void updatePatientDto_shouldStoreDataCorrectly() {
        UpdatePatientDto dto = new UpdatePatientDto("Jane", null, "jane@example.com", null);

        assertThat(dto.getFirstName()).isEqualTo("Jane");
        assertThat(dto.getLastName()).isNull();
        assertThat(dto.getEmail()).isEqualTo("jane@example.com");
        assertThat(dto.getNumberPhone()).isNull();
    }

    @Test
    void createAppointmentDto_shouldStoreDataCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        CreateAppointmentDto dto = new CreateAppointmentDto();
        dto.setAppointmentDate(now);
        dto.setDoctorId(1L);
        dto.setPatientId(2L);
        dto.setRoomId(3L);

        assertThat(dto.getAppointmentDate()).isEqualTo(now);
        assertThat(dto.getDoctorId()).isEqualTo(1L);
        assertThat(dto.getPatientId()).isEqualTo(2L);
        assertThat(dto.getRoomId()).isEqualTo(3L);
    }

}
