package com.medusa.repository;

import com.medusa.domain.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the Cita entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    Page<Cita> findAllByTrabajo_Sede_Id(Pageable pageable, Long id);

    List<Cita> findAllByFechaYHoraIsBetween(Instant date1,Instant date2);

    Cita findByFechaYHoraBetweenAndTrabajo_Id(Instant date1,Instant date2, Long id);

    Page<Cita> findAllByFechaYHoraIsBetween(Pageable pageable,Instant date1,Instant date2);

    Page<Cita> findAllByTrabajo_Sede_IdAndFechaYHoraBetween(Pageable pageable, Long id, Instant date, Instant maxDate);

    Page<Cita> findAllByFechaYHoraAfter(Pageable pageable,Instant date);

    Page<Cita> findAllByTrabajo_Sede_IdAndFechaYHoraAfter(Pageable pageable, Long id, Instant date);

    Page<Cita> findAllByTrabajo_Cliente_Documento(Pageable pageable,String documento);

    Page<Cita> findAllByTrabajo_Cliente_Email(Pageable pageable,String email);

}
