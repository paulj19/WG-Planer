package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.AccountRepository;
import com.wg_planner.backend.Repository.ResidentAccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        Validate.notNull(accountId, "parameter account id must not be %s", null);
        return residentAccountRepository.getResidentAccount(accountId);
    }

    public ResidentAccount getResidentAccountByRoom(Room room) {
        Validate.notNull(room, "parameter room must not be %s", null);
        return residentAccountRepository.getResidentAccountByRoom(room.getId());
    }

    public ResidentAccount getResidentAccountByUsername(String username) {
        Validate.notNull(username, "parameter username must not be %s", null);
        Validate.notEmpty(username, "parameter username must not be empty");
        return (ResidentAccount) accountRepository.findAccountByUsername(username);
    }

    public Room getMyRoom(ResidentAccount residentAccount) {
        Validate.notNull(residentAccount, "parameter resident account must not be %s", null);
        return roomRepository.getMyRoom(residentAccount.getId());
    }

    public void save(ResidentAccount residentAccount) {
        Validate.notNull(residentAccount, "parameter resident account must not be %s", null);
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
