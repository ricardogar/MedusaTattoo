package com.medusa.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.medusa.domain.Inscripcion;

import com.medusa.domain.Rayaton;
import com.medusa.repository.InscripcionRepository;
import com.medusa.repository.RayatonRepository;
import com.medusa.web.rest.errors.BadRequestAlertException;
import com.medusa.web.rest.util.HeaderUtil;
import com.medusa.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Inscripcion.
 */
@RestController
@RequestMapping("/api")
public class InscripcionResource {

    private final Logger log = LoggerFactory.getLogger(InscripcionResource.class);

    private static final String ENTITY_NAME = "inscripcion";

    private final InscripcionRepository inscripcionRepository;

    private  final RayatonRepository rayatonRepository;

    public InscripcionResource(InscripcionRepository inscripcionRepository, RayatonRepository rayatonRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.rayatonRepository = rayatonRepository;
    }

    /**
     * POST  /inscripcions : Create a new inscripcion.
     *
     * @param inscripcion the inscripcion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inscripcion, or with status 400 (Bad Request) if the inscripcion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/inscripcions")
    @Timed
    public ResponseEntity<Inscripcion> createInscripcion(@Valid @RequestBody Inscripcion inscripcion) throws URISyntaxException {
        log.debug("REST request to save Inscripcion : {}", inscripcion);
        if (inscripcion.getId() != null) {
            throw new BadRequestAlertException("A new inscripcion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rayaton rayaton = rayatonRepository.getLastRayaton(LocalDate.now());
        if (rayaton==null){
            throw new BadRequestAlertException("there is no event to register", ENTITY_NAME, "norayaton");
        }
        inscripcion.setRayaton(rayaton);

        Inscripcion result = inscripcionRepository.save(inscripcion);
        return ResponseEntity.created(new URI("/api/inscripcions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inscripcions : Updates an existing inscripcion.
     *
     * @param inscripcion the inscripcion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inscripcion,
     * or with status 400 (Bad Request) if the inscripcion is not valid,
     * or with status 500 (Internal Server Error) if the inscripcion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/inscripcions")
    @Timed
    public ResponseEntity<Inscripcion> updateInscripcion(@Valid @RequestBody Inscripcion inscripcion) throws URISyntaxException {
        log.debug("REST request to update Inscripcion : {}", inscripcion);
        if (inscripcion.getId() == null) {
            return createInscripcion(inscripcion);
        }
        Inscripcion result = inscripcionRepository.save(inscripcion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inscripcion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inscripcions : get all the inscripcions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of inscripcions in body
     */
    @GetMapping("/inscripcions")
    @Timed
    public ResponseEntity<List<Inscripcion>> getAllInscripcions(Pageable pageable) {
        log.debug("REST request to get a page of Inscripcions");
        Page<Inscripcion> page = inscripcionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/inscripcions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /inscripcions/:id : get the "id" inscripcion.
     *
     * @param id the id of the inscripcion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inscripcion, or with status 404 (Not Found)
     */
    @GetMapping("/inscripcions/{id}")
    @Timed
    public ResponseEntity<Inscripcion> getInscripcion(@PathVariable Long id) {
        log.debug("REST request to get Inscripcion : {}", id);
        Inscripcion inscripcion = inscripcionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(inscripcion));
    }

    /**
     * DELETE  /inscripcions/:id : delete the "id" inscripcion.
     *
     * @param id the id of the inscripcion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/inscripcions/{id}")
    @Timed
    public ResponseEntity<Void> deleteInscripcion(@PathVariable Long id) {
        log.debug("REST request to delete Inscripcion : {}", id);
        inscripcionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
