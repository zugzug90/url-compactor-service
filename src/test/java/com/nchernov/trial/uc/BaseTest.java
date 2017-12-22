package com.nchernov.trial.uc;

import com.nchernov.trial.uc.services.UrlCompactor;
import com.nchernov.trial.uc.services.UrlMappingManager;
import com.nchernov.trial.uc.services.dao.UrlMappingDao;
import com.nchernov.trial.uc.services.dao.UrlVisitDao;
import com.nchernov.trial.uc.services.impl.Base62UrlCompactor;
import com.nchernov.trial.uc.services.impl.RetryIfDuplicateUrlMappingManager;

import static com.nchernov.trial.uc.MockUtils.configureMock;
import static org.mockito.Mockito.mock;

public class BaseTest {
    protected static final String ORIGIN = "https://gist.github.com/subfuzion/08c5d85437d5d4f00e58";
    protected static final String PSEUDO_HASH = "JVCR";

    protected final UrlCompactor urlCompactor = new Base62UrlCompactor();
    protected final UrlMappingDao urlMappingDao = mock(InMemoryMappingDao.class);
    protected final UrlVisitDao urlVisitDao = mock(UrlVisitDao.class);
    protected final UrlMappingManager urlMappingManager = new RetryIfDuplicateUrlMappingManager(urlMappingDao,
            urlVisitDao, urlCompactor);

    {
        configureMock(urlMappingDao);
    }
}
