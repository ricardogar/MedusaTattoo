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
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @Column(name = "telefono", length = 10, nullable = false)
    private String telefono;

    @NotNull
    @Lob
    @Column(name = "imagen", nullable = false)
    private byte[] imagen;

    @Column(name = "imagen_content_type", nullable = false)
    private String imagenContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado_inscripcion estado;

    @ManyToOne
    private Rayaton rayaton;

    @ManyToOne
    private Sede sede;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Inscripcion nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public Inscripcion telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public Inscripcion imagen(byte[] imagen) {
        this.imagen = imagen;
        return this;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return imagenContentType;
    }

    public Inscripcion imagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
        return this;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
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

    public Sede getSede() {
        return sede;
    }

    public Inscripcion sede(Sede sede) {
        this.sede = sede;
        return this;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
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
            ", nombre='" + getNombre() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", imagenContentType='" + getImagenContentType() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
