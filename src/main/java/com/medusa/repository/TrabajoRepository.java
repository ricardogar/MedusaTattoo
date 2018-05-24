package com.medusa.repository;

import com.medusa.domain.Trabajo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Trabajo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Long> {

}
