package com.medusa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Rayaton.
 */
@Entity
@Table(name = "rayaton")
public class Rayaton implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Max(value = 100000)
    @Column(name = "cupos", nullable = false)
    private Integer cupos;

    @NotNull
    @Min(value = 20000)
    @Max(value = 10000000)
    @Column(name = "valor_cupo", nullable = false)
    private Integer valorCupo;

    @Lob
    @Column(name = "comentario")
    private String comentario;

    @OneToMany(mappedBy = "rayaton")
    @JsonIgnore
    private Set<Inscripcion> inscripcions = new HashSet<>();

    @OneToMany(mappedBy = "rayaton")
    @JsonIgnore
    private Set<Trabajo> trabajos = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rayaton_tatuador",
               joinColumns = @JoinColumn(name="rayatons_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tatuadors_id", referencedColumnName="id"))
    private Set<Tatuador> tatuadors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Rayaton fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getCupos() {
        return cupos;
    }

    public Rayaton cupos(Integer cupos) {
        this.cupos = cupos;
        return this;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }

    public Integer getValorCupo() {
        return valorCupo;
    }

    public Rayaton valorCupo(Integer valorCupo) {
        this.valorCupo = valorCupo;
        return this;
    }

    public void setValorCupo(Integer valorCupo) {
        this.valorCupo = valorCupo;
    }

    public String getComentario() {
        return comentario;
    }

    public Rayaton comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Set<Inscripcion> getInscripcions() {
        return inscripcions;
    }

    public Rayaton inscripcions(Set<Inscripcion> inscripcions) {
        this.inscripcions = inscripcions;
        return this;
    }

    public Rayaton addInscripcion(Inscripcion inscripcion) {
        this.inscripcions.add(inscripcion);
        inscripcion.setRayaton(this);
        return this;
    }

    public Rayaton removeInscripcion(Inscripcion inscripcion) {
        this.inscripcions.remove(inscripcion);
        inscripcion.setRayaton(null);
        return this;
    }

    public void setInscripcions(Set<Inscripcion> inscripcions) {
        this.inscripcions = inscripcions;
    }

    public Set<Trabajo> getTrabajos() {
        return trabajos;
    }

    public Rayaton trabajos(Set<Trabajo> trabajos) {
        this.trabajos = trabajos;
        return this;
    }

    public Rayaton addTrabajo(Trabajo trabajo) {
        this.trabajos.add(trabajo);
        trabajo.setRayaton(this);
        return this;
    }

    public Rayaton removeTrabajo(Trabajo trabajo) {
        this.trabajos.remove(trabajo);
        trabajo.setRayaton(null);
        return this;
    }

    public void setTrabajos(Set<Trabajo> trabajos) {
        this.trabajos = trabajos;
    }

    public Set<Tatuador> getTatuadors() {
        return tatuadors;
    }

    public Rayaton tatuadors(Set<Tatuador> tatuadors) {
        this.tatuadors = tatuadors;
        return this;
    }

    public Rayaton addTatuador(Tatuador tatuador) {
        this.tatuadors.add(tatuador);
        tatuador.getRayatones().add(this);
        return this;
    }

    public Rayaton removeTatuador(Tatuador tatuador) {
        this.tatuadors.remove(tatuador);
        tatuador.getRayatones().remove(this);
        return this;
    }

    public void setTatuadors(Set<Tatuador> tatuadors) {
        this.tatuadors = tatuadors;
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
        Rayaton rayaton = (Rayaton) o;
        if (rayaton.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rayaton.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rayaton{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", cupos=" + getCupos() +
            ", valorCupo=" + getValorCupo() +
            ", comentario='" + getComentario() + "'" +
            "}";
    }
}
