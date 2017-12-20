package com.nchernov.trial.uc;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.services.dao.UrlMappingDao;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of {@link com.nchernov.trial.uc.services.dao.UrlMappingDao UrlMappingDao}
 * Relies on in-memory structure: Map<>
 */
public abstract class InMemoryMappingDao implements UrlMappingDao  {
    private Map<String, UrlMapping> db;

    @Override
    public UrlMapping findByPseudoHash(String pseudoHash) {
        initDbIfNeeded();
        return db.get(pseudoHash);
    }

    private void initDbIfNeeded() {
        if (db == null) {
            db = new HashMap<>();
        }
    }

    @Override
    public boolean existsByPseudoHash(String pseudoHash) {
        initDbIfNeeded();
        return db.containsKey(pseudoHash);
    }

    @Override
    public UrlMapping save(UrlMapping urlMapping) throws DataIntegrityViolationException {
        initDbIfNeeded();
        if (db.containsKey(urlMapping.getPseudoHash())) {
            throw new DataIntegrityViolationException("Such item already exists");
        }
        db.put(urlMapping.getPseudoHash(), urlMapping);
        return urlMapping;
    }
}
