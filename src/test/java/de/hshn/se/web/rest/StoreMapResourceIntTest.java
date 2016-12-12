package de.hshn.se.web.rest;

import de.hshn.se.IsaApp;

import de.hshn.se.domain.StoreMap;
import de.hshn.se.domain.Store;
import de.hshn.se.repository.StoreMapRepository;
import de.hshn.se.service.StoreMapService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static de.hshn.se.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StoreMapResource REST controller.
 *
 * @see StoreMapResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IsaApp.class)
public class StoreMapResourceIntTest {

    private static final ZonedDateTime DEFAULT_VALIDITY_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALIDITY_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_VALIDITY_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALIDITY_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Inject
    private StoreMapRepository storeMapRepository;

    @Inject
    private StoreMapService storeMapService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restStoreMapMockMvc;

    private StoreMap storeMap;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StoreMapResource storeMapResource = new StoreMapResource();
        ReflectionTestUtils.setField(storeMapResource, "storeMapService", storeMapService);
        this.restStoreMapMockMvc = MockMvcBuilders.standaloneSetup(storeMapResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoreMap createEntity(EntityManager em) {
        StoreMap storeMap = new StoreMap()
                .validityStart(DEFAULT_VALIDITY_START)
                .validityEnd(DEFAULT_VALIDITY_END)
                .url(DEFAULT_URL);
        // Add required entity
        Store store = StoreResourceIntTest.createEntity(em);
        em.persist(store);
        em.flush();
        storeMap.setStore(store);
        return storeMap;
    }

    @Before
    public void initTest() {
        storeMap = createEntity(em);
    }

    @Test
    @Transactional
    public void createStoreMap() throws Exception {
        int databaseSizeBeforeCreate = storeMapRepository.findAll().size();

        // Create the StoreMap

        restStoreMapMockMvc.perform(post("/api/store-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeMap)))
            .andExpect(status().isCreated());

        // Validate the StoreMap in the database
        List<StoreMap> storeMapList = storeMapRepository.findAll();
        assertThat(storeMapList).hasSize(databaseSizeBeforeCreate + 1);
        StoreMap testStoreMap = storeMapList.get(storeMapList.size() - 1);
        assertThat(testStoreMap.getValidityStart()).isEqualTo(DEFAULT_VALIDITY_START);
        assertThat(testStoreMap.getValidityEnd()).isEqualTo(DEFAULT_VALIDITY_END);
        assertThat(testStoreMap.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createStoreMapWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeMapRepository.findAll().size();

        // Create the StoreMap with an existing ID
        StoreMap existingStoreMap = new StoreMap();
        existingStoreMap.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreMapMockMvc.perform(post("/api/store-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStoreMap)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StoreMap> storeMapList = storeMapRepository.findAll();
        assertThat(storeMapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValidityStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeMapRepository.findAll().size();
        // set the field null
        storeMap.setValidityStart(null);

        // Create the StoreMap, which fails.

        restStoreMapMockMvc.perform(post("/api/store-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeMap)))
            .andExpect(status().isBadRequest());

        List<StoreMap> storeMapList = storeMapRepository.findAll();
        assertThat(storeMapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidityEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeMapRepository.findAll().size();
        // set the field null
        storeMap.setValidityEnd(null);

        // Create the StoreMap, which fails.

        restStoreMapMockMvc.perform(post("/api/store-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeMap)))
            .andExpect(status().isBadRequest());

        List<StoreMap> storeMapList = storeMapRepository.findAll();
        assertThat(storeMapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeMapRepository.findAll().size();
        // set the field null
        storeMap.setUrl(null);

        // Create the StoreMap, which fails.

        restStoreMapMockMvc.perform(post("/api/store-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeMap)))
            .andExpect(status().isBadRequest());

        List<StoreMap> storeMapList = storeMapRepository.findAll();
        assertThat(storeMapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStoreMaps() throws Exception {
        // Initialize the database
        storeMapRepository.saveAndFlush(storeMap);

        // Get all the storeMapList
        restStoreMapMockMvc.perform(get("/api/store-maps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeMap.getId().intValue())))
            .andExpect(jsonPath("$.[*].validityStart").value(hasItem(sameInstant(DEFAULT_VALIDITY_START))))
            .andExpect(jsonPath("$.[*].validityEnd").value(hasItem(sameInstant(DEFAULT_VALIDITY_END))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getStoreMap() throws Exception {
        // Initialize the database
        storeMapRepository.saveAndFlush(storeMap);

        // Get the storeMap
        restStoreMapMockMvc.perform(get("/api/store-maps/{id}", storeMap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storeMap.getId().intValue()))
            .andExpect(jsonPath("$.validityStart").value(sameInstant(DEFAULT_VALIDITY_START)))
            .andExpect(jsonPath("$.validityEnd").value(sameInstant(DEFAULT_VALIDITY_END)))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStoreMap() throws Exception {
        // Get the storeMap
        restStoreMapMockMvc.perform(get("/api/store-maps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreMap() throws Exception {
        // Initialize the database
        storeMapService.save(storeMap);

        int databaseSizeBeforeUpdate = storeMapRepository.findAll().size();

        // Update the storeMap
        StoreMap updatedStoreMap = storeMapRepository.findOne(storeMap.getId());
        updatedStoreMap
                .validityStart(UPDATED_VALIDITY_START)
                .validityEnd(UPDATED_VALIDITY_END)
                .url(UPDATED_URL);

        restStoreMapMockMvc.perform(put("/api/store-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStoreMap)))
            .andExpect(status().isOk());

        // Validate the StoreMap in the database
        List<StoreMap> storeMapList = storeMapRepository.findAll();
        assertThat(storeMapList).hasSize(databaseSizeBeforeUpdate);
        StoreMap testStoreMap = storeMapList.get(storeMapList.size() - 1);
        assertThat(testStoreMap.getValidityStart()).isEqualTo(UPDATED_VALIDITY_START);
        assertThat(testStoreMap.getValidityEnd()).isEqualTo(UPDATED_VALIDITY_END);
        assertThat(testStoreMap.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingStoreMap() throws Exception {
        int databaseSizeBeforeUpdate = storeMapRepository.findAll().size();

        // Create the StoreMap

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStoreMapMockMvc.perform(put("/api/store-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeMap)))
            .andExpect(status().isCreated());

        // Validate the StoreMap in the database
        List<StoreMap> storeMapList = storeMapRepository.findAll();
        assertThat(storeMapList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStoreMap() throws Exception {
        // Initialize the database
        storeMapService.save(storeMap);

        int databaseSizeBeforeDelete = storeMapRepository.findAll().size();

        // Get the storeMap
        restStoreMapMockMvc.perform(delete("/api/store-maps/{id}", storeMap.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StoreMap> storeMapList = storeMapRepository.findAll();
        assertThat(storeMapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
