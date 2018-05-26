package com.medusa.repository;

import com.medusa.domain.Trabajo;
import com.medusa.domain.enumeration.Estado_trabajo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Trabajo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Long> {

    //@Query("SELECT t FROM Trabajo t JOIN t.sede s where s.id =(SELECT a.id FROM User u JOIN u.sede a WHERE u.id=?1)")
    Page<Trabajo> findAllBySede_Id(Pageable pageable, Long id);
    Page<Trabajo> findAllByEstadoLike(Pageable pageable, Estado_trabajo estado);
    Page<Trabajo> findAllBySede_IdAndEstadoLike(Pageable pageable, Long id,Estado_trabajo estado);
    List<Trabajo> findAllByTatuador_Id(Long id);


}
