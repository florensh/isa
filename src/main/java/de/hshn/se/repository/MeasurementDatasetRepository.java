package de.hshn.se.repository;

import de.hshn.se.domain.MeasurementDataset;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MeasurementDataset entity.
 */
@SuppressWarnings("unused")
public interface MeasurementDatasetRepository extends JpaRepository<MeasurementDataset,Long> {

}
