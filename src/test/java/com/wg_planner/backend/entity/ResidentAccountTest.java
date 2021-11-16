package com.wg_planner.backend.entity;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Disabled
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ResidentAccountTest {
    @Autowired
    ResidentAccountService residentAccountService;
    @Autowired
    FloorService floorService;
    @Autowired
    RoomService roomService;

    Floor testFloor = new Floor.FloorBuilder("3A", CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE)).build();
    Room testRoom = new Room("310", testFloor);
    Room testRoom2 = new Room("311", testFloor);

    @Before
    public void setUp() {
        floorService.save(testFloor);
//        roomService.save(testRoom);
//        roomService.save(testRoom2);
    }

    @Test
    public void ResidentAccount_ValidParameters_AccountCreatedSavedAndReturned() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        ResidentAccount residentAccount = new ResidentAccount("testValid_first_name",
                "testValid_last_name",
                "testValid@testCreate.com", "testValid_username", encoder.encode(
                "testValid_password"), testRoom,
                false, authorities);
        residentAccountService.save(residentAccount);
        ResidentAccount residentAccountCreatedRetrieved =
                residentAccountService.getResidentAccountByUsername(
                        "testValid_username");
        Assert.assertEquals(residentAccount, residentAccountCreatedRetrieved);
    }

    @Test
    public void ResidentAccount_ValidParameters_AccountCreatedAndReturnedWithAllFieldsValid() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        ResidentAccount residentAccount = new ResidentAccount("testValid_first_name",
                "testValid_last_name",
                "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), testRoom,
                false, authorities);
        residentAccountService.save(residentAccount);
        ResidentAccount residentAccountCreatedRetrieved = residentAccountService.getResidentAccountByUsername("testValid_username");
//        System.out.println(residentAccount);
//        System.out.println(residentAccountCreatedRetrieved);
        Assert.assertEquals("testValid_first_name", residentAccountCreatedRetrieved.getFirstName());
        Assert.assertEquals("testValid_last_name", residentAccountCreatedRetrieved.getLastName());
        Assert.assertEquals("testValid@testCreate.com".toLowerCase(),
                residentAccountCreatedRetrieved.getEmail());
        Assert.assertEquals("testValid_username", residentAccountCreatedRetrieved.getUsername());
        Assert.assertTrue(encoder.matches("testValid_password",
                residentAccountCreatedRetrieved.getPassword()));
        Assert.assertEquals(testRoom, residentAccountCreatedRetrieved.getRoom());
        Assert.assertEquals(testFloor, residentAccountCreatedRetrieved.getRoom().getFloor());
        Assert.assertEquals(authorities.toString(),
                residentAccountCreatedRetrieved.getAuthorities().toString());
    }

    @Test
    public void ResidentAccount_CreateWithRoomNull_RuntimeExceptionThrown() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        Assert.assertThrows(RuntimeException.class, () -> new ResidentAccount("testValid_first_name",
                "testValid_last_name",
                "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), null,
                false, authorities));
    }

    @Test
    public void ResidentAccount_SetRoomNull_RuntimeExceptionThrown() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        ResidentAccount testResidentAccount = new ResidentAccount("testValid_first_name",
                "testValid_last_name",
                "testValid@testCreate.com", "testValid_username", encoder.encode(
                "testValid_password"), testRoom,
                false, authorities);
        Assert.assertThrows(RuntimeException.class, () -> testResidentAccount.setRoom(null));
    }

//    @Test
//    @Rollback
//    public void Account_CreateWithEmailRedundant_ThrowRuntimeException() {
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        authorities.add(new SimpleGrantedAuthority("USER"));
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        ResidentAccount residentAccount = new ResidentAccount("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), testRoom, false, authorities);
//        residentAccountService.save(residentAccount);
//        List<GrantedAuthority> authorities_redundant = new ArrayList<GrantedAuthority>();
//        authorities_redundant.add(new SimpleGrantedAuthority("USER"));
//        ResidentAccount residentAccountRedundant = new ResidentAccount("testValid_first_name_redundant", "testValid_last_name_redundant", "testValid@testCreate.com", "testValid_username_redundant", encoder.encode("testValid_password_redundant"),testRoom2, false,authorities_redundant);
////        residentAccountService.save(residentAccountRedundant);
//        Assert.assertThrows(RuntimeException.class, () -> residentAccountService.save(residentAccountRedundant));
//    }

    @Test
    public void ResidentAccount_setAway_awayCorrectSetSavedAndReturned() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        ResidentAccount residentAccount = new ResidentAccount("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), testRoom, false, authorities);
        residentAccountService.save(residentAccount);
        residentAccount.setAway(true);
//        residentAccountService.save(residentAccount);
        Assert.assertTrue(residentAccount.isAway());
        residentAccount.setAway(false);
//        residentAccountService.save(residentAccount);
        Assert.assertFalse(residentAccount.isAway());
    }
    @Test
    public void ResidentAccount_changeRoom_ReturnsUpdatedRoom() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        ResidentAccount testResidentAccount = new ResidentAccount("testValid_first_name",
                "testValid_last_name",
                "testValid@testCreate.com", "testValid_username", encoder.encode(
                "testValid_password"), testRoom,
                false, authorities);
        residentAccountService.save(testResidentAccount);
        testResidentAccount.setRoom(testRoom2);
        testRoom2.setResidentAccount(testResidentAccount);
//        roomService.save(testRoom2);
//        residentAccountService.save(testResidentAccount);
        Assert.assertEquals(testRoom2, testResidentAccount.getRoom());
    }

}
