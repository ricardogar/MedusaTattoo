package com.medusa.repository;

import com.medusa.domain.Tatuador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.util.List;


/**
 * Spring Data JPA repository for the Tatuador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TatuadorRepository extends JpaRepository<Tatuador, Long> {

    Page<Tatuador> findAllBySede_Id(Pageable pageable, Long idSede);
    Page<Tatuador> findAllByEstadoIsTrue(Pageable pageable);
    Page<Tatuador> findAllBySede_IdAndEstadoIsTrue(Pageable pageable, Long idSede);

    @Query(value = "UPDATE tatuador t SET t.estado=0 WHERE t.sede_id=:id",nativeQuery = true)
    List<Tatuador> disableBySede(@Param("id") Long idSede);

    @Query(value = "UPDATE tatuador t SET t.estado=1 WHERE t.sede_id=:id",nativeQuery = true)
    List<Tatuador> enableBySede(@Param("id") Long idSede);

    @Query(value = "select tt.id, tt.apodo,s.nombre,sum(p.valor) from pago p " +
        "join trabajo t on p.trabajo_id=t.id " +
        "join tatuador tt on t.tatuador_id=tt.id " +
        "join sede s on tt.sede_id=s.id " +
        "where p.fecha between :minDate and :maxDate " +
        "group by tt.id",nativeQuery = true)
    List<Object[]> MoneyBetweenDates(@Param("minDate") Instant minDate, @Param("maxDate") Instant maxDate);

    @Query(value = "select e.id, e.apodo, count(e.tf) from " +
        "(select t.id,t.apodo,p.id as tf from trabajo p " +
        "join tatuador t on p.tatuador_id=t.id " +
        "join cita c on c.trabajo_id=p.id " +
        "where p.estado= :estado and c.fecha_y_hora between :minDate and :maxDate " +
        "group by t.id, p.id) e " +
        "group by e.id, e.apodo",nativeQuery = true)
    List<Object[]> WorksBetweenDates(@Param("minDate") Instant minDate, @Param("maxDate") Instant maxDate,@Param("estado") String estado);



}
