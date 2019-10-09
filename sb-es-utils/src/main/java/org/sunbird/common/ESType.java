package org.sunbird.common;

public enum ESType {
    cert("cert");
    private String typeName;

    private ESType(String name) {
        this.typeName = name;
    }

    public String getTypeName() {
        return typeName;
    }
}
