package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.AccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, RoomRepository roomRepository) {
        this.accountRepository = accountRepository;
        this.roomRepository = roomRepository;
    }

    public Account getAccount(Long accountId) {
        return accountRepository.getAccount(accountId);
    }

//    @PostConstruct
//    public void populateTestData() {
//        Room room = roomRepository.search("311");
////        if(room )
//        Account account = new Account();
//        Room room1 = new Room("311");
//        account.setRoom(room1);
////        accountRepository.save(account);
//    }

//    public Room getMyRoom() {
//        return accountRepository.findMyAccount().getRoom();
//    }
}
