package com.medusa.repository;

import com.medusa.domain.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.time.LocalDate;


/**
 * Spring Data JPA repository for the Cita entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {



    //@Query("SELECT c FROM Cita c JOIN c.trabajo t JOIN t.sede s WHERE s.id=(SELECT a.id FROM User u JOIN u.sede a WHERE u.id=?1)")
    Page<Cita> findAllByTrabajo_Sede_Id(Pageable pageable, Long id);

    Page<Cita> findAllByFechaYHoraIsBetween(Pageable pageable,Instant date1,Instant date2);

    //@Query("SELECT c FROM Cita c JOIN c.trabajo t JOIN t.sede s WHERE s.id=(SELECT a.id FROM User u JOIN u.sede a WHERE u.id= :id) AND c.fechaYHora BETWEEN :date AND :maxDate")
    Page<Cita> findAllByTrabajo_Sede_IdAndFechaYHoraBetween(Pageable pageable, Long id, Instant date, Instant maxDate);

    Page<Cita> findAllByFechaYHoraAfter(Pageable pageable,Instant date);

    //@Query("SELECT c FROM Cita c JOIN c.trabajo t JOIN t.sede s WHERE s.id=(SELECT a.id FROM User u JOIN u.sede a WHERE u.id= :id) AND c.fechaYHora > :date")
    Page<Cita> findAllByTrabajo_Sede_IdAndFechaYHoraAfter(Pageable pageable, Long id, Instant date);
}
