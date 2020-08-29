package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.AccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.roomRepository = roomRepository;
    }


    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room findByRoomNumber(String roomNumber) {
        if(roomNumber == null || roomNumber.isEmpty()) {
            return null;
        } else {
            return roomRepository.search(roomNumber);
        }
    }

    public long count() { return roomRepository.count(); }

    @PostConstruct
    public void populateTestData() {
        if(roomRepository.count() == 0) {
            roomRepository.saveAll(Stream.of("307", "308", "309", "310", "311", "312", "313", "314", "315")
                    .map(roomName -> {
                        Room room = new Room(roomName);
//                        room.setRoomNumber(roomName);
                        return room;
                    }).collect(Collectors.toList()));
        }

        Room room = roomRepository.search("311");
        Account account = new Account(room);
        accountRepository.save(account);
    }

}
