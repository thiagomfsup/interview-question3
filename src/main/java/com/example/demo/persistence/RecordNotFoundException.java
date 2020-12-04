package com.example.demo.persistence;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Throw when the request data cannot be found in the database.
 * 
 * <p>
 * When thrown during a web request execution, it automatically returns
 * <code>HTTP 404 NOT FOUND</code>.
 * </p>
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2789559542028129297L;

    public RecordNotFoundException(String msg) {
        super(msg);
    }
}
