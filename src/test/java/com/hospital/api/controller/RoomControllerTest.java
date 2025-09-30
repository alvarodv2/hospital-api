package com.hospital.api.controller;

import com.hospital.api.dto.CreateRoomDto;
import com.hospital.api.dto.RoomResponseDto;
import com.hospital.api.dto.UpdateRoomDto;
import com.hospital.api.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("RoomController - Unit Tests")
@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private RoomResponseDto testRoom;
    private List<RoomResponseDto> testRooms;

    @BeforeEach
    void setUp() {
        testRoom = new RoomResponseDto(1L, "Examination Room 101", "First Floor, West Wing");
        testRooms = Arrays.asList(testRoom);
    }

    @Test
    @DisplayName("Should return list of rooms")
    void getAllRooms_ShouldReturnListOfRooms() {
        when(roomService.getAllRooms()).thenReturn(testRooms);

        ResponseEntity<List<RoomResponseDto>> response = roomController.getAllRooms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRooms, response.getBody());
        verify(roomService).getAllRooms();
    }

    @Test
    @DisplayName("Should return room by ID")
    void getRoomById_ShouldReturnRoom() {
        when(roomService.getRoomById(1L)).thenReturn(testRoom);

        ResponseEntity<RoomResponseDto> response = roomController.getRoomById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoom, response.getBody());
        verify(roomService).getRoomById(1L);
    }

    @Test
    @DisplayName("Should create room and return CREATED status")
    void createRoom_ShouldReturnCreatedRoom() {
        when(roomService.createRoom(any(CreateRoomDto.class))).thenReturn(testRoom);

        CreateRoomDto dto = new CreateRoomDto("Examination Room 101", "First Floor, West Wing");
        ResponseEntity<RoomResponseDto> response = roomController.createRoom(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testRoom, response.getBody());
        verify(roomService).createRoom(dto);
    }

    @Test
    @DisplayName("Should update room and return updated room")
    void updateRoom_ShouldReturnUpdatedRoom() {
        when(roomService.updateRoom(eq(1L), any(UpdateRoomDto.class))).thenReturn(testRoom);

        UpdateRoomDto dto = new UpdateRoomDto("Updated Name", "Updated Location");
        ResponseEntity<RoomResponseDto> response = roomController.updateRoom(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoom, response.getBody());
        verify(roomService).updateRoom(1L, dto);
    }

    @Test
    @DisplayName("Should delete room and return NO_CONTENT status")
    void deleteRoom_ShouldReturnNoContent() {
        doNothing().when(roomService).deleteRoom(1L);

        ResponseEntity<Void> response = roomController.deleteRoom(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roomService).deleteRoom(1L);
    }


}
