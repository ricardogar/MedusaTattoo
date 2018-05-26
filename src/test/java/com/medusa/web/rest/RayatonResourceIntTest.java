package com.medusa.web.rest;

import com.medusa.MedusaTattooApp;

import com.medusa.domain.Rayaton;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.medusa.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RayatonResource REST controller.
 *
 * @see RayatonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedusaTattooApp.class)
public class RayatonResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_CUPOS = 100000;
    private static final Integer UPDATED_CUPOS = 99999;

    private static final Integer DEFAULT_VALOR_CUPO = 20000;
    private static final Integer UPDATED_VALOR_CUPO = 20001;

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    @Autowired
    private RayatonRepository rayatonRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRayatonMockMvc;

    private Rayaton rayaton;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RayatonResource rayatonResource = new RayatonResource(rayatonRepository);
        this.restRayatonMockMvc = MockMvcBuilders.standaloneSetup(rayatonResource)
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
    public static Rayaton createEntity(EntityManager em) {
        Rayaton rayaton = new Rayaton()
            .fecha(DEFAULT_FECHA)
            .cupos(DEFAULT_CUPOS)
            .valorCupo(DEFAULT_VALOR_CUPO)
            .comentario(DEFAULT_COMENTARIO)
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE);
        return rayaton;
    }

    @Before
    public void initTest() {
        rayaton = createEntity(em);
    }

    @Test
    @Transactional
    public void createRayaton() throws Exception {
        int databaseSizeBeforeCreate = rayatonRepository.findAll().size();

        // Create the Rayaton
        restRayatonMockMvc.perform(post("/api/rayatons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rayaton)))
            .andExpect(status().isCreated());

        // Validate the Rayaton in the database
        List<Rayaton> rayatonList = rayatonRepository.findAll();
        assertThat(rayatonList).hasSize(databaseSizeBeforeCreate + 1);
        Rayaton testRayaton = rayatonList.get(rayatonList.size() - 1);
        assertThat(testRayaton.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testRayaton.getCupos()).isEqualTo(DEFAULT_CUPOS);
        assertThat(testRayaton.getValorCupo()).isEqualTo(DEFAULT_VALOR_CUPO);
        assertThat(testRayaton.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testRayaton.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testRayaton.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createRayatonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rayatonRepository.findAll().size();

        // Create the Rayaton with an existing ID
        rayaton.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRayatonMockMvc.perform(post("/api/rayatons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rayaton)))
            .andExpect(status().isBadRequest());

        // Validate the Rayaton in the database
        List<Rayaton> rayatonList = rayatonRepository.findAll();
        assertThat(rayatonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = rayatonRepository.findAll().size();
        // set the field null
        rayaton.setFecha(null);

        // Create the Rayaton, which fails.

        restRayatonMockMvc.perform(post("/api/rayatons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rayaton)))
            .andExpect(status().isBadRequest());

        List<Rayaton> rayatonList = rayatonRepository.findAll();
        assertThat(rayatonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCuposIsRequired() throws Exception {
        int databaseSizeBeforeTest = rayatonRepository.findAll().size();
        // set the field null
        rayaton.setCupos(null);

        // Create the Rayaton, which fails.

        restRayatonMockMvc.perform(post("/api/rayatons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rayaton)))
            .andExpect(status().isBadRequest());

        List<Rayaton> rayatonList = rayatonRepository.findAll();
        assertThat(rayatonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorCupoIsRequired() throws Exception {
        int databaseSizeBeforeTest = rayatonRepository.findAll().size();
        // set the field null
        rayaton.setValorCupo(null);

        // Create the Rayaton, which fails.

        restRayatonMockMvc.perform(post("/api/rayatons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rayaton)))
            .andExpect(status().isBadRequest());

        List<Rayaton> rayatonList = rayatonRepository.findAll();
        assertThat(rayatonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRayatons() throws Exception {
        // Initialize the database
        rayatonRepository.saveAndFlush(rayaton);

        // Get all the rayatonList
        restRayatonMockMvc.perform(get("/api/rayatons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rayaton.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].cupos").value(hasItem(DEFAULT_CUPOS)))
            .andExpect(jsonPath("$.[*].valorCupo").value(hasItem(DEFAULT_VALOR_CUPO)))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO.toString())))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))));
    }

    @Test
    @Transactional
    public void getRayaton() throws Exception {
        // Initialize the database
        rayatonRepository.saveAndFlush(rayaton);

        // Get the rayaton
        restRayatonMockMvc.perform(get("/api/rayatons/{id}", rayaton.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rayaton.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.cupos").value(DEFAULT_CUPOS))
            .andExpect(jsonPath("$.valorCupo").value(DEFAULT_VALOR_CUPO))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO.toString()))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64Utils.encodeToString(DEFAULT_IMAGEN)));
    }

    @Test
    @Transactional
    public void getNonExistingRayaton() throws Exception {
        // Get the rayaton
        restRayatonMockMvc.perform(get("/api/rayatons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRayaton() throws Exception {
        // Initialize the database
        rayatonRepository.saveAndFlush(rayaton);
        int databaseSizeBeforeUpdate = rayatonRepository.findAll().size();

        // Update the rayaton
        Rayaton updatedRayaton = rayatonRepository.findOne(rayaton.getId());
        // Disconnect from session so that the updates on updatedRayaton are not directly saved in db
        em.detach(updatedRayaton);
        updatedRayaton
            .fecha(UPDATED_FECHA)
            .cupos(UPDATED_CUPOS)
            .valorCupo(UPDATED_VALOR_CUPO)
            .comentario(UPDATED_COMENTARIO)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE);

        restRayatonMockMvc.perform(put("/api/rayatons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRayaton)))
            .andExpect(status().isOk());

        // Validate the Rayaton in the database
        List<Rayaton> rayatonList = rayatonRepository.findAll();
        assertThat(rayatonList).hasSize(databaseSizeBeforeUpdate);
        Rayaton testRayaton = rayatonList.get(rayatonList.size() - 1);
        assertThat(testRayaton.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testRayaton.getCupos()).isEqualTo(UPDATED_CUPOS);
        assertThat(testRayaton.getValorCupo()).isEqualTo(UPDATED_VALOR_CUPO);
        assertThat(testRayaton.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testRayaton.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testRayaton.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingRayaton() throws Exception {
        int databaseSizeBeforeUpdate = rayatonRepository.findAll().size();

        // Create the Rayaton

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRayatonMockMvc.perform(put("/api/rayatons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rayaton)))
            .andExpect(status().isCreated());

        // Validate the Rayaton in the database
        List<Rayaton> rayatonList = rayatonRepository.findAll();
        assertThat(rayatonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRayaton() throws Exception {
        // Initialize the database
        rayatonRepository.saveAndFlush(rayaton);
        int databaseSizeBeforeDelete = rayatonRepository.findAll().size();

        // Get the rayaton
        restRayatonMockMvc.perform(delete("/api/rayatons/{id}", rayaton.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rayaton> rayatonList = rayatonRepository.findAll();
        assertThat(rayatonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rayaton.class);
        Rayaton rayaton1 = new Rayaton();
        rayaton1.setId(1L);
        Rayaton rayaton2 = new Rayaton();
        rayaton2.setId(rayaton1.getId());
        assertThat(rayaton1).isEqualTo(rayaton2);
        rayaton2.setId(2L);
        assertThat(rayaton1).isNotEqualTo(rayaton2);
        rayaton1.setId(null);
        assertThat(rayaton1).isNotEqualTo(rayaton2);
    }
}
