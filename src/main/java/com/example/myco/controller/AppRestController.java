package com.example.myco.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppRestController {

    @GetMapping("/rest/user/ep")
    public String userEp() {
        return "The user endpoint, accessible to all";
    }

    @GetMapping("/rest/admin/ep")
    public String adminEp() {
        return "The user endpoint, accessible to Admin ONLY";
    }

    @GetMapping("/rest/engineer/ep")
    public String enggEp() {
        return "The user endpoint, accessible to engineer & above ONLY";
    }

    @GetMapping("/rest/operator/ep")
    public String opEp() {
        return "The user endpoint, accessible to operator & above ONLY";
    }

    @GetMapping("/rest/viewer/ep")
    public String viewEp() {
        return "The user endpoint, accessible to viewer & above ONLY";
    }

    @GetMapping("/ws/ep")
    public String wsEp() {
        return "The user endpoint, accessible to engg, operator, viewer ONLY";
    }

    @GetMapping("/getA")
    public String getA() {
        return "GetA";
    }
}
