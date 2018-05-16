package com.medusa.repository;

import com.medusa.domain.Rayaton;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Rayaton entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RayatonRepository extends JpaRepository<Rayaton, Long> {
    @Query("select distinct rayaton from Rayaton rayaton left join fetch rayaton.tatuadors")
    List<Rayaton> findAllWithEagerRelationships();

    @Query("select rayaton from Rayaton rayaton left join fetch rayaton.tatuadors where rayaton.id =:id")
    Rayaton findOneWithEagerRelationships(@Param("id") Long id);

}
