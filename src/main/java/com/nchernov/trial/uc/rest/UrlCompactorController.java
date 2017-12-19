package com.nchernov.trial.uc.rest;

import com.nchernov.trial.uc.rest.dto.CompactResultResponse;
import com.nchernov.trial.uc.services.UrlCompactor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

@Controller
@RequestMapping("/${rest.prefix}/urls")
@ComponentScan("com.nchernov.trial.uc.services")
public class UrlCompactorController {
    UrlCompactor urlCompactor;

    public UrlCompactorController(@Autowired UrlCompactor urlCompactor) {
        this.urlCompactor = urlCompactor;
    }

    @RequestMapping(path = "/compact", method= RequestMethod.POST)
    public @ResponseBody CompactResultResponse compact(@RequestParam(value = "url") String url,
                                  HttpServletRequest request) throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("host", request.getRemoteAddr());

        CompactResultResponse validatioResult = validate(url);
        if (null != validatioResult) {
            return validatioResult;
        }

        return new CompactResultResponse(urlCompactor.compact(url, context));
    }

    private CompactResultResponse validate(String url) {
        if (isEmpty(url)) {
            return new CompactResultResponse(false, "URL should not be empty");
        }
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return new CompactResultResponse(false, "Unsupported protocol");
        }
        return null;
    }
}
