package de.hshn.se.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.hshn.se.domain.MeasurementDataset;
import de.hshn.se.service.MeasurementDatasetService;
import de.hshn.se.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing MeasurementDataset.
 */
@RestController
@RequestMapping("/api")
public class MeasurementDatasetResource {

    private final Logger log = LoggerFactory.getLogger(MeasurementDatasetResource.class);
        
    @Inject
    private MeasurementDatasetService measurementDatasetService;

    /**
     * POST  /measurement-datasets : Create a new measurementDataset.
     *
     * @param measurementDataset the measurementDataset to create
     * @return the ResponseEntity with status 201 (Created) and with body the new measurementDataset, or with status 400 (Bad Request) if the measurementDataset has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/measurement-datasets")
    @Timed
    public ResponseEntity<MeasurementDataset> createMeasurementDataset(@Valid @RequestBody MeasurementDataset measurementDataset) throws URISyntaxException {
        log.debug("REST request to save MeasurementDataset : {}", measurementDataset);
        if (measurementDataset.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("measurementDataset", "idexists", "A new measurementDataset cannot already have an ID")).body(null);
        }
        MeasurementDataset result = measurementDatasetService.save(measurementDataset);
        return ResponseEntity.created(new URI("/api/measurement-datasets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("measurementDataset", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /measurement-datasets : Updates an existing measurementDataset.
     *
     * @param measurementDataset the measurementDataset to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated measurementDataset,
     * or with status 400 (Bad Request) if the measurementDataset is not valid,
     * or with status 500 (Internal Server Error) if the measurementDataset couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/measurement-datasets")
    @Timed
    public ResponseEntity<MeasurementDataset> updateMeasurementDataset(@Valid @RequestBody MeasurementDataset measurementDataset) throws URISyntaxException {
        log.debug("REST request to update MeasurementDataset : {}", measurementDataset);
        if (measurementDataset.getId() == null) {
            return createMeasurementDataset(measurementDataset);
        }
        MeasurementDataset result = measurementDatasetService.save(measurementDataset);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("measurementDataset", measurementDataset.getId().toString()))
            .body(result);
    }

    /**
     * GET  /measurement-datasets : get all the measurementDatasets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of measurementDatasets in body
     */
    @GetMapping("/measurement-datasets")
    @Timed
    public List<MeasurementDataset> getAllMeasurementDatasets() {
        log.debug("REST request to get all MeasurementDatasets");
        return measurementDatasetService.findAll();
    }

    /**
     * GET  /measurement-datasets/:id : get the "id" measurementDataset.
     *
     * @param id the id of the measurementDataset to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the measurementDataset, or with status 404 (Not Found)
     */
    @GetMapping("/measurement-datasets/{id}")
    @Timed
    public ResponseEntity<MeasurementDataset> getMeasurementDataset(@PathVariable Long id) {
        log.debug("REST request to get MeasurementDataset : {}", id);
        MeasurementDataset measurementDataset = measurementDatasetService.findOne(id);
        return Optional.ofNullable(measurementDataset)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /measurement-datasets/:id : delete the "id" measurementDataset.
     *
     * @param id the id of the measurementDataset to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/measurement-datasets/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeasurementDataset(@PathVariable Long id) {
        log.debug("REST request to delete MeasurementDataset : {}", id);
        measurementDatasetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("measurementDataset", id.toString())).build();
    }

}
