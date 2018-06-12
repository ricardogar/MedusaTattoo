package com.medusa.service.dto;

import com.medusa.domain.Foto;

import java.util.ArrayList;
import java.util.List;

public class GalleryDTO {
    private String showAllText="Todos";
    private String filterEffect="scaleup";
    private Boolean hoverDirection=true;
    private int hoverDelay=0;
    private Boolean hoverInverse=false;
    private int expandingSpeed=500;
    private int expandingHeight=500;
    private List<GalleryItemDTO> items = new ArrayList<>();

    public GalleryDTO(List<Foto> fotos) {
        fotos.forEach(e -> items.add(new GalleryItemDTO(e)));
    }

    public String getShowAllText() {
        return showAllText;
    }

    public void setShowAllText(String showAllText) {
        this.showAllText = showAllText;
    }

    public String getFilterEffect() {
        return filterEffect;
    }

    public void setFilterEffect(String filterEffect) {
        this.filterEffect = filterEffect;
    }

    public Boolean getHoverDirection() {
        return hoverDirection;
    }

    public void setHoverDirection(Boolean hoverDirection) {
        this.hoverDirection = hoverDirection;
    }

    public int getHoverDelay() {
        return hoverDelay;
    }

    public void setHoverDelay(int hoverDelay) {
        this.hoverDelay = hoverDelay;
    }

    public Boolean getHoverInverse() {
        return hoverInverse;
    }

    public void setHoverInverse(Boolean hoverInverse) {
        this.hoverInverse = hoverInverse;
    }

    public int getExpandingSpeed() {
        return expandingSpeed;
    }

    public void setExpandingSpeed(int expandingSpeed) {
        this.expandingSpeed = expandingSpeed;
    }

    public int getExpandingHeight() {
        return expandingHeight;
    }

    public void setExpandingHeight(int expandingHeight) {
        this.expandingHeight = expandingHeight;
    }

    public List<GalleryItemDTO> getItems() {
        return items;
    }

    public void setItems(List<GalleryItemDTO> items) {
        this.items = items;
    }
}
