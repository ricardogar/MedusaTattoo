package com.medusa.service.mapper;

import com.medusa.domain.Cita;
import com.medusa.repository.CalendarColorRepository;
import com.medusa.service.dto.CalendarColorDTO;
import com.medusa.service.dto.CalendarEventDTO;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EventMapper {

    public static List<CalendarEventDTO> citaToEventAdmin(List<Cita> citas){
        List<CalendarEventDTO> dtoList=new ArrayList<>();
        for (Cita cita : citas) {
            CalendarEventDTO eventDTO = new CalendarEventDTO();
            eventDTO.setTitle(cita.getTrabajo().getNombre());
            eventDTO.setColor(cita.getFechaYHora().isBefore(Instant.now())?CalendarColorRepository.CITA_PASADA:CalendarColorRepository.CITA_PRESENTE);
            eventDTO.setStartsAt(cita.getFechaYHora());
            eventDTO.setEndsAt(cita.getFechaYHora().plus(cita.getDuracion(), ChronoUnit.HOURS));
            if (cita.getFechaYHora().isBefore(Instant.now())){
                eventDTO.setDraggable(false);
                eventDTO.setResizable(false);
            }else{
                eventDTO.setDraggable(true);
                eventDTO.setResizable(true);
            }

            eventDTO.setCita(cita);
            dtoList.add(eventDTO);
        }
        return dtoList;
    }
    public static List<CalendarEventDTO> citaToEventClient(List<Cita> citas){
        List<CalendarEventDTO> dtoList=new ArrayList<>();
        for (Cita cita : citas) {
            CalendarEventDTO eventDTO = new CalendarEventDTO();
            eventDTO.setTitle("Tatuaje en: "+cita.getTrabajo().getSede().getNombre()+" ("+cita.getTrabajo().getSede().getDireccion()+")");
            eventDTO.setColor(cita.getFechaYHora().isBefore(Instant.now())?CalendarColorRepository.CITA_PASADA:CalendarColorRepository.CITA_PRESENTE);
            eventDTO.setStartsAt(cita.getFechaYHora());
            eventDTO.setEndsAt(cita.getFechaYHora().plus(cita.getDuracion(), ChronoUnit.HOURS));
            eventDTO.setDraggable(false);
            eventDTO.setResizable(false);
            eventDTO.setCita(cita);
            dtoList.add(eventDTO);
        }
        return dtoList;
    }

}
