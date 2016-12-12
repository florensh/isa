package de.hshn.se.repository;

import de.hshn.se.domain.Visit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Visit entity.
 */
@SuppressWarnings("unused")
public interface VisitRepository extends JpaRepository<Visit,Long> {

}
