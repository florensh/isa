package de.hshn.se.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hshn.se.PathGenerator;
import de.hshn.se.domain.MeasurementDataset;
import de.hshn.se.domain.Store;
import de.hshn.se.domain.StoreMap;
import de.hshn.se.domain.Visit;
import de.hshn.se.domain.Visitor;
import de.hshn.se.domain.Waypoint;
import de.hshn.se.repository.StoreMapRepository;
import de.hshn.se.repository.StoreRepository;
import de.hshn.se.repository.VisitRepository;
import de.hshn.se.repository.VisitorRepository;
import de.hshn.se.repository.WaypointRepository;

/**
 * Service Implementation for managing Visit.
 */
@Service
@Transactional
public class VisitService {

    private final Logger log = LoggerFactory.getLogger(VisitService.class);
    
    @Inject
    private VisitRepository visitRepository;

	@Inject
	private StoreRepository storeRepository;

	@Inject
	private VisitorRepository visitorRepository;

	@Inject
	private WaypointRepository waypointRepository;

	@Inject
	private StoreMapRepository storeMapRepository;

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

	public void create(MeasurementDataset measurementSet) {
		log.info("Creating new visit");
		Visit visit = new Visit();
		Store store = this.storeRepository.findOne(measurementSet.getStoreId());

		StoreMap map = this.storeMapRepository.findByStoreId(store.getId());
		map.setWallMap(map.getWallMap());
		map.setPathMap(map.getPathMap());

		Visitor visitor = new Visitor();
		visitor = this.visitorRepository.save(visitor);

		visit.setVisitor(visitor);
		visit.setStore(store);
		visit.setDateOfVisit(ZonedDateTime.now());
		Set<Waypoint> storedWaypoints = new HashSet<Waypoint>();
		Set<Waypoint> waypoints = PathGenerator.bpf(10000, measurementSet, store, map);
		visit = this.visitRepository.save(visit);
		for (Waypoint w : waypoints) {
			w.setVisit(visit);
			storedWaypoints.add(this.waypointRepository.save(w));
		}
		if (storedWaypoints != null && !storedWaypoints.isEmpty()) {
			visit.setWaypoints(storedWaypoints);
			this.visitRepository.save(visit);
		}

	}
}
