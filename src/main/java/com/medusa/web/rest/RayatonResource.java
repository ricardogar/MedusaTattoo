package com.medusa.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.medusa.domain.Rayaton;

import com.medusa.repository.RayatonRepository;
import com.medusa.web.rest.errors.BadRequestAlertException;
import com.medusa.web.rest.util.HeaderUtil;
import com.medusa.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Rayaton.
 */
@RestController
@RequestMapping("/api")
public class RayatonResource {

    private final Logger log = LoggerFactory.getLogger(RayatonResource.class);

    private static final String ENTITY_NAME = "rayaton";

    private final RayatonRepository rayatonRepository;

    public RayatonResource(RayatonRepository rayatonRepository) {
        this.rayatonRepository = rayatonRepository;
    }

    /**
     * POST  /rayatons : Create a new rayaton.
     *
     * @param rayaton the rayaton to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rayaton, or with status 400 (Bad Request) if the rayaton has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rayatons")
    @Timed
    public ResponseEntity<Rayaton> createRayaton(@Valid @RequestBody Rayaton rayaton) throws URISyntaxException {
        log.debug("REST request to save Rayaton : {}", rayaton);
        if (rayaton.getId() != null) {
            throw new BadRequestAlertException("A new rayaton cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rayaton result = rayatonRepository.save(rayaton);
        return ResponseEntity.created(new URI("/api/rayatons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rayatons : Updates an existing rayaton.
     *
     * @param rayaton the rayaton to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rayaton,
     * or with status 400 (Bad Request) if the rayaton is not valid,
     * or with status 500 (Internal Server Error) if the rayaton couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rayatons")
    @Timed
    public ResponseEntity<Rayaton> updateRayaton(@Valid @RequestBody Rayaton rayaton) throws URISyntaxException {
        log.debug("REST request to update Rayaton : {}", rayaton);
        if (rayaton.getId() == null) {
            return createRayaton(rayaton);
        }
        Rayaton result = rayatonRepository.save(rayaton);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rayaton.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rayatons : get all the rayatons.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rayatons in body
     */
    @GetMapping("/rayatons")
    @Timed
    public ResponseEntity<List<Rayaton>> getAllRayatons(Pageable pageable) {
        log.debug("REST request to get a page of Rayatons");
        Page<Rayaton> page = rayatonRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rayatons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rayatons : get all the rayatons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rayatons in body
     */
    @GetMapping("/rayatons/money/{minDate}/{maxDate}")
    @Timed
    public List<Object[]> moneyBetweenDates(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minDate,
                                                        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxDate) {
        log.debug("REST request to get a page of Rayatons");
        return rayatonRepository.moneyBetweenDates(minDate.toInstant(ZoneOffset.UTC),maxDate.toInstant(ZoneOffset.UTC));
    }

    /**
     * GET  /rayatons : get all the rayatons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rayatons in body
     */
    @GetMapping("/rayatons/works/{minDate}/{maxDate}")
    @Timed
    public ResponseEntity<List<Object[]>> worksBetweenDates(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minDate,
                                                            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxDate) {
        log.debug("REST request to get a page of Rayatons");
        List<Object[]> objects = rayatonRepository.worksBetweenDates(minDate.toInstant(ZoneOffset.UTC),
            maxDate.toInstant(ZoneOffset.UTC));
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }


    /**
     * GET  /rayatons/:id : get the "id" rayaton.
     *
     * @param id the id of the rayaton to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rayaton, or with status 404 (Not Found)
     */
    @GetMapping("/rayatons/{id}")
    @Timed
    public ResponseEntity<Rayaton> getRayaton(@PathVariable Long id) {
        log.debug("REST request to get Rayaton : {}", id);
        Rayaton rayaton = rayatonRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rayaton));
    }

    /**
     * DELETE  /rayatons/:id : delete the "id" rayaton.
     *
     * @param id the id of the rayaton to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rayatons/{id}")
    @Timed
    public ResponseEntity<Void> deleteRayaton(@PathVariable Long id) {
        log.debug("REST request to delete Rayaton : {}", id);
        rayatonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
