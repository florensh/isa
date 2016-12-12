package de.hshn.se.repository;

import de.hshn.se.domain.StoreMap;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StoreMap entity.
 */
@SuppressWarnings("unused")
public interface StoreMapRepository extends JpaRepository<StoreMap,Long> {

}
