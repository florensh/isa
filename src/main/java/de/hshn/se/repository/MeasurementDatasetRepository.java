package de.hshn.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hshn.se.domain.MeasurementDataset;

/**
 * Spring Data JPA repository for the MeasurementDataset entity.
 */
@SuppressWarnings("unused")
public interface MeasurementDatasetRepository extends JpaRepository<MeasurementDataset,Long> {

}
