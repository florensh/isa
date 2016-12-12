package de.hshn.se.service;

import de.hshn.se.domain.Visit;
import de.hshn.se.repository.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Visit.
 */
@Service
@Transactional
public class VisitService {

    private final Logger log = LoggerFactory.getLogger(VisitService.class);
    
    @Inject
    private VisitRepository visitRepository;

    /**
     * Save a visit.
     *
     * @param visit the entity to save
     * @return the persisted entity
     */
    public Visit save(Visit visit) {
        log.debug("Request to save Visit : {}", visit);
        Visit result = visitRepository.save(visit);
        return result;
    }

    /**
     *  Get all the visits.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Visit> findAll(Pageable pageable) {
        log.debug("Request to get all Visits");
        Page<Visit> result = visitRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one visit by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Visit findOne(Long id) {
        log.debug("Request to get Visit : {}", id);
        Visit visit = visitRepository.findOne(id);
        return visit;
    }

    /**
     *  Delete the  visit by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Visit : {}", id);
        visitRepository.delete(id);
    }
}
