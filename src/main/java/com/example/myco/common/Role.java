package com.example.myco.common;

public enum Role {
    ADMIN, ENGINEER, OPERATOR, VIEWER;

    public String getRole()
    {
        return "ROLE_" + name();
    }

}
