package com.medusa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.medusa.domain.enumeration.Tipo_documento;

import com.medusa.domain.enumeration.Genero;

/**
 * A Tatuador.
 */
@Entity
@Table(name = "tatuador")
public class Tatuador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipodocumento", nullable = false)
    private Tipo_documento tipodocumento;

    @NotNull
    @Size(min = 8, max = 10)
    @Pattern(regexp = "^[1-9][0-9]*$")
    @Column(name = "documento", length = 10, nullable = false)
    private String documento;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    @Column(name = "telefono", length = 10, nullable = false)
    private String telefono;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private Genero genero;

    @NotNull
    @Column(name = "apodo", nullable = false)
    private String apodo;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @Column(name = "estado")
    private Boolean estado;

    @OneToMany(mappedBy = "tatuador")
    @JsonIgnore
    private Set<Trabajo> trabajos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Sede sede;

    @ManyToMany(mappedBy = "tatuadors")
    @JsonIgnore
    private Set<Rayaton> rayatones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tipo_documento getTipodocumento() {
        return tipodocumento;
    }

    public Tatuador tipodocumento(Tipo_documento tipodocumento) {
        this.tipodocumento = tipodocumento;
        return this;
    }

    public void setTipodocumento(Tipo_documento tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public Tatuador documento(String documento) {
        this.documento = documento;
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public Tatuador nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Tatuador apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public Tatuador telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Genero getGenero() {
        return genero;
    }

    public Tatuador genero(Genero genero) {
        this.genero = genero;
        return this;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getApodo() {
        return apodo;
    }

    public Tatuador apodo(String apodo) {
        this.apodo = apodo;
        return this;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public Tatuador foto(byte[] foto) {
        this.foto = foto;
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public Tatuador fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public Boolean isEstado() {
        return estado;
    }

    public Tatuador estado(Boolean estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Set<Trabajo> getTrabajos() {
        return trabajos;
    }

    public Tatuador trabajos(Set<Trabajo> trabajos) {
        this.trabajos = trabajos;
        return this;
    }

    public Tatuador addTrabajo(Trabajo trabajo) {
        this.trabajos.add(trabajo);
        trabajo.setTatuador(this);
        return this;
    }

    public Tatuador removeTrabajo(Trabajo trabajo) {
        this.trabajos.remove(trabajo);
        trabajo.setTatuador(null);
        return this;
    }

    public void setTrabajos(Set<Trabajo> trabajos) {
        this.trabajos = trabajos;
    }

    public Sede getSede() {
        return sede;
    }

    public Tatuador sede(Sede sede) {
        this.sede = sede;
        return this;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Set<Rayaton> getRayatones() {
        return rayatones;
    }

    public Tatuador rayatones(Set<Rayaton> rayatons) {
        this.rayatones = rayatons;
        return this;
    }

    public Tatuador addRayatones(Rayaton rayaton) {
        this.rayatones.add(rayaton);
        rayaton.getTatuadors().add(this);
        return this;
    }

    public Tatuador removeRayatones(Rayaton rayaton) {
        this.rayatones.remove(rayaton);
        rayaton.getTatuadors().remove(this);
        return this;
    }

    public void setRayatones(Set<Rayaton> rayatons) {
        this.rayatones = rayatons;
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
        Tatuador tatuador = (Tatuador) o;
        if (tatuador.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tatuador.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tatuador{" +
            "id=" + getId() +
            ", tipodocumento='" + getTipodocumento() + "'" +
            ", documento='" + getDocumento() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", genero='" + getGenero() + "'" +
            ", apodo='" + getApodo() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            ", estado='" + isEstado() + "'" +
            "}";
    }
}
