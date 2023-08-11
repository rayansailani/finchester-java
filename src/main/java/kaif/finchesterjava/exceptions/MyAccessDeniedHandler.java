package kaif.finchesterjava.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        writeCustomResponse(response);
    }

    private void writeCustomResponse(HttpServletResponse response) {
        if (!response.isCommitted()) {
            try {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("{ \"error\": \"User is not authorized.\"}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}