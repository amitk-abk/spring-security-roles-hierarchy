package com.example.myco.common;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Role {
    ADMIN, ENGINEER, OPERATOR, VIEWER;

    //Forms role hierarchy like ADMIN > ENGINEER > OPERATOR > VIEWER
    //The sign > is not greater than but it means include, so that ADMIN include all other roles and hence
    //Admin can access APIs for all other roles. Similarly ENGINEER can access for operator and viewer
    //Viewer is least open, it can not any other API other than that of its own.
    public static String getRoleHierarchy() {
        return Arrays.stream(Role.values())
                .map(Role::getRole)
                .collect(Collectors.joining(" > "));
    }

    public String getRole() {
        return "ROLE_" + name();
    }
}
