package com.hospital.api.controller;

import com.hospital.api.entity.Room;
import com.hospital.api.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private Room testRoom;
    private List<Room> testRooms;

    @BeforeEach
    void setUp() {
        testRoom = Room.builder()
                .id(1L)
                .name("Examination Room 101")
                .location("First Floor, West Wing")
                .build();

        testRooms = Arrays.asList(testRoom);
    }

    @Test
    void getAllRooms_ShouldReturnListOfRooms() {
        when(roomService.getAllRooms()).thenReturn(testRooms);

        ResponseEntity<List<Room>> response = roomController.getAllRooms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRooms, response.getBody());
        verify(roomService).getAllRooms();
    }

    @Test
    void getRoomById_ShouldReturnRoom() {
        when(roomService.getRoomById(1L)).thenReturn(testRoom);

        ResponseEntity<Room> response = roomController.getRoomById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoom, response.getBody());
        verify(roomService).getRoomById(1L);
    }

    @Test
    void createRoom_ShouldReturnCreatedRoom() {
        when(roomService.createRoom(any(Room.class))).thenReturn(testRoom);

        ResponseEntity<Room> response = roomController.createRoom(testRoom);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testRoom, response.getBody());
        verify(roomService).createRoom(testRoom);
    }

    @Test
    void updateRoom_ShouldReturnUpdatedRoom() {
        when(roomService.updateRoom(eq(1L), any(Room.class))).thenReturn(testRoom);

        ResponseEntity<Room> response = roomController.updateRoom(1L, testRoom);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRoom, response.getBody());
        verify(roomService).updateRoom(1L, testRoom);
    }

    @Test
    void deleteRoom_ShouldReturnNoContent() {
        doNothing().when(roomService).deleteRoom(1L);

        ResponseEntity<Void> response = roomController.deleteRoom(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roomService).deleteRoom(1L);
    }


}