package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.ResidentAccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service

public class ResidentAccountService {
    private final ResidentAccountRepository residentAccountRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public ResidentAccountService(ResidentAccountRepository residentAccountRepository, RoomRepository roomRepository) {
        this.residentAccountRepository = residentAccountRepository;
        this.roomRepository = roomRepository;
    }

    public ResidentAccount getResidentAccount(Long accountId) {
        return residentAccountRepository.getResidentAccount(accountId);
    }

    public ResidentAccount getResidentAccountByRoom(Room room) {
//        return residentAccountRepository.findAll().stream().filter(residentAccount -> residentAccount.getRoom().getId().equals(room.getId())).findFirst().get();
        List<ResidentAccount> residentAccounts = residentAccountRepository.findAll();
        Long id = room.getId();

        return residentAccountRepository.getResidentAccountByRoom(room.getId());
    }

    public ResidentAccount getResidentAccountByUsername(String username) {
        return residentAccountRepository.getResidentAccountByUsername(username);
    }

    public Room getMyRoom(ResidentAccount residentAccount) {
        return roomRepository.getMyRoom(residentAccount.getId());
    }

//    @PostConstruct
//    public void populateTestData() {
//        List<Room> rooms = roomRepository.findAll();
//        roomRepository.findAll().forEach(room -> residentAccountRepository.save(new ResidentAccount(room)));
//        List<Account> residentAccounts = residentAccountRepository.findAll();
//        Room room = roomRepository.search("311");
//        ResidentAccount residentAccount = new ResidentAccount(room);
//        residentAccountRepository.save(residentAccount);
//    }
}
