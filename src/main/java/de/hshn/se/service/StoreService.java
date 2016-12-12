package de.hshn.se.service;

import de.hshn.se.domain.Store;
import de.hshn.se.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Store.
 */
@Service
@Transactional
public class StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreService.class);
    
    @Inject
    private StoreRepository storeRepository;

    /**
     * Save a store.
     *
     * @param store the entity to save
     * @return the persisted entity
     */
    public Store save(Store store) {
        log.debug("Request to save Store : {}", store);
        Store result = storeRepository.save(store);
        return result;
    }

    /**
     *  Get all the stores.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Store> findAll(Pageable pageable) {
        log.debug("Request to get all Stores");
        Page<Store> result = storeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one store by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Store findOne(Long id) {
        log.debug("Request to get Store : {}", id);
        Store store = storeRepository.findOne(id);
        return store;
    }

    /**
     *  Delete the  store by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Store : {}", id);
        storeRepository.delete(id);
    }
}
