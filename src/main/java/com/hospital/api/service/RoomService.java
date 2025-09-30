package com.hospital.api.service;

import com.hospital.api.dto.CreateRoomDto;
import com.hospital.api.dto.RoomResponseDto;
import com.hospital.api.dto.UpdateRoomDto;
import com.hospital.api.entity.Room;
import com.hospital.api.exception.notfound.RoomNotFoundException;
import com.hospital.api.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<RoomResponseDto> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public RoomResponseDto getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(String.valueOf(id)));
        return toResponseDto(room);
    }

    public RoomResponseDto createRoom(CreateRoomDto dto) {
        Room room = Room.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .build();
        return toResponseDto(roomRepository.save(room));
    }

    public RoomResponseDto updateRoom(Long id, UpdateRoomDto dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(String.valueOf(id)));

        if (dto.getName() != null) room.setName(dto.getName());
        if (dto.getLocation() != null) room.setLocation(dto.getLocation());

        return toResponseDto(roomRepository.save(room));
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(String.valueOf(id)));
        roomRepository.delete(room);
    }

    private RoomResponseDto toResponseDto(Room room) {
        return new RoomResponseDto(
                room.getId(),
                room.getName(),
                room.getLocation()
        );
    }



}
