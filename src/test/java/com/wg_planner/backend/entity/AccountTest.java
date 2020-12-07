package com.wg_planner.backend.entity;

import com.wg_planner.backend.Service.ResidentAccountService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(Enclosed.class)
@SpringBootTest
@ActiveProfiles("test")
//@RunWith( SpringRunner.class )
@Transactional

public class AccountTest {
    public static class AccountFieldTest {
        @Test
        public void ResidentAccount_CreateWithFirstNameNull_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account(null, "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void ResidentAccount_CreateWithFirstNameEmpty_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("", "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithFirstNameGT250Char_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account(StringUtils.repeat("a", 251), "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithLastNameNull_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", null, "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithLastNameEmpty_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "", "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithLastNameGT250Char_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", StringUtils.repeat("a", 251), "testValid@testCreate.com", "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithUsernameNull_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", null, encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithUsernameEmpty_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", "", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithUsernameGT250Char_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", StringUtils.repeat("a", 251), encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithPasswordNull_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode(null), authorities));
        }
        //todo
//        @Test
//        public void Account_CreateWithPasswordEmpty_RuntimeExceptionThrown() {
//            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//            authorities.add(new SimpleGrantedAuthority("USER"));
//            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "testValid@testCreate.com", "testValid_username", encoder.encode(""), authorities));
//        }

        @Test
        public void Account_CreateWithEmailNull_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", null, "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithEmailEmpty_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "", "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithAuthoritiesNull_RuntimeExceptionThrown() {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", null, "testValid_username", encoder.encode("testValid_password"), null));
        }

        @Test
        public void Account_CreateWithAuthoritiesEmpty_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "", "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        @Test
        public void Account_CreateWithEmailInvalid_RuntimeExceptionThrown() {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "example.com", "testValid_username", encoder.encode("testValid_password"), authorities));
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "A@b@c@domain.com", "testValid_username", encoder.encode("testValid_password"), authorities));
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "a”b(c)d,e:f;gi[j\\k]l@domain.com", "testValid_username", encoder.encode("testValid_password"), authorities));
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "abc”test”email@domain.com", "testValid_username", encoder.encode("testValid_password"), authorities));
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "abc is”not\\valid@domain.com", "testValid_username", encoder.encode("testValid_password"), authorities));
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "abc\\ is\\”not\\valid@domain.com", "testValid_username", encoder.encode("testValid_password"), authorities));
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", ".test@domain.com", "testValid_username", encoder.encode("testValid_password"), authorities));
            Assert.assertThrows(IllegalArgumentException.class, () -> new Account("testValid_first_name", "testValid_last_name", "test@domain..com", "testValid_username", encoder.encode("testValid_password"), authorities));
        }

        //todo verify creation of object
//        @Test
//        public void Account_CreateWithEmailValid_CreatesAccount() {
//            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//            authorities.add(new SimpleGrantedAuthority("USER"));
//            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//            Assert.assert(new Account("testValid_first_name", "testValid_last_name", "example.com", "testValid_username", encoder.encode("testValid_password"), authorities));
//            Assert.assertTrue(new Account("testValid_first_name", "testValid_last_name", "example.com", "testValid_username", encoder.encode("testValid_password"), authorities);
//            Assert.assertTrue(new Account("testValid_first_name", "testValid_last_name", "example.com", "testValid_username", encoder.encode("testValid_password"), authorities);
//            Assert.assertTrue(new Account("testValid_first_name", "testValid_last_name", "example.com", "testValid_username", encoder.encode("testValid_password"), authorities);
//            Assert.assertTrue(new Account("testValid_first_name", "testValid_last_name", "example.com", "testValid_username", encoder.encode("testValid_password"), authorities);
//            Assert.assertTrue(new Account("testValid_first_name", "testValid_last_name", "example.com", "testValid_username", encoder.encode("testValid_password"), authorities);
//            Assert.assertTrue(new Account("testValid_first_name", "testValid_last_name", "example.com", "testValid_username", encoder.encode("testValid_password"), authorities);
//        }
    }
}
