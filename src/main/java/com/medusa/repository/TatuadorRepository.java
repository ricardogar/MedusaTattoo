package com.medusa.repository;

import com.medusa.domain.Tatuador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tatuador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TatuadorRepository extends JpaRepository<Tatuador, Long> {

    Page<Tatuador> findAllBySede_Id(Pageable pageable, Long idSede);
    Page<Tatuador> findAllByEstadoIsTrue(Pageable pageable);
    Page<Tatuador> findAllBySede_IdAndEstadoIsTrue(Pageable pageable, Long idSede);

}
