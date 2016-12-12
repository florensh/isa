package de.hshn.se.repository;

import de.hshn.se.domain.Visitor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Visitor entity.
 */
@SuppressWarnings("unused")
public interface VisitorRepository extends JpaRepository<Visitor,Long> {

}
