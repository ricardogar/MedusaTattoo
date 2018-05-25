package com.medusa.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.medusa.domain.Tatuador;

import com.medusa.domain.User;
import com.medusa.repository.TatuadorRepository;
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
 * REST controller for managing Tatuador.
 */
@RestController
@RequestMapping("/api")
public class TatuadorResource {

    private final Logger log = LoggerFactory.getLogger(TatuadorResource.class);

    private static final String ENTITY_NAME = "tatuador";

    private final TatuadorRepository tatuadorRepository;

    private final UserRepository userRepository;

    public TatuadorResource(TatuadorRepository tatuadorRepository, UserRepository userRepository) {
        this.tatuadorRepository = tatuadorRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST  /tatuadors : Create a new tatuador.
     *
     * @param tatuador the tatuador to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tatuador, or with status 400 (Bad Request) if the tatuador has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tatuadors")
    @Timed
    public ResponseEntity<Tatuador> createTatuador(@Valid @RequestBody Tatuador tatuador) throws URISyntaxException {
        log.debug("REST request to save Tatuador : {}", tatuador);
        if (tatuador.getId() != null) {
            throw new BadRequestAlertException("A new tatuador cannot already have an ID", ENTITY_NAME, "idexists");
        }
		tatuador.setEstado(true);
        Tatuador result = tatuadorRepository.save(tatuador);
        return ResponseEntity.created(new URI("/api/tatuadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tatuadors : Updates an existing tatuador.
     *
     * @param tatuador the tatuador to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tatuador,
     * or with status 400 (Bad Request) if the tatuador is not valid,
     * or with status 500 (Internal Server Error) if the tatuador couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tatuadors")
    @Timed
    public ResponseEntity<Tatuador> updateTatuador(@Valid @RequestBody Tatuador tatuador) throws URISyntaxException {
        log.debug("REST request to update Tatuador : {}", tatuador);
        if (tatuador.getId() == null) {
            return createTatuador(tatuador);
        }
        Tatuador result = tatuadorRepository.save(tatuador);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tatuador.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tatuadors : get all the tatuadors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tatuadors in body
     */
    @GetMapping("/tatuadors")
    @Timed
    public ResponseEntity<List<Tatuador>> getAllTatuadors(Pageable pageable) {
        log.debug("REST request to get a page of Tatuadors");
        Page<Tatuador> page = tatuadorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tatuadors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tatuadors/cuenta/:id : get all the tatuadors filtering by account.
     *
     * @param pageable the pagination information
     * @param id the account identifier
     * @return the ResponseEntity with status 200 (OK) and the list of tatuadors in body
     */
    @GetMapping("/tatuadors/cuenta/{id}")
    @Timed
    public ResponseEntity<List<Tatuador>> getAllTatuadorsByCuenta(Pageable pageable, @PathVariable("id") Long id) {
        log.debug("REST request to get a page of Tatuadors");
        User user = userRepository.findOne(id);
        Page<Tatuador> page;
        if (user.isAdmin()){
            page = tatuadorRepository.findAll(pageable);
        }else{
            page = tatuadorRepository.findAllBySede_Id(pageable, user.getSede().getId());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tatuadors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tatuadors/cuenta/:id : get all the tatuadors filtering by account.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tatuadors in body
     */
    @GetMapping("/tatuadors/money/{minDate}/{maxDate}")
    @Timed
    public ResponseEntity<List<Object[]>> moneyByTatuadorsBetweenDates(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minDate,
                                                                       @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxDate) {
        log.debug("REST request to get a page of Tatuadors");
        List<Object[]> objects;
         objects = tatuadorRepository.TatuadoraAndMoneyBetweenDates(minDate.toInstant(ZoneOffset.UTC),
                                                                maxDate.toInstant(ZoneOffset.UTC));
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }



    /**
     * GET  /tatuadors/sede/:id : get all the tatuadors filtering by sede.
     *
     * @param pageable the pagination information
     * @param id the sede identifier
     * @return the ResponseEntity with status 200 (OK) and the list of tatuadors in body
     */
    @GetMapping("/tatuadors/sede/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<Tatuador>> getAllTatuadorsBySede(Pageable pageable, @PathVariable("id") Long id) {
        log.debug("REST request to get a page of Tatuadors");
        Page<Tatuador> page = tatuadorRepository.findAllBySede_Id(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tatuadors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tatuadors/cuenta/:id : get all the tatuadors filtering by account and only with active state.
     *
     * @param pageable the pagination information
     * @param id the account identifier
     * @return the ResponseEntity with status 200 (OK) and the list of tatuadors in body
     */
    @GetMapping("/tatuadors/activo/cuenta/{id}/")
    @Timed
    public ResponseEntity<List<Tatuador>> getAllActiveTatuadorsByCuenta(Pageable pageable, @PathVariable("id") Long id) {
        log.debug("REST request to get a page of Tatuadors");
        User user = userRepository.findOne(id);
        Page<Tatuador> page;
        if (user.isAdmin()){
            page = tatuadorRepository.findAllByEstadoIsTrue(pageable);
        }else{
            page = tatuadorRepository.findAllBySede_IdAndEstadoIsTrue(pageable, user.getSede().getId());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tatuadors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }



    /**
     * GET  /tatuadors/:id : get the "id" tatuador.
     *
     * @param id the id of the tatuador to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tatuador, or with status 404 (Not Found)
     */
    @GetMapping("/tatuadors/{id}")
    @Timed
    public ResponseEntity<Tatuador> getTatuador(@PathVariable Long id) {
        log.debug("REST request to get Tatuador : {}", id);
        Tatuador tatuador = tatuadorRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tatuador));
    }

    /**
     * DELETE  /tatuadors/:id : delete the "id" tatuador.
     *
     * @param id the id of the tatuador to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tatuadors/{id}")
    @Timed
    public ResponseEntity<Void> deleteTatuador(@PathVariable Long id) {
        log.debug("REST request to delete Tatuador : {}", id);
        Tatuador tatuador = tatuadorRepository.findOne(id);
        tatuador.setEstado(false);
        tatuadorRepository.save(tatuador);
        //tatuadorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
