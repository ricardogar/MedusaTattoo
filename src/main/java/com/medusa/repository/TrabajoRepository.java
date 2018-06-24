package com.medusa.repository;

import com.medusa.domain.Trabajo;
import com.medusa.domain.enumeration.Estado_trabajo;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Trabajo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Long> {

    Page<Trabajo> findAllBySede_Id(Pageable pageable, Long id);
    Page<Trabajo> findAllByEstadoLike(Pageable pageable, Estado_trabajo estado);
    Page<Trabajo> findAllBySede_IdAndEstadoLike(Pageable pageable, Long id,Estado_trabajo estado);
    List<Trabajo> findAllByTatuador_Id(Long id);
    Page<Trabajo> findAllByRayaton_Id(Pageable pageable, Long id);
    @Query(value = "UPDATE medusatattoo.trabajo t SET t.estado='CANCELADO' WHERE t.sede_id=:id and t.estado='EN_PROGRESO'",nativeQuery = true)
    List<Trabajo> disableBySede(@Param("id") Long idSede);

    @Query(value = "UPDATE trabajo t SET t.estado='EN_PROGRESO' WHERE t.sede_id=:id and t.estado='CANCELADO'",nativeQuery = true)
    List<Trabajo> enableBySede(@Param("id") Long idSede);

    Page<Trabajo> findAllByCliente_Email(Pageable pageable, String email);




}
