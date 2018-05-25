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

    /*
    select tt.apodo,sum(p.valor) from pago p join trabajo t on p.trabajo_id=t.id join tatuador tt on t.tatuador_id=tt.id where p.fecha between :minDate and :maxDate group by tt.id;
     */

    @Query(value = "select tt.apodo,sum(p.valor) from pago p join trabajo t on p.trabajo_id=t.id join tatuador tt on t.tatuador_id=tt.id where p.fecha between :minDate and :maxDate group by tt.id",nativeQuery = true)
    List<Object[]> TatuadoraAndMoneyBetweenDates(@Param("minDate") Instant minDate,@Param("maxDate") Instant maxDate);

}
