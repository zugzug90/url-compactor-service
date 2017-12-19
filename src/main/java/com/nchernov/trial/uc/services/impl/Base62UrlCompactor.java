package com.nchernov.trial.uc.services.impl;

import com.nchernov.trial.uc.services.UrlCompactor;
import io.seruco.encoding.base62.Base62;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Component
public class Base62UrlCompactor implements UrlCompactor {
    private static final Base62 BASE_62 = Base62.createInstance();

    @Override
    public String compact(String originalUrl, Map<String, Object> context) {
        Object host = context.get("host");
        host = host == null ? "" : host;

        try {
            byte[] md5RawString = String.format("%s-%s", host, Long.toString(System.currentTimeMillis())).getBytes();
            String shortened = new String(BASE_62.encode(MessageDigest.getInstance("MD5").digest(md5RawString)));
            return shortened.substring(0, 7);
        } catch (NoSuchAlgorithmException e) {
            //ignore since we believe such algorithm is always on board
        }
        return null;
    }
}
