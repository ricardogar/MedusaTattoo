package com.medusa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PalabraClave.
 */
@Entity
@Table(name = "palabra_clave")
public class PalabraClave implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "palabra", nullable = false)
    private String palabra;

    @ManyToMany(mappedBy = "palabraClaves")
    @JsonIgnore
    private Set<Foto> fotos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPalabra() {
        return palabra;
    }

    public PalabraClave palabra(String palabra) {
        this.palabra = palabra;
        return this;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public Set<Foto> getFotos() {
        return fotos;
    }

    public PalabraClave fotos(Set<Foto> fotos) {
        this.fotos = fotos;
        return this;
    }

    public PalabraClave addFoto(Foto foto) {
        this.fotos.add(foto);
        foto.getPalabraClaves().add(this);
        return this;
    }

    public PalabraClave removeFoto(Foto foto) {
        this.fotos.remove(foto);
        foto.getPalabraClaves().remove(this);
        return this;
    }

    public void setFotos(Set<Foto> fotos) {
        this.fotos = fotos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PalabraClave palabraClave = (PalabraClave) o;
        if (palabraClave.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), palabraClave.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PalabraClave{" +
            "id=" + getId() +
            ", palabra='" + getPalabra() + "'" +
            "}";
    }
}
