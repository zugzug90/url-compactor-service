package com.nchernov.trial.uc.rest;

import com.nchernov.trial.uc.domain.UrlVisitSummary;
import com.nchernov.trial.uc.services.dao.UrlSummaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/${rest.prefix}/stat")
@ComponentScan("com.nchernov.trial.uc.services")
public class UrlVisitSummaryController {

    private UrlSummaryDao urlSummaryDao;

    public UrlVisitSummaryController(@Autowired UrlSummaryDao urlSummaryDao) {
        this.urlSummaryDao = urlSummaryDao;
    }

    @RequestMapping(path = "/top", method= RequestMethod.GET)
    public @ResponseBody
    List<UrlVisitSummary> topMostVisited(@RequestParam(value = "count") int count) {
        return Collections.emptyList();
    }
}
