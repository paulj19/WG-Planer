package com.wg_planner.backend.entity;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
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
@Transactional
public class TaskTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    TaskService taskService;
    @Autowired
    FloorService floorService;

    @Test
    public void Task_CreateInValidParams_RuntimeExceptionThrown() {
        Assert.assertThrows(RuntimeException.class, () -> new Task(null, createAndReturnFloor()));
        Assert.assertThrows(RuntimeException.class, () -> new Task(null, null));
        Assert.assertThrows(RuntimeException.class, () -> new Task("123", null));
        Assert.assertThrows(RuntimeException.class, () -> new Task(StringUtils.repeat("a", 251), createAndReturnFloor()));
        Assert.assertThrows(RuntimeException.class, () -> new Task("123", createAndReturnFloor(), null));
    }

    public Floor createAndReturnFloor() {
        return new Floor.FloorBuilder("3A", 9).build();
    }
    public ResidentAccount createAndReturnResidentAccount(Room testRoom) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new ResidentAccount("testValid_first_name",
                "testValid_last_name",
                "testvalid@testcreate.com", "testValid_username_redundant", encoder.encode(
                "testValid_password"), testRoom,
                false, authorities);
    }
}
