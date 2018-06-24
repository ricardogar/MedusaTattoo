package com.medusa.service.dto;

import com.medusa.domain.Foto;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class GalleryItemDTO {

    private String title="";
    private String description="";
    private List<String> thumbnail = new ArrayList<>();
    private List<String> large = new ArrayList<>();
    private List<String> img_title = new ArrayList<>();
    private List<String> button_list = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    private long id;

    public GalleryItemDTO() {
    }
    public GalleryItemDTO(Foto foto) {
        if (foto.getTrabajo()==null){
            title="Medusa Tattoo";
        }else{
            title=foto.getTrabajo().getTatuador().getApodo();
        }
        description=foto.getDescripcion();
        if (foto.getMiniatura() == null) {
            System.out.println("estan siendo nulo");
        }else{
            thumbnail.add("data:"+foto.getMiniaturaContentType()+";base64,"+Base64.getEncoder().encodeToString(foto.getMiniatura()));
        }
        large.add("data:"+foto.getImagenContentType()+";base64,"+Base64.getEncoder().encodeToString(foto.getImagen()));
        img_title.add(title);
        foto.getPalabraClaves().forEach(e -> tags.add(e.getPalabra()));
        id=foto.getId();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(List<String> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getLarge() {
        return large;
    }

    public void setLarge(List<String> large) {
        this.large = large;
    }

    public List<String> getImg_title() {
        return img_title;
    }

    public void setImg_title(List<String> img_title) {
        this.img_title = img_title;
    }

    public List<String> getButton_list() {
        return button_list;
    }

    public void setButton_list(List<String> button_list) {
        this.button_list = button_list;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
