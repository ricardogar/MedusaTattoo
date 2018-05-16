package com.medusa.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.medusa.domain.Pago;

import com.medusa.repository.PagoRepository;
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
import java.time.Instant;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pago.
 */
@RestController
@RequestMapping("/api")
public class PagoResource {

    private final Logger log = LoggerFactory.getLogger(PagoResource.class);

    private static final String ENTITY_NAME = "pago";

    private final PagoRepository pagoRepository;

    public PagoResource(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    /**
     * POST  /pagos : Create a new pago.
     *
     * @param pago the pago to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pago, or with status 400 (Bad Request) if the pago has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pagos")
    @Timed
    public ResponseEntity<Pago> createPago(@Valid @RequestBody Pago pago) throws URISyntaxException {
        log.debug("REST request to save Pago : {}", pago);
        if (pago.getId() != null) {
            throw new BadRequestAlertException("A new pago cannot already have an ID", ENTITY_NAME, "idexists");
        }
		pago.setFecha(Instant.now());
        Pago result = pagoRepository.save(pago);
        return ResponseEntity.created(new URI("/api/pagos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pagos : Updates an existing pago.
     *
     * @param pago the pago to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pago,
     * or with status 400 (Bad Request) if the pago is not valid,
     * or with status 500 (Internal Server Error) if the pago couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pagos")
    @Timed
    public ResponseEntity<Pago> updatePago(@Valid @RequestBody Pago pago) throws URISyntaxException {
        log.debug("REST request to update Pago : {}", pago);
        if (pago.getId() == null) {
            return createPago(pago);
        }
		pago.setFecha(Instant.now());
        Pago result = pagoRepository.save(pago);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pago.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pagos : get all the pagos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pagos in body
     */
    @GetMapping("/pagos")
    @Timed
    public ResponseEntity<List<Pago>> getAllPagos(Pageable pageable) {
        log.debug("REST request to get a page of Pagos");
        Page<Pago> page = pagoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pagos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pagos/:id : get the "id" pago.
     *
     * @param id the id of the pago to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pago, or with status 404 (Not Found)
     */
    @GetMapping("/pagos/{id}")
    @Timed
    public ResponseEntity<Pago> getPago(@PathVariable Long id) {
        log.debug("REST request to get Pago : {}", id);
        Pago pago = pagoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pago));
    }

    /**
     * DELETE  /pagos/:id : delete the "id" pago.
     *
     * @param id the id of the pago to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pagos/{id}")
    @Timed
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        log.debug("REST request to delete Pago : {}", id);
        pagoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
