package com.medusa.repository;

import com.medusa.domain.Foto;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Foto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
    @Query("select distinct foto from Foto foto left join fetch foto.palabraClaves")
    List<Foto> findAllWithEagerRelationships();

    @Query("select foto from Foto foto left join fetch foto.palabraClaves where foto.id =:id")
    Foto findOneWithEagerRelationships(@Param("id") Long id);

}
