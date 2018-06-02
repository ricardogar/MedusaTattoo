package com.medusa.service.dto;

import com.medusa.domain.Cita;

import java.time.Instant;
import java.time.LocalDateTime;

public class CalendarEventDTO {

    String title;
    CalendarColorDTO color;
    Instant startsAt;
    Instant endsAt;
    boolean draggable;
    boolean resizable;
    Cita cita;

    public CalendarEventDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CalendarColorDTO getColor() {
        return color;
    }

    public void setColor(CalendarColorDTO color) {
        this.color = color;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }
}
