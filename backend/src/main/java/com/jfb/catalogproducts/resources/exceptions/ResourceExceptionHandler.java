package com.jfb.catalogproducts.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import com.jfb.catalogproducts.services.exceptions.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {
  
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
    StandardError err = new StandardError();
    HttpStatus status = HttpStatus.NOT_FOUND;
      err.setTimestamp(Instant.now());
      err.setStatus(status.value());
      err.setError("Recurso não encontrado");
      err.setMessage(e.getMessage());
      err.setPath(request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }
  
}
