package com.nchernov.trial.uc.app;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.services.UrlMappingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.nchernov.trial.uc.app.ViewConstants.UC_VIEW_ERROR;

@Controller
public class RedirectController {

    private UrlMappingManager urlMappingManager;

    public RedirectController(@Autowired UrlMappingManager urlMappingManager) {
        this.urlMappingManager = urlMappingManager;
    }

    @RequestMapping(value = "/{encodedUrl}", method = RequestMethod.GET)
    public void redirectToOrigin(HttpServletResponse httpServletResponse,
                                 @PathVariable("encodedUrl") String pseudoHash) throws IOException {

        UrlMapping urlMapping = urlMappingManager.visit(pseudoHash);
        if (urlMapping == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            httpServletResponse.sendRedirect(UC_VIEW_ERROR);
            return;
        }
        httpServletResponse.sendRedirect(urlMapping.getUrl());
    }
}
