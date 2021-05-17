package com.wg_planner.backend.Http.Response;

import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

public class ResponseEntity extends org.springframework.http.ResponseEntity {
    public ResponseEntity(HttpStatus status) {
        super(status);
    }

    public ResponseEntity(Object body, HttpStatus status) {
        super(body, status);
    }

    public ResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {

        super(headers, status);
    }

    public ResponseEntity(Object body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }
}
