package com.medusa.repository;

import com.medusa.domain.Trabajo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Trabajo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Long> {

    @Query("SELECT t FROM Trabajo t JOIN t.sede s where s.id =(SELECT a.id FROM User u JOIN u.sede a WHERE u.id=?1)")
    Page<Trabajo> findAllByCuenta(Pageable pageable, Long id);
}
