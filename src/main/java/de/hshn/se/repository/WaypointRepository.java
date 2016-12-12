package de.hshn.se.repository;

import de.hshn.se.domain.Waypoint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Waypoint entity.
 */
@SuppressWarnings("unused")
public interface WaypointRepository extends JpaRepository<Waypoint,Long> {

}
