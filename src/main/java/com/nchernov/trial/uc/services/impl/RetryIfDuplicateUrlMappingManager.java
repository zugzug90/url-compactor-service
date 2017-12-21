package com.nchernov.trial.uc.services.impl;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.domain.UrlVisit;
import com.nchernov.trial.uc.exceptions.urlmapping.CreationException;
import com.nchernov.trial.uc.services.UrlCompactor;
import com.nchernov.trial.uc.services.UrlMappingManager;
import com.nchernov.trial.uc.services.dao.UrlMappingDao;
import com.nchernov.trial.uc.services.dao.UrlVisitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static com.nchernov.trial.uc.util.UrlUtils.enrichWithShortLink;

@Component
public class RetryIfDuplicateUrlMappingManager implements UrlMappingManager {

    private @Value("${creation.retry.count}") int retryCount = 1;
    private @Value("${base.url}") String baseUrl = "http://localhost";
    private @Value("${server.port}") int serverPort = 9090;

    @Autowired
    private UrlMappingDao urlMappingDao;

    @Autowired
    private UrlVisitDao urlVisitDao;

    @Autowired
    private UrlCompactor urlCompactor;

    public RetryIfDuplicateUrlMappingManager(UrlMappingDao urlMappingDao,
                                             UrlVisitDao urlVisitDao,
                                             UrlCompactor urlCompactor) {
        this.urlMappingDao = urlMappingDao;
        this.urlVisitDao = urlVisitDao;
        this.urlCompactor = urlCompactor;
    }

    @Override
    public UrlMapping findByPseudoHash(String pseudoHash) {
        UrlMapping urlMapping = urlMappingDao.findByPseudoHash(pseudoHash);
        if (urlMapping != null) {
            enrichWithShortLink(urlMapping, baseUrl, serverPort);
        }
        return urlMapping;
    }

    @Override
    public UrlMapping create(String url, Map<String, Object> context) throws CreationException {
        String pseudoHash = urlCompactor.compact(url, context);
        int operationsCount = retryCount + 1;
        do {
            try {
                UrlMapping urlMapping = urlMappingDao.save(new UrlMapping(url, pseudoHash));
                enrichWithShortLink(urlMapping, baseUrl, serverPort);
                return urlMapping;
            } catch (DataIntegrityViolationException ex) {
                //TODO: add logging here: failed to store pseudohash, found duplicate. Retry
                pseudoHash = urlCompactor.compact(url, context);
                operationsCount--;
            }
        } while (operationsCount > 0);
        //TODO: add logging here: failed to store pseudohash due to exceeded retry count limit
        throw new CreationException("Failed to create shortlink for url: " + url + ". Please, try again later.");
    }

    @Override
    public UrlMapping visit(String pseudoHash) {
        UrlMapping urlMapping = findByPseudoHash(pseudoHash);
        if (urlMapping == null) {
            return null;
        }
        urlVisitDao.save(new UrlVisit(urlMapping, new Date()));
        return urlMapping;
    }
}
