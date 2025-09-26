package com.hospital.api.service;

import com.hospital.api.entity.Room;
import com.hospital.api.exception.notfound.RoomNotFoundException;
import com.hospital.api.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(String.valueOf(id)));
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(String.valueOf(id)));

        room.setName(roomDetails.getName());
        room.setLocation(roomDetails.getLocation());

        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(String.valueOf(id)));

        roomRepository.delete(room);
    }
}
