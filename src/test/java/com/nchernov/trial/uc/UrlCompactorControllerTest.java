package com.nchernov.trial.uc;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.exceptions.urlmapping.CreationException;
import com.nchernov.trial.uc.rest.UrlCompactorController;
import com.nchernov.trial.uc.rest.dto.CompactResultResponse;
import com.nchernov.trial.uc.services.UrlCompactor;
import com.nchernov.trial.uc.services.UrlMappingManager;
import com.nchernov.trial.uc.services.dao.UrlMappingDao;
import com.nchernov.trial.uc.services.impl.Base62UrlCompactor;
import com.nchernov.trial.uc.services.impl.RetryIfDuplicateUrlMappingManager;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.Stack;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlCompactorControllerTest {
    private static final UrlCompactor URL_COMPACTOR = new Base62UrlCompactor();
    private static final UrlMappingDao URL_MAPPING_DAO = mock(InMemoryMappingDao.class);
    private static final UrlMappingManager urlMappingManager = new RetryIfDuplicateUrlMappingManager(URL_MAPPING_DAO, URL_COMPACTOR);

    private UrlCompactorController urlCompactorController = new UrlCompactorController(urlMappingManager);
    private static final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

    static {
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        when(URL_MAPPING_DAO.save(any(UrlMapping.class))).thenCallRealMethod();
        when(URL_MAPPING_DAO.findByPseudoHash(anyString())).thenCallRealMethod();
        when(URL_MAPPING_DAO.existsByPseudoHash(anyString())).thenCallRealMethod();
    }


    @Test
    public void shortLink() throws Exception {
        String origin = "https://gist.github.com/subfuzion/08c5d85437d5d4f00e58";
        CompactResultResponse result = urlCompactorController.compact(origin, httpServletRequest);
        assertTrue(result.isSuccess());
        assertTrue(result.getShortLink().length() < origin.length());
    }

    @Test
    public void emptyUrl() throws Exception {
        String origin = "";
        CompactResultResponse result = urlCompactorController.compact(origin, httpServletRequest);
        assertFalse(result.isSuccess());
        //TODO: i18n this
        assertEquals("URL should not be empty", result.getError());
    }

    @Test
    public void mailtoUrl() throws Exception {
        String origin = "mailto:whoami@abcxyz.serious.business.com";
        CompactResultResponse result = urlCompactorController.compact(origin, httpServletRequest);
        assertTrue(result.isSuccess());
        assertTrue(result.getShortLink().length() < origin.length());
    }

    @Test
    public void invalidUrl() throws Exception {
        String origin = "It's a kind of magic";
        CompactResultResponse result = urlCompactorController.compact(origin, httpServletRequest);
        assertFalse(result.isSuccess());
        //TODO: i18n this
        assertEquals("Unsupported protocol", result.getError());
    }

    @Test(expected = CreationException.class)
    public void duplicatedShortLinkWithExceededRetries() throws Exception {
        UrlMappingManager urlMappingManager = new RetryIfDuplicateUrlMappingManager(URL_MAPPING_DAO, (originalUrl, context) -> "IDDQD");
        UrlCompactorController urlCompactorController = new UrlCompactorController(urlMappingManager);

        String origin = "https://gist.github.com/subfuzion/08c5d85437d5d4f00e58";
        CompactResultResponse result = urlCompactorController.compact(origin, httpServletRequest);
        assertTrue(result.isSuccess());

        urlCompactorController.compact(origin, httpServletRequest);
    }

    @Test
    public void duplicatedShortLink() throws Exception {
        Stack<String> hashes = new Stack<>();
        hashes.push("Unique");
        hashes.push("IDDQD");
        hashes.push("IDDQD");

        UrlMappingManager urlMappingManager = new RetryIfDuplicateUrlMappingManager(URL_MAPPING_DAO, (originalUrl, context) -> hashes.pop());
        UrlCompactorController urlCompactorController = new UrlCompactorController(urlMappingManager);

        String origin = "https://gist.github.com/subfuzion/08c5d85437d5d4f00e58";
        CompactResultResponse result = urlCompactorController.compact(origin, httpServletRequest);
        assertTrue(result.isSuccess());

        CompactResultResponse newResult = urlCompactorController.compact(origin, httpServletRequest);
        assertTrue(newResult.isSuccess());

        assertNotEquals(result.getShortLink(), newResult.getShortLink());
    }
}
