package com.nchernov.trial.uc;

import com.nchernov.trial.uc.exceptions.urlmapping.CreationException;
import com.nchernov.trial.uc.rest.UrlCompactorController;
import com.nchernov.trial.uc.rest.dto.CompactResultResponse;
import com.nchernov.trial.uc.services.UrlMappingManager;
import com.nchernov.trial.uc.services.impl.RetryIfDuplicateUrlMappingManager;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.Stack;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlCompactorControllerTest extends BaseTest {

    private UrlCompactorController urlCompactorController = new UrlCompactorController(urlMappingManager);
    private final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

    {
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
    }


    @Test
    public void shortLink() throws Exception {
        CompactResultResponse result = urlCompactorController.compact(ORIGIN, httpServletRequest);
        assertTrue(result.isSuccess());
        assertTrue(result.getShortLink().length() < ORIGIN.length());
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
        UrlMappingManager urlMappingManager = new RetryIfDuplicateUrlMappingManager(urlMappingDao,
                urlVisitDao, (originalUrl, context) -> "IDDQD");
        UrlCompactorController urlCompactorController = new UrlCompactorController(urlMappingManager);

        CompactResultResponse result = urlCompactorController.compact(ORIGIN, httpServletRequest);
        assertTrue(result.isSuccess());

        urlCompactorController.compact(ORIGIN + "/", httpServletRequest);
    }

    @Test
    public void duplicatedShortLink() throws Exception {
        Stack<String> hashes = new Stack<>();
        hashes.push("Unique");
        hashes.push("IDDQD");
        hashes.push("IDDQD");

        UrlMappingManager urlMappingManager = new RetryIfDuplicateUrlMappingManager(urlMappingDao,
                urlVisitDao, (originalUrl, context) -> hashes.pop());
        UrlCompactorController urlCompactorController = new UrlCompactorController(urlMappingManager);

        CompactResultResponse result = urlCompactorController.compact(ORIGIN, httpServletRequest);
        assertTrue(result.isSuccess());

        CompactResultResponse newResult = urlCompactorController.compact(ORIGIN + "/", httpServletRequest);
        assertTrue(newResult.isSuccess());

        assertNotEquals(result.getShortLink(), newResult.getShortLink());
    }

    @Test
    public void duplicatedOriginLink() throws Exception {
        CompactResultResponse result = urlCompactorController.compact(ORIGIN, httpServletRequest);
        CompactResultResponse secondResult = urlCompactorController.compact(ORIGIN, httpServletRequest);

        assertTrue(secondResult.isSuccess());
        assertEquals(1, urlMappingDao.count());
        assertEquals(result.getShortLink(), secondResult.getShortLink());
    }

    @Test
    public void derivedFromExistingShortLink() throws Exception {
        CompactResultResponse result = urlCompactorController.compact(ORIGIN, httpServletRequest);

        result = urlCompactorController.compact(result.getShortLink(), httpServletRequest);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().startsWith("Provided URL is already a working short link for origin:"));
        assertEquals(1, urlMappingDao.count());
    }
}
