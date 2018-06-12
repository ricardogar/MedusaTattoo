package com.medusa.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.medusa.domain.PalabraClave;

import com.medusa.repository.PalabraClaveRepository;
import com.medusa.web.rest.errors.BadRequestAlertException;
import com.medusa.web.rest.util.HeaderUtil;
import com.medusa.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PalabraClave.
 */
@RestController
@RequestMapping("/api")
public class PalabraClaveResource {

    private final Logger log = LoggerFactory.getLogger(PalabraClaveResource.class);

    private static final String ENTITY_NAME = "palabraClave";

    private final PalabraClaveRepository palabraClaveRepository;

    public PalabraClaveResource(PalabraClaveRepository palabraClaveRepository) {
        this.palabraClaveRepository = palabraClaveRepository;
    }

    /**
     * POST  /palabra-claves : Create a new palabraClave.
     *
     * @param palabraClave the palabraClave to create
     * @return the ResponseEntity with status 201 (Created) and with body the new palabraClave, or with status 400 (Bad Request) if the palabraClave has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/palabra-claves")
    @Timed
    public ResponseEntity<PalabraClave> createPalabraClave(@Valid @RequestBody PalabraClave palabraClave) throws URISyntaxException {
        log.debug("REST request to save PalabraClave : {}", palabraClave);
        if (palabraClave.getId() != null) {
            throw new BadRequestAlertException("A new palabraClave cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PalabraClave result = palabraClaveRepository.save(palabraClave);
        return ResponseEntity.created(new URI("/api/palabra-claves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /palabra-claves : Updates an existing palabraClave.
     *
     * @param palabraClave the palabraClave to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated palabraClave,
     * or with status 400 (Bad Request) if the palabraClave is not valid,
     * or with status 500 (Internal Server Error) if the palabraClave couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/palabra-claves")
    @Timed
    public ResponseEntity<PalabraClave> updatePalabraClave(@Valid @RequestBody PalabraClave palabraClave) throws URISyntaxException {
        log.debug("REST request to update PalabraClave : {}", palabraClave);
        if (palabraClave.getId() == null) {
            return createPalabraClave(palabraClave);
        }
        PalabraClave result = palabraClaveRepository.save(palabraClave);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, palabraClave.getId().toString()))
            .body(result);
    }

    /**
     * GET  /palabra-claves : get all the palabraClaves.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of palabraClaves in body
     */
    @GetMapping("/palabra-claves")
    @Timed
    public ResponseEntity<List<PalabraClave>> getAllPalabraClaves(@PageableDefault(page = 0,value = 1000,size = 1000) Pageable pageable) {
        log.debug("REST request to get a page of PalabraClaves");
        List<PalabraClave> page = palabraClaveRepository.findAll();
        //HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders("/api/paalabra-claves");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /palabra-claves/:id : get the "id" palabraClave.
     *
     * @param id the id of the palabraClave to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the palabraClave, or with status 404 (Not Found)
     */
    @GetMapping("/palabra-claves/{id}")
    @Timed
    public ResponseEntity<PalabraClave> getPalabraClave(@PathVariable Long id) {
        log.debug("REST request to get PalabraClave : {}", id);
        PalabraClave palabraClave = palabraClaveRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(palabraClave));
    }

    /**
     * DELETE  /palabra-claves/:id : delete the "id" palabraClave.
     *
     * @param id the id of the palabraClave to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/palabra-claves/{id}")
    @Timed
    public ResponseEntity<Void> deletePalabraClave(@PathVariable Long id) {
        log.debug("REST request to delete PalabraClave : {}", id);
        palabraClaveRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
