package com.wg_planner.backend.entity;

import com.wg_planner.backend.Repository.AccountRepository;
import com.wg_planner.backend.Repository.FloorRepository;
import com.wg_planner.backend.Repository.ResidentAccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.Service.ResidentAccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
//@RunWith( SpringRunner.class )
@Transactional
public class ResidentAccountServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private ResidentAccountRepository testResidentAccountRepository;
    @Autowired
    private FloorRepository testFloorRepository;
    @Autowired
    private RoomRepository testRoomRepository;
    @Autowired
    private AccountRepository testAccountRepository;
    @Autowired
    ResidentAccountService testResidentAccountService;

    Floor testFloor = new Floor.FloorBuilder("3A", "9", "300").build();
    Room testRoom = new Room("310", testFloor);

    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    ResidentAccount testResidentAccount;

    @Before
    public void setUp() {
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        testResidentAccount = new ResidentAccount("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), testRoom, false, authorities);
        testFloorRepository.save(testFloor);
        testRoomRepository.save(testRoom);
        testResidentAccountRepository.save(testResidentAccount);
    }

    @Test
    public void ResidentAccountService_ConstructorNullParameter_ThrowsRuntimeException() {
        Assert.assertThrows(RuntimeException.class, () -> {
            new ResidentAccountService(null, testRoomRepository, testAccountRepository);
        });
        Assert.assertThrows(RuntimeException.class, () -> {
            new ResidentAccountService(testResidentAccountRepository, null, testAccountRepository);
        });
        Assert.assertThrows(RuntimeException.class, () -> {
            new ResidentAccountService(testResidentAccountRepository, testRoomRepository, null);
        });
    }

    @Test
    public void ResidentAccountService_getResidentAccountByRoomNullParameter_throwsRuntimeException() {
        Assert.assertThrows(RuntimeException.class, () -> {
            testResidentAccountService.getResidentAccountByRoom(null);
        });
    }

    @Test
    public void ResidentAccountService_getRoomByResidentAccountNullParameter_throwsRuntimeException() {
        Assert.assertThrows(RuntimeException.class, () -> {
            testResidentAccountService.getRoomByResidentAccount(null);
        });
    }

    @Test
    public void ResidentAccountService_getResidentAccountByUsernameNullParameter_throwsRuntimeException() {
        Assert.assertThrows(RuntimeException.class, () -> {
            testResidentAccountService.getResidentAccountByUsername(null);
        });
    }

    @Test
    public void ResidentAccountService_getResidentAccountByUsernameEmptyParameter_throwsRuntimeException() {
        Assert.assertThrows(RuntimeException.class, () -> {
            testResidentAccountService.getResidentAccountByUsername("");
        });
    }

    @Test
    public void ResidentAccountService_saveNullParameter_throwsRuntimeException() {
        Assert.assertThrows(RuntimeException.class, () -> {
            testResidentAccountService.save(null);
        });
    }

    @Test
    @Transactional
    public void ResidentAccount_ValidParameters_AccountCreatedAndReturned() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        ResidentAccount residentAccount = new ResidentAccount("testValid_first_name",
                "testValid_last_name",
                "testValid@testCreate.com", "testValid_username_redundant", encoder.encode(
                "testValid_password"), testRoom,
                false, authorities);
        testResidentAccountService.save(residentAccount);
        ResidentAccount residentAccountCreatedRetrieved =
                testResidentAccountService.getResidentAccountByUsername(
                        "testValid_username_redundant");
        Assert.assertEquals(residentAccount, residentAccountCreatedRetrieved);
    }
}
