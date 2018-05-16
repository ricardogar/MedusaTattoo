package com.medusa.repository;

import com.medusa.domain.Inscripcion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Inscripcion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

}
