package com.hospital.api.controller;

import com.hospital.api.dto.CreateRoomDto;
import com.hospital.api.dto.RoomResponseDto;
import com.hospital.api.dto.UpdateRoomDto;
import com.hospital.api.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "API for managing hospital rooms")
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Get all rooms", description = "Retrieve a list of all rooms")
    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @Operation(summary = "Get room by ID", description = "Retrieve a room by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @Operation(summary = "Create a room", description = "Add a new room to the hospital")
    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody CreateRoomDto dto) {
        RoomResponseDto createdRoom = roomService.createRoom(dto);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @Operation(summary = "Update room", description = "Update an existing room by ID")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long id, @RequestBody UpdateRoomDto dto) {
        return ResponseEntity.ok(roomService.updateRoom(id, dto));
    }

    @Operation(summary = "Delete room", description = "Delete a room by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }


}
