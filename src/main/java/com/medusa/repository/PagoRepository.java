package com.medusa.repository;

import com.medusa.domain.Pago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pago entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Page<Pago> findAllByTrabajo_Sede_Id(Pageable pageable, Long id);
    Page<Pago> findAllByTrabajo_Cliente_Email(Pageable pageable, String email);
}
