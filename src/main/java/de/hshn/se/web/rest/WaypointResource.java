package de.hshn.se.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.hshn.se.domain.Waypoint;

import de.hshn.se.repository.WaypointRepository;
import de.hshn.se.web.rest.util.HeaderUtil;
import de.hshn.se.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Waypoint.
 */
@RestController
@RequestMapping("/api")
public class WaypointResource {

    private final Logger log = LoggerFactory.getLogger(WaypointResource.class);
        
    @Inject
    private WaypointRepository waypointRepository;

    /**
     * POST  /waypoints : Create a new waypoint.
     *
     * @param waypoint the waypoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new waypoint, or with status 400 (Bad Request) if the waypoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/waypoints")
    @Timed
    public ResponseEntity<Waypoint> createWaypoint(@Valid @RequestBody Waypoint waypoint) throws URISyntaxException {
        log.debug("REST request to save Waypoint : {}", waypoint);
        if (waypoint.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("waypoint", "idexists", "A new waypoint cannot already have an ID")).body(null);
        }
        Waypoint result = waypointRepository.save(waypoint);
        return ResponseEntity.created(new URI("/api/waypoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("waypoint", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /waypoints : Updates an existing waypoint.
     *
     * @param waypoint the waypoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated waypoint,
     * or with status 400 (Bad Request) if the waypoint is not valid,
     * or with status 500 (Internal Server Error) if the waypoint couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/waypoints")
    @Timed
    public ResponseEntity<Waypoint> updateWaypoint(@Valid @RequestBody Waypoint waypoint) throws URISyntaxException {
        log.debug("REST request to update Waypoint : {}", waypoint);
        if (waypoint.getId() == null) {
            return createWaypoint(waypoint);
        }
        Waypoint result = waypointRepository.save(waypoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("waypoint", waypoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /waypoints : get all the waypoints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of waypoints in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/waypoints")
    @Timed
    public ResponseEntity<List<Waypoint>> getAllWaypoints(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Waypoints");
        Page<Waypoint> page = waypointRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/waypoints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /waypoints/:id : get the "id" waypoint.
     *
     * @param id the id of the waypoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the waypoint, or with status 404 (Not Found)
     */
    @GetMapping("/waypoints/{id}")
    @Timed
    public ResponseEntity<Waypoint> getWaypoint(@PathVariable Long id) {
        log.debug("REST request to get Waypoint : {}", id);
        Waypoint waypoint = waypointRepository.findOne(id);
        return Optional.ofNullable(waypoint)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /waypoints/:id : delete the "id" waypoint.
     *
     * @param id the id of the waypoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/waypoints/{id}")
    @Timed
    public ResponseEntity<Void> deleteWaypoint(@PathVariable Long id) {
        log.debug("REST request to delete Waypoint : {}", id);
        waypointRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("waypoint", id.toString())).build();
    }

}
