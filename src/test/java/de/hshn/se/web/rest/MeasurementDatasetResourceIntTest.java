package de.hshn.se.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import de.hshn.se.IsaApp;
import de.hshn.se.domain.MeasurementDataset;
import de.hshn.se.domain.StoreMap;
import de.hshn.se.repository.MeasurementDatasetRepository;
import de.hshn.se.service.MeasurementDatasetService;

/**
 * Test class for the MeasurementDatasetResource REST controller.
 *
 * @see MeasurementDatasetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IsaApp.class)
public class MeasurementDatasetResourceIntTest {

	private static final String DEFAULT_MEASUREMENTS = "[{\"accuracy\":15.0,\"direction\":-1.9100282,\"lat\":49.23063899,\"length\":0.65,\"lon\":9.16206985,\"magx\":72.71,\"magy\":-39.66,\"magz\":7.01,\"timestamp\":1487721557353},{\"accuracy\":9.0,\"direction\":-2.0513911,\"lat\":49.2305598,\"length\":0.65,\"lon\":9.16210254,\"magx\":75.8,\"magy\":-37.7,\"magz\":35.7,\"timestamp\":1487721562012},{\"accuracy\":9.0,\"direction\":-2.0514338,\"lat\":49.2305598,\"length\":0.65,\"lon\":9.16210254,\"magx\":75.4,\"magy\":-37.4,\"magz\":37.0,\"timestamp\":1487721563127},{\"accuracy\":10.0,\"direction\":-2.0671222,\"lat\":49.23055904,\"length\":0.65,\"lon\":9.16208012,\"magx\":75.4,\"magy\":-37.6,\"magz\":37.7,\"timestamp\":1487721564122},{\"accuracy\":9.0,\"direction\":-2.0615032,\"lat\":49.2305803,\"length\":0.65,\"lon\":9.16207172,\"magx\":75.4,\"magy\":-37.7,\"magz\":37.5,\"timestamp\":1487721565140},{\"accuracy\":9.0,\"direction\":-2.0389159,\"lat\":49.23061028,\"length\":0.65,\"lon\":9.16206886,\"magx\":75.4,\"magy\":-38.2,\"magz\":37.2,\"timestamp\":1487721566005},{\"accuracy\":9.0,\"direction\":-2.0811613,\"lat\":49.23061028,\"length\":0.65,\"lon\":9.16206886,\"magx\":75.4,\"magy\":-38.1,\"magz\":35.3,\"timestamp\":1487721567053},{\"accuracy\":9.0,\"direction\":-2.0678058,\"lat\":49.23061028,\"length\":0.65,\"lon\":9.16206886,\"magx\":75.8,\"magy\":-38.0,\"magz\":35.1,\"timestamp\":1487721568067},{\"accuracy\":10.0,\"direction\":-2.085341,\"lat\":49.23059838,\"length\":0.65,\"lon\":9.16206315,\"magx\":75.4,\"magy\":-38.2,\"magz\":35.0,\"timestamp\":1487721569172},{\"accuracy\":10.0,\"direction\":-2.073573,\"lat\":49.23058842,\"length\":0.65,\"lon\":9.16203395,\"magx\":75.0,\"magy\":-38.5,\"magz\":34.5,\"timestamp\":1487721570264},{\"accuracy\":null,\"direction\":-1.7356226,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":85.0,\"magy\":-34.1,\"magz\":12.2,\"timestamp\":1487721575719},{\"accuracy\":null,\"direction\":-1.7428205,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":85.4,\"magy\":-33.7,\"magz\":11.7,\"timestamp\":1487721576605},{\"accuracy\":null,\"direction\":-1.748117,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":85.8,\"magy\":-33.6,\"magz\":8.9,\"timestamp\":1487721577555},{\"accuracy\":null,\"direction\":-1.7476091,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":85.8,\"magy\":-33.5,\"magz\":8.9,\"timestamp\":1487721578461},{\"accuracy\":null,\"direction\":-1.7481084,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":85.8,\"magy\":-33.9,\"magz\":8.5,\"timestamp\":1487721579362},{\"accuracy\":null,\"direction\":-1.7441298,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.6,\"magy\":-33.5,\"magz\":8.0,\"timestamp\":1487721580150},{\"accuracy\":null,\"direction\":-1.7427201,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.6,\"magy\":-33.5,\"magz\":7.4,\"timestamp\":1487721581048},{\"accuracy\":null,\"direction\":-1.745565,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.2,\"magy\":-33.1,\"magz\":7.7,\"timestamp\":1487721581903},{\"accuracy\":null,\"direction\":-1.733409,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.6,\"magy\":-33.0,\"magz\":7.4,\"timestamp\":1487721582783},{\"accuracy\":null,\"direction\":-1.728198,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.2,\"magy\":-32.9,\"magz\":5.1,\"timestamp\":1487721583704},{\"accuracy\":null,\"direction\":-1.72979,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.6,\"magy\":-33.2,\"magz\":6.0,\"timestamp\":1487721584510},{\"accuracy\":null,\"direction\":-1.7316362,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.6,\"magy\":-33.2,\"magz\":6.6,\"timestamp\":1487721585365},{\"accuracy\":null,\"direction\":-1.7280838,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.6,\"magy\":-32.7,\"magz\":4.0,\"timestamp\":1487721586227},{\"accuracy\":null,\"direction\":-1.7316568,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.6,\"magy\":-32.4,\"magz\":5.2,\"timestamp\":1487721587040},{\"accuracy\":null,\"direction\":-1.729442,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.6,\"magy\":-32.5,\"magz\":3.7,\"timestamp\":1487721587923}]";
	private static final String UPDATED_MEASUREMENTS = "[{\"accuracy\":null,\"direction\":-2.0680683,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":74.4,\"magy\":-37.3,\"magz\":40.2,\"timestamp\":1487714259589},{\"accuracy\":null,\"direction\":-2.0613184,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":75.2,\"magy\":-37.6,\"magz\":40.7,\"timestamp\":1487714260866},{\"accuracy\":null,\"direction\":-2.0581114,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":74.8,\"magy\":-37.4,\"magz\":40.7,\"timestamp\":1487714261749},{\"accuracy\":null,\"direction\":-2.080394,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":76.0,\"magy\":-36.5,\"magz\":42.2,\"timestamp\":1487714262701},{\"accuracy\":null,\"direction\":-2.061836,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":76.4,\"magy\":-37.0,\"magz\":42.6,\"timestamp\":1487714263591},{\"accuracy\":null,\"direction\":-2.060536,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":76.8,\"magy\":-37.2,\"magz\":43.0,\"timestamp\":1487714264476},{\"accuracy\":null,\"direction\":-2.0697856,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":76.4,\"magy\":-37.0,\"magz\":43.0,\"timestamp\":1487714265374},{\"accuracy\":null,\"direction\":-2.0736618,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":76.0,\"magy\":-36.1,\"magz\":43.0,\"timestamp\":1487714266385},{\"accuracy\":null,\"direction\":-2.0842068,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":76.0,\"magy\":-36.4,\"magz\":42.6,\"timestamp\":1487714267375},{\"accuracy\":null,\"direction\":-2.0749724,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":75.2,\"magy\":-36.5,\"magz\":42.6,\"timestamp\":1487714268704},{\"accuracy\":null,\"direction\":-1.8523544,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.8,\"magy\":-36.6,\"magz\":26.5,\"timestamp\":1487714273457},{\"accuracy\":null,\"direction\":-1.8397481,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.8,\"magy\":-36.6,\"magz\":26.1,\"timestamp\":1487714274372},{\"accuracy\":null,\"direction\":-1.8346667,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.8,\"magy\":-36.4,\"magz\":25.7,\"timestamp\":1487714275322},{\"accuracy\":null,\"direction\":-1.8326081,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.8,\"magy\":-37.3,\"magz\":25.3,\"timestamp\":1487714276230},{\"accuracy\":null,\"direction\":-1.8476883,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":85.2,\"magy\":-36.6,\"magz\":26.7,\"timestamp\":1487714277064},{\"accuracy\":null,\"direction\":-1.8518368,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.8,\"magy\":-37.0,\"magz\":25.7,\"timestamp\":1487714277892},{\"accuracy\":null,\"direction\":-1.8445406,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":85.2,\"magy\":-36.7,\"magz\":27.6,\"timestamp\":1487714278821},{\"accuracy\":null,\"direction\":-1.8527666,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.4,\"magy\":-36.7,\"magz\":26.6,\"timestamp\":1487714279773},{\"accuracy\":null,\"direction\":-1.8425344,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.8,\"magy\":-37.5,\"magz\":27.4,\"timestamp\":1487714280652},{\"accuracy\":null,\"direction\":-1.8555423,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.0,\"magy\":-36.9,\"magz\":27.2,\"timestamp\":1487714281765},{\"accuracy\":null,\"direction\":-1.8477939,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.4,\"magy\":-36.9,\"magz\":26.8,\"timestamp\":1487714282773},{\"accuracy\":null,\"direction\":-1.8518593,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.4,\"magy\":-36.9,\"magz\":27.3,\"timestamp\":1487714283625},{\"accuracy\":null,\"direction\":-1.8632745,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.0,\"magy\":-37.3,\"magz\":26.7,\"timestamp\":1487714284544},{\"accuracy\":null,\"direction\":-1.8503717,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.0,\"magy\":-37.5,\"magz\":26.4,\"timestamp\":1487714285590},{\"accuracy\":null,\"direction\":-1.8525136,\"lat\":null,\"length\":0.65,\"lon\":null,\"magx\":84.0,\"magy\":-36.9,\"magz\":26.9,\"timestamp\":1487714286618}]";

	// private static final Long DEFAULT_STORE_ID = 1L;
	// private static final Long UPDATED_STORE_ID = 2L;

	private static final String DEFAULT_DEVICE_IDENTIFICATION = "00-80-41-ae-fd-7e";
	private static final String UPDATED_DEVICE_IDENTIFICATION = "00-80-41-ae-fd-8e";

	private static final Double DEFAULT_START_LAT = 49.123113d;
	private static final Double UPDATED_START_LAT = 49.123112d;

	private static final Double DEFAULT_START_LON = 9.211183d;
	private static final Double UPDATED_START_LON = 9.211182d;

	private static final Float DEFAULT_START_ACCURACY = 5F;
	private static final Float UPDATED_START_ACCURACY = 5F;

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

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/docs/asciidoc/snippets");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeasurementDatasetResource measurementDatasetResource = new MeasurementDatasetResource();
        ReflectionTestUtils.setField(measurementDatasetResource, "measurementDatasetService", measurementDatasetService);
        this.restMeasurementDatasetMockMvc = MockMvcBuilders.standaloneSetup(measurementDatasetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
				.setMessageConverters(jacksonMessageConverter).apply(documentationConfiguration(this.restDocumentation))
				.build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasurementDataset createEntity(EntityManager em) {
		StoreMap storeMap = StoreMapResourceIntTest.createEntity(em);
		em.persist(storeMap);
        MeasurementDataset measurementDataset = new MeasurementDataset()
                .measurements(DEFAULT_MEASUREMENTS)
				.storeId(storeMap.getStore().getId())
                .deviceIdentification(DEFAULT_DEVICE_IDENTIFICATION)
				.startLat(DEFAULT_START_LAT)
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
				// .andDo(document("createMeasurementDatasetUsingPOST",
				// preprocessResponse(prettyPrint())))
            .andExpect(status().isCreated());

        // Validate the MeasurementDataset in the database
        List<MeasurementDataset> measurementDatasetList = measurementDatasetRepository.findAll();
        assertThat(measurementDatasetList).hasSize(databaseSizeBeforeCreate + 1);
        MeasurementDataset testMeasurementDataset = measurementDatasetList.get(measurementDatasetList.size() - 1);
        assertThat(testMeasurementDataset.getMeasurements()).isEqualTo(DEFAULT_MEASUREMENTS);
		// assertThat(testMeasurementDataset.getStoreId()).isEqualTo(DEFAULT_STORE_ID);
        assertThat(testMeasurementDataset.getDeviceIdentification()).isEqualTo(DEFAULT_DEVICE_IDENTIFICATION);
		assertThat(testMeasurementDataset.getStartLat()).isEqualTo(DEFAULT_START_LAT);
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
		measurementDataset.setStartLat(null);

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
				// .andExpect(jsonPath("$.[*].storeId").value(hasItem(DEFAULT_STORE_ID.intValue())))
            .andExpect(jsonPath("$.[*].deviceIdentification").value(hasItem(DEFAULT_DEVICE_IDENTIFICATION.toString())))
				.andExpect(jsonPath("$.[*].startLat").value(hasItem(DEFAULT_START_LAT.doubleValue())))
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
				// .andExpect(jsonPath("$.storeId").value(DEFAULT_STORE_ID.intValue()))
            .andExpect(jsonPath("$.deviceIdentification").value(DEFAULT_DEVICE_IDENTIFICATION.toString()))
				.andExpect(jsonPath("$.startLat").value(DEFAULT_START_LAT.doubleValue()))
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
				// .storeId(UPDATED_STORE_ID)
                .deviceIdentification(UPDATED_DEVICE_IDENTIFICATION)
				.startLat(UPDATED_START_LAT)
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
		// assertThat(testMeasurementDataset.getStoreId()).isEqualTo(UPDATED_STORE_ID);
        assertThat(testMeasurementDataset.getDeviceIdentification()).isEqualTo(UPDATED_DEVICE_IDENTIFICATION);
		assertThat(testMeasurementDataset.getStartLat()).isEqualTo(UPDATED_START_LAT);
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
