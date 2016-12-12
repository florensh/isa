package de.hshn.se.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.hshn.se.domain.StoreMap;
import de.hshn.se.service.StoreMapService;
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
 * REST controller for managing StoreMap.
 */
@RestController
@RequestMapping("/api")
public class StoreMapResource {

    private final Logger log = LoggerFactory.getLogger(StoreMapResource.class);
        
    @Inject
    private StoreMapService storeMapService;

    /**
     * POST  /store-maps : Create a new storeMap.
     *
     * @param storeMap the storeMap to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeMap, or with status 400 (Bad Request) if the storeMap has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/store-maps")
    @Timed
    public ResponseEntity<StoreMap> createStoreMap(@Valid @RequestBody StoreMap storeMap) throws URISyntaxException {
        log.debug("REST request to save StoreMap : {}", storeMap);
        if (storeMap.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("storeMap", "idexists", "A new storeMap cannot already have an ID")).body(null);
        }
        StoreMap result = storeMapService.save(storeMap);
        return ResponseEntity.created(new URI("/api/store-maps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("storeMap", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /store-maps : Updates an existing storeMap.
     *
     * @param storeMap the storeMap to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeMap,
     * or with status 400 (Bad Request) if the storeMap is not valid,
     * or with status 500 (Internal Server Error) if the storeMap couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/store-maps")
    @Timed
    public ResponseEntity<StoreMap> updateStoreMap(@Valid @RequestBody StoreMap storeMap) throws URISyntaxException {
        log.debug("REST request to update StoreMap : {}", storeMap);
        if (storeMap.getId() == null) {
            return createStoreMap(storeMap);
        }
        StoreMap result = storeMapService.save(storeMap);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("storeMap", storeMap.getId().toString()))
            .body(result);
    }

    /**
     * GET  /store-maps : get all the storeMaps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of storeMaps in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/store-maps")
    @Timed
    public ResponseEntity<List<StoreMap>> getAllStoreMaps(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StoreMaps");
        Page<StoreMap> page = storeMapService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/store-maps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /store-maps/:id : get the "id" storeMap.
     *
     * @param id the id of the storeMap to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeMap, or with status 404 (Not Found)
     */
    @GetMapping("/store-maps/{id}")
    @Timed
    public ResponseEntity<StoreMap> getStoreMap(@PathVariable Long id) {
        log.debug("REST request to get StoreMap : {}", id);
        StoreMap storeMap = storeMapService.findOne(id);
        return Optional.ofNullable(storeMap)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /store-maps/:id : delete the "id" storeMap.
     *
     * @param id the id of the storeMap to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/store-maps/{id}")
    @Timed
    public ResponseEntity<Void> deleteStoreMap(@PathVariable Long id) {
        log.debug("REST request to delete StoreMap : {}", id);
        storeMapService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("storeMap", id.toString())).build();
    }

}
