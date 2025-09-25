package com.hospital.api.repository;

import com.hospital.api.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void testSaveAndFindById() {
        Room room = Room.builder()
                .name("Room A")
                .location("First Floor")
                .build();

        room = roomRepository.save(room);

        Optional<Room> found = roomRepository.findById(room.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Room A");
        assertThat(found.get().getLocation()).isEqualTo("First Floor");
    }

    @Test
    void testFindAll() {
        Room room1 = Room.builder().name("Room A").location("First Floor").build();
        Room room2 = Room.builder().name("Room B").location("Second Floor").build();

        roomRepository.save(room1);
        roomRepository.save(room2);

        List<Room> rooms = roomRepository.findAll();

        assertThat(rooms).hasSize(2);
    }

    @Test
    void testDelete() {
        Room room = Room.builder()
                .name("Room C")
                .location("Third Floor")
                .build();

        room = roomRepository.save(room);

        roomRepository.deleteById(room.getId());

        assertThat(roomRepository.findById(room.getId())).isEmpty();
    }

}
