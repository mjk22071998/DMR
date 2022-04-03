package com.example.dmr.medicalrep.model;

public class Medicine {
    String name;
    String potency;
    String description;

    public Medicine() {
    }

    public Medicine(String name, String potency, String description) {
        this.name = name;
        this.potency = potency;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPotency() {
        return potency;
    }

    public void setPotency(String potency) {
        this.potency = potency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
