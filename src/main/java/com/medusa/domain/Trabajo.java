package com.medusa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.medusa.domain.enumeration.Estado_trabajo;

import com.medusa.domain.enumeration.Tipo_trabajo;

/**
 * A Trabajo.
 */
@Entity
@Table(name = "trabajo")
public class Trabajo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Size(min = 5)
    @Pattern(regexp = "^[1-9][0-9]*$")
    @Column(name = "costo_total", nullable = false)
    private String costoTotal;

    @Pattern(regexp = "^[1-9][0-9]*$")
    @Column(name = "total_pagado")
    private String totalPagado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado_trabajo estado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo_trabajo tipo;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @ManyToOne
    private Rayaton rayaton;

    @OneToMany(mappedBy = "trabajo")
    @JsonIgnore
    private Set<Pago> pagos = new HashSet<>();

    @OneToMany(mappedBy = "trabajo")
    @JsonIgnore
    private Set<Cita> citas = new HashSet<>();

    @OneToMany(mappedBy = "trabajo")
    @JsonIgnore
    private Set<Foto> fotos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Tatuador tatuador;

    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;

    @ManyToOne(optional = false)
    @NotNull
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

    public Trabajo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCostoTotal() {
        return costoTotal;
    }

    public Trabajo costoTotal(String costoTotal) {
        this.costoTotal = costoTotal;
        return this;
    }

    public void setCostoTotal(String costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getTotalPagado() {
        return totalPagado;
    }

    public Trabajo totalPagado(String totalPagado) {
        this.totalPagado = totalPagado;
        return this;
    }

    public void setTotalPagado(String totalPagado) {
        this.totalPagado = totalPagado;
    }

    public Estado_trabajo getEstado() {
        return estado;
    }

    public Trabajo estado(Estado_trabajo estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado_trabajo estado) {
        this.estado = estado;
    }

    public Tipo_trabajo getTipo() {
        return tipo;
    }

    public Trabajo tipo(Tipo_trabajo tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(Tipo_trabajo tipo) {
        this.tipo = tipo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public Trabajo foto(byte[] foto) {
        this.foto = foto;
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public Trabajo fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public Rayaton getRayaton() {
        return rayaton;
    }

    public Trabajo rayaton(Rayaton rayaton) {
        this.rayaton = rayaton;
        return this;
    }

    public void setRayaton(Rayaton rayaton) {
        this.rayaton = rayaton;
    }

    public Set<Pago> getPagos() {
        return pagos;
    }

    public Trabajo pagos(Set<Pago> pagos) {
        this.pagos = pagos;
        return this;
    }

    public Trabajo addPago(Pago pago) {
        this.pagos.add(pago);
        pago.setTrabajo(this);
        return this;
    }

    public Trabajo removePago(Pago pago) {
        this.pagos.remove(pago);
        pago.setTrabajo(null);
        return this;
    }

    public void setPagos(Set<Pago> pagos) {
        this.pagos = pagos;
    }

    public Set<Cita> getCitas() {
        return citas;
    }

    public Trabajo citas(Set<Cita> citas) {
        this.citas = citas;
        return this;
    }

    public Trabajo addCita(Cita cita) {
        this.citas.add(cita);
        cita.setTrabajo(this);
        return this;
    }

    public Trabajo removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.setTrabajo(null);
        return this;
    }

    public void setCitas(Set<Cita> citas) {
        this.citas = citas;
    }

    public Set<Foto> getFotos() {
        return fotos;
    }

    public Trabajo fotos(Set<Foto> fotos) {
        this.fotos = fotos;
        return this;
    }

    public Trabajo addFoto(Foto foto) {
        this.fotos.add(foto);
        foto.setTrabajo(this);
        return this;
    }

    public Trabajo removeFoto(Foto foto) {
        this.fotos.remove(foto);
        foto.setTrabajo(null);
        return this;
    }

    public void setFotos(Set<Foto> fotos) {
        this.fotos = fotos;
    }

    public Tatuador getTatuador() {
        return tatuador;
    }

    public Trabajo tatuador(Tatuador tatuador) {
        this.tatuador = tatuador;
        return this;
    }

    public void setTatuador(Tatuador tatuador) {
        this.tatuador = tatuador;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Trabajo cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Sede getSede() {
        return sede;
    }

    public Trabajo sede(Sede sede) {
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
        Trabajo trabajo = (Trabajo) o;
        if (trabajo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trabajo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Trabajo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", costoTotal='" + getCostoTotal() + "'" +
            ", totalPagado='" + getTotalPagado() + "'" +
            ", estado='" + getEstado() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            "}";
    }
}
