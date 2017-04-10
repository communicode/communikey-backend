/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.config;

import static java.util.Optional.ofNullable;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.net.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CorsFilter implements Filter {

    private static final Logger log = LogManager.getLogger();

    public CorsFilter() {
        log.debug("Initialize CORS filter");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No configuration necessary
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", URLEncoder.encode(ofNullable(request.getHeader(HttpHeaders.ORIGIN)).orElse("*"),
                StandardCharsets.UTF_8.displayName()));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "-1");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization");
        response.setHeader("Access-Control-Expose-Headers", "Location");

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // Handled by Spring
    }
}
