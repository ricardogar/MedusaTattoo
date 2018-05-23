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
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

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

    @Column(name = "direccion")
    private String direccion;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<Trabajo> trabajos = new HashSet<>();

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

    public Cliente tipodocumento(Tipo_documento tipodocumento) {
        this.tipodocumento = tipodocumento;
        return this;
    }

    public void setTipodocumento(Tipo_documento tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public Cliente documento(String documento) {
        this.documento = documento;
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public Cliente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Cliente apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public Cliente telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Genero getGenero() {
        return genero;
    }

    public Cliente genero(Genero genero) {
        this.genero = genero;
        return this;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getDireccion() {
        return direccion;
    }

    public Cliente direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Set<Trabajo> getTrabajos() {
        return trabajos;
    }

    public Cliente trabajos(Set<Trabajo> trabajos) {
        this.trabajos = trabajos;
        return this;
    }

    public Cliente addTrabajo(Trabajo trabajo) {
        this.trabajos.add(trabajo);
        trabajo.setCliente(this);
        return this;
    }

    public Cliente removeTrabajo(Trabajo trabajo) {
        this.trabajos.remove(trabajo);
        trabajo.setCliente(null);
        return this;
    }

    public void setTrabajos(Set<Trabajo> trabajos) {
        this.trabajos = trabajos;
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
        Cliente cliente = (Cliente) o;
        if (cliente.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cliente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", tipodocumento='" + getTipodocumento() + "'" +
            ", documento='" + getDocumento() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", genero='" + getGenero() + "'" +
            ", direccion='" + getDireccion() + "'" +
            "}";
    }
}
