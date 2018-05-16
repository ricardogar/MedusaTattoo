package com.medusa.web.rest;

import com.medusa.MedusaTattooApp;

import com.medusa.domain.Sede;
import com.medusa.repository.SedeRepository;
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
import java.util.List;

import static com.medusa.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SedeResource REST controller.
 *
 * @see SedeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedusaTattooApp.class)
public class SedeResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "";
    private static final String UPDATED_TELEFONO = "B";

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSedeMockMvc;

    private Sede sede;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SedeResource sedeResource = new SedeResource(sedeRepository);
        this.restSedeMockMvc = MockMvcBuilders.standaloneSetup(sedeResource)
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
    public static Sede createEntity(EntityManager em) {
        Sede sede = new Sede()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO);
        return sede;
    }

    @Before
    public void initTest() {
        sede = createEntity(em);
    }

    @Test
    @Transactional
    public void createSede() throws Exception {
        int databaseSizeBeforeCreate = sedeRepository.findAll().size();

        // Create the Sede
        restSedeMockMvc.perform(post("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isCreated());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeCreate + 1);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSede.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testSede.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    public void createSedeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sedeRepository.findAll().size();

        // Create the Sede with an existing ID
        sede.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSedeMockMvc.perform(post("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isBadRequest());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().size();
        // set the field null
        sede.setNombre(null);

        // Create the Sede, which fails.

        restSedeMockMvc.perform(post("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isBadRequest());

        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().size();
        // set the field null
        sede.setDireccion(null);

        // Create the Sede, which fails.

        restSedeMockMvc.perform(post("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isBadRequest());

        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().size();
        // set the field null
        sede.setTelefono(null);

        // Create the Sede, which fails.

        restSedeMockMvc.perform(post("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isBadRequest());

        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSedes() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        // Get all the sedeList
        restSedeMockMvc.perform(get("/api/sedes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sede.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())));
    }

    @Test
    @Transactional
    public void getSede() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        // Get the sede
        restSedeMockMvc.perform(get("/api/sedes/{id}", sede.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sede.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSede() throws Exception {
        // Get the sede
        restSedeMockMvc.perform(get("/api/sedes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSede() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);
        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();

        // Update the sede
        Sede updatedSede = sedeRepository.findOne(sede.getId());
        // Disconnect from session so that the updates on updatedSede are not directly saved in db
        em.detach(updatedSede);
        updatedSede
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO);

        restSedeMockMvc.perform(put("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSede)))
            .andExpect(status().isOk());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSede.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSede.getTelefono()).isEqualTo(UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void updateNonExistingSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();

        // Create the Sede

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSedeMockMvc.perform(put("/api/sedes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isCreated());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSede() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);
        int databaseSizeBeforeDelete = sedeRepository.findAll().size();

        // Get the sede
        restSedeMockMvc.perform(delete("/api/sedes/{id}", sede.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sede.class);
        Sede sede1 = new Sede();
        sede1.setId(1L);
        Sede sede2 = new Sede();
        sede2.setId(sede1.getId());
        assertThat(sede1).isEqualTo(sede2);
        sede2.setId(2L);
        assertThat(sede1).isNotEqualTo(sede2);
        sede1.setId(null);
        assertThat(sede1).isNotEqualTo(sede2);
    }
}
