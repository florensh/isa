package de.hshn.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hshn.se.domain.StoreMap;

/**
 * Spring Data JPA repository for the StoreMap entity.
 */
@SuppressWarnings("unused")
public interface StoreMapRepository extends JpaRepository<StoreMap,Long> {

	StoreMap findByStoreId(Long id);

}
