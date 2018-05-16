package com.medusa.repository;

import com.medusa.domain.Tatuador;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tatuador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TatuadorRepository extends JpaRepository<Tatuador, Long> {

}
