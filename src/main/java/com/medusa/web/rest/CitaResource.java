package com.medusa.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.medusa.domain.Cita;

import com.medusa.domain.User;
import com.medusa.repository.CitaRepository;
import com.medusa.repository.UserRepository;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cita.
 */
@RestController
@RequestMapping("/api")
public class CitaResource {

    private final Logger log = LoggerFactory.getLogger(CitaResource.class);

    private static final String ENTITY_NAME = "cita";

    private final CitaRepository citaRepository;

    private final UserRepository userRepository;
    public CitaResource(CitaRepository citaRepository, UserRepository userRepository) {
        this.citaRepository = citaRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST  /citas : Create a new cita.
     *
     * @param cita the cita to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cita, or with status 400 (Bad Request) if the cita has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/citas")
    @Timed
    public ResponseEntity<Cita> createCita(@Valid @RequestBody Cita cita) throws URISyntaxException {
        log.debug("REST request to save Cita : {}", cita);
        if (cita.getId() != null) {
            throw new BadRequestAlertException("A new cita cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cita result = citaRepository.save(cita);
        return ResponseEntity.created(new URI("/api/citas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /citas : Updates an existing cita.
     *
     * @param cita the cita to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cita,
     * or with status 400 (Bad Request) if the cita is not valid,
     * or with status 500 (Internal Server Error) if the cita couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/citas")
    @Timed
    public ResponseEntity<Cita> updateCita(@Valid @RequestBody Cita cita) throws URISyntaxException {
        log.debug("REST request to update Cita : {}", cita);
        if (cita.getId() == null) {
            return createCita(cita);
        }
        Cita result = citaRepository.save(cita);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cita.getId().toString()))
            .body(result);
    }

    /**
     * GET  /citas : get all the citas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of citas in body
     */
    @GetMapping("/citas")
    @Timed
    public ResponseEntity<List<Cita>> getAllCitas(Pageable pageable) {
        log.debug("REST request to get a page of Citas");
        Page<Cita> page = citaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/citas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /citas/cliente/:documento : get all citas filtered by client document.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of citas in body
     */
    @GetMapping("/citas/cliente/{documento}")
    @Timed
    public ResponseEntity<List<Cita>> getCitasByClienteDocumento(Pageable pageable,@PathVariable String documento) {
        log.debug("REST request to get a page of Citas");
        Page<Cita> page = citaRepository.findAllByTrabajo_Cliente_Documento(pageable,documento);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/citas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /citas/cuenta/:id : get all the citas filter by sede.
     *
     * @param pageable the pagination information
     * @param id the cuenta identifier
     * @return the ResponseEntity with status 200 (OK) and the list of citas in body
     */
    @GetMapping("/citas/cuenta/{id}")
    @Timed
    public ResponseEntity<List<Cita>> getCitasByAccount(Pageable pageable, @PathVariable Long id) {
        log.debug("REST request to get a page of Citas filtered by sede");
        User user = userRepository.findOne(id);

        Page<Cita> page;
        if (user.isAdmin()){
            page = citaRepository.findAll(pageable);
        }else {
            page = citaRepository.findAllByTrabajo_Sede_Id(pageable,user.getSede().getId());
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/citas/cuenta");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /citas/cuenta/:id : get all the citas filter by cuenta and after a date.
     *
     * @param pageable the pagination information
     * @param id the cuenta identifier
     * @param date the date to set initial search
     * @return the ResponseEntity with status 200 (OK) and the list of citas in body
     */
    @GetMapping("/citas/cuenta/{id}/after/{date}")
    @Timed
    public ResponseEntity<List<Cita>> getCitasByAccountAfterDate(Pageable pageable, @PathVariable Long id,
                                                                 @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        log.debug("REST request to get a page of Citas filtered by account and after a date");
        User user = userRepository.findOne(id);

        Page<Cita> page;
        if (user.isAdmin()){
            page = citaRepository.findAllByFechaYHoraAfter(pageable,date.toInstant(ZoneOffset.UTC));
        }else {
            page = citaRepository.findAllByTrabajo_Sede_IdAndFechaYHoraAfter(pageable,user.getSede().getId(),date.toInstant(ZoneOffset.UTC));
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/citas/cuenta");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /citas/cuenta/:id : get all the citas filter by cuenta that occurs in a date.
     *
     * @param pageable the pagination information
     * @param id the cuenta identifier
     * @return the ResponseEntity with status 200 (OK) and the list of citas in body
     */
    @GetMapping("/citas/cuenta/{id}/in/{date}")
    @Timed
    public ResponseEntity<List<Cita>> getCitasByAccountInDate(Pageable pageable, @PathVariable Long id,
                                                              @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        log.debug("REST request to get a page of Citas filtered by account and date");
        User user = userRepository.findOne(id);
        Instant instant=date.toInstant(ZoneOffset.UTC);

        Page<Cita> page;
        if (user.isAdmin()){
            page = citaRepository.findAllByFechaYHoraIsBetween(pageable,
                instant,
                instant.plus(1,ChronoUnit.DAYS));
        }else {
            page = citaRepository.findAllByTrabajo_Sede_IdAndFechaYHoraBetween(pageable,user.getSede().getId(),
                instant,
                instant.plus(1,ChronoUnit.DAYS));
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/citas/cuenta");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /citas/cuenta/:id/fecha/:minDate/:maxDate : get all the citas filter by cuenta between 2 dates.
     *
     * @param pageable the pagination information
     * @param id the cuenta identifier
     * @param minDate the date to start search
     * @param maxDate the top limit date
     * @return the ResponseEntity with status 200 (OK) and the list of citas in body
     */
    @GetMapping("/citas/cuenta/{id}/between/{minDate}/{maxDate}")
    @Timed
    public ResponseEntity<List<Cita>> getCitasByAccountBetweenDates(Pageable pageable, @PathVariable Long id,
                                                                    @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minDate,
                                                                    @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxDate) {
        log.debug("REST request to get a page of Citas filtered by account and date");
        User user = userRepository.findOne(id);
        Instant minInstant=minDate.toInstant(ZoneOffset.UTC);
        Instant maxInstant=maxDate.toInstant(ZoneOffset.UTC);

        Page<Cita> page;
        if (user.isAdmin()){
            page = citaRepository.findAllByFechaYHoraIsBetween(pageable,
                minInstant,
                maxInstant);
        }else {
            page = citaRepository.findAllByTrabajo_Sede_IdAndFechaYHoraBetween(pageable,user.getSede().getId(),
                minInstant,
                maxInstant);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/citas/cuenta");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /citas/:id : get the "id" cita.
     *
     * @param id the id of the cita to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cita, or with status 404 (Not Found)
     */
    @GetMapping("/citas/{id}")
    @Timed
    public ResponseEntity<Cita> getCita(@PathVariable Long id) {
        log.debug("REST request to get Cita : {}", id);
        Cita cita = citaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cita));
    }

    /**
     * DELETE  /citas/:id : delete the "id" cita.
     *
     * @param id the id of the cita to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/citas/{id}")
    @Timed
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        log.debug("REST request to delete Cita : {}", id);
        citaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
