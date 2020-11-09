package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.AccountRepository;
import com.wg_planner.backend.Repository.ResidentAccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service

public class ResidentAccountService {
    private static final Logger LOGGER = Logger.getLogger(ResidentAccountService.class
            .getName());
    private final ResidentAccountRepository residentAccountRepository;
    private final RoomRepository roomRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public ResidentAccountService(ResidentAccountRepository residentAccountRepository, RoomRepository roomRepository, AccountRepository accountRepository) {
        this.residentAccountRepository = residentAccountRepository;
        this.roomRepository = roomRepository;
        this.accountRepository = accountRepository;
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
        return (ResidentAccount) accountRepository.findAccountByUsername(username);
    }

    public Room getMyRoom(ResidentAccount residentAccount) {
        return roomRepository.getMyRoom(residentAccount.getId());
    }

    public void save(ResidentAccount residentAccount) {
        if(residentAccount == null) {
            LOGGER.log(Level.SEVERE, "ResidentAccount Service: residentAccount passed to save is null");
            return;
        }
        residentAccountRepository.save(residentAccount);
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
