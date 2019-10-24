package org.sunbird;

/**
 * this is an actor operation class
 * @author anmolgupta
 */
public enum ActorOperations {

    ADD("add"),
    VALIDATE("validate"),
    DOWNLOAD("download"),
    GENERATE("generate"),
    VERIFY("verify");

    private String operation;

    ActorOperations(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}

