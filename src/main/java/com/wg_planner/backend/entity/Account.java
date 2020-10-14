package com.wg_planner.backend.entity;

import com.wg_planner.backend.utils.AccountType;

import javax.persistence.*;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
public class Account extends AbstractEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    AccountType accountType;

    //TODO remove this
//    @OneToOne
//    @JoinColumn(name = "roomId")
//    private Room room;

    public Account() {
    }

    @Override
    public Long getId() {
        return id;
    }
//    public Account(Room room) {
//        this.room = room;
//    }

//    public Room getRoom() {
//        return room;
//    }

//    public void setRoom(Room room) {
//        this.room = room;
//    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
