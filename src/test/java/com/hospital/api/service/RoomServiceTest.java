package com.hospital.api.service;

import com.hospital.api.dto.CreateRoomDto;
import com.hospital.api.dto.RoomResponseDto;
import com.hospital.api.dto.UpdateRoomDto;
import com.hospital.api.entity.Room;
import com.hospital.api.exception.notfound.RoomNotFoundException;
import com.hospital.api.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room1;
    private Room room2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        room1 = Room.builder()
                .id(1L)
                .name("Room A")
                .location("First Floor")
                .build();

        room2 = Room.builder()
                .id(2L)
                .name("Room B")
                .location("Second Floor")
                .build();
    }

    @Test
    void getAllRooms_ShouldReturnListOfRooms() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2));

        List<RoomResponseDto> rooms = roomService.getAllRooms();

        assertThat(rooms).hasSize(2);
        assertThat(rooms.get(0).getName()).isEqualTo("Room A");
        assertThat(rooms.get(1).getName()).isEqualTo("Room B");
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void getRoomById_WhenExists_ShouldReturnRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room1));

        RoomResponseDto found = roomService.getRoomById(1L);

        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getName()).isEqualTo("Room A");
        verify(roomRepository, times(1)).findById(1L);
    }

    @Test
    void getRoomById_WhenNotFound_ShouldThrowException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(1L));
        verify(roomRepository, times(1)).findById(1L);
    }

    @Test
    void createRoom_ShouldSaveAndReturnRoom() {
        CreateRoomDto dto = new CreateRoomDto("Room A", "First Floor");
        when(roomRepository.save(any(Room.class))).thenReturn(room1);

        RoomResponseDto created = roomService.createRoom(dto);

        assertThat(created.getName()).isEqualTo("Room A");
        assertThat(created.getLocation()).isEqualTo("First Floor");
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void updateRoom_WhenExists_ShouldUpdateAndReturnRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room1));
        when(roomRepository.save(any(Room.class))).thenReturn(room1);

        UpdateRoomDto updates = new UpdateRoomDto("Updated Room", "Third Floor");

        RoomResponseDto updated = roomService.updateRoom(1L, updates);

        assertThat(updated.getName()).isEqualTo("Updated Room");
        assertThat(updated.getLocation()).isEqualTo("Third Floor");
        verify(roomRepository, times(1)).findById(1L);
        verify(roomRepository, times(1)).save(room1);
    }

    @Test
    void updateRoom_WhenNotFound_ShouldThrowException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateRoomDto updates = new UpdateRoomDto("Name", "Loc");

        assertThrows(RoomNotFoundException.class, () -> roomService.updateRoom(1L, updates));
        verify(roomRepository, times(1)).findById(1L);
    }

    @Test
    void deleteRoom_WhenExists_ShouldDeleteRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room1));
        doNothing().when(roomRepository).delete(room1);

        roomService.deleteRoom(1L);

        verify(roomRepository, times(1)).findById(1L);
        verify(roomRepository, times(1)).delete(room1);
    }

    @Test
    void deleteRoom_WhenNotFound_ShouldThrowException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.deleteRoom(1L));
        verify(roomRepository, times(1)).findById(1L);
    }


}
