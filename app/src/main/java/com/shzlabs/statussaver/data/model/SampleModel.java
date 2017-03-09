package com.shzlabs.statussaver.data.model;

/**
 * Created by shaz on 14/2/17.
 */

public class SampleModel {
    private String name;
    private String description;

    public SampleModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
