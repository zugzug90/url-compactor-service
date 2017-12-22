package com.nchernov.trial.uc;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.services.dao.UrlMappingDao;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MockUtils {


    public static void configureMock(UrlMappingDao urlMappingDao) {
        when(urlMappingDao.save(any(UrlMapping.class))).thenCallRealMethod();
        when(urlMappingDao.findByPseudoHash(anyString())).thenCallRealMethod();
        when(urlMappingDao.existsByPseudoHash(anyString())).thenCallRealMethod();
        when(urlMappingDao.findByUrl(anyString())).thenCallRealMethod();
        when(urlMappingDao.count()).thenCallRealMethod();
    }
}
