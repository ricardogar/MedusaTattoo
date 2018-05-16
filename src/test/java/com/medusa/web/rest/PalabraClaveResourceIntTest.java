package com.medusa.web.rest;

import com.medusa.MedusaTattooApp;

import com.medusa.domain.PalabraClave;
import com.medusa.repository.PalabraClaveRepository;
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
 * Test class for the PalabraClaveResource REST controller.
 *
 * @see PalabraClaveResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedusaTattooApp.class)
public class PalabraClaveResourceIntTest {

    private static final String DEFAULT_PALABRA = "AAAAAAAAAA";
    private static final String UPDATED_PALABRA = "BBBBBBBBBB";

    @Autowired
    private PalabraClaveRepository palabraClaveRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPalabraClaveMockMvc;

    private PalabraClave palabraClave;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PalabraClaveResource palabraClaveResource = new PalabraClaveResource(palabraClaveRepository);
        this.restPalabraClaveMockMvc = MockMvcBuilders.standaloneSetup(palabraClaveResource)
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
    public static PalabraClave createEntity(EntityManager em) {
        PalabraClave palabraClave = new PalabraClave()
            .palabra(DEFAULT_PALABRA);
        return palabraClave;
    }

    @Before
    public void initTest() {
        palabraClave = createEntity(em);
    }

    @Test
    @Transactional
    public void createPalabraClave() throws Exception {
        int databaseSizeBeforeCreate = palabraClaveRepository.findAll().size();

        // Create the PalabraClave
        restPalabraClaveMockMvc.perform(post("/api/palabra-claves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(palabraClave)))
            .andExpect(status().isCreated());

        // Validate the PalabraClave in the database
        List<PalabraClave> palabraClaveList = palabraClaveRepository.findAll();
        assertThat(palabraClaveList).hasSize(databaseSizeBeforeCreate + 1);
        PalabraClave testPalabraClave = palabraClaveList.get(palabraClaveList.size() - 1);
        assertThat(testPalabraClave.getPalabra()).isEqualTo(DEFAULT_PALABRA);
    }

    @Test
    @Transactional
    public void createPalabraClaveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = palabraClaveRepository.findAll().size();

        // Create the PalabraClave with an existing ID
        palabraClave.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPalabraClaveMockMvc.perform(post("/api/palabra-claves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(palabraClave)))
            .andExpect(status().isBadRequest());

        // Validate the PalabraClave in the database
        List<PalabraClave> palabraClaveList = palabraClaveRepository.findAll();
        assertThat(palabraClaveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPalabraIsRequired() throws Exception {
        int databaseSizeBeforeTest = palabraClaveRepository.findAll().size();
        // set the field null
        palabraClave.setPalabra(null);

        // Create the PalabraClave, which fails.

        restPalabraClaveMockMvc.perform(post("/api/palabra-claves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(palabraClave)))
            .andExpect(status().isBadRequest());

        List<PalabraClave> palabraClaveList = palabraClaveRepository.findAll();
        assertThat(palabraClaveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPalabraClaves() throws Exception {
        // Initialize the database
        palabraClaveRepository.saveAndFlush(palabraClave);

        // Get all the palabraClaveList
        restPalabraClaveMockMvc.perform(get("/api/palabra-claves?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(palabraClave.getId().intValue())))
            .andExpect(jsonPath("$.[*].palabra").value(hasItem(DEFAULT_PALABRA.toString())));
    }

    @Test
    @Transactional
    public void getPalabraClave() throws Exception {
        // Initialize the database
        palabraClaveRepository.saveAndFlush(palabraClave);

        // Get the palabraClave
        restPalabraClaveMockMvc.perform(get("/api/palabra-claves/{id}", palabraClave.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(palabraClave.getId().intValue()))
            .andExpect(jsonPath("$.palabra").value(DEFAULT_PALABRA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPalabraClave() throws Exception {
        // Get the palabraClave
        restPalabraClaveMockMvc.perform(get("/api/palabra-claves/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePalabraClave() throws Exception {
        // Initialize the database
        palabraClaveRepository.saveAndFlush(palabraClave);
        int databaseSizeBeforeUpdate = palabraClaveRepository.findAll().size();

        // Update the palabraClave
        PalabraClave updatedPalabraClave = palabraClaveRepository.findOne(palabraClave.getId());
        // Disconnect from session so that the updates on updatedPalabraClave are not directly saved in db
        em.detach(updatedPalabraClave);
        updatedPalabraClave
            .palabra(UPDATED_PALABRA);

        restPalabraClaveMockMvc.perform(put("/api/palabra-claves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPalabraClave)))
            .andExpect(status().isOk());

        // Validate the PalabraClave in the database
        List<PalabraClave> palabraClaveList = palabraClaveRepository.findAll();
        assertThat(palabraClaveList).hasSize(databaseSizeBeforeUpdate);
        PalabraClave testPalabraClave = palabraClaveList.get(palabraClaveList.size() - 1);
        assertThat(testPalabraClave.getPalabra()).isEqualTo(UPDATED_PALABRA);
    }

    @Test
    @Transactional
    public void updateNonExistingPalabraClave() throws Exception {
        int databaseSizeBeforeUpdate = palabraClaveRepository.findAll().size();

        // Create the PalabraClave

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPalabraClaveMockMvc.perform(put("/api/palabra-claves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(palabraClave)))
            .andExpect(status().isCreated());

        // Validate the PalabraClave in the database
        List<PalabraClave> palabraClaveList = palabraClaveRepository.findAll();
        assertThat(palabraClaveList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePalabraClave() throws Exception {
        // Initialize the database
        palabraClaveRepository.saveAndFlush(palabraClave);
        int databaseSizeBeforeDelete = palabraClaveRepository.findAll().size();

        // Get the palabraClave
        restPalabraClaveMockMvc.perform(delete("/api/palabra-claves/{id}", palabraClave.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PalabraClave> palabraClaveList = palabraClaveRepository.findAll();
        assertThat(palabraClaveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PalabraClave.class);
        PalabraClave palabraClave1 = new PalabraClave();
        palabraClave1.setId(1L);
        PalabraClave palabraClave2 = new PalabraClave();
        palabraClave2.setId(palabraClave1.getId());
        assertThat(palabraClave1).isEqualTo(palabraClave2);
        palabraClave2.setId(2L);
        assertThat(palabraClave1).isNotEqualTo(palabraClave2);
        palabraClave1.setId(null);
        assertThat(palabraClave1).isNotEqualTo(palabraClave2);
    }
}
