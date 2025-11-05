package com.assignx.AssignxServer.global.auth.exception;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhssong.errorping.ErrorpingService;
import com.jhssong.errorping.exception.BaseDomainException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler {
    private static final Logger EXCEPTION_DETAIL_LOGGER = getLogger("ERROR_DETAIL_LOGGER");
    private final ErrorpingService errorpingService;

    private ProblemDetail createProblemDetail(HttpStatus status,
                                              String detail,
                                              HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create("about:blank"));
        problem.setTitle(status.getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("method", request.getMethod());
        problem.setProperty("timestamp", ZonedDateTime.now().toOffsetDateTime().toString());
        return problem;
    }

    private String convertProblemDetailToMap(ProblemDetail problemDetail) throws JsonProcessingException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", problemDetail.getType());
        body.put("title", problemDetail.getTitle());
        body.put("status", problemDetail.getStatus());
        body.put("detail", problemDetail.getDetail());
        body.put("instance", problemDetail.getInstance());
        body.put("method", problemDetail.getProperties().get("method"));
        body.put("timestamp", problemDetail.getProperties().get("timestamp").toString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(body);
    }

    private void logDetailedException(Exception ex) {
        StackTraceElement[] origin = ex.getStackTrace();
        EXCEPTION_DETAIL_LOGGER.error(
                "Exception occurred at {}.{}({}:{}): {}",
                origin[0].getClassName(),
                origin[0].getMethodName(),
                origin[0].getFileName(),
                origin[0].getLineNumber(),
                ex.getMessage(),
                ex
        );
    }

    public void handle(BaseDomainException ex,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        ProblemDetail problem = createProblemDetail(ex.getStatus(), ex.getMessage(), request);
        if (ex.getStatus().is5xxServerError()) {
            log.error("[{}] status={} method={} uri={} message={}",
                    ex.getClass().getSimpleName(),
                    ex.getStatus().value(),
                    request.getMethod(),
                    request.getRequestURI(),
                    ex.getMessage());
            logDetailedException(ex);
            errorpingService.sendError(problem);
        } else {
            log.warn("[{}] status={} method={} uri={} message={}",
                    ex.getClass().getSimpleName(),
                    ex.getStatus().value(),
                    request.getMethod(),
                    request.getRequestURI(),
                    ex.getMessage());
        }

        SecurityContextHolder.clearContext();
        response.setStatus(problem.getStatus());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(convertProblemDetailToMap(problem));
    }
}
