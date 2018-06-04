package com.medusa.web.rest;

import com.medusa.MedusaTattooApp;

import com.medusa.domain.Cita;
import com.medusa.domain.Trabajo;
import com.medusa.repository.CitaRepository;
import com.medusa.repository.UserRepository;
import com.medusa.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.medusa.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CitaResource REST controller.
 *
 * @see CitaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedusaTattooApp.class)
public class CitaResourceIntTest {

    private static final Instant DEFAULT_FECHA_Y_HORA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_Y_HORA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_DURACION = 1.0;
    private static final Double UPDATED_DURACION = 2.0;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCitaMockMvc;

    private Cita cita;
    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CitaResource citaResource = new CitaResource(citaRepository, userRepository);
        this.restCitaMockMvc = MockMvcBuilders.standaloneSetup(citaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createEntity(EntityManager em) {
        Cita cita = new Cita()
            .fechaYHora(DEFAULT_FECHA_Y_HORA)
            .duracion(DEFAULT_DURACION);
        // Add required entity
        Trabajo trabajo = TrabajoResourceIntTest.createEntity(em);
        em.persist(trabajo);
        em.flush();
        cita.setTrabajo(trabajo);
        return cita;
    }

    @Before
    public void initTest() {
        cita = createEntity(em);
    }

    @Test
    @Transactional
    public void createCita() throws Exception {
        int databaseSizeBeforeCreate = citaRepository.findAll().size();

        // Create the Cita
        restCitaMockMvc.perform(post("/api/citas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cita)))
            .andExpect(status().isCreated());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeCreate + 1);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getFechaYHora()).isEqualTo(DEFAULT_FECHA_Y_HORA);
        assertThat(testCita.getDuracion()).isEqualTo(DEFAULT_DURACION);
    }

    @Test
    @Transactional
    public void createCitaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = citaRepository.findAll().size();

        // Create the Cita with an existing ID
        cita.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitaMockMvc.perform(post("/api/citas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cita)))
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaYHoraIsRequired() throws Exception {
        int databaseSizeBeforeTest = citaRepository.findAll().size();
        // set the field null
        cita.setFechaYHora(null);

        // Create the Cita, which fails.

        restCitaMockMvc.perform(post("/api/citas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cita)))
            .andExpect(status().isBadRequest());

        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDuracionIsRequired() throws Exception {
        int databaseSizeBeforeTest = citaRepository.findAll().size();
        // set the field null
        cita.setDuracion(null);

        // Create the Cita, which fails.

        restCitaMockMvc.perform(post("/api/citas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cita)))
            .andExpect(status().isBadRequest());

        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCitas() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        // Get all the citaList
        restCitaMockMvc.perform(get("/api/citas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cita.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaYHora").value(hasItem(DEFAULT_FECHA_Y_HORA.toString())))
            .andExpect(jsonPath("$.[*].duracion").value(hasItem(DEFAULT_DURACION)));
    }

    @Test
    @Transactional
    public void getCita() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        // Get the cita
        restCitaMockMvc.perform(get("/api/citas/{id}", cita.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cita.getId().intValue()))
            .andExpect(jsonPath("$.fechaYHora").value(DEFAULT_FECHA_Y_HORA.toString()))
            .andExpect(jsonPath("$.duracion").value(DEFAULT_DURACION));
    }

    @Test
    @Transactional
    public void getNonExistingCita() throws Exception {
        // Get the cita
        restCitaMockMvc.perform(get("/api/citas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCita() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();

        // Update the cita
        Cita updatedCita = citaRepository.findOne(cita.getId());
        // Disconnect from session so that the updates on updatedCita are not directly saved in db
        em.detach(updatedCita);
        updatedCita
            .fechaYHora(UPDATED_FECHA_Y_HORA)
            .duracion(UPDATED_DURACION);

        restCitaMockMvc.perform(put("/api/citas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCita)))
            .andExpect(status().isOk());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getFechaYHora()).isEqualTo(UPDATED_FECHA_Y_HORA);
        assertThat(testCita.getDuracion()).isEqualTo(UPDATED_DURACION);
    }

    @Test
    @Transactional
    public void updateNonExistingCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();

        // Create the Cita

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCitaMockMvc.perform(put("/api/citas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cita)))
            .andExpect(status().isCreated());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCita() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);
        int databaseSizeBeforeDelete = citaRepository.findAll().size();

        // Get the cita
        restCitaMockMvc.perform(delete("/api/citas/{id}", cita.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cita.class);
        Cita cita1 = new Cita();
        cita1.setId(1L);
        Cita cita2 = new Cita();
        cita2.setId(cita1.getId());
        assertThat(cita1).isEqualTo(cita2);
        cita2.setId(2L);
        assertThat(cita1).isNotEqualTo(cita2);
        cita1.setId(null);
        assertThat(cita1).isNotEqualTo(cita2);
    }
}
