package org.mortbay.jetty.security;

public class ConstraintMapping {
    Constraint constraint;
    String method;
    String pathSpec;

    public Constraint getConstraint() {
        return this.constraint;
    }

    public String getMethod() {
        return this.method;
    }

    public String getPathSpec() {
        return this.pathSpec;
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public void setPathSpec(String str) {
        this.pathSpec = str;
    }
}
