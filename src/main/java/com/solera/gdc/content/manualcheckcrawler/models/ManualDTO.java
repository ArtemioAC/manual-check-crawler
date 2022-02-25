package com.solera.gdc.content.manualcheckcrawler.models;

import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
public class ManualDTO {
    private final String make;
    private final String platform;
    private Set<Model> models;

    public ManualDTO(final String make, final String platform) {
        this.make = make;
        this.platform = platform;
        this.models = new HashSet<>();
    }
}
