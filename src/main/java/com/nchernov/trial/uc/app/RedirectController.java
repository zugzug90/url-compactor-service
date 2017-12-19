package com.nchernov.trial.uc.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RedirectController {

    @RequestMapping(value = "/{encodedUrl}", method = RequestMethod.GET)
    public void redirectToTwitter(HttpServletResponse httpServletResponse,
                                  @PathVariable("encodedUrl") String encodedUrl) throws IOException {
        httpServletResponse.sendRedirect("https://" + encodedUrl + ".com");
    }
}
