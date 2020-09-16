package com.wg_planner.backend.entity;

import javax.persistence.*;

@Entity
public class Resident extends Account implements Cloneable {
    @Id
    public Long getId() {
        return super.getId();
    }

}
