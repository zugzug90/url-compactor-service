package com.nchernov.trial.uc.services.impl;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.domain.UrlVisit;
import com.nchernov.trial.uc.exceptions.urlmapping.CreationException;
import com.nchernov.trial.uc.services.HashGenerator;
import com.nchernov.trial.uc.services.UrlMappingManager;
import com.nchernov.trial.uc.services.dao.UrlMappingDao;
import com.nchernov.trial.uc.services.dao.UrlVisitDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.nchernov.trial.uc.util.UrlUtils.enrichWithShortLink;

@Component
public class RetryingUrlMappingManager implements UrlMappingManager {

    private @Value("${creation.retry.count}") int retryCount = 1;
    private @Value("${base.url}") String baseUrl = "http://localhost";
    private @Value("${server.port}") int serverPort = 9090;
    private final Logger log = LoggerFactory.getLogger(RetryingUrlMappingManager.class);

    @Autowired
    private UrlMappingDao urlMappingDao;

    @Autowired
    private UrlVisitDao urlVisitDao;

    @Autowired
    private HashGenerator hashGenerator;

    public RetryingUrlMappingManager(UrlMappingDao urlMappingDao,
                                     UrlVisitDao urlVisitDao,
                                     HashGenerator hashGenerator) {
        this.urlMappingDao = urlMappingDao;
        this.urlVisitDao = urlVisitDao;
        this.hashGenerator = hashGenerator;
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
    public UrlMapping create(String url) throws CreationException {
        UrlMapping storedMapping = urlMappingDao.findByUrl(url);
        if (storedMapping != null) {
            return storedMapping;
        }
        String pseudoHash = hashGenerator.generateHash();
        int operationsCount = retryCount + 1;
        do {
            try {
                UrlMapping urlMapping = urlMappingDao.save(new UrlMapping(url, pseudoHash));
                enrichWithShortLink(urlMapping, baseUrl, serverPort);
                return urlMapping;
            } catch (DataIntegrityViolationException ex) {
                log.error("Failed to store pseudohash, found duplicate. Retry");
                pseudoHash = hashGenerator.generateHash();
                operationsCount--;
            }
        } while (operationsCount > 0);
        log.error("failed to store pseudohash due to exceeded retry count limit");
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
