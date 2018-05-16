package com.medusa.domain;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class Secretaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Sede sede;


    @OneToOne
    @MapsId
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Secretaria sede(Sede sede) {
        this.sede = sede;
        return this;
    }

    public Secretaria user(User user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Secretaria secretaria = (Secretaria) o;
        if (secretaria.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), secretaria.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Secretaria{" +
            "id=" + getId() +
            ", sede='" + getSede().getNombre() + "'" +
            "}";
    }


}
