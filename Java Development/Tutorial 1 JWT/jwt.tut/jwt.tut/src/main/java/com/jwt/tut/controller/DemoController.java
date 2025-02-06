package com.jwt.tut.controller;

// Importing required classes and annotations
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Mark this class as a REST controller to handle HTTP requests
@RestController
public class DemoController {

    /**
     * Endpoint accessible to authenticated users.
     *
     * @return a ResponseEntity with the message "Hello World" and HTTP status 200 (OK)
     */
    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        // Responds with a "Hello World" message
        return ResponseEntity.ok("Hello World");
    }

    /**
     * Endpoint restricted to users with "ADMIN" authority.
     *
     * @return a ResponseEntity with the message "Admin" and HTTP status 200 (OK)
     */
    @GetMapping("/admin_only")
    public ResponseEntity<String> adminOnly() {
        // Responds with an "Admin" message
        return ResponseEntity.ok("Admin");
    }
}
