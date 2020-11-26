package com.wg_planner.backend.entity;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.RoomService;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
//@RunWith( SpringRunner.class )
@Transactional
public class ResidentAccountTest {
    @Autowired
    ResidentAccountService residentAccountService;
    @Autowired
    FloorService floorService;
    @Autowired
    RoomService roomService;

    Floor testFloor = new Floor.FloorBuilder("3A", "9", "300").build();
    Room testRoom = new Room("310", testFloor);

    @Before
    public void setUp() {
        floorService.save(testFloor);
        roomService.save(testRoom);
    }

    @Test
    public void ResidentAccount_ValidParameters_AccountCreatedAndReturned() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        ResidentAccount residentAccount = new ResidentAccount("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), testRoom, false, authorities);
        residentAccountService.save(residentAccount);
        ResidentAccount residentAccountCreatedRetrieved = residentAccountService.getResidentAccountByUsername("testValid_username");
        System.out.println(residentAccount);
        System.out.println(residentAccountCreatedRetrieved);
        Assert.assertEquals(residentAccount, residentAccountCreatedRetrieved);
    }

    @Test
    public void ResidentAccount_ValidParameters_AccountCreatedAndReturnedWithValidFields() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        ResidentAccount residentAccount = new ResidentAccount("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), testRoom, false, authorities);
        residentAccountService.save(residentAccount);
        ResidentAccount residentAccountCreatedRetrieved = residentAccountService.getResidentAccountByUsername("testValid_username");
        System.out.println(residentAccount);
        System.out.println(residentAccountCreatedRetrieved);
        Assert.assertEquals("testValid_first_name", residentAccountCreatedRetrieved.getFirstName());
        Assert.assertEquals("testValid_last_name", residentAccountCreatedRetrieved.getLastName());
        Assert.assertEquals("testValid@testCreate.com".toLowerCase(), residentAccountCreatedRetrieved.getEmail());
        Assert.assertEquals("testValid_username", residentAccountCreatedRetrieved.getUsername());
        Assert.assertTrue(encoder.matches("testValid_password", residentAccountCreatedRetrieved.getPassword()));
        Assert.assertEquals(testRoom, residentAccountCreatedRetrieved.getRoom());
        Assert.assertEquals(testFloor, residentAccountCreatedRetrieved.getRoom().getFloor());
        Assert.assertEquals(authorities.toString(), residentAccountCreatedRetrieved.getAuthorities().toString());
    }

}
