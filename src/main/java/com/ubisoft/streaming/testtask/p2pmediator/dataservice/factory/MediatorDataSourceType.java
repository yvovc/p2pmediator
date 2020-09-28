package com.ubisoft.streaming.testtask.p2pmediator.dataservice.factory;

public enum MediatorDataSourceType {
    JOOQ_DSL("JooqDSL");

    String value;

    MediatorDataSourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
