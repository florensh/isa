package de.hshn.se.web.rest;

import de.hshn.se.IsaApp;

import de.hshn.se.domain.MeasurementDataset;
import de.hshn.se.repository.MeasurementDatasetRepository;
import de.hshn.se.service.MeasurementDatasetService;

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
import org.springframework.util.Base64Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MeasurementDatasetResource REST controller.
 *
 * @see MeasurementDatasetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IsaApp.class)
public class MeasurementDatasetResourceIntTest {

    private static final String DEFAULT_MEASUREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_MEASUREMENTS = "BBBBBBBBBB";

    private static final Long DEFAULT_STORE_ID = 1L;
    private static final Long UPDATED_STORE_ID = 2L;

    private static final String DEFAULT_DEVICE_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_IDENTIFICATION = "BBBBBBBBBB";

    private static final Double DEFAULT_START_LAN = 1D;
    private static final Double UPDATED_START_LAN = 2D;

    private static final Double DEFAULT_START_LON = 1D;
    private static final Double UPDATED_START_LON = 2D;

    private static final Float DEFAULT_START_ACCURACY = 1F;
    private static final Float UPDATED_START_ACCURACY = 2F;

    @Inject
    private MeasurementDatasetRepository measurementDatasetRepository;

    @Inject
    private MeasurementDatasetService measurementDatasetService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMeasurementDatasetMockMvc;

    private MeasurementDataset measurementDataset;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeasurementDatasetResource measurementDatasetResource = new MeasurementDatasetResource();
        ReflectionTestUtils.setField(measurementDatasetResource, "measurementDatasetService", measurementDatasetService);
        this.restMeasurementDatasetMockMvc = MockMvcBuilders.standaloneSetup(measurementDatasetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasurementDataset createEntity(EntityManager em) {
        MeasurementDataset measurementDataset = new MeasurementDataset()
                .measurements(DEFAULT_MEASUREMENTS)
                .storeId(DEFAULT_STORE_ID)
                .deviceIdentification(DEFAULT_DEVICE_IDENTIFICATION)
                .startLan(DEFAULT_START_LAN)
                .startLon(DEFAULT_START_LON)
                .startAccuracy(DEFAULT_START_ACCURACY);
        return measurementDataset;
    }

    @Before
    public void initTest() {
        measurementDataset = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeasurementDataset() throws Exception {
        int databaseSizeBeforeCreate = measurementDatasetRepository.findAll().size();

        // Create the MeasurementDataset

        restMeasurementDatasetMockMvc.perform(post("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDataset)))
            .andExpect(status().isCreated());

        // Validate the MeasurementDataset in the database
        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeCreate + 1);
        MeasurementDataset testMeasurementDataset = measurementDatasetList.get(measurementDatasetList.size() - 1);
        assertThat(testMeasurementDataset.getMeasurements()).isEqualTo(DEFAULT_MEASUREMENTS);
        assertThat(testMeasurementDataset.getStoreId()).isEqualTo(DEFAULT_STORE_ID);
        assertThat(testMeasurementDataset.getDeviceIdentification()).isEqualTo(DEFAULT_DEVICE_IDENTIFICATION);
        assertThat(testMeasurementDataset.getStartLan()).isEqualTo(DEFAULT_START_LAN);
        assertThat(testMeasurementDataset.getStartLon()).isEqualTo(DEFAULT_START_LON);
        assertThat(testMeasurementDataset.getStartAccuracy()).isEqualTo(DEFAULT_START_ACCURACY);
    }

    @Test
    @Transactional
    public void createMeasurementDatasetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = measurementDatasetRepository.findAll().size();

        // Create the MeasurementDataset with an existing ID
        MeasurementDataset existingMeasurementDataset = new MeasurementDataset();
        existingMeasurementDataset.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasurementDatasetMockMvc.perform(post("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMeasurementDataset)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMeasurementsIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementDatasetRepository.findAll().size();
        // set the field null
        measurementDataset.setMeasurements(null);

        // Create the MeasurementDataset, which fails.

        restMeasurementDatasetMockMvc.perform(post("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDataset)))
            .andExpect(status().isBadRequest());

        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStoreIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementDatasetRepository.findAll().size();
        // set the field null
        measurementDataset.setStoreId(null);

        // Create the MeasurementDataset, which fails.

        restMeasurementDatasetMockMvc.perform(post("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDataset)))
            .andExpect(status().isBadRequest());

        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviceIdentificationIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementDatasetRepository.findAll().size();
        // set the field null
        measurementDataset.setDeviceIdentification(null);

        // Create the MeasurementDataset, which fails.

        restMeasurementDatasetMockMvc.perform(post("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDataset)))
            .andExpect(status().isBadRequest());

        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartLanIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementDatasetRepository.findAll().size();
        // set the field null
        measurementDataset.setStartLan(null);

        // Create the MeasurementDataset, which fails.

        restMeasurementDatasetMockMvc.perform(post("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDataset)))
            .andExpect(status().isBadRequest());

        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartLonIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementDatasetRepository.findAll().size();
        // set the field null
        measurementDataset.setStartLon(null);

        // Create the MeasurementDataset, which fails.

        restMeasurementDatasetMockMvc.perform(post("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDataset)))
            .andExpect(status().isBadRequest());

        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartAccuracyIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementDatasetRepository.findAll().size();
        // set the field null
        measurementDataset.setStartAccuracy(null);

        // Create the MeasurementDataset, which fails.

        restMeasurementDatasetMockMvc.perform(post("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDataset)))
            .andExpect(status().isBadRequest());

        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMeasurementDatasets() throws Exception {
        // Initialize the database
        measurementDatasetRepository.saveAndFlush(measurementDataset);

        // Get all the measurementDatasetList
        restMeasurementDatasetMockMvc.perform(get("/api/measurement-datasets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurementDataset.getId().intValue())))
            .andExpect(jsonPath("$.[*].measurements").value(hasItem(DEFAULT_MEASUREMENTS.toString())))
            .andExpect(jsonPath("$.[*].storeId").value(hasItem(DEFAULT_STORE_ID.intValue())))
            .andExpect(jsonPath("$.[*].deviceIdentification").value(hasItem(DEFAULT_DEVICE_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].startLan").value(hasItem(DEFAULT_START_LAN.doubleValue())))
            .andExpect(jsonPath("$.[*].startLon").value(hasItem(DEFAULT_START_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].startAccuracy").value(hasItem(DEFAULT_START_ACCURACY.doubleValue())));
    }

    @Test
    @Transactional
    public void getMeasurementDataset() throws Exception {
        // Initialize the database
        measurementDatasetRepository.saveAndFlush(measurementDataset);

        // Get the measurementDataset
        restMeasurementDatasetMockMvc.perform(get("/api/measurement-datasets/{id}", measurementDataset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(measurementDataset.getId().intValue()))
            .andExpect(jsonPath("$.measurements").value(DEFAULT_MEASUREMENTS.toString()))
            .andExpect(jsonPath("$.storeId").value(DEFAULT_STORE_ID.intValue()))
            .andExpect(jsonPath("$.deviceIdentification").value(DEFAULT_DEVICE_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.startLan").value(DEFAULT_START_LAN.doubleValue()))
            .andExpect(jsonPath("$.startLon").value(DEFAULT_START_LON.doubleValue()))
            .andExpect(jsonPath("$.startAccuracy").value(DEFAULT_START_ACCURACY.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMeasurementDataset() throws Exception {
        // Get the measurementDataset
        restMeasurementDatasetMockMvc.perform(get("/api/measurement-datasets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeasurementDataset() throws Exception {
        // Initialize the database
        measurementDatasetService.save(measurementDataset);

        int databaseSizeBeforeUpdate = measurementDatasetRepository.findAll().size();

        // Update the measurementDataset
        MeasurementDataset updatedMeasurementDataset = measurementDatasetRepository.findOne(measurementDataset.getId());
        updatedMeasurementDataset
                .measurements(UPDATED_MEASUREMENTS)
                .storeId(UPDATED_STORE_ID)
                .deviceIdentification(UPDATED_DEVICE_IDENTIFICATION)
                .startLan(UPDATED_START_LAN)
                .startLon(UPDATED_START_LON)
                .startAccuracy(UPDATED_START_ACCURACY);

        restMeasurementDatasetMockMvc.perform(put("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeasurementDataset)))
            .andExpect(status().isOk());

        // Validate the MeasurementDataset in the database
        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeUpdate);
        MeasurementDataset testMeasurementDataset = measurementDatasetList.get(measurementDatasetList.size() - 1);
        assertThat(testMeasurementDataset.getMeasurements()).isEqualTo(UPDATED_MEASUREMENTS);
        assertThat(testMeasurementDataset.getStoreId()).isEqualTo(UPDATED_STORE_ID);
        assertThat(testMeasurementDataset.getDeviceIdentification()).isEqualTo(UPDATED_DEVICE_IDENTIFICATION);
        assertThat(testMeasurementDataset.getStartLan()).isEqualTo(UPDATED_START_LAN);
        assertThat(testMeasurementDataset.getStartLon()).isEqualTo(UPDATED_START_LON);
        assertThat(testMeasurementDataset.getStartAccuracy()).isEqualTo(UPDATED_START_ACCURACY);
    }

    @Test
    @Transactional
    public void updateNonExistingMeasurementDataset() throws Exception {
        int databaseSizeBeforeUpdate = measurementDatasetRepository.findAll().size();

        // Create the MeasurementDataset

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMeasurementDatasetMockMvc.perform(put("/api/measurement-datasets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurementDataset)))
            .andExpect(status().isCreated());

        // Validate the MeasurementDataset in the database
        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMeasurementDataset() throws Exception {
        // Initialize the database
        measurementDatasetService.save(measurementDataset);

        int databaseSizeBeforeDelete = measurementDatasetRepository.findAll().size();

        // Get the measurementDataset
        restMeasurementDatasetMockMvc.perform(delete("/api/measurement-datasets/{id}", measurementDataset.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
