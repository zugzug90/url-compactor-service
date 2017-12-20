package com.nchernov.trial.uc.app;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.domain.UrlVisit;
import com.nchernov.trial.uc.services.UrlMappingManager;
import com.nchernov.trial.uc.services.dao.UrlVisitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
public class RedirectController {

    private @Autowired UrlMappingManager urlMappingManager;
    private @Autowired UrlVisitDao urlVisitDao;

    @RequestMapping(value = "/{encodedUrl}", method = RequestMethod.GET)
    public void redirectToOrigin(HttpServletResponse httpServletResponse,
                                 @PathVariable("encodedUrl") String encodedUrl) throws IOException {
        UrlMapping urlMapping = urlMappingManager.findByPseudoHash(encodedUrl);
        if (urlMapping == null) {
            //TODO: redirect to 404 hipster styled page
            httpServletResponse.sendRedirect("/");
        }
        urlVisitDao.save(new UrlVisit(urlMapping, new Date()));

        httpServletResponse.sendRedirect(urlMapping.getUrl());
    }
}
