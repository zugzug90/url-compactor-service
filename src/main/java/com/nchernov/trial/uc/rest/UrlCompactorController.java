package com.nchernov.trial.uc.rest;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.rest.dto.CompactResultResponse;
import com.nchernov.trial.uc.services.UrlMappingManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.nchernov.trial.uc.util.UrlUtils.generateFullBaseUrl;
import static org.springframework.util.StringUtils.isEmpty;

@Controller
@RequestMapping("/${rest.prefix}/urls")
@ComponentScan("com.nchernov.trial.uc.services")
public class UrlCompactorController {
    private UrlMappingManager urlMappingManager;

    private @Value("${base.url}") String baseUrl = "http://localhost";
    private @Value("${server.port}") int serverPort = 9090;

    public UrlCompactorController(UrlMappingManager urlMappingManager) {
        this.urlMappingManager = urlMappingManager;
    }

    @RequestMapping(path = "/compact", method= RequestMethod.POST)
    public @ResponseBody CompactResultResponse compact(@RequestParam(value = "url") String url,
                                  HttpServletRequest request) throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("host", request.getRemoteAddr());

        CompactResultResponse validationResult = validate(url);
        if (null != validationResult) {
            return validationResult;
        }

        return new CompactResultResponse(urlMappingManager.create(url, context).getShortLink());
    }

    private CompactResultResponse validate(String url) {
        if (isEmpty(url)) {
            return new CompactResultResponse(false, "URL should not be empty");
        }
        try {
            return checkIfNotDerivedFromExistingShortLink(new URL(url));
        } catch (MalformedURLException e) {
            return new CompactResultResponse(false, "Unsupported protocol");
        }
    }

    private CompactResultResponse checkIfNotDerivedFromExistingShortLink(URL url) {
        if (url.toString().startsWith(generateFullBaseUrl(baseUrl, serverPort))) {
            String[] paths = url.getPath().split("[/]", -1);
            if (paths.length > 0) {
                UrlMapping urlMapping = urlMappingManager.findByPseudoHash(paths[paths.length - 1]);
                if (urlMapping != null) {
                    return new CompactResultResponse(false, "Provided URL is already a working short link for origin: " + urlMapping.getUrl() + ". URL shortening will be skipped");
                }
            }
        }
        return null;
    }
}
