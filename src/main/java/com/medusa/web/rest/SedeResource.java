package com.medusa.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.medusa.config.Constants;
import com.medusa.domain.Sede;

import com.medusa.repository.SedeRepository;
import com.medusa.repository.TatuadorRepository;
import com.medusa.repository.TrabajoRepository;
import com.medusa.repository.UserRepository;
import com.medusa.security.AuthoritiesConstants;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Sede.
 */
@RestController
@RequestMapping("/api")
public class SedeResource {

    private final Logger log = LoggerFactory.getLogger(SedeResource.class);

    private static final String ENTITY_NAME = "sede";

    private final SedeRepository sedeRepository;
    private final TatuadorRepository tatuadorRepository;
    private final UserRepository userRepository;
    private final TrabajoRepository trabajoRepository;

    public SedeResource(SedeRepository sedeRepository, TatuadorRepository tatuadorRepository, UserRepository userRepository, TrabajoRepository trabajoRepository) {
        this.sedeRepository = sedeRepository;
        this.tatuadorRepository = tatuadorRepository;
        this.userRepository = userRepository;
        this.trabajoRepository = trabajoRepository;
    }

    /**
     * POST  /sedes : Create a new sede.
     *
     * @param sede the sede to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sede, or with status 400 (Bad Request) if the sede has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sedes")
    @Timed
    public ResponseEntity<Sede> createSede(@Valid @RequestBody Sede sede) throws URISyntaxException {
        log.debug("REST request to save Sede : {}", sede);
        if (sede.getId() != null) {
            throw new BadRequestAlertException("A new sede cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sede result = sedeRepository.save(sede);
        return ResponseEntity.created(new URI("/api/sedes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sedes : Updates an existing sede.
     *
     * @param sede the sede to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sede,
     * or with status 400 (Bad Request) if the sede is not valid,
     * or with status 500 (Internal Server Error) if the sede couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sedes")
    @Timed
    public ResponseEntity<Sede> updateSede(@Valid @RequestBody Sede sede) throws URISyntaxException {
        log.debug("REST request to update Sede : {}", sede);

        if (sede.getId() == null) {
            return createSede(sede);
        }
        Sede sede1=sedeRepository.findOne(sede.getId());
        Sede result;
        /*
        if (!sede1.isEstado() && sede.isEstado()){
            userRepository.enableBySede(sede.getId());
            tatuadorRepository.enableBySede(sede.getId());
            trabajoRepository.enableBySede(sede.getId());
        }
         */
        result = sedeRepository.save(sede);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sede.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sedes : get all the sedes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sedes in body
     */
    @GetMapping("/sedes")
    @Timed
    public ResponseEntity<List<Sede>> getAllSedes(Pageable pageable) {
        log.debug("REST request to get a page of Sedes");
        Page<Sede> page = sedeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sedes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /sedes : get all the sedes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sedes in body
     */
    @GetMapping("/sedes/abierta")
    @Timed
    public ResponseEntity<List<Sede>> getOpenSedes(Pageable pageable) {
        log.debug("REST request to get a page of Sedes");
        Page<Sede> page = sedeRepository.findAllByEstadoIsTrue(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sedes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sedes : get all the sedes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sedes in body
     */
    @GetMapping("/sedes/money/{minDate}/{maxDate}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<Object[]>> moneyBetweenDates(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minDate,
                                                            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxDate) {
        log.debug("REST request to get a page of Sedes");
        List<Object[]> objects = sedeRepository.moneyBetweenDates(minDate.toInstant(ZoneOffset.UTC),
                                                                maxDate.toInstant(ZoneOffset.UTC));
        //HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(objects, "/api/sedes");
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }


    /**
     * GET  /sedes : get all the sedes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sedes in body
     */
    @GetMapping("/sedes/works/{minDate}/{maxDate}/{status}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<Object[]>> worksBetweenDates(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minDate,
                                                            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxDate,
                                                            @PathVariable String status) {
        log.debug("REST request to get a page of Sedes");
        List<Object[]> objects = sedeRepository.worksBetweenDates(minDate.toInstant(ZoneOffset.UTC),
            maxDate.toInstant(ZoneOffset.UTC),status);
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }



    /**
     * GET  /sedes/:id : get the "id" sede.
     *
     * @param id the id of the sede to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sede, or with status 404 (Not Found)
     */
    @GetMapping("/sedes/{id}")
    @Timed
    public ResponseEntity<Sede> getSede(@PathVariable Long id) {
        log.debug("REST request to get Sede : {}", id);
        Sede sede = sedeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sede));
    }

    /**
     * DELETE  /sedes/:id : delete the "id" sede.
     *
     * @param id the id of the sede to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sedes/{id}")
    @Timed
    public ResponseEntity<Void> deleteSede(@PathVariable Long id) {
        log.debug("REST request to delete Sede : {}", id);
        Sede sede = sedeRepository.findOne(id);
        sede.setEstado(false);

        //tatuadorRepository.disableBySede(id);
        //userRepository.disableBySede(id);
        //trabajoRepository.disableBySede(id);
        sedeRepository.save(sede);
        //sedeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
