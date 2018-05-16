package com.medusa.repository;

import com.medusa.domain.Sede;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sede entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {

}
