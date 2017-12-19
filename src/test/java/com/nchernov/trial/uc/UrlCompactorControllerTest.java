package com.nchernov.trial.uc;

import com.nchernov.trial.uc.rest.UrlCompactorController;
import com.nchernov.trial.uc.rest.dto.CompactResultResponse;
import com.nchernov.trial.uc.services.UrlCompactor;
import com.nchernov.trial.uc.services.impl.Base62UrlCompactor;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlCompactorControllerTest {
    private final UrlCompactor URL_COMPACTOR = new Base62UrlCompactor();
    private UrlCompactorController urlCompactorController = new UrlCompactorController(URL_COMPACTOR);
    private static final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

    static {
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
    }


    @Test
    public void shortLink() throws Exception {
        String origin = "https://gist.github.com/subfuzion/08c5d85437d5d4f00e58";
        CompactResultResponse result = urlCompactorController.compact(origin, httpServletRequest);
        assertTrue(result.isSuccess());
        assertTrue(result.getResultUrl().length() < origin.length());
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
        String origin = "mailto:whoami@abc.com";
        CompactResultResponse result = urlCompactorController.compact(origin, httpServletRequest);
        assertTrue(result.isSuccess());
        assertTrue(result.getResultUrl().length() < origin.length());
    }

    @Test
    public void invalidUrl() throws Exception {
        String origin = "It's a kind of magic";
        CompactResultResponse result = urlCompactorController.compact(origin, httpServletRequest);
        assertFalse(result.isSuccess());
        //TODO: i18n this
        assertEquals("Unsupported protocol", result.getError());
    }
}
