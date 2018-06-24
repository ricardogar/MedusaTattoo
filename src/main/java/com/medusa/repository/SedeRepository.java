package com.medusa.repository;

import com.medusa.domain.Sede;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.util.List;


/**
 * Spring Data JPA repository for the Sede entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {

    @Query(value = "select s.id, s.nombre,sum(p.valor) from pago p  " +
        "join trabajo t on p.trabajo_id=t.id  " +
        "join sede s on t.sede_id=s.id " +
        "where p.fecha between :minDate and :maxDate " +
        "group by s.id",nativeQuery = true)
    List<Object[]> moneyBetweenDates(@Param("minDate") Instant minDate, @Param("maxDate") Instant maxDate);

    @Query(value = "select e.id, e.nombre, count(e.tf) from " +
        "(select s.id,s.nombre,p.id as tf from trabajo p " +
        "join sede s on p.sede_id=s.id " +
        "join cita c on c.trabajo_id=p.id " +
        "where p.estado= :estado and c.fecha_y_hora between :minDate and :maxDate " +
        "group by s.id, p.id) e " +
        "group by e.id, e.nombre",nativeQuery = true)
    List<Object[]> worksBetweenDates(@Param("minDate") Instant minDate, @Param("maxDate") Instant maxDate,@Param("estado") String estado);

    Page<Sede> findAllByEstadoIsTrue(Pageable pageable);
}
