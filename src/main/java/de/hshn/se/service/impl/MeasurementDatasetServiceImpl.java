package de.hshn.se.service.impl;

import de.hshn.se.service.MeasurementDatasetService;
import de.hshn.se.domain.MeasurementDataset;
import de.hshn.se.repository.MeasurementDatasetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing MeasurementDataset.
 */
@Service
@Transactional
public class MeasurementDatasetServiceImpl implements MeasurementDatasetService{

    private final Logger log = LoggerFactory.getLogger(MeasurementDatasetServiceImpl.class);
    
    @Inject
    private MeasurementDatasetRepository measurementDatasetRepository;

    /**
     * Save a measurementDataset.
     *
     * @param measurementDataset the entity to save
     * @return the persisted entity
     */
    public MeasurementDataset save(MeasurementDataset measurementDataset) {
        log.debug("Request to save MeasurementDataset : {}", measurementDataset);
        MeasurementDataset result = measurementDatasetRepository.save(measurementDataset);
        return result;
    }

    /**
     *  Get all the measurementDatasets.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<MeasurementDataset> findAll() {
        log.debug("Request to get all MeasurementDatasets");
        List<MeasurementDataset> result = measurementDatasetRepository.findAll();

        return result;
    }

    /**
     *  Get one measurementDataset by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MeasurementDataset findOne(Long id) {
        log.debug("Request to get MeasurementDataset : {}", id);
        MeasurementDataset measurementDataset = measurementDatasetRepository.findOne(id);
        return measurementDataset;
    }

    /**
     *  Delete the  measurementDataset by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MeasurementDataset : {}", id);
        measurementDatasetRepository.delete(id);
    }
}
