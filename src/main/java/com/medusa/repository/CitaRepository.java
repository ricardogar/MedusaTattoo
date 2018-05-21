package com.medusa.repository;

import com.medusa.domain.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cita entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT c FROM Cita c JOIN c.trabajo t JOIN t.sede s WHERE s.id=(SELECT a.id FROM User u JOIN u.sede a WHERE u.id=?1)")
    Page<Cita> findAllByCuenta(Pageable pageable, Long id);
}
