package sas.api.filter;

import sas.business._interface.util.auth.IJwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@Order(1024)
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class Ord1024TokenFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(Ord1024TokenFilter.class);
    static List<String> urlsWithoutToken = List.of(
            "/sign_in",
            "/sign_up",
            "/forbidden"
    );

    @Autowired
    private IJwtUtils jwtUtils;

    @Override
    public void doFilter
            (ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        boolean isUrlWithoutToken = isUrlWithoutToken(httpRequest.getRequestURL().toString());

        if (!isUrlWithoutToken && !Objects.equals(httpRequest.getMethod(), "OPTIONS")) {
            try {
                String username = jwtUtils.validateToken(httpRequest.getHeader("token"));
                httpRequest.setAttribute("username", username);
            }
            catch (Exception e) {
                ((HttpServletResponse) response).sendError(403, "Sir, you are forbidden to access this resource.");
                log.info("Sir, someone is forbidden to access this resource.");
                return;
            }
        }

        chain.doFilter(httpRequest, response);
    }

    private boolean isUrlWithoutToken(String reqUrl) {
        for (String url : urlsWithoutToken) {
            if (reqUrl.endsWith(url)) {
                return true;
            }
        }
        return false;
    }
}
