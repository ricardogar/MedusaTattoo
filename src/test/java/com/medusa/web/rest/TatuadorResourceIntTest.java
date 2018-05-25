package com.medusa.web.rest;

import com.medusa.MedusaTattooApp;

import com.medusa.domain.Tatuador;
import com.medusa.domain.Sede;
import com.medusa.repository.TatuadorRepository;
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

import com.medusa.domain.enumeration.Tipo_documento;
import com.medusa.domain.enumeration.Genero;
/**
 * Test class for the TatuadorResource REST controller.
 *
 * @see TatuadorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedusaTattooApp.class)
public class TatuadorResourceIntTest {

    private static final Tipo_documento DEFAULT_TIPODOCUMENTO = Tipo_documento.CEDULA;
    private static final Tipo_documento UPDATED_TIPODOCUMENTO = Tipo_documento.CEDULA_EXTRANJERIA;

    private static final String DEFAULT_DOCUMENTO = "51";
    private static final String UPDATED_DOCUMENTO = "7";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "8";
    private static final String UPDATED_TELEFONO = "";

    private static final Genero DEFAULT_GENERO = Genero.MASCULINO;
    private static final Genero UPDATED_GENERO = Genero.FEMENINO;

    private static final String DEFAULT_APODO = "AAAAAAAAAA";
    private static final String UPDATED_APODO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_ESTADO = false;
    private static final Boolean UPDATED_ESTADO = true;

    @Autowired
    private TatuadorRepository tatuadorRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTatuadorMockMvc;

    private Tatuador tatuador;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TatuadorResource tatuadorResource = new TatuadorResource(tatuadorRepository);
        this.restTatuadorMockMvc = MockMvcBuilders.standaloneSetup(tatuadorResource)
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
    public static Tatuador createEntity(EntityManager em) {
        Tatuador tatuador = new Tatuador()
            .tipodocumento(DEFAULT_TIPODOCUMENTO)
            .documento(DEFAULT_DOCUMENTO)
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .telefono(DEFAULT_TELEFONO)
            .genero(DEFAULT_GENERO)
            .apodo(DEFAULT_APODO)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE)
            .estado(DEFAULT_ESTADO);
        // Add required entity
        Sede sede = SedeResourceIntTest.createEntity(em);
        em.persist(sede);
        em.flush();
        tatuador.setSede(sede);
        return tatuador;
    }

    @Before
    public void initTest() {
        tatuador = createEntity(em);
    }

    @Test
    @Transactional
    public void createTatuador() throws Exception {
        int databaseSizeBeforeCreate = tatuadorRepository.findAll().size();

        // Create the Tatuador
        restTatuadorMockMvc.perform(post("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isCreated());

        // Validate the Tatuador in the database
        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeCreate + 1);
        Tatuador testTatuador = tatuadorList.get(tatuadorList.size() - 1);
        assertThat(testTatuador.getTipodocumento()).isEqualTo(DEFAULT_TIPODOCUMENTO);
        assertThat(testTatuador.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
        assertThat(testTatuador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTatuador.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testTatuador.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testTatuador.getGenero()).isEqualTo(DEFAULT_GENERO);
        assertThat(testTatuador.getApodo()).isEqualTo(DEFAULT_APODO);
        assertThat(testTatuador.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testTatuador.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testTatuador.isEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void createTatuadorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tatuadorRepository.findAll().size();

        // Create the Tatuador with an existing ID
        tatuador.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTatuadorMockMvc.perform(post("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isBadRequest());

        // Validate the Tatuador in the database
        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTipodocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tatuadorRepository.findAll().size();
        // set the field null
        tatuador.setTipodocumento(null);

        // Create the Tatuador, which fails.

        restTatuadorMockMvc.perform(post("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isBadRequest());

        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tatuadorRepository.findAll().size();
        // set the field null
        tatuador.setDocumento(null);

        // Create the Tatuador, which fails.

        restTatuadorMockMvc.perform(post("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isBadRequest());

        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tatuadorRepository.findAll().size();
        // set the field null
        tatuador.setNombre(null);

        // Create the Tatuador, which fails.

        restTatuadorMockMvc.perform(post("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isBadRequest());

        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tatuadorRepository.findAll().size();
        // set the field null
        tatuador.setApellido(null);

        // Create the Tatuador, which fails.

        restTatuadorMockMvc.perform(post("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isBadRequest());

        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tatuadorRepository.findAll().size();
        // set the field null
        tatuador.setTelefono(null);

        // Create the Tatuador, which fails.

        restTatuadorMockMvc.perform(post("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isBadRequest());

        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGeneroIsRequired() throws Exception {
        int databaseSizeBeforeTest = tatuadorRepository.findAll().size();
        // set the field null
        tatuador.setGenero(null);

        // Create the Tatuador, which fails.

        restTatuadorMockMvc.perform(post("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isBadRequest());

        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApodoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tatuadorRepository.findAll().size();
        // set the field null
        tatuador.setApodo(null);

        // Create the Tatuador, which fails.

        restTatuadorMockMvc.perform(post("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isBadRequest());

        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTatuadors() throws Exception {
        // Initialize the database
        tatuadorRepository.saveAndFlush(tatuador);

        // Get all the tatuadorList
        restTatuadorMockMvc.perform(get("/api/tatuadors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tatuador.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipodocumento").value(hasItem(DEFAULT_TIPODOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())))
            .andExpect(jsonPath("$.[*].apodo").value(hasItem(DEFAULT_APODO.toString())))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.booleanValue())));
    }

    @Test
    @Transactional
    public void getTatuador() throws Exception {
        // Initialize the database
        tatuadorRepository.saveAndFlush(tatuador);

        // Get the tatuador
        restTatuadorMockMvc.perform(get("/api/tatuadors/{id}", tatuador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tatuador.getId().intValue()))
            .andExpect(jsonPath("$.tipodocumento").value(DEFAULT_TIPODOCUMENTO.toString()))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO.toString()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO.toString()))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()))
            .andExpect(jsonPath("$.apodo").value(DEFAULT_APODO.toString()))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTatuador() throws Exception {
        // Get the tatuador
        restTatuadorMockMvc.perform(get("/api/tatuadors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTatuador() throws Exception {
        // Initialize the database
        tatuadorRepository.saveAndFlush(tatuador);
        int databaseSizeBeforeUpdate = tatuadorRepository.findAll().size();

        // Update the tatuador
        Tatuador updatedTatuador = tatuadorRepository.findOne(tatuador.getId());
        // Disconnect from session so that the updates on updatedTatuador are not directly saved in db
        em.detach(updatedTatuador);
        updatedTatuador
            .tipodocumento(UPDATED_TIPODOCUMENTO)
            .documento(UPDATED_DOCUMENTO)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .telefono(UPDATED_TELEFONO)
            .genero(UPDATED_GENERO)
            .apodo(UPDATED_APODO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .estado(UPDATED_ESTADO);

        restTatuadorMockMvc.perform(put("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTatuador)))
            .andExpect(status().isOk());

        // Validate the Tatuador in the database
        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeUpdate);
        Tatuador testTatuador = tatuadorList.get(tatuadorList.size() - 1);
        assertThat(testTatuador.getTipodocumento()).isEqualTo(UPDATED_TIPODOCUMENTO);
        assertThat(testTatuador.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
        assertThat(testTatuador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTatuador.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testTatuador.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testTatuador.getGenero()).isEqualTo(UPDATED_GENERO);
        assertThat(testTatuador.getApodo()).isEqualTo(UPDATED_APODO);
        assertThat(testTatuador.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testTatuador.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testTatuador.isEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void updateNonExistingTatuador() throws Exception {
        int databaseSizeBeforeUpdate = tatuadorRepository.findAll().size();

        // Create the Tatuador

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTatuadorMockMvc.perform(put("/api/tatuadors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tatuador)))
            .andExpect(status().isCreated());

        // Validate the Tatuador in the database
        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTatuador() throws Exception {
        // Initialize the database
        tatuadorRepository.saveAndFlush(tatuador);
        int databaseSizeBeforeDelete = tatuadorRepository.findAll().size();

        // Get the tatuador
        restTatuadorMockMvc.perform(delete("/api/tatuadors/{id}", tatuador.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tatuador> tatuadorList = tatuadorRepository.findAll();
        assertThat(tatuadorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tatuador.class);
        Tatuador tatuador1 = new Tatuador();
        tatuador1.setId(1L);
        Tatuador tatuador2 = new Tatuador();
        tatuador2.setId(tatuador1.getId());
        assertThat(tatuador1).isEqualTo(tatuador2);
        tatuador2.setId(2L);
        assertThat(tatuador1).isNotEqualTo(tatuador2);
        tatuador1.setId(null);
        assertThat(tatuador1).isNotEqualTo(tatuador2);
    }
}
