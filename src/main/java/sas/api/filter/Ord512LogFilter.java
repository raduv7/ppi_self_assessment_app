package sas.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(512)
public class Ord512LogFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(Ord512LogFilter.class);

    @Override
    public void doFilter
            (ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.trace("New req on: " + ((HttpServletRequest) request).getRequestURL().toString());

        chain.doFilter(request, response);

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if(httpResponse.getStatus() != 200) {
            log.error("Finished req on: " + ((HttpServletRequest) request).getRequestURL().toString() +
                    " with status " + httpResponse.getStatus());
        }
        log.trace("Finished req on: " + ((HttpServletRequest) request).getRequestURL().toString() +
                " with status " + httpResponse.getStatus());
    }
}
