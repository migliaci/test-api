package com.migliaci.myretail.interceptors;

import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.migliaci.myretail.exception.DataAccessException;
import com.migliaci.myretail.exception.MissingDataException;
import com.migliaci.myretail.resource.ProductResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice(assignableTypes = {ProductResource.class})
public class ErrorResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleException(IllegalArgumentException iae) {

        //something was wrong with the request
        return new ResponseEntity<>(iae.getMessage(), generateHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(InvalidQueryException iqe) {

        //cassandra is not up or was queried incorrectly
        return new ResponseEntity<>(iqe.getMessage(), generateHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(HttpClientErrorException hce) {

        //something went wrong with request to external resource
        return new ResponseEntity<>(hce.getMessage(), generateHeaders(), HttpStatus.valueOf(hce.getRawStatusCode()));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(IOException ioe) {

        //something went wrong with request to external resource
        return new ResponseEntity<>(ioe.getMessage(), generateHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(DataAccessException dae) {

        //something went wrong with request to external resource
        return new ResponseEntity<>(dae.getMessage(), generateHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(NumberFormatException nfe) {

        //something was wrong with the request
        return new ResponseEntity<>(nfe.getMessage(), generateHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(MissingDataException mde) {

        //data could not be found in external resource
        return new ResponseEntity<>(mde.getMessage(), generateHeaders(), HttpStatus.NOT_FOUND);
    }


    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return headers;
    }

}
