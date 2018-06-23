package com.medusa.service.mapper;

import com.medusa.domain.Cita;
import com.medusa.domain.enumeration.Estado_trabajo;
import com.medusa.repository.CalendarColorRepository;
import com.medusa.service.dto.CalendarColorDTO;
import com.medusa.service.dto.CalendarEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventMapper {


    public static List<CalendarEventDTO> citaToEventAdmin(List<Cita> citas){
        final Logger log = LoggerFactory.getLogger(EventMapper.class);
        List<CalendarEventDTO> dtoList=new ArrayList<>();
        for (Cita cita : citas) {
            CalendarEventDTO eventDTO = new CalendarEventDTO();
            eventDTO.setTitle(cita.getTrabajo().getNombre());
            if (cita.getFechaYHora().isBefore(Instant.now()) || !cita.getTrabajo().getEstado().equals(Estado_trabajo.EN_PROGRESO) ){
                eventDTO.setColor(CalendarColorRepository.CITA_PASADA);
            }else{
                eventDTO.setColor(CalendarColorRepository.CITA_PRESENTE);
            }
            eventDTO.setStartsAt(cita.getFechaYHora());
            if (cita.getDuracion()%1==0){
                eventDTO.setEndsAt(cita.getFechaYHora().plus(cita.getDuracion().intValue(), ChronoUnit.HOURS));
            }else{
                eventDTO.setEndsAt(cita.getFechaYHora()
                    .plus(cita.getDuracion().intValue(), ChronoUnit.HOURS)
                    .plus(30,ChronoUnit.MINUTES));
            }
            if (cita.getFechaYHora().isBefore(Instant.now()) || !cita.getTrabajo().getEstado().equals(Estado_trabajo.EN_PROGRESO)){
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
            eventDTO.setEndsAt(cita.getFechaYHora().plus(cita.getDuracion().intValue(), ChronoUnit.HOURS));
            if (cita.getDuracion()%2!=0){
                eventDTO.setEndsAt(cita.getFechaYHora().plus(30, ChronoUnit.MINUTES));
            }

            eventDTO.setDraggable(false);
            eventDTO.setResizable(false);
            eventDTO.setCita(cita);
            dtoList.add(eventDTO);
        }
        return dtoList;
    }

}
