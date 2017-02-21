package de.hshn.se.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import de.hshn.se.domain.Visitor;
import de.hshn.se.repository.VisitorRepository;
import de.hshn.se.web.rest.util.HeaderUtil;
import de.hshn.se.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * REST controller for managing Visitor.
 */
@RestController
@ApiIgnore
@RequestMapping("/api")
public class VisitorResource {

    private final Logger log = LoggerFactory.getLogger(VisitorResource.class);
        
    @Inject
    private VisitorRepository visitorRepository;

    /**
     * POST  /visitors : Create a new visitor.
     *
     * @param visitor the visitor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new visitor, or with status 400 (Bad Request) if the visitor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/visitors")
	@ApiIgnore
    @Timed
    public ResponseEntity<Visitor> createVisitor(@RequestBody Visitor visitor) throws URISyntaxException {
        log.debug("REST request to save Visitor : {}", visitor);
        if (visitor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("visitor", "idexists", "A new visitor cannot already have an ID")).body(null);
        }
        Visitor result = visitorRepository.save(visitor);
        return ResponseEntity.created(new URI("/api/visitors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("visitor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /visitors : Updates an existing visitor.
     *
     * @param visitor the visitor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated visitor,
     * or with status 400 (Bad Request) if the visitor is not valid,
     * or with status 500 (Internal Server Error) if the visitor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/visitors")
	@ApiIgnore
    @Timed
    public ResponseEntity<Visitor> updateVisitor(@RequestBody Visitor visitor) throws URISyntaxException {
        log.debug("REST request to update Visitor : {}", visitor);
        if (visitor.getId() == null) {
            return createVisitor(visitor);
        }
        Visitor result = visitorRepository.save(visitor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("visitor", visitor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /visitors : get all the visitors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of visitors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/visitors")
    @Timed
    public ResponseEntity<List<Visitor>> getAllVisitors(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Visitors");
        Page<Visitor> page = visitorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/visitors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /visitors/:id : get the "id" visitor.
     *
     * @param id the id of the visitor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the visitor, or with status 404 (Not Found)
     */
    @GetMapping("/visitors/{id}")
    @Timed
    public ResponseEntity<Visitor> getVisitor(@PathVariable Long id) {
        log.debug("REST request to get Visitor : {}", id);
        Visitor visitor = visitorRepository.findOne(id);
        return Optional.ofNullable(visitor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /visitors/:id : delete the "id" visitor.
     *
     * @param id the id of the visitor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/visitors/{id}")
	@ApiIgnore
    @Timed
    public ResponseEntity<Void> deleteVisitor(@PathVariable Long id) {
        log.debug("REST request to delete Visitor : {}", id);
        visitorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("visitor", id.toString())).build();
    }

}
