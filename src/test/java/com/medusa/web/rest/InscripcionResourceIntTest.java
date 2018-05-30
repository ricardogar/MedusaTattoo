package com.medusa.web.rest;

import com.medusa.MedusaTattooApp;

import com.medusa.domain.Inscripcion;
import com.medusa.repository.InscripcionRepository;
import com.medusa.repository.RayatonRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.medusa.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medusa.domain.enumeration.Estado_inscripcion;
/**
 * Test class for the InscripcionResource REST controller.
 *
 * @see InscripcionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedusaTattooApp.class)
public class InscripcionResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "3";
    private static final String UPDATED_TELEFONO = "5";

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    private static final Estado_inscripcion DEFAULT_ESTADO = Estado_inscripcion.PRE_INSCRITO;
    private static final Estado_inscripcion UPDATED_ESTADO = Estado_inscripcion.INSCRITO;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInscripcionMockMvc;

    private Inscripcion inscripcion;
    private RayatonRepository rayatonRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InscripcionResource inscripcionResource = new InscripcionResource(inscripcionRepository, rayatonRepository);
        this.restInscripcionMockMvc = MockMvcBuilders.standaloneSetup(inscripcionResource)
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
    public static Inscripcion createEntity(EntityManager em) {
        Inscripcion inscripcion = new Inscripcion()
            .nombre(DEFAULT_NOMBRE)
            .telefono(DEFAULT_TELEFONO)
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE)
            .estado(DEFAULT_ESTADO);
        return inscripcion;
    }

    @Before
    public void initTest() {
        inscripcion = createEntity(em);
    }

    @Test
    @Transactional
    public void createInscripcion() throws Exception {
        int databaseSizeBeforeCreate = inscripcionRepository.findAll().size();

        // Create the Inscripcion
        restInscripcionMockMvc.perform(post("/api/inscripcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inscripcion)))
            .andExpect(status().isCreated());

        // Validate the Inscripcion in the database
        List<Inscripcion> inscripcionList = inscripcionRepository.findAll();
        assertThat(inscripcionList).hasSize(databaseSizeBeforeCreate + 1);
        Inscripcion testInscripcion = inscripcionList.get(inscripcionList.size() - 1);
        assertThat(testInscripcion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testInscripcion.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testInscripcion.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testInscripcion.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);
        assertThat(testInscripcion.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void createInscripcionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inscripcionRepository.findAll().size();

        // Create the Inscripcion with an existing ID
        inscripcion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscripcionMockMvc.perform(post("/api/inscripcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inscripcion)))
            .andExpect(status().isBadRequest());

        // Validate the Inscripcion in the database
        List<Inscripcion> inscripcionList = inscripcionRepository.findAll();
        assertThat(inscripcionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscripcionRepository.findAll().size();
        // set the field null
        inscripcion.setNombre(null);

        // Create the Inscripcion, which fails.

        restInscripcionMockMvc.perform(post("/api/inscripcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inscripcion)))
            .andExpect(status().isBadRequest());

        List<Inscripcion> inscripcionList = inscripcionRepository.findAll();
        assertThat(inscripcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscripcionRepository.findAll().size();
        // set the field null
        inscripcion.setTelefono(null);

        // Create the Inscripcion, which fails.

        restInscripcionMockMvc.perform(post("/api/inscripcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inscripcion)))
            .andExpect(status().isBadRequest());

        List<Inscripcion> inscripcionList = inscripcionRepository.findAll();
        assertThat(inscripcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImagenIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscripcionRepository.findAll().size();
        // set the field null
        inscripcion.setImagen(null);

        // Create the Inscripcion, which fails.

        restInscripcionMockMvc.perform(post("/api/inscripcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inscripcion)))
            .andExpect(status().isBadRequest());

        List<Inscripcion> inscripcionList = inscripcionRepository.findAll();
        assertThat(inscripcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscripcionRepository.findAll().size();
        // set the field null
        inscripcion.setEstado(null);

        // Create the Inscripcion, which fails.

        restInscripcionMockMvc.perform(post("/api/inscripcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inscripcion)))
            .andExpect(status().isBadRequest());

        List<Inscripcion> inscripcionList = inscripcionRepository.findAll();
        assertThat(inscripcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInscripcions() throws Exception {
        // Initialize the database
        inscripcionRepository.saveAndFlush(inscripcion);

        // Get all the inscripcionList
        restInscripcionMockMvc.perform(get("/api/inscripcions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscripcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    public void getInscripcion() throws Exception {
        // Initialize the database
        inscripcionRepository.saveAndFlush(inscripcion);

        // Get the inscripcion
        restInscripcionMockMvc.perform(get("/api/inscripcions/{id}", inscripcion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inscripcion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO.toString()))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64Utils.encodeToString(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInscripcion() throws Exception {
        // Get the inscripcion
        restInscripcionMockMvc.perform(get("/api/inscripcions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscripcion() throws Exception {
        // Initialize the database
        inscripcionRepository.saveAndFlush(inscripcion);
        int databaseSizeBeforeUpdate = inscripcionRepository.findAll().size();

        // Update the inscripcion
        Inscripcion updatedInscripcion = inscripcionRepository.findOne(inscripcion.getId());
        // Disconnect from session so that the updates on updatedInscripcion are not directly saved in db
        em.detach(updatedInscripcion);
        updatedInscripcion
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .estado(UPDATED_ESTADO);

        restInscripcionMockMvc.perform(put("/api/inscripcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInscripcion)))
            .andExpect(status().isOk());

        // Validate the Inscripcion in the database
        List<Inscripcion> inscripcionList = inscripcionRepository.findAll();
        assertThat(inscripcionList).hasSize(databaseSizeBeforeUpdate);
        Inscripcion testInscripcion = inscripcionList.get(inscripcionList.size() - 1);
        assertThat(testInscripcion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testInscripcion.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testInscripcion.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testInscripcion.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testInscripcion.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void updateNonExistingInscripcion() throws Exception {
        int databaseSizeBeforeUpdate = inscripcionRepository.findAll().size();

        // Create the Inscripcion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInscripcionMockMvc.perform(put("/api/inscripcions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inscripcion)))
            .andExpect(status().isCreated());

        // Validate the Inscripcion in the database
        List<Inscripcion> inscripcionList = inscripcionRepository.findAll();
        assertThat(inscripcionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInscripcion() throws Exception {
        // Initialize the database
        inscripcionRepository.saveAndFlush(inscripcion);
        int databaseSizeBeforeDelete = inscripcionRepository.findAll().size();

        // Get the inscripcion
        restInscripcionMockMvc.perform(delete("/api/inscripcions/{id}", inscripcion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Inscripcion> inscripcionList = inscripcionRepository.findAll();
        assertThat(inscripcionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inscripcion.class);
        Inscripcion inscripcion1 = new Inscripcion();
        inscripcion1.setId(1L);
        Inscripcion inscripcion2 = new Inscripcion();
        inscripcion2.setId(inscripcion1.getId());
        assertThat(inscripcion1).isEqualTo(inscripcion2);
        inscripcion2.setId(2L);
        assertThat(inscripcion1).isNotEqualTo(inscripcion2);
        inscripcion1.setId(null);
        assertThat(inscripcion1).isNotEqualTo(inscripcion2);
    }
}
