package com.medusa.repository;

import com.medusa.domain.Rayaton;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Rayaton entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RayatonRepository extends JpaRepository<Rayaton, Long> {
    @Query("select distinct rayaton from Rayaton rayaton left join fetch rayaton.tatuadors")
    List<Rayaton> findAllWithEagerRelationships();

    @Query("select rayaton from Rayaton rayaton left join fetch rayaton.tatuadors where rayaton.id =:id")
    Rayaton findOneWithEagerRelationships(@Param("id") Long id);

    @Query(value = "select r.id,r.fecha,sum(p.valor) from pago p " +
                    "join trabajo t on p.trabajo_id=t.id  " +
                    "join rayaton r on t.rayaton_id=r.id  " +
                    "where p.fecha between :minDate and :maxDate " +
                    "group by r.id",nativeQuery = true)
    List<Object[]> moneyBetweenDates(@Param("minDate") Instant minDate, @Param("maxDate") Instant maxDate);

    @Query(value = "select e.id, e.fecha, count(e.tf) from " +
        "(select r.id,r.fecha,p.id as tf from trabajo p " +
        "join rayaton r on p.rayaton_id=r.id " +
        "join cita c on c.trabajo_id=p.id " +
        "where p.estado='FINALIZADO' and c.fecha_y_hora between :minDate and :maxDate " +
        "group by r.id, p.id) e " +
        "group by e.id, e.fecha",nativeQuery = true)
    List<Object[]> worksBetweenDates(@Param("minDate") Instant minDate, @Param("maxDate") Instant maxDate);

    @Query("SELECT r FROM Rayaton r left join fetch r.tatuadors WHERE r.id=(SELECT MAX(id) from Rayaton) AND r.fecha>= :now")
    Rayaton getLastRayaton(@Param("now") LocalDate now);

}
