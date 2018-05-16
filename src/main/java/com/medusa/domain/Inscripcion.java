package com.medusa.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.medusa.domain.enumeration.Estado_inscripcion;

/**
 * A Inscripcion.
 */
@Entity
@Table(name = "inscripcion")
public class Inscripcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado_inscripcion estado;

    @ManyToOne
    private Rayaton rayaton;

    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estado_inscripcion getEstado() {
        return estado;
    }

    public Inscripcion estado(Estado_inscripcion estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado_inscripcion estado) {
        this.estado = estado;
    }

    public Rayaton getRayaton() {
        return rayaton;
    }

    public Inscripcion rayaton(Rayaton rayaton) {
        this.rayaton = rayaton;
        return this;
    }

    public void setRayaton(Rayaton rayaton) {
        this.rayaton = rayaton;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Inscripcion cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        Inscripcion inscripcion = (Inscripcion) o;
        if (inscripcion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), inscripcion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
