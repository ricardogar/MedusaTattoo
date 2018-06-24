package com.medusa.domain;


import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.validation.constraints.*;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * A Foto.
 */
@Entity
@Table(name = "foto")
public class Foto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Lob
    @Column(name = "imagen", nullable = false)
    private byte[] imagen;

    @Column(name = "imagen_content_type", nullable = false)
    private String imagenContentType;

    @Lob
    @Column(name = "miniatura")
    private byte[] miniatura;

    @Column(name = "miniatura_content_type")
    private String miniaturaContentType;

    @ManyToMany
    @JoinTable(name = "foto_palabra_clave",
               joinColumns = @JoinColumn(name="fotos_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="palabra_claves_id", referencedColumnName="id"))
    private Set<PalabraClave> palabraClaves = new HashSet<>();

    @ManyToOne
    private Trabajo trabajo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Foto descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public Foto imagen(byte[] imagen) {
        this.imagen = imagen;
        return this;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return imagenContentType;
    }

    public Foto imagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
        return this;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    public Set<PalabraClave> getPalabraClaves() {
        return palabraClaves;
    }

    public Foto palabraClaves(Set<PalabraClave> palabraClaves) {
        this.palabraClaves = palabraClaves;
        return this;
    }

    public Foto addPalabraClave(PalabraClave palabraClave) {
        this.palabraClaves.add(palabraClave);
        palabraClave.getFotos().add(this);
        return this;
    }

    public Foto removePalabraClave(PalabraClave palabraClave) {
        this.palabraClaves.remove(palabraClave);
        palabraClave.getFotos().remove(this);
        return this;
    }

    public byte[] getMiniatura() {
        return miniatura;
    }

    public void setMiniatura(byte[] miniatura) {
        this.miniatura = miniatura;
    }

    public String getMiniaturaContentType() {
        return miniaturaContentType;
    }

    public void setMiniaturaContentType(String miniaturaContentType) {
        this.miniaturaContentType = miniaturaContentType;
    }

    public void setPalabraClaves(Set<PalabraClave> palabraClaves) {
        this.palabraClaves = palabraClaves;
    }

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public Foto trabajo(Trabajo trabajo) {
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
        Foto foto = (Foto) o;
        if (foto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), foto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Foto{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", imagen='" + Arrays.toString(getImagen()) + "'" +
            ", imagenContentType='" + getImagenContentType() + "'" +
            "}";
    }
}
