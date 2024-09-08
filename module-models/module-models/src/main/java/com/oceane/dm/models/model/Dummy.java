package com.oceane.dm.models.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "dummy")
public class Dummy {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dummy dummy = (Dummy) o;
        return id.equals(dummy.id) && Objects.equals(name, dummy.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Dummy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
