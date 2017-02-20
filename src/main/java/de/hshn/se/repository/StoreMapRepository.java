package de.hshn.se.repository;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hshn.se.domain.StoreMap;

/**
 * Spring Data JPA repository for the StoreMap entity.
 */
@SuppressWarnings("unused")
public interface StoreMapRepository extends JpaRepository<StoreMap,Long> {

	StoreMap findByStoreIdAndValidityStartBeforeAndValidityEndAfter(Long id, ZonedDateTime validityStart,
			ZonedDateTime validityEnd);

}
