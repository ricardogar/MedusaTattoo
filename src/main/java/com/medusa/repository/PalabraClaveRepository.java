package com.medusa.repository;

import com.medusa.domain.PalabraClave;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PalabraClave entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PalabraClaveRepository extends JpaRepository<PalabraClave, Long> {

}
