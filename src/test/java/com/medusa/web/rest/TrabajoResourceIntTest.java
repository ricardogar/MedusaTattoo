package com.medusa.web.rest;

import com.medusa.MedusaTattooApp;

import com.medusa.domain.Trabajo;
import com.medusa.domain.Tatuador;
import com.medusa.domain.Cliente;
import com.medusa.domain.Sede;
import com.medusa.repository.TrabajoRepository;
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

import com.medusa.domain.enumeration.Estado_trabajo;
import com.medusa.domain.enumeration.Tipo_trabajo;
/**
 * Test class for the TrabajoResource REST controller.
 *
 * @see TrabajoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedusaTattooApp.class)
public class TrabajoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_COSTO_TOTAL = "5";
    private static final String UPDATED_COSTO_TOTAL = "2";

    private static final String DEFAULT_TOTAL_PAGADO = "7";
    private static final String UPDATED_TOTAL_PAGADO = "4";

    private static final Estado_trabajo DEFAULT_ESTADO = Estado_trabajo.EN_PROGRESO;
    private static final Estado_trabajo UPDATED_ESTADO = Estado_trabajo.FINALIZADO;

    private static final Tipo_trabajo DEFAULT_TIPO = Tipo_trabajo.NORMAL;
    private static final Tipo_trabajo UPDATED_TIPO = Tipo_trabajo.RAYATON;

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrabajoMockMvc;

    private Trabajo trabajo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrabajoResource trabajoResource = new TrabajoResource(trabajoRepository);
        this.restTrabajoMockMvc = MockMvcBuilders.standaloneSetup(trabajoResource)
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
    public static Trabajo createEntity(EntityManager em) {
        Trabajo trabajo = new Trabajo()
            .nombre(DEFAULT_NOMBRE)
            .costoTotal(DEFAULT_COSTO_TOTAL)
            .totalPagado(DEFAULT_TOTAL_PAGADO)
            .estado(DEFAULT_ESTADO)
            .tipo(DEFAULT_TIPO)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
        // Add required entity
        Tatuador tatuador = TatuadorResourceIntTest.createEntity(em);
        em.persist(tatuador);
        em.flush();
        trabajo.setTatuador(tatuador);
        // Add required entity
        Cliente cliente = ClienteResourceIntTest.createEntity(em);
        em.persist(cliente);
        em.flush();
        trabajo.setCliente(cliente);
        // Add required entity
        Sede sede = SedeResourceIntTest.createEntity(em);
        em.persist(sede);
        em.flush();
        trabajo.setSede(sede);
        return trabajo;
    }

    @Before
    public void initTest() {
        trabajo = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrabajo() throws Exception {
        int databaseSizeBeforeCreate = trabajoRepository.findAll().size();

        // Create the Trabajo
        restTrabajoMockMvc.perform(post("/api/trabajos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isCreated());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeCreate + 1);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTrabajo.getCostoTotal()).isEqualTo(DEFAULT_COSTO_TOTAL);
        assertThat(testTrabajo.getTotalPagado()).isEqualTo(DEFAULT_TOTAL_PAGADO);
        assertThat(testTrabajo.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testTrabajo.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testTrabajo.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testTrabajo.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createTrabajoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trabajoRepository.findAll().size();

        // Create the Trabajo with an existing ID
        trabajo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrabajoMockMvc.perform(post("/api/trabajos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajoRepository.findAll().size();
        // set the field null
        trabajo.setNombre(null);

        // Create the Trabajo, which fails.

        restTrabajoMockMvc.perform(post("/api/trabajos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isBadRequest());

        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCostoTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajoRepository.findAll().size();
        // set the field null
        trabajo.setCostoTotal(null);

        // Create the Trabajo, which fails.

        restTrabajoMockMvc.perform(post("/api/trabajos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isBadRequest());

        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajoRepository.findAll().size();
        // set the field null
        trabajo.setEstado(null);

        // Create the Trabajo, which fails.

        restTrabajoMockMvc.perform(post("/api/trabajos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isBadRequest());

        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = trabajoRepository.findAll().size();
        // set the field null
        trabajo.setTipo(null);

        // Create the Trabajo, which fails.

        restTrabajoMockMvc.perform(post("/api/trabajos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isBadRequest());

        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrabajos() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get all the trabajoList
        restTrabajoMockMvc.perform(get("/api/trabajos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].costoTotal").value(hasItem(DEFAULT_COSTO_TOTAL.toString())))
            .andExpect(jsonPath("$.[*].totalPagado").value(hasItem(DEFAULT_TOTAL_PAGADO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))));
    }

    @Test
    @Transactional
    public void getTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get the trabajo
        restTrabajoMockMvc.perform(get("/api/trabajos/{id}", trabajo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trabajo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.costoTotal").value(DEFAULT_COSTO_TOTAL.toString()))
            .andExpect(jsonPath("$.totalPagado").value(DEFAULT_TOTAL_PAGADO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    public void getNonExistingTrabajo() throws Exception {
        // Get the trabajo
        restTrabajoMockMvc.perform(get("/api/trabajos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Update the trabajo
        Trabajo updatedTrabajo = trabajoRepository.findOne(trabajo.getId());
        // Disconnect from session so that the updates on updatedTrabajo are not directly saved in db
        em.detach(updatedTrabajo);
        updatedTrabajo
            .nombre(UPDATED_NOMBRE)
            .costoTotal(UPDATED_COSTO_TOTAL)
            .totalPagado(UPDATED_TOTAL_PAGADO)
            .estado(UPDATED_ESTADO)
            .tipo(UPDATED_TIPO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restTrabajoMockMvc.perform(put("/api/trabajos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrabajo)))
            .andExpect(status().isOk());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTrabajo.getCostoTotal()).isEqualTo(UPDATED_COSTO_TOTAL);
        assertThat(testTrabajo.getTotalPagado()).isEqualTo(UPDATED_TOTAL_PAGADO);
        assertThat(testTrabajo.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testTrabajo.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTrabajo.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testTrabajo.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Create the Trabajo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTrabajoMockMvc.perform(put("/api/trabajos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isCreated());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);
        int databaseSizeBeforeDelete = trabajoRepository.findAll().size();

        // Get the trabajo
        restTrabajoMockMvc.perform(delete("/api/trabajos/{id}", trabajo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trabajo.class);
        Trabajo trabajo1 = new Trabajo();
        trabajo1.setId(1L);
        Trabajo trabajo2 = new Trabajo();
        trabajo2.setId(trabajo1.getId());
        assertThat(trabajo1).isEqualTo(trabajo2);
        trabajo2.setId(2L);
        assertThat(trabajo1).isNotEqualTo(trabajo2);
        trabajo1.setId(null);
        assertThat(trabajo1).isNotEqualTo(trabajo2);
    }
}
