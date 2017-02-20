package de.hshn.se.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hshn.se.domain.MeasurementDataset;
import de.hshn.se.repository.MeasurementDatasetRepository;
import de.hshn.se.service.MeasurementDatasetService;
import de.hshn.se.service.VisitService;

/**
 * Service Implementation for managing MeasurementDataset.
 */
@Service
@Transactional
public class MeasurementDatasetServiceImpl implements MeasurementDatasetService{

    private final Logger log = LoggerFactory.getLogger(MeasurementDatasetServiceImpl.class);
    
    @Inject
    private MeasurementDatasetRepository measurementDatasetRepository;

	@Inject
	private VisitService visitService;

    /**
     * Save a measurementDataset.
     *
     * @param measurementDataset the entity to save
     * @return the persisted entity
     */
    @Override
	public MeasurementDataset save(MeasurementDataset measurementDataset) {
		log.info("Request to save MeasurementDataset : {}", measurementDataset);
        MeasurementDataset result = measurementDatasetRepository.save(measurementDataset);
		// visitService.create(result);
        return result;
    }

    /**
     *  Get all the measurementDatasets.
     *  
     *  @return the list of entities
     */
    @Override
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
    @Override
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
    @Override
	public void delete(Long id) {
        log.debug("Request to delete MeasurementDataset : {}", id);
        measurementDatasetRepository.delete(id);
    }
}
