package com.wg_planner.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Resident extends Account implements Cloneable {

    private Boolean away = false;

    @NotNull
    @NotEmpty
    @OneToOne
    private Room room;

    public Resident() {
    }

    public Resident(@NotNull @NotEmpty Room room) {
        this.room = room;
    }

    @Id
    public Long getId() {
        return super.getId();
    }

    public Boolean isAway() {
        return away;
    }

    public void setAway(Boolean away) {
        this.away = away;
    }


}
