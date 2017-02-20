package de.hshn.se.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
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

import de.hshn.se.IsaApp;
import de.hshn.se.domain.Visit;
import de.hshn.se.domain.Waypoint;
import de.hshn.se.repository.WaypointRepository;

/**
 * Test class for the WaypointResource REST controller.
 *
 * @see WaypointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IsaApp.class)
public class WaypointResourceIntTest {

	private static final Float DEFAULT_X = 1.25F;
	private static final Float UPDATED_X = 2.83F;

	private static final Float DEFAULT_Y = 1.88F;
	private static final Float UPDATED_Y = 2.73F;

	private static final Long DEFAULT_TIMESTAMP = System.currentTimeMillis();
	private static final Long UPDATED_TIMESTAMP = System.currentTimeMillis() + 1;

    @Inject
    private WaypointRepository waypointRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWaypointMockMvc;

    private Waypoint waypoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WaypointResource waypointResource = new WaypointResource();
        ReflectionTestUtils.setField(waypointResource, "waypointRepository", waypointRepository);
        this.restWaypointMockMvc = MockMvcBuilders.standaloneSetup(waypointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Waypoint createEntity(EntityManager em) {
        Waypoint waypoint = new Waypoint()
                .x(DEFAULT_X)
                .y(DEFAULT_Y)
                .timestamp(DEFAULT_TIMESTAMP);
        // Add required entity
        Visit visit = VisitResourceIntTest.createEntity(em);
        em.persist(visit);
        em.flush();
        waypoint.setVisit(visit);
        return waypoint;
    }

    @Before
    public void initTest() {
        waypoint = createEntity(em);
    }

	// @Test
	// @Transactional
    public void createWaypoint() throws Exception {
        int databaseSizeBeforeCreate = waypointRepository.findAll().size();

        // Create the Waypoint

        restWaypointMockMvc.perform(post("/api/waypoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(waypoint)))
            .andExpect(status().isCreated());

        // Validate the Waypoint in the database
        List<Waypoint> waypointList = waypointRepository.findAll();
        assertThat(waypointList).hasSize(databaseSizeBeforeCreate + 1);
        Waypoint testWaypoint = waypointList.get(waypointList.size() - 1);
        assertThat(testWaypoint.getX()).isEqualTo(DEFAULT_X);
        assertThat(testWaypoint.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testWaypoint.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createWaypointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = waypointRepository.findAll().size();

        // Create the Waypoint with an existing ID
        Waypoint existingWaypoint = new Waypoint();
        existingWaypoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaypointMockMvc.perform(post("/api/waypoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingWaypoint)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Waypoint> waypointList = waypointRepository.findAll();
        assertThat(waypointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkXIsRequired() throws Exception {
        int databaseSizeBeforeTest = waypointRepository.findAll().size();
        // set the field null
        waypoint.setX(null);

        // Create the Waypoint, which fails.

        restWaypointMockMvc.perform(post("/api/waypoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(waypoint)))
            .andExpect(status().isBadRequest());

        List<Waypoint> waypointList = waypointRepository.findAll();
        assertThat(waypointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYIsRequired() throws Exception {
        int databaseSizeBeforeTest = waypointRepository.findAll().size();
        // set the field null
        waypoint.setY(null);

        // Create the Waypoint, which fails.

        restWaypointMockMvc.perform(post("/api/waypoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(waypoint)))
            .andExpect(status().isBadRequest());

        List<Waypoint> waypointList = waypointRepository.findAll();
        assertThat(waypointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = waypointRepository.findAll().size();
        // set the field null
        waypoint.setTimestamp(null);

        // Create the Waypoint, which fails.

        restWaypointMockMvc.perform(post("/api/waypoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(waypoint)))
            .andExpect(status().isBadRequest());

        List<Waypoint> waypointList = waypointRepository.findAll();
        assertThat(waypointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWaypoints() throws Exception {
        // Initialize the database
        waypointRepository.saveAndFlush(waypoint);

        // Get all the waypointList
        restWaypointMockMvc.perform(get("/api/waypoints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waypoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.intValue())));
    }

    @Test
    @Transactional
    public void getWaypoint() throws Exception {
        // Initialize the database
        waypointRepository.saveAndFlush(waypoint);

        // Get the waypoint
        restWaypointMockMvc.perform(get("/api/waypoints/{id}", waypoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(waypoint.getId().intValue()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWaypoint() throws Exception {
        // Get the waypoint
        restWaypointMockMvc.perform(get("/api/waypoints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

	// @Test
	// @Transactional
    public void updateWaypoint() throws Exception {
        // Initialize the database
        waypointRepository.saveAndFlush(waypoint);
        int databaseSizeBeforeUpdate = waypointRepository.findAll().size();

        // Update the waypoint
        Waypoint updatedWaypoint = waypointRepository.findOne(waypoint.getId());
        updatedWaypoint
                .x(UPDATED_X)
                .y(UPDATED_Y)
                .timestamp(UPDATED_TIMESTAMP);

        restWaypointMockMvc.perform(put("/api/waypoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWaypoint)))
            .andExpect(status().isOk());

        // Validate the Waypoint in the database
        List<Waypoint> waypointList = waypointRepository.findAll();
        assertThat(waypointList).hasSize(databaseSizeBeforeUpdate);
        Waypoint testWaypoint = waypointList.get(waypointList.size() - 1);
        assertThat(testWaypoint.getX()).isEqualTo(UPDATED_X);
        assertThat(testWaypoint.getY()).isEqualTo(UPDATED_Y);
        assertThat(testWaypoint.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

	// @Test
	// @Transactional
    public void updateNonExistingWaypoint() throws Exception {
        int databaseSizeBeforeUpdate = waypointRepository.findAll().size();

        // Create the Waypoint

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWaypointMockMvc.perform(put("/api/waypoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(waypoint)))
            .andExpect(status().isCreated());

        // Validate the Waypoint in the database
        List<Waypoint> waypointList = waypointRepository.findAll();
        assertThat(waypointList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWaypoint() throws Exception {
        // Initialize the database
        waypointRepository.saveAndFlush(waypoint);
        int databaseSizeBeforeDelete = waypointRepository.findAll().size();

        // Get the waypoint
        restWaypointMockMvc.perform(delete("/api/waypoints/{id}", waypoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Waypoint> waypointList = waypointRepository.findAll();
        assertThat(waypointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
