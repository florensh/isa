package de.hshn.se.service;

import de.hshn.se.domain.MeasurementDataset;
import java.util.List;

/**
 * Service Interface for managing MeasurementDataset.
 */
public interface MeasurementDatasetService {

    /**
     * Save a measurementDataset.
     *
     * @param measurementDataset the entity to save
     * @return the persisted entity
     */
    MeasurementDataset save(MeasurementDataset measurementDataset);

    /**
     *  Get all the measurementDatasets.
     *  
     *  @return the list of entities
     */
    List<MeasurementDataset> findAll();

    /**
     *  Get the "id" measurementDataset.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MeasurementDataset findOne(Long id);

    /**
     *  Delete the "id" measurementDataset.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
