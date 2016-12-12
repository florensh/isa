package de.hshn.se.service;

import de.hshn.se.domain.StoreMap;
import de.hshn.se.repository.StoreMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing StoreMap.
 */
@Service
@Transactional
public class StoreMapService {

    private final Logger log = LoggerFactory.getLogger(StoreMapService.class);
    
    @Inject
    private StoreMapRepository storeMapRepository;

    /**
     * Save a storeMap.
     *
     * @param storeMap the entity to save
     * @return the persisted entity
     */
    public StoreMap save(StoreMap storeMap) {
        log.debug("Request to save StoreMap : {}", storeMap);
        StoreMap result = storeMapRepository.save(storeMap);
        return result;
    }

    /**
     *  Get all the storeMaps.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<StoreMap> findAll(Pageable pageable) {
        log.debug("Request to get all StoreMaps");
        Page<StoreMap> result = storeMapRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one storeMap by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public StoreMap findOne(Long id) {
        log.debug("Request to get StoreMap : {}", id);
        StoreMap storeMap = storeMapRepository.findOne(id);
        return storeMap;
    }

    /**
     *  Delete the  storeMap by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StoreMap : {}", id);
        storeMapRepository.delete(id);
    }
}
