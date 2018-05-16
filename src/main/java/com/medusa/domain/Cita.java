package com.medusa.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Cita.
 */
@Entity
@Table(name = "cita")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha_y_hora", nullable = false)
    private Instant fechaYHora;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    @Column(name = "duracion", nullable = false)
    private Integer duracion;

    @ManyToOne(optional = false)
    @NotNull
    private Trabajo trabajo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaYHora() {
        return fechaYHora;
    }

    public Cita fechaYHora(Instant fechaYHora) {
        this.fechaYHora = fechaYHora;
        return this;
    }

    public void setFechaYHora(Instant fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public Cita duracion(Integer duracion) {
        this.duracion = duracion;
        return this;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public Cita trabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
        return this;
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
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
        Cita cita = (Cita) o;
        if (cita.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cita.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cita{" +
            "id=" + getId() +
            ", fechaYHora='" + getFechaYHora() + "'" +
            ", duracion=" + getDuracion() +
            "}";
    }
}
