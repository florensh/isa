package de.hshn.se.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.hshn.se.domain.Store;
import de.hshn.se.service.StoreService;
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
 * REST controller for managing Store.
 */
@RestController
@RequestMapping("/api")
public class StoreResource {

    private final Logger log = LoggerFactory.getLogger(StoreResource.class);
        
    @Inject
    private StoreService storeService;

    /**
     * POST  /stores : Create a new store.
     *
     * @param store the store to create
     * @return the ResponseEntity with status 201 (Created) and with body the new store, or with status 400 (Bad Request) if the store has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stores")
    @Timed
    public ResponseEntity<Store> createStore(@Valid @RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to save Store : {}", store);
        if (store.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("store", "idexists", "A new store cannot already have an ID")).body(null);
        }
        Store result = storeService.save(store);
        return ResponseEntity.created(new URI("/api/stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("store", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stores : Updates an existing store.
     *
     * @param store the store to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated store,
     * or with status 400 (Bad Request) if the store is not valid,
     * or with status 500 (Internal Server Error) if the store couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stores")
    @Timed
    public ResponseEntity<Store> updateStore(@Valid @RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to update Store : {}", store);
        if (store.getId() == null) {
            return createStore(store);
        }
        Store result = storeService.save(store);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("store", store.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stores : get all the stores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stores in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stores")
    @Timed
    public ResponseEntity<List<Store>> getAllStores(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Stores");
        Page<Store> page = storeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stores/:id : get the "id" store.
     *
     * @param id the id of the store to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the store, or with status 404 (Not Found)
     */
    @GetMapping("/stores/{id}")
    @Timed
    public ResponseEntity<Store> getStore(@PathVariable Long id) {
        log.debug("REST request to get Store : {}", id);
        Store store = storeService.findOne(id);
        return Optional.ofNullable(store)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stores/:id : delete the "id" store.
     *
     * @param id the id of the store to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stores/{id}")
    @Timed
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        log.debug("REST request to delete Store : {}", id);
        storeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("store", id.toString())).build();
    }

}
