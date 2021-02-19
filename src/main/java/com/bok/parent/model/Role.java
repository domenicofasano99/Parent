package com.bok.parent.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
public class Role {

    public static final String USER = "USER";

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    public Role() {
        //hibernate
    }

    public Role(String name) {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
