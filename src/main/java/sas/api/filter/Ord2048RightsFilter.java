package sas.api.filter;


import sas.api.controller.AssessmentController;
import sas.api.controller.AuthenticationController;
import sas.business._interface.util.record.IUserOperationRecord;
import sas.infrastructure.repository.auth.IUserRepository;
import sas.model.entity.auth.Operation;
import sas.model.entity.auth.User;
import sas.model.entity.auth._enum.EOperationType;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.*;

@Order(2048)
@Component
public class Ord2048RightsFilter implements Filter {
    @Autowired private IUserRepository userRepository;
    @Autowired private IUserOperationRecord userOperationRecord;

    private static final Logger log = LoggerFactory.getLogger(Ord2048RightsFilter.class);
    private static final Map<Class<?>, String> controllerToGroup;
    static {
        controllerToGroup = new IdentityHashMap<>();
        controllerToGroup.put(AuthenticationController.class, "");
        controllerToGroup.put(AssessmentController.class, "");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if(httpRequest.getAttribute("username") != null) {
            Pair<User, Operation> pair = hasRight(httpRequest);
            if(pair == null) {
                ((HttpServletResponse) response).sendError(401, "Sir, you don't have enough rights to access this resource.");
                log.info("Sir, someone doesn't have enough rights to access this resource.");
                return;
            }
            httpRequest.removeAttribute("username");
            httpRequest.setAttribute("actor", pair.getFirst());
            httpRequest.setAttribute("operation", pair.getSecond());
        }

        chain.doFilter(request, response);
    }

    private Pair<User, Operation> hasRight(HttpServletRequest httpRequest) {
        Optional<User> user = userRepository.findByUsername((String) httpRequest.getAttribute("username"));

        if(user.isPresent()) {
            User actor = user.get();
            actor.setProfile(userOperationRecord.getProfile(actor));
            Operation operation = getAssociatedRight(httpRequest);

            if (actor.hasRight(operation)) {
                return Pair.of(actor, operation);
            }
        }

        return null;
    }

    private Operation getAssociatedRight(HttpServletRequest request) {
        HandlerExecutionChain handlerExecutionChain =
                (HandlerExecutionChain) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

        if (handlerExecutionChain != null) {
            Object handler = handlerExecutionChain.getHandler();
            if (handler instanceof HandlerMethod handlerMethod) {

                String group = controllerToGroup.get(handlerMethod.getBeanType());
                if(group == null) {
                    return new Operation(EOperationType.NONE, "none");
                }

                String operationTypeStr = handlerMethod.getMethod().getAnnotation(SuppressWarnings.class).value()[0];
                EOperationType operationType = EOperationType.valueOf(operationTypeStr.toUpperCase());

                return new Operation(operationType, group);
            }
        }

        return new Operation(EOperationType.NONE, "none");
    }
}
